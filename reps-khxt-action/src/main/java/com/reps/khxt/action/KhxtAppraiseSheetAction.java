package com.reps.khxt.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.LoginToken;
import com.reps.core.RepsConstant;
import com.reps.core.commons.Pagination;
import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.DateUtil;
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformanceWork;
import com.reps.khxt.enums.ProgressStatus;
import com.reps.khxt.service.IKhxtAppraiseSheetFileService;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtGroupService;
import com.reps.khxt.service.IKhxtItemService;
import com.reps.khxt.service.IKhxtKhrProcessService;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.khxt.service.IKhxtLevelWeightService;
import com.reps.khxt.service.IKhxtPerformanceMembersService;
import com.reps.khxt.service.IKhxtPerformanceWorkService;
import com.reps.khxt.util.ConfigurePath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 月考核管理
 * 
 * @author ：Alex
 */
@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/appraise")
public class KhxtAppraiseSheetAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtAppraiseSheetAction.class);
	@Autowired
	private IKhxtAppraiseSheetService sheetService;

	@Autowired
	private IKhxtLevelService khxtlevelService;

	@Autowired
	private IKhxtLevelWeightService WeightService;

	@Autowired
	private IKhxtItemService itemService;

	@Autowired
	private IKhxtKhrProcessService KhxtKhrProcessService;

	@Autowired
	private IKhxtAppraiseSheetFileService fileService;

	@Autowired
	private IKhxtPerformanceWorkService workService;

	@Autowired
	private IKhxtPerformanceMembersService memberService;

	@Autowired
	private IKhxtGroupService groupService;

	/**
	 * 月考核列表
	 * 
	 * @author Alex
	 * @param pager
	 * @param sheet
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/khrlist")
	public ModelAndView khrlist(Pagination pager, KhxtAppraiseSheet sheet) throws Exception {
		ModelAndView mav = getModelAndView("/khxt/appraise/khrlist");
		ListResult<KhxtAppraiseSheet> listResult = sheetService.query(pager.getStartRow(), pager.getPageSize(), sheet);
		Map<String, String> LevelMap = buildLevelMap(khxtlevelService.findAll());

		// 考核进度
		Map<String, String> status = new HashMap<>();
		status.put("", "");
		status.put("0", ProgressStatus.UN_FINISHED.getStatus());
		status.put("1", ProgressStatus.FINISHED.getStatus());
		status.put("2", ProgressStatus.HAS_OVER.getStatus());
		// 考核年月
		Map<String, String> year = getYear();
		// 分页数据
		mav.addObject("list", listResult.getList());
		// 考核对象
		mav.addObject("levelMap", LevelMap);
		mav.addObject("season", year);
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		mav.addObject("status", status);
		return mav;
	}

	/**
	 * 上报月考核列表
	 * 
	 * @author Alex
	 * @param sheet
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/bkhrlist")
	public ModelAndView bkhrlist(KhxtAppraiseSheet sheet) throws Exception {
		ModelAndView mav = getModelAndView("/khxt/appraise/bkhrlist");
		// 查询指定人员信息
		LoginToken loginToken = getCurrentToken();
		if (null == loginToken) {
			throw new RepsException("您还没有登陆！");
		}
		KhxtPerformanceMembers members = new KhxtPerformanceMembers();
		if (StringUtils.isBlank(loginToken.getPersonId())) {
			throw new RepsException("考核人ID不存在！");
		}
		// 封装查询条件
		members.setBkhrPersonId(loginToken.getPersonId());
		if (StringUtil.isNotBlank(sheet.getName()) || StringUtil.isNotBlank(sheet.getSeason())) {
			members.setAppraiseSheet(sheet);
		}
		List<KhxtPerformanceMembers> list = memberService.find(members);
		List<KhxtAppraiseSheet> listResult = new ArrayList<>();

		if (!CollectionUtils.isEmpty(list)) {
			Map<String, KhxtAppraiseSheet> map = new HashMap<>();
			for (KhxtPerformanceMembers khxtPerformanceMembers : list) {
				KhxtAppraiseSheet appraiseSheet = sheetService.get(khxtPerformanceMembers.getSheetId());
				// 处理日期
				String endEate = appraiseSheet.getEndEate();
				appraiseSheet.setEndEate(DateUtil.formatStrDateTime(endEate, "yyyyMMdd", "yyyy年MM月dd日"));
				appraiseSheet.setStatus(Integer.valueOf(khxtPerformanceMembers.getStatus()));
				// 过滤数据
				map.put(appraiseSheet.getId(), appraiseSheet);
			}
			for (String key : map.keySet()) {
				listResult.add(map.get(key));
			}
		}
		// 考核年月
		Map<String, String> year = getYear();
		// 分页数据
		mav.addObject("list", listResult);

		mav.addObject("season", year);
		return mav;
	}

	/**
	 * 考核表情况列表
	 * 
	 * @author Alex
	 * @date 2018年4月12日
	 * @param sheet
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/assessslist")
	public ModelAndView bkhrQuery(Pagination pager, KhxtAppraiseSheet sheet) throws Exception {
		// 查询考核人员名单
		ModelAndView mav = khrlist(pager, sheet);

		mav.setViewName("/khxt/assessdetail/list");

		// 查询考核人级别
		Short[] b = { 1, 3 };
		List<KhxtLevel> khr = khxtlevelService.findByPower(b);
		// 考核对象级别
		Short[] k = { 2, 3 };
		List<KhxtLevel> bkhr = khxtlevelService.findByPower(k);

		Map<String, String> khrMap = buildLevelMap(khr);
		Map<String, String> bkhrMap = buildLevelMap(bkhr);
		mav.addObject("khrMap", khrMap);
		mav.addObject("bkhrMap", bkhrMap);
		return mav;
	}

	/**
	 * 统计进度
	 * 
	 * @author Alex
	 * @param pager
	 * @param sheet
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/count")
	public ModelAndView count(Pagination pager, KhxtAppraiseSheet sheet) throws Exception {
		List<KhxtAppraiseSheet> listResult = sheetService.count();

		for (KhxtAppraiseSheet khxtAppraiseSheet : listResult) {
			sheetService.update(khxtAppraiseSheet);
		}
		ModelAndView mav = khrlist(pager, sheet);

		return mav;
	}

	// 封装级别设置
	private Map<String, String> buildLevelMap(List<KhxtLevel> list) {

		Map<String, String> levelMap = new HashMap<>();
		for (KhxtLevel khxtlevel : list) {
			levelMap.put(khxtlevel.getId(), khxtlevel.getName());
		}
		return levelMap;
	}

	@RequestMapping(value = "/toadd")
	public Object toAdd() {
		try {
			ModelAndView mav = getModelAndView("/khxt/appraise/add");
			// 查询考核人级别
			Short[] b = { 1, 3 };
			List<KhxtLevel> khr = khxtlevelService.findByPower(b);
			// 查询被考核人级别
			Short[] k = { 2, 3 };
			List<KhxtLevel> bkhr = khxtlevelService.findByPower(k);
			Map<String, String> bkhrMap = buildLevelMap(bkhr);
			// 查询所有权重
			Map<String, String> weightMap = buildWeightMap(WeightService.findAll());
			// 生成考核年月
			Map<String, String> season = getYear();
			// 查询所有指标
			List<KhxtItem> itemlist = itemService.findAll();
			// 文件上传地址
			String path = ConfigurePath.ATTACHMENT_UPLOAD_PATH;
			mav.addObject("uploadPath", path);
			mav.addObject("khr", khr);
			mav.addObject("bkhrMap", bkhrMap);
			mav.addObject("weightMap", weightMap);
			mav.addObject("itemMap", itemlist);
			mav.addObject("season", season);
			mav.addObject("current", DateUtil.format(new Date(), "yyyyMM"));

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	/**
	 * 添加月考核
	 * 
	 * @author Alex
	 * @param sheet
	 * @param khrids
	 * @param itemIds
	 * @param fileName
	 * @param fileType
	 * @param fileSize
	 * @param fileUrl
	 * @return
	 * @return Object
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, String[] fileName, String[] fileType,
			String[] fileSize, String[] fileUrl) {
		try {
			List<KhxtAppraiseSheetFile> file = new ArrayList<>();
			// 封装文件参数
			for (int i = 0; i < fileName.length; i++) {
				KhxtAppraiseSheetFile sheetFile = new KhxtAppraiseSheetFile();
				if (StringUtil.isNotBlank(fileName[i]) && StringUtil.isNotBlank(fileUrl[i])) {
					sheetFile.setFileName(fileName[i]);
					sheetFile.setFileType(fileType[i]);
					sheetFile.setFileSize(Long.valueOf(fileSize[i]));
					sheetFile.setFileUrl(fileUrl[i].split(":")[1]);
					file.add(sheetFile);
				}
			}
			sheet.setUserId(getCurrentToken().getUserId());
			sheetService.save(sheet, khrids, itemIds, file);

			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String sheetId) {
		ModelAndView mav = getModelAndView("/khxt/appraise/edit");
		KhxtAppraiseSheet sheet = sheetService.get(sheetId);
		// 处理日期
		sheet.setEndEate(DateUtil.transToDate_yyyyMMdd(sheet.getEndEate()));
		sheet.setBeginDate(DateUtil.transToDate_yyyyMMdd(sheet.getBeginDate()));

		String[] khrIds = sheet.getKhrId().split(",");
		List<String> listkhrId = Arrays.asList(khrIds);
		// 查询考核人
		Short[] b = { 1, 3 };
		List<KhxtLevel> khr = khxtlevelService.findByPower(b);
		// 查询被考核人
		Short[] k = { 2, 3 };
		List<KhxtLevel> bkhr = khxtlevelService.findByPower(k);
		Map<String, String> bkhrMap = buildLevelMap(bkhr);
		// 考核年月
		Map<String, String> year = getYear();
		// 查询所有权重
		Map<String, String> weightMap = buildWeightMap(WeightService.findAll());
		// 查询所有指标
		List<KhxtItem> itemlist = itemService.findAll();
		// 查询考核文件
		List<KhxtAppraiseSheetFile> list = fileService.findFileBySheetId(sheetId);

		if (CollectionUtils.isEmpty(list)) {
			List<KhxtAppraiseSheetFile> list1 = new ArrayList<>();
			KhxtAppraiseSheetFile sheetFile = new KhxtAppraiseSheetFile();
			sheetFile.setFileName("请上传文件");
			list1.add(sheetFile);
			mav.addObject("file", list1);
		} else {
			mav.addObject("file", list);
		}
		// 文件上传地址
		String path = ConfigurePath.ATTACHMENT_UPLOAD_PATH;
		mav.addObject("uploadPath", path);
		mav.addObject("khr", khr);
		mav.addObject("listkhrId", listkhrId);
		mav.addObject("bkhrMap", bkhrMap);
		mav.addObject("weightMap", weightMap);
		mav.addObject("itemMap", itemlist);
		mav.addObject("sheet", sheet);
		mav.addObject("season", year);
		return mav;
	}

	/**
	 * 修改月考核
	 * 
	 * @author Alex
	 * @param sheet
	 * @param khrids
	 * @param itemIds
	 * @param fileName
	 * @param fileType
	 * @param fileSize
	 * @param fileUrl
	 * @param fileid
	 * @return
	 * @return Object
	 */
	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, String[] fileName, String[] fileType,
			String[] fileSize, String[] fileUrl, String[] fileid) {
		try {
			List<KhxtAppraiseSheetFile> file = new ArrayList<>();
			// 分装文件参数
			if (fileName.length == fileType.length && fileType.length == fileSize.length
					&& fileSize.length == fileUrl.length) {
				for (int i = 0; i < fileName.length; i++) {
					KhxtAppraiseSheetFile sheetFile = new KhxtAppraiseSheetFile();
					if (StringUtil.isNotBlank(fileName[i]) && StringUtil.isNotBlank(fileUrl[i])) {
						sheetFile.setFileName(fileName[i]);
						sheetFile.setFileType(fileType[i]);
						sheetFile.setFileSize(Long.valueOf(fileSize[i]));
						sheetFile.setFileUrl(fileUrl[i]);
						sheetFile.setId(fileid[i]);
						file.add(sheetFile);
					}
				}
			}
			sheetService.update(sheet, khrids, itemIds, file);

			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(KhxtAppraiseSheet sheet) {
		try {
			sheetService.delete(sheet);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	/**
	 * 跳转到上报月考核计划页面
	 * 
	 * @author Alex
	 * @param sheetId
	 * @return
	 * @return Object
	 */
	@RequestMapping(value = "/toWorkPlan")
	public Object toWorkPlan(String sheetId) {

		ModelAndView mav = getModelAndView("/khxt/appraise/workplan");
		KhxtAppraiseSheet sheet = sheetService.get(sheetId);
		// 查询工作绩效
		String personId = getCurrentToken().getPersonId();
		KhxtPerformanceWork khxtPerformanceWork = new KhxtPerformanceWork();
		khxtPerformanceWork.setSheetId(sheet.getId());
		khxtPerformanceWork.setPersonId(personId);

		List<KhxtPerformanceWork> list = workService.find(khxtPerformanceWork);

		// 处理计划和实施
		if (!CollectionUtils.isEmpty(list)) {
			KhxtPerformanceWork work = list.get(0);
			// 计划
			String planning = work.getPlanning();
			String[] plan = planning.split(",");
			// 纪实
			String execution = work.getExecution();
			String[] excu = execution.split(",");
			List<Map<String, String>> listWork = getListWork(plan, excu);
			mav.addObject("listWork", listWork);
			mav.addObject("workId", work.getId());
		} else {
			mav.addObject("work", "work");
		}

		mav.addObject("sheet", sheet);

		return mav;

	}

	// 处理页面显示工作计划
	private List<Map<String, String>> getListWork(String[] plan, String[] excu) {

		List<Map<String, String>> list = new ArrayList<>();
		if (plan.length >= excu.length) {
			for (int i = 0; i < plan.length; i++) {
				Map<String, String> map = new HashMap<>();
				map.put("planning", plan[i]);

				if (excu.length <= i) {
					map.put("execution", " ");
				} else {
					map.put("execution", excu[i]);
				}
				list.add(map);
			}
		} else {
			for (int i = 0; i < excu.length; i++) {
				Map<String, String> map = new HashMap<>();
				map.put("execution", excu[i]);
				if (plan.length <= i) {
					map.put("planning", " ");
				} else {
					map.put("planning", plan[i]);
				}
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 上报工作计划
	 * 
	 * @author Alex
	 * @date 2018年4月12日
	 * @param planning
	 * @param execution
	 * @param sheetId
	 * @param appear
	 * @param workId
	 * @return
	 * @return Object
	 */
	@RequestMapping(value = "/addWorkPlan")
	@ResponseBody
	public Object addWorkPlan(String[] planning, String[] execution, String sheetId, String appear, String workId) {
		try {
			String personId = getCurrentToken().getPersonId();
			sheetService.saveWorkPlan(planning, execution, sheetId, personId, appear, workId);

			if (StringUtils.isNotBlank(appear)) {
				return ajax(AjaxStatus.OK, "上报成功！");
			} else {
				return ajax(AjaxStatus.OK, "添加成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	// 获取三年内的所有年月
	private Map<String, String> getYear() {
		Map<String, String> map = new TreeMap<>();

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR) - 1;
		cal.set(year, 0, 1);

		for (int i = 0; i < 36; i++, cal.add(Calendar.MONTH, 1)) {
			Date d = cal.getTime();
			String key = DateUtil.format(d, "yyyMM");
			String value = DateUtil.format(d, "yyyy年MM月");
			map.put(key, value);
		}
		Date date = new Date();
		map.put(DateUtil.format(date, "yyyMM"), DateUtil.format(date, "yyyy年MM月"));
		return map;
	}

	// 分装级别权重
	private Map<String, String> buildWeightMap(List<KhxtLevelWeight> list) {

		Map<String, String> levelMap = new HashMap<>();
		for (KhxtLevelWeight khxtlevel : list) {
			levelMap.put(khxtlevel.getId(), khxtlevel.getYear() + "年" + khxtlevel.getName());
		}
		return levelMap;
	}

	@SuppressWarnings("unused")
	private Map<String, String> buildItemMap(List<KhxtItem> list) {

		Map<String, String> levelMap = new HashMap<>();
		levelMap.put("", "全部");
		for (KhxtItem item : list) {
			levelMap.put(item.getId(), item.getName());
		}
		return levelMap;
	}

	@RequestMapping(value = "/listpoint")
	public ModelAndView listPoint(Pagination pager, KhxtAppraiseSheet sheet) throws Exception {
		ModelAndView mav = getModelAndView("/khxt/appraise/listpoint");
		LoginToken currentToken = this.getCurrentToken();
		if (null == currentToken) {
			throw new RepsException("您还没有登陆！");
		}
		sheet.setKhrPersonId(currentToken.getPersonId());
		sheet.setCheckKhr(StringUtil.isBlank(currentToken.getPersonId()) ? false : true);
		ListResult<KhxtAppraiseSheet> listResult = sheetService.query(pager.getStartRow(), pager.getPageSize(), sheet);
		Map<String, String> LevelMap = buildLevelMap(khxtlevelService.findAll());
		// 设置考核人
		for (KhxtAppraiseSheet appraiseSheet : listResult.getList()) {
			appraiseSheet
					.setCheckKhr(KhxtKhrProcessService.checkKhr(appraiseSheet.getId(), currentToken.getPersonId()));
			appraiseSheet.setCheckCompletedMarking(
					KhxtKhrProcessService.checkCompletedMarking(appraiseSheet.getId(), currentToken.getPersonId()));
			appraiseSheet.setWeightDisplay(
					getLevelWeightDisplay(appraiseSheet.getKhrId(), appraiseSheet.getLevelWeight().getWeight()));
		}
		// 考核年月
		Map<String, String> year = getYear();
		// 分页数据
		mav.addObject("list", listResult.getList());
		mav.addObject("sheet", sheet);
		// 考核对象
		mav.addObject("levelMap", LevelMap);
		mav.addObject("season", year);
		mav.addObject("identity", currentToken.getIdentity());
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@SuppressWarnings("unchecked")
	private String getLevelWeightDisplay(String levelIds, String weightJson) throws RepsException {
		StringBuilder sblw = new StringBuilder();
		if (StringUtil.isNotBlank(levelIds)) {
			String[] ids = levelIds.split(",");
			JSONArray weightArray = JSONArray.fromObject(weightJson);
			if (null != weightArray && !weightArray.isEmpty()) {
				for (String id : ids) {
					for (Iterator<JSONObject> iterator = weightArray.iterator(); iterator.hasNext();) {
						JSONObject weightObj = iterator.next();
						String levelId = weightObj.getString("levelId");
						if (id.equals(levelId)) {
							KhxtLevel khxtLevel = khxtlevelService.get(levelId);
							sblw.append(khxtLevel.getName());
							sblw.append(weightObj.getString("weight"));
							sblw.append("%;");
							break;
						}
					}
				}
				sblw.deleteCharAt(sblw.toString().lastIndexOf(";"));
			}
		}
		return sblw.toString();
	}

	@RequestMapping(value = "/show")
	public ModelAndView show(String sheetId, String callbackUrl) {
		ModelAndView mav = getModelAndView("/khxt/appraise/show");
		KhxtAppraiseSheet sheet = sheetService.get(sheetId);
		// 处理显示时间
		String season = sheet.getSeason();
		sheet.setSeason(DateUtil.formatStrDateTime(season, "yyyyMM", "yyyy年MM月"));
		String endEate = sheet.getEndEate();
		sheet.setEndEate(DateUtil.formatStrDateTime(endEate, "yyyyMMdd", "yyyy年MM月dd日"));
		String beginDate = sheet.getBeginDate();
		sheet.setBeginDate(DateUtil.formatStrDateTime(beginDate, "yyyyMMdd", "yyyy年MM月dd日"));
		// 查询考核文件
		List<KhxtAppraiseSheetFile> sheetFileList = fileService.findFileBySheetId(sheetId);
		String khrLevelIds = sheet.getKhrId();
		if (StringUtil.isNotBlank(khrLevelIds)) {
			StringBuilder sb = new StringBuilder();
			for (String id : khrLevelIds.split(",")) {
				sb.append(khxtlevelService.get(id).getName());
				sb.append(";");
			}
			sb.deleteCharAt(sb.toString().lastIndexOf(";"));
			sheet.setLevelDisplay(sb.toString());
		}
		// 文件下载地址
		mav.addObject("attachmentFilePath", ConfigurePath.ATTACHMENT_FILE_PATH);
		mav.addObject("file", sheetFileList);
		mav.addObject("sheet", sheet);
		mav.addObject("callbackUrl", callbackUrl);
		return mav;
	}

	@RequestMapping(value = "/assessshow")
	public ModelAndView assessshow(String sheetId, String callbackUrl) throws Exception {
		ModelAndView mav = show(sheetId, callbackUrl);
		mav.setViewName("/khxt/assessdetail/assessshow");
		KhxtAppraiseSheet sheet = (KhxtAppraiseSheet) mav.getModelMap().get("sheet");
		// 处理权重
		sheet.setWeight(sheetService.getJsonWeight(sheet.getLevelWeight()));
		String khrIds = sheet.getKhrId();
		String bkhrId = sheet.getBkhrId();

		// 查询考核分组
		List<String> list = groupService.getByLvelId(khrIds, bkhrId);
		if (!CollectionUtils.isEmpty(list)) {
			mav.addObject("khrpersonName", list.get(0));
			mav.addObject("bkhrpersonName", list.get(1));
		}

		return mav;
	}

	@RequestMapping(value = "/download")
	public void download(HttpServletResponse response, String id, String callbackUrl) throws IOException {
		try {
			KhxtAppraiseSheetFile fileInfo = fileService.get(id);
			if (null == fileInfo) {
				response.addHeader("content-type", "text/html; charset=utf-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				StringBuffer errorHtml = new StringBuffer(
						"<div style='font-size:12px;width:500px;margin: 0 auto;padding-top:100px;text-align:center'");
				errorHtml.append(">");
				errorHtml.append("<div><font color='red'>对不起，文件已经不存在,可能已经被删除，不能提供下载</font></div>");
				errorHtml.append("<div style='padding-top:20px'><a href='" + callbackUrl + "'>继续查看其它资源</a></div>");
				errorHtml.append("</div>");
				response.getWriter().write(errorHtml.toString());
				// response.
				return;
			}

			// String resFilePath =
			// info.getFilePath().startsWith("/")?info.getFilePath():("/"+info.getFilePath());
			// 本地下载
			String path = ConfigurePath.ATTACHMENT_UPLOAD_PATH;
			String filePath = path
					+ (fileInfo.getFileUrl().startsWith("/") ? fileInfo.getFileUrl() : ("/" + fileInfo.getFileUrl()));
			File file = new File(filePath);
			response.reset();
			if (!file.exists()) {
				response.addHeader("content-type", "text/html; charset=utf-8");
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				StringBuffer errorHtml = new StringBuffer(
						"<div style='font-size:12px;width:500px;margin: 0 auto;padding-top:100px;text-align:center'");
				errorHtml.append(">");
				errorHtml.append("<div><font color='red'>对不起，文件已经不存在,可能已经被删除，不能提供下载</font></div>");
				errorHtml.append("<div style='padding-top:20px'><a href='" + callbackUrl + "'>继续查看其它资源</a></div>");
				errorHtml.append("</div>");
				response.getWriter().write(errorHtml.toString());
				// response.
				return;
			}

			InputStream inStream = new FileInputStream(filePath);// 文件的存放路径

			response.addHeader("Content-Disposition", "attachment;filename=\"" + new String(
					(fileInfo.getFileName() + "." + fileInfo.getFileType()).getBytes("GB2312"), "ISO_8859_1") + "\"");
			// 循环取出流中的数据
			byte[] b = new byte[inStream.available()];
			int len;
			while ((len = inStream.read(b)) > 0) {
				response.getOutputStream().write(b, 0, len);
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("下载id=" + id + "的资源文件出错");
		}
	}

}
