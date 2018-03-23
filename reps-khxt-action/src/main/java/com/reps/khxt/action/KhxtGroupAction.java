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
import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.service.IKhxGroupService;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.khxt.vo.UserVo;
import com.reps.system.entity.User;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/group")
public class KhxtGroupAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtCategoryAction.class);

	@Autowired
	private IKhxGroupService kxhtGroupService;

	@Autowired
	private IKhxtLevelService levelService;

	@Autowired
	private IKhxtLevelPersonService levelPersonService;

	/**
	 * 考核分组列表
	 * 
	 * @author Alex
	 * @date 2018年3月21日
	 * @param pager
	 * @param khxtCategory
	 * @return
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Pagination pager, KhxtGroup khxtGroup) {
		ModelAndView mav = getModelAndView("/khxt/group/list");
		ListResult<KhxtGroup> listResult = kxhtGroupService.query(pager.getStartRow(), pager.getPageSize(), khxtGroup);
		// 分页数据
		mav.addObject("list", listResult.getList());
		// 分页参数
		pager.setTotalRecord(listResult.getCount().longValue());
		mav.addObject("pager", pager);
		return mav;
	}

	@RequestMapping(value = "/toadd")
	public Object toAdd() {
		ModelAndView mav = getModelAndView("/khxt/group/add");
		Map<String, String> LevelMap = buildLevelMap(levelService.findAll());
		try {
			mav.addObject("LevelMap", LevelMap);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtGroup khxtGroup) {
		try {
			kxhtGroupService.save(khxtGroup);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id) {
		ModelAndView mav = getModelAndView("/khxt/group/edit");
		KhxtGroup khxtGroup = kxhtGroupService.get(id);
		mav.addObject("khxtGroup", khxtGroup);
		return mav;
	}
	
	private Map<String, String> buildLevelMap(List<KhxtLevel> levelList) {
		Map<String, String> levelMap = new HashMap<>();
		levelMap.put("", "全部");
		for (KhxtLevel khxtlevel : levelList) {
			levelMap.put(khxtlevel.getId(), khxtlevel.getName());
		}
		return levelMap;
	}

	@RequestMapping({ "/multiple" })
	public ModelAndView multiple(Pagination pager, User user, String dialogId, String showName, String hideName,
			String hideNameValue, String callBack, KhxtLevelPerson levelid) throws RepsException {
		ModelAndView mav = new ModelAndView("/khxt/levelperson/chooseuser");
//		List<KhxtLevelPerson> listResult = levelPersonService.choosePerson(levelid);
//
//		mav.addObject("list", listResult);

		mav.addObject("user", user);
		mav.addObject("levelId", levelid.getLevelId());
		mav.addObject("dialogId", dialogId);
		mav.addObject("showName", showName);
		mav.addObject("hideName", hideName);
		mav.addObject("hideNameValue", hideNameValue);
		mav.addObject("callBack", callBack);
		mav.addObject("admins", RepsConstant.getAdmins());
		return mav;
	}
}
