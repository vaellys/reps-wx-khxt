package com.reps.khxt.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.LoginToken;
import com.reps.core.RepsConstant;
import com.reps.core.commons.Pagination;
import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtPerformanceMembersService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/member")
public class KhxtPerformanceMembersAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtPerformanceMembersAction.class);

	@Autowired
	IKhxtPerformanceMembersService khxtPerformanceMembersService;
	
	@Autowired
	IKhxtAppraiseSheetService khxtAppraiseSheetService;
	
	@RequestMapping(value = "/appraisepoint")
	public Object list(Pagination pager, KhxtPerformanceMembers khxtPerformanceMembers) {
		ModelAndView mav = getModelAndView("/khxt/member/appraisepoint");
		try {
			LoginToken currentToken = this.getCurrentToken();
			if(null == currentToken) {
				throw new RepsException("您还没有登陆！");
			}
			//String personId = currentToken.getPersonId();
			String personId = "4028daac5acc24f8015acfaaf3d70021";
			if(StringUtil.isBlank(personId)) {
				throw new RepsException("该登陆人不是考核人！");
			}
			KhxtAppraiseSheet khxtAppraiseSheet = khxtAppraiseSheetService.get(khxtPerformanceMembers.getSheetId(), true);
			mav.addObject("items", khxtAppraiseSheet.getItem());
			//设置考核人ID
			khxtPerformanceMembers.setKhrPersonId(personId);
			
			List<KhxtPerformanceMembers> results = khxtPerformanceMembersService.find(khxtPerformanceMembers);
			mav.addObject("performanceMembers", results);
			mav.addObject("khrPersonName", currentToken.getName());
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(String memberJson, String itemPointJson) {
		try {
			khxtPerformanceMembersService.updateAndParseJson(memberJson, itemPointJson);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
}
