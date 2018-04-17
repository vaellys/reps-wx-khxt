package com.reps.khxt.action;

import java.util.LinkedHashMap;
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
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.service.IKhxtCategoryService;
import com.reps.khxt.service.IKhxtItemService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/item")
public class KhxtItemAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtItemAction.class);

	@Autowired
	IKhxtItemService khxtItemService;
	
	@Autowired
	IKhxtCategoryService khxtCategoryService;

	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtItem khxtItem) {
		ModelAndView mav = getModelAndView("/khxt/item/list");
		ListResult<KhxtItem> listResult = khxtItemService.query(pager.getStartRow(), pager.getPageSize(), khxtItem);
		//构建指标类型MAP
		Map<String, String> categoryMap = buildCategoryMap(khxtCategoryService.findAll());
		mav.addObject("categoryMap", categoryMap);
		// 分页数据
		mav.addObject("list", listResult.getList());
		mav.addObject("item", khxtItem);
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@RequestMapping(value = "/toadd")
	public Object toAdd() {
		ModelAndView mav = getModelAndView("/khxt/item/add");
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
		Map<String, String> categoryMap = new LinkedHashMap<>();
		categoryMap.put("", "");
		for (KhxtCategory khxtCategory : categoryList) {
			categoryMap.put(khxtCategory.getId(), khxtCategory.getName());
		}
		return categoryMap;
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtItem khxtItem) {
		try {
			boolean save = khxtItemService.save(khxtItem);
			if(save){
				return ajax(AjaxStatus.OK, "添加成功");
			}else{
				return ajax(AjaxStatus.ERROR, "指标名称已存在，请重新输入！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id) {
		ModelAndView mav = getModelAndView("/khxt/item/edit");
		KhxtItem item = khxtItemService.get(id, false);
		//构建指标类型MAP
		Map<String, String> categoryMap = buildCategoryMap(khxtCategoryService.findAll());
		mav.addObject("item", item);
		mav.addObject("categoryMap", categoryMap);
		return mav;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(KhxtItem khxtItem) {
		try {
			khxtItemService.update(khxtItem);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(KhxtItem khxtItem) {
		try {
			khxtItemService.delete(khxtItem);
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
			ModelAndView mav = new ModelAndView("/khxt/item/show");
			KhxtItem khxtItem = khxtItemService.get(id, false);
			mav.addObject("item", khxtItem);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取详情失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

}
