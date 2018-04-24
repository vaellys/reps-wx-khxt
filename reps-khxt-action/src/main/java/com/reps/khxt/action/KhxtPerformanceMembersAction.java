package com.reps.khxt.action;

import java.net.URLDecoder;
import java.util.List;

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
import com.reps.core.util.DateUtil;
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformanceWork;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.service.IKhxtPerformanceMembersService;
import com.reps.khxt.service.IKhxtPerformancePointService;
import com.reps.khxt.service.IKhxtPerformanceWorkService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/member")
public class KhxtPerformanceMembersAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtPerformanceMembersAction.class);

	@Autowired
	IKhxtPerformanceMembersService khxtPerformanceMembersService;

	@Autowired
	IKhxtAppraiseSheetService khxtAppraiseSheetService;

	@Autowired
	IKhxtPerformanceWorkService khxtPerformanceWorkService;

	@Autowired
	IKhxtLevelPersonService khxtLevelPersonService;

	@Autowired
	IKhxtPerformancePointService pointService;

	@RequestMapping(value = "/appraisepoint")
	public Object list(Pagination pager, KhxtPerformanceMembers khxtPerformanceMembers) {
		ModelAndView mav = getModelAndView("/khxt/member/appraisepoint");
		try {
			LoginToken currentToken = this.getCurrentToken();
			if (null == currentToken) {
				throw new RepsException("您还没有登陆！");
			}
			KhxtAppraiseSheet khxtAppraiseSheet = khxtAppraiseSheetService.get(khxtPerformanceMembers.getSheetId(), true);
			
			khxtAppraiseSheet.setSeason(DateUtil.formatStrDateTime(khxtAppraiseSheet.getSeason(), "yyyyMM", "yyyy年MM月"));
			mav.addObject("items", khxtAppraiseSheet.getItem());
			// 设置考核人ID
			khxtPerformanceMembers.setKhrPersonId(currentToken.getPersonId());
			List<KhxtPerformanceMembers> results = khxtPerformanceMembersService.find(khxtPerformanceMembers, true);
			mav.addObject("performanceMembers", results);
			mav.addObject("khrPersonName", currentToken.getName());
			mav.addObject("member", khxtPerformanceMembers);
			mav.addObject("khxtAppraiseSheet", khxtAppraiseSheet);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/show")
	public Object show(KhxtPerformanceMembers khxtPerformanceMembers) {
		ModelAndView mav = getModelAndView("/khxt/khrquery/show");
		try {
			String khrPersonName = khxtPerformanceMembers.getKhrPersonName();
			if(StringUtil.isNotBlank(khrPersonName)) {
				khrPersonName = URLDecoder.decode(khrPersonName, "UTF-8");
				khxtPerformanceMembers.setKhrPersonName(khrPersonName);
			}
			KhxtAppraiseSheet khxtAppraiseSheet = khxtAppraiseSheetService.get(khxtPerformanceMembers.getSheetId(), true);
			khxtAppraiseSheet.setSeason(DateUtil.formatStrDateTime(khxtAppraiseSheet.getSeason(), "yyyyMM", "yyyy年MM月"));
			mav.addObject("items", khxtAppraiseSheet.getItem());
			// 设置考核人ID
			List<KhxtPerformanceMembers> results = khxtPerformanceMembersService.find(khxtPerformanceMembers, true);
			mav.addObject("performanceMembers", results);
			mav.addObject("khrPersonName", khrPersonName);
			mav.addObject("khxtAppraiseSheet", khxtAppraiseSheet);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(String memberJson, String itemPointJson, KhxtPerformanceMembers khxtPerformanceMembers) {
		try {
			khxtPerformanceMembersService.updateAndParseJson(memberJson, itemPointJson, khxtPerformanceMembers);
			return ajax(AjaxStatus.OK, "打分完成");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("打分失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping({ "/workdetail" })
	public Object workdetail(KhxtPerformanceWork performanceWork) {
		try {
			ModelAndView mav = new ModelAndView("/khxt/member/workdetail");
			List<KhxtPerformanceWork> list = khxtPerformanceWorkService.find(performanceWork);
			KhxtLevelPerson person = khxtLevelPersonService.getByPersonId(performanceWork.getPersonId());
			mav.addObject("workList", list);
			mav.addObject("performanceWork", performanceWork);
			mav.addObject("levelPerson", person);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	/**
	 * 被考核人查询
	 * 
	 * @author Alex
	 * @date 2018年4月13日
	 * @param sheet
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/bkhrquery")
	public ModelAndView bkhrList(Pagination pager, KhxtPerformanceMembers khxtPerformanceMembers) throws Exception {
		ModelAndView mav = getModelAndView("/khxt/bkhrquery/list");
		LoginToken loginToken = getCurrentToken();
		if (loginToken == null) {
			throw new RepsException("请登录");
		}
		khxtPerformanceMembers.setBkhrPersonId(loginToken.getPersonId());
		List<KhxtPerformanceMembers> listResult = khxtPerformanceMembersService.query(khxtPerformanceMembers);
		// 分页数据
		mav.addObject("list", listResult);
		return mav;
	}

	/**
	 * 得分详情
	 * 
	 * @author Alex
	 * @date 2018年4月13日
	 * @param khxtPerformanceMembers
	 * @return
	 * @throws Exception
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/scoringdetails")
	public ModelAndView scoringDetails(KhxtPerformanceMembers khxtPerformanceMembers) throws Exception {

		ModelAndView mav = getModelAndView("/khxt/bkhrquery/scoringdetails");
		LoginToken loginToken = getCurrentToken();
		if (loginToken == null) {
			throw new RepsException("请登录");
		}
		khxtPerformanceMembers.setBkhrPersonId(loginToken.getPersonId());
		List<KhxtPerformanceMembers> list = khxtPerformanceMembersService.find(khxtPerformanceMembers);
		if (!CollectionUtils.isEmpty(list)) {
			KhxtPerformanceMembers members = list.get(0);

			mav.addObject("members", members);

			KhxtAppraiseSheet sheet = members.getAppraiseSheet();

			mav.addObject("sheet", sheet);
		}
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping(value = "/scoringdetail")
	public ModelAndView scoringDetail(KhxtPerformanceMembers khxtPerformanceMembers) throws Exception {

		ModelAndView mav = getModelAndView("/khxt/appraise/scoringdetail");
		LoginToken loginToken = getCurrentToken();
		if (loginToken == null) {
			throw new RepsException("请登录");
		}
		KhxtAppraiseSheet khxtAppraiseSheet = khxtAppraiseSheetService.get(khxtPerformanceMembers.getSheetId(), true);
		khxtAppraiseSheet.setSeason(DateUtil.formatStrDateTime(khxtAppraiseSheet.getSeason(), "yyyyMM", "yyyy年MM月"));
		khxtPerformanceMembers.setBkhrPersonId(loginToken.getPersonId());
		KhxtPerformanceMembers member = khxtPerformanceMembersService.findBkhrScoring(khxtPerformanceMembers);
		mav.addObject("member", member);
		mav.addObject("items", khxtAppraiseSheet.getItem());
		mav.addObject("sheet", khxtAppraiseSheet);
		return mav;
	}
	
}
