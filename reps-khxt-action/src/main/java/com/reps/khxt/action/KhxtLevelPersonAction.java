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
import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.vo.UserVo;
import com.reps.system.entity.User;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/levelperson")
public class KhxtLevelPersonAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtLevelPersonAction.class);

	@Autowired
	IKhxtLevelPersonService khxtLevelPersonService;

	@RequestMapping(value = "/save")
	@ResponseBody
	public Object save(KhxtLevelPerson khxtLevelPerson) {
		try {
			String personIds = khxtLevelPerson.getPersonIds();
			if (StringUtil.isBlank(personIds)) {
				return ajax(AjaxStatus.FAIL, "请选择要分配的用户");
			}
			khxtLevelPersonService.saveAll(khxtLevelPerson);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtLevelPerson khxtLevelPerson) {
		ModelAndView mav = getModelAndView("/khxt/levelperson/list");
		ListResult<KhxtLevelPerson> listResult = khxtLevelPersonService.query(pager.getStartRow(), pager.getPageSize(), khxtLevelPerson);
		mav.addObject("levelId", khxtLevelPerson.getLevelId());
		mav.addObject("levelPerson", khxtLevelPerson);
		// 分页数据
		mav.addObject("list", listResult.getList());
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@RequestMapping({ "/multiple" })
	public ModelAndView multiple(Pagination pager, User user, String dialogId, String showName, String hideName, String hideNameValue, String callBack, String levelId) throws RepsException {
		ModelAndView mav = new ModelAndView("/khxt/levelperson/chooseuser");
		ListResult<UserVo> listResult = khxtLevelPersonService.choosePerson(pager.getStartRow(), pager.getPageSize(), user, levelId);
		pager.setTotalRecord(listResult.getCount().longValue());

		mav.addObject("list", listResult.getList());

		mav.addObject("pager", pager);

		mav.addObject("user", user);
		mav.addObject("levelId", levelId);
		mav.addObject("dialogId", dialogId);
		mav.addObject("showName", showName);
		mav.addObject("hideName", hideName);
		mav.addObject("hideNameValue", hideNameValue);
		mav.addObject("callBack", callBack);
		mav.addObject("admins", RepsConstant.getAdmins());
		return mav;
	}

	@RequestMapping({ "/delete" })
	@ResponseBody
	public Object delete(String levelPersonIds) {
		if (StringUtil.isBlank(levelPersonIds)) {
			return ajax(AjaxStatus.FAIL, "请选择要删除的用户");
		}
		try {
			this.khxtLevelPersonService.deleteAll(levelPersonIds);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

}
