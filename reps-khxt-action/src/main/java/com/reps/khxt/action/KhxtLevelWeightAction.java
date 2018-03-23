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
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.khxt.service.IKhxtLevelWeightService;
import static com.reps.khxt.util.WeightUtil.*;

import java.util.List;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/levelweight")
public class KhxtLevelWeightAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtLevelWeightAction.class);

	@Autowired
	IKhxtLevelWeightService khxtLevelWeightService;
	
	@Autowired
	IKhxtLevelService khxtLevelService;

	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtLevelWeight khxtLevelWeight) {
		ModelAndView mav = getModelAndView("/khxt/levelweight/list");
		ListResult<KhxtLevelWeight> listResult = khxtLevelWeightService.query(pager.getStartRow(), pager.getPageSize(), khxtLevelWeight);
		// 分页数据
		mav.addObject("list", listResult.getList());
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@RequestMapping(value = "/toadd")
	public Object toAdd(KhxtCategory khxtCategory) {
		ModelAndView mav = getModelAndView("/khxt/levelweight/add");
		try {
			List<KhxtLevel> levelList = khxtLevelService.findAll();
			mav.addObject("applyYearMap", APPLY_YEAR_RANGE);
			mav.addObject("levelList", levelList);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtLevelWeight khxtLevelWeight) {
		try {
			khxtLevelWeightService.save(khxtLevelWeight);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id) {
		ModelAndView mav = getModelAndView("/khxt/levelweight/edit");
		KhxtLevelWeight khxtLevelWeight = khxtLevelWeightService.get(id);
		List<KhxtLevel> levelList = khxtLevelService.findAll();
		mav.addObject("applyYearMap", APPLY_YEAR_RANGE);
		mav.addObject("levelList", levelList);
		mav.addObject("levelWeight", khxtLevelWeight);
		return mav;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(KhxtLevelWeight khxtLevelWeight) {
		try {
			khxtLevelWeightService.update(khxtLevelWeight);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(KhxtLevelWeight khxtLevelWeight) {
		try {
			khxtLevelWeightService.delete(khxtLevelWeight);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping({ "/show" })
	public Object show(String id) {
		try {
			ModelAndView mav = new ModelAndView("/khxt/levelweight/show");
			KhxtLevelWeight khxtLevelWeight = khxtLevelWeightService.get(id);
			List<KhxtLevel> levelList = khxtLevelService.findAll();
			mav.addObject("applyYearMap", APPLY_YEAR_RANGE);
			mav.addObject("levelList", levelList);
			mav.addObject("levelWeight", khxtLevelWeight);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取详情失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/copy")
	@ResponseBody
	public Object copy(String id) {
		try {
			khxtLevelWeightService.copy(id);
			return ajax(AjaxStatus.OK, "复制成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("复制失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

}
