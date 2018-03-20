package com.reps.khxt.action;

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
import com.reps.khxt.service.IKhxtCategoryService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/category")
public class KhxtCategoryAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtCategoryAction.class);

	@Autowired
	IKhxtCategoryService khxtCategoryService;

	/**
	 * 分类列表
	 * 
	 * @param pager
	 * @param khxtCategory
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtCategory khxtCategory) {
		ModelAndView mav = getModelAndView("/khxt/category/list");
		ListResult<KhxtCategory> listResult = khxtCategoryService.query(pager.getStartRow(), pager.getPageSize(), khxtCategory);
		// 分页数据
		mav.addObject("list", listResult.getList());
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@RequestMapping(value = "/toadd")
	public Object toAdd(KhxtCategory khxtCategory) {
		ModelAndView mav = getModelAndView("/khxt/category/add");
		try {
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtCategory khxtCategory) {
		try {
			khxtCategoryService.save(khxtCategory);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id) {
		ModelAndView mav = getModelAndView("/khxt/category/edit");
		KhxtCategory category = khxtCategoryService.get(id);
		mav.addObject("category", category);
		return mav;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(KhxtCategory khxtCategory) {
		try {
			khxtCategoryService.update(khxtCategory);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(KhxtCategory khxtCategory) {
		try {
			khxtCategoryService.delete(khxtCategory);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除分类失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

}
