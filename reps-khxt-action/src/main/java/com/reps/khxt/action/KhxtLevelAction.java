package com.reps.khxt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.RepsConstant;
import com.reps.core.commons.Pagination;
import com.reps.core.orm.ListResult;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtCategory;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.service.IKhxtCategoryService;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.service.IKhxtLevelService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/level")
public class KhxtLevelAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtLevelAction.class);

	@Autowired
	IKhxtLevelService khxtLevelService;
	
	@Autowired
	IKhxtCategoryService khxtCategoryService;
	
	@Autowired
	IKhxtLevelPersonService khxtLevelPersonService;

	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtLevel khxtLevel) {
		ModelAndView mav = getModelAndView("/khxt/level/list");
		ListResult<KhxtLevel> listResult = khxtLevelService.query(pager.getStartRow(), pager.getPageSize(), khxtLevel);
		//构建指标类型MAP
		Map<String, String> categoryMap = buildCategoryMap(khxtCategoryService.findAll());
		mav.addObject("categoryMap", categoryMap);
		// 分页数据
		mav.addObject("list", listResult.getList());
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@RequestMapping(value = "/toadd")
	public Object toAdd() {
		ModelAndView mav = getModelAndView("/khxt/level/add");
		try {
			Map<String, String> categoryMap = buildCategoryMap(khxtCategoryService.findAll());
			mav.addObject("categoryMap", categoryMap);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	private Map<String, String> buildCategoryMap(List<KhxtCategory> categoryList) {
		Map<String, String> categoryMap = new HashMap<>();
		categoryMap.put("", "全部");
		for (KhxtCategory khxtCategory : categoryList) {
			categoryMap.put(khxtCategory.getId(), khxtCategory.getName());
		}
		return categoryMap;
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtLevel khxtLevel) {
		try {
			khxtLevelService.save(khxtLevel);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id) {
		ModelAndView mav = getModelAndView("/khxt/level/edit");
		KhxtLevel level = khxtLevelService.get(id);
		//构建指标类型MAP
		Map<String, String> categoryMap = buildCategoryMap(khxtCategoryService.findAll());
		mav.addObject("level", level);
		mav.addObject("categoryMap", categoryMap);
		return mav;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(KhxtLevel khxtLevel) {
		try {
			khxtLevelService.update(khxtLevel);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(KhxtLevel khxtLevel) {
		try {
			khxtLevelService.delete(khxtLevel);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除分类失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping({ "/show" })
	public Object show(String id) {
		try {
			ModelAndView mav = new ModelAndView("/khxt/level/show");
			KhxtLevel khxtLevel = khxtLevelService.get(id);
			mav.addObject("level", khxtLevel);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取详情失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

}
