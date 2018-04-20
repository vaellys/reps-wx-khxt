package com.reps.khxt.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.RepsConstant;
import com.reps.core.commons.Pagination;
import com.reps.core.orm.ListResult;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtKhrProcess;
import com.reps.khxt.service.IKhxtKhrProcessService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/khrprocess")
public class KhxtKhrProcessAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtKhrProcessAction.class);

	@Autowired
	IKhxtKhrProcessService khxtKhrProcessService;
	
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtKhrProcess process) {
		ModelAndView mav = getModelAndView("/khxt/khrquery/list");
		ListResult<KhxtKhrProcess> listResult = khxtKhrProcessService.query(pager.getStartRow(), pager.getPageSize(), process);
		// 分页数据
		mav.addObject("list", listResult.getList());
		mav.addObject("khrProcess", process);
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@RequestMapping({ "/show" })
	public Object show(String id) {
		try {
			ModelAndView mav = new ModelAndView("/khxt/levelweight/show");
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取详情失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
}
