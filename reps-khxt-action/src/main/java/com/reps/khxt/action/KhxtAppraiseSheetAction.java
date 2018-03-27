package com.reps.khxt.action;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.reps.core.RepsConstant;
import com.reps.core.commons.Pagination;
import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtItemService;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.khxt.service.IKhxtLevelWeightService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/appraise")
public class KhxtAppraiseSheetAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtAppraiseSheetAction.class);
	@Autowired
	private IKhxtAppraiseSheetService sheetService;

	@Autowired
	private IKhxtLevelService khxtlevelService;

	@Autowired
	private IKhxtLevelPersonService levelPersonService;

	@Autowired
	private IKhxtLevelWeightService WeightService;

	@Autowired
	private IKhxtItemService itemService;

	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtAppraiseSheet sheet) {
		ModelAndView mav = getModelAndView("/khxt/appraise/list");
		ListResult<KhxtAppraiseSheet> listResult = sheetService.query(pager.getStartRow(), pager.getPageSize(), sheet);

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
	public Object add(KhxtAppraiseSheet sheet, String[] khrids, String itemIds) {
		try {
			sheet.setUserId(getCurrentToken().getUserId());
			sheetService.save(sheet, khrids, itemIds);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
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

	@RequestMapping(value = "/personlist")
	public Object personlist(KhxtLevelPerson person) {

		ModelAndView mav = getModelAndView("/khxt/appraise/personlist");
		// 查询所有被考核人
		List<KhxtLevelPerson> p = levelPersonService.findLevelPerson(person);

		mav.addObject("list", p);
		return mav;

	}

	@RequestMapping(value = "/toWorkPlan")
	public Object toWorkPlan(KhxtLevelPerson person) {

		ModelAndView mav = getModelAndView("/khxt/appraise/workplan");
		// 查询所有被考核人
		KhxtLevelPerson khxtLevelPerson = levelPersonService.getByPersonId(person.getPersonId());

		mav.addObject("person", khxtLevelPerson);
		return mav;

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
		ListResult<KhxtAppraiseSheet> listResult = sheetService.query(pager.getStartRow(), pager.getPageSize(), sheet);

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

}
