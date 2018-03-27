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
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.service.IKhxtGroupService;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.system.entity.User;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/group")
public class KhxtGroupAction extends BaseAction {

	protected final Logger logger = LoggerFactory.getLogger(KhxtCategoryAction.class);

	@Autowired
	private IKhxtGroupService kxhtGroupService;

	@Autowired
	private IKhxtLevelService khxtlevelService;

	@Autowired
	private IKhxtLevelPersonService khxtlevelPersonService;

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
		Map<String, String> LevelMap = buildLevelMap(khxtlevelService.listKhxtLevel());
		try {
			mav.addObject("LevelMap", LevelMap);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	/**
	 * 添加考核分组
	 * 
	 * @author Alex
	 * @date 2018年3月22日
	 * @param group
	 * @param khrIds
	 * @param bkhrIds
	 * @return
	 * @return Object
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Object add(KhxtGroup group, String khrIds, String bkhrIds) {
		try {
			kxhtGroupService.save(group, khrIds, bkhrIds);
			return ajax(AjaxStatus.OK, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/toedit")
	public ModelAndView toEdit(String id) throws Exception {
		ModelAndView mav = getModelAndView("/khxt/group/edit");
		KhxtGroup group = kxhtGroupService.get(id);

		Map<String, String> LevelMap = buildLevelMap(khxtlevelService.findAll());

		mav.addObject("LevelMap", LevelMap);

		mav.addObject("group", group);

		return mav;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public Object edit(KhxtGroup group, String khrIds, String bkhrIds) {
		try {
			kxhtGroupService.update(group,khrIds,bkhrIds);
			return ajax(AjaxStatus.OK, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object delete(KhxtGroup khxtgroup) {
		try {
			kxhtGroupService.delete(khxtgroup);
			return ajax(AjaxStatus.OK, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除分类失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}

	private Map<String, String> buildLevelMap(List<KhxtLevel> levelList) {
		Map<String, String> levelMap = new HashMap<>();
		levelMap.put("", "全部");
		for (KhxtLevel khxtlevel : levelList) {
			levelMap.put(khxtlevel.getId(), khxtlevel.getName());
		}
		return levelMap;
	}

	@RequestMapping(value = "/khrlistlevel")
	public ModelAndView khrList(User user, String dialogId, String showName, String hideName, String hideNameValue,
			String callBack, KhxtLevelPerson khxtLevelPerson) {
		ModelAndView mav = getModelAndView("/khxt/group/khrperson");
		List<KhxtLevelPerson> listResult = khxtlevelPersonService.findLevelPerson(khxtLevelPerson);
		// 分页数据
		mav.addObject("list", listResult);

		mav.addObject("user", user);
		mav.addObject("levelId", khxtLevelPerson.getLevelId());
		mav.addObject("dialogId", dialogId);
		mav.addObject("showName", showName);
		mav.addObject("hideName", hideName);
		mav.addObject("hideNameValue", hideNameValue);
		mav.addObject("callBack", callBack);
		mav.addObject("admins", RepsConstant.getAdmins());

		return mav;
	}

	@RequestMapping(value = "/bkhrlistlevel")
	public ModelAndView bkhrList(User user, String dialogId, String showName, String hideName, String hideNameValue,
			String callBack, KhxtLevelPerson khxtLevelPerson) {
		ModelAndView mav = getModelAndView("/khxt/group/bkhrperson");
		List<KhxtLevelPerson> listResult = khxtlevelPersonService.findLevelPerson(khxtLevelPerson);
		// 分页数据
		mav.addObject("list", listResult);

		mav.addObject("user", user);
		mav.addObject("levelId", khxtLevelPerson.getLevelId());
		mav.addObject("dialogId", dialogId);
		mav.addObject("showName", showName);
		mav.addObject("hideName", hideName);
		mav.addObject("hideNameValue", hideNameValue);
		mav.addObject("callBack", callBack);
		mav.addObject("admins", RepsConstant.getAdmins());

		return mav;
	}
}
