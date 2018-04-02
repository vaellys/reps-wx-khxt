package com.reps.khxt.action;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.entity.KhxtPerformanceWork;
import com.reps.khxt.service.IKhxtAppraiseSheetFileService;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtItemService;
import com.reps.khxt.service.IKhxtKhrProcessService;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.khxt.service.IKhxtLevelWeightService;
import com.reps.khxt.service.IKhxtPerformanceWorkService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtAppraiseSheet sheet) {
		ModelAndView mav = getModelAndView("/khxt/appraise/list");
		ListResult<KhxtAppraiseSheet> listResult = sheetService.query(pager.getStartRow(), pager.getPageSize(), sheet,false);

		Map<String, String> LevelMap = buildLevelMap(khxtlevelService.findAll());

		// 考核进度
		Map<String, String> status = new HashMap<>();
		status.put("", "请选择");
		status.put("0", "未完成");
		status.put("1", "已完成");
		status.put("2", "已选择");
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

	@RequestMapping(value = "/count")
	public ModelAndView count(Pagination pager, KhxtAppraiseSheet sheet) {
		ModelAndView mav = getModelAndView("/khxt/appraise/list");
		ListResult<KhxtAppraiseSheet> listResult = sheetService.query(pager.getStartRow(), pager.getPageSize(), sheet,true);

		Map<String, String> LevelMap = buildLevelMap(khxtlevelService.findAll());
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
		return mav;
	}

	private Map<String, String> buildLevelMap(List<KhxtLevel> list) {

		Map<String, String> levelMap = new HashMap<>();
		levelMap.put(" ", "全部");
		for (KhxtLevel khxtlevel : list) {
			levelMap.put(khxtlevel.getId(), khxtlevel.getName());
		}
		return levelMap;
	}

	@RequestMapping(value = "/toadd")
	public Object toAdd() {
		try {
			ModelAndView mav = getModelAndView("/khxt/appraise/add");
			// 查询考核人
			Short[] b = { 1, 3 };
			List<KhxtLevel> khr = khxtlevelService.findByPower(b);
			// 查询被考核人
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
			String path = RepsConstant.getContextProperty("file.http.path");
			mav.addObject("uploadPath", path);
			mav.addObject("khr", khr);
			mav.addObject("bkhrMap", bkhrMap);
			mav.addObject("weightMap", weightMap);
			mav.addObject("itemMap", itemlist);
			mav.addObject("season", season);
			mav.addObject("current", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, KhxtAppraiseSheetFile file) {
		try {

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
		// 已选指标
		Set<KhxtItem> item = sheet.getItem();
		// 查询考核文件
		KhxtAppraiseSheetFile file = fileService.findFileBySheetId(sheetId);

		String path = RepsConstant.getContextProperty("file.http.path");
		mav.addObject("uploadPath", path);
		mav.addObject("khr", khr);
		mav.addObject("listkhrId", listkhrId);
		mav.addObject("bkhrMap", bkhrMap);
		mav.addObject("weightMap", weightMap);
		mav.addObject("itemMap", itemlist);
		mav.addObject("item", item);
		mav.addObject("file", file);
		mav.addObject("sheet", sheet);
		mav.addObject("season", year);
		return mav;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, KhxtAppraiseSheetFile file,
			String sheetId, String fileid) {
		try {
			sheet.setId(sheetId);
			file.setId(fileid);
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
			logger.error("删除分类失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toWorkPlan")
	public Object toWorkPlan(String sheetId) {

		ModelAndView mav = getModelAndView("/khxt/appraise/workplan");
		KhxtAppraiseSheet sheet = sheetService.get(sheetId);
		// 查询工作绩效
		String personId = getCurrentToken().getPersonId();
		personId = "4028daac609229ac016121e0bca80b66";
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

	@RequestMapping(value = "/addWorkPlan")
	public void addWorkPlan(String[] planning, String[] execution, String sheetId, String appear, String workId) {
		String personId = getCurrentToken().getPersonId();
		personId = "4028daac609229ac016121e0bca80b66";
		sheetService.saveWorkPlan(planning, execution, sheetId, personId, appear, workId);

	}

	private Map<String, String> getYear() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy年MM月");
		LocalDate date = LocalDate.now();
		int year = date.getYear();
		LocalDate start = LocalDate.of(year - 1, 1, 1);

		LocalDate end = LocalDate.of(year + 1, 12, 1);

		Map<String, String> map = new TreeMap<String, String>();

		if (!start.equals(end)) {
			for (int i = 0; i < 36; i++) {
				LocalDate localdate = start.plusMonths(i);
				localdate.format(formatter);
				map.put("", "请选择");
				map.put(localdate.format(formatter), localdate.format(formatter1));
			}
		}
		map.put(date.format(formatter), date.format(formatter1));
		return map;
	}

	private Map<String, String> buildWeightMap(List<KhxtLevelWeight> list) {

		Map<String, String> levelMap = new HashMap<>();
		levelMap.put("", "全部");
		for (KhxtLevelWeight khxtlevel : list) {
			levelMap.put(khxtlevel.getId(), khxtlevel.getName());
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
	public ModelAndView listPoint(Pagination pager, KhxtAppraiseSheet sheet) {
		ModelAndView mav = getModelAndView("/khxt/appraise/listpoint");
		LoginToken currentToken = this.getCurrentToken();
		if(null == currentToken) {
			throw new RepsException("您还没有登陆！");
		}
		sheet.setKhrPersonId(currentToken.getPersonId());
		ListResult<KhxtAppraiseSheet> listResult = sheetService.query(pager.getStartRow(), pager.getPageSize(), sheet, false);
		Map<String, String> LevelMap = buildLevelMap(khxtlevelService.findAll());
		//设置考核人
		for (KhxtAppraiseSheet appraiseSheet : listResult.getList()) {
			appraiseSheet.setCheckKhr(KhxtKhrProcessService.checkKhr(appraiseSheet.getId(), currentToken.getPersonId()));
			appraiseSheet.setCheckCompletedMarking(KhxtKhrProcessService.checkCompletedMarking(appraiseSheet.getId(), currentToken.getPersonId()));
			appraiseSheet.setWeightDisplay(getLevelWeightDisplay(appraiseSheet.getKhrId(), appraiseSheet.getLevelWeight().getWeight()));
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
		if(StringUtil.isNotBlank(levelIds)) {
			String[] ids = levelIds.split(",");
			JSONArray weightArray = JSONArray.fromObject(weightJson);
			if(null != weightArray && !weightArray.isEmpty()) {
				for (String id : ids) {
					for (Iterator<JSONObject> iterator = weightArray.iterator(); iterator.hasNext();) {
						JSONObject weightObj = iterator.next();
						String levelId = weightObj.getString("levelId");
						if(id.equals(levelId)) {
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

}
