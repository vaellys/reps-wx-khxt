package com.reps.khxt.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtPerformanceMembersDao;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtKhrProcess;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformancePoint;
import com.reps.khxt.enums.AppraiseStatus;
import com.reps.khxt.enums.MarkStatus;
import com.reps.khxt.service.IKhxtKhrProcessService;
import com.reps.khxt.service.IKhxtPerformanceMembersService;
import com.reps.khxt.service.IKhxtPerformancePointService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class KhxtPerformanceMembersServiceImpl implements IKhxtPerformanceMembersService {

	protected final Logger logger = LoggerFactory.getLogger(KhxtPerformanceMembersServiceImpl.class);

	@Autowired
	private KhxtPerformanceMembersDao dao;

	@Autowired
	private IKhxtPerformancePointService khxtPerformancePointService;

	@Autowired
	private IKhxtKhrProcessService khxtKhrProcessService;

	@Override
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers khxtPerformanceMembers, boolean eager)
			throws RepsException {
		if (null == khxtPerformanceMembers) {
			throw new RepsException("参数异常");
		}
		String khrPersonId = khxtPerformanceMembers.getKhrPersonId();
		if (StringUtil.isBlank(khrPersonId)) {
			throw new RepsException("参数异常:考核人ID为空");
		}
		List<KhxtPerformanceMembers> resultList = dao.find(khxtPerformanceMembers);
		if (eager) {
			if (null != resultList && !resultList.isEmpty()) {
				for (KhxtPerformanceMembers member : resultList) {
					Hibernate.initialize(member.getPerformancePoints());
				}
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateAndParseJson(String memberJson, String itemPointJson,
			KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException {
		if (StringUtil.isBlank(memberJson) || StringUtil.isBlank(itemPointJson)) {
			throw new RepsException("参数异常");
		}
		// 更新考核人员名单状态信息
		JSONArray memberArray = JSONArray.fromObject(memberJson);
		if (null != memberArray && !memberArray.isEmpty()) {
			for (Iterator<JSONObject> iterator = memberArray.iterator(); iterator.hasNext();) {
				JSONObject memberObj = iterator.next();
				KhxtPerformanceMembers member = new KhxtPerformanceMembers();
				member.setId(memberObj.getString("memberId"));
				member.setAppraiseTime(new Date());
				member.setTotalPoints(memberObj.getDouble("totalPoint"));
				member.setStatus(AppraiseStatus.APPRAISED.getId());
				// 更新人员名单状态信息
				update(member);
			}
		}
		// 更新个人考核评分信息
		JSONArray itemPointArray = JSONArray.fromObject(itemPointJson);
		if (null != itemPointArray && !itemPointJson.isEmpty()) {
			for (Iterator<JSONObject> iterator = itemPointArray.iterator(); iterator.hasNext();) {
				JSONObject itemPointObj = iterator.next();
				KhxtPerformancePoint point = new KhxtPerformancePoint();
				KhxtPerformanceMembers member = new KhxtPerformanceMembers();
				member.setId(itemPointObj.getString("memberId"));
				point.setKhxtPerformanceMembers(member);
				KhxtItem item = new KhxtItem();
				item.setId(itemPointObj.getString("itemId"));
				point.setKhxtItem(item);
				point.setPoint(itemPointObj.getDouble("point"));
				if (!itemPointObj.containsKey("pointId") || StringUtil.isBlank(itemPointObj.getString("pointId"))) {
					khxtPerformancePointService.save(point);
				} else {
					point.setId(itemPointObj.getString("pointId"));
					khxtPerformancePointService.update(point);
				}
			}
		}
		// 若被考核人上报情况都已打分则更新打分情况状态
		if (this.checkAppraiseFinished(khxtPerformanceMembers)) {
			KhxtKhrProcess khxtKhrProcess = new KhxtKhrProcess();
			khxtKhrProcess.setSheetId(khxtPerformanceMembers.getSheetId());
			khxtKhrProcess.setKhrPersonId(khxtPerformanceMembers.getKhrPersonId());
			khxtKhrProcess.setStatus(MarkStatus.FINISHED_MARKING.getId().intValue());
			khxtKhrProcessService.updateStatus(khxtKhrProcess);
		}

	}

	@Override
	public void update(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException {
		if (null == khxtPerformanceMembers) {
			throw new RepsException("参数异常");
		}
		KhxtPerformanceMembers member = this.get(khxtPerformanceMembers.getId());
		Double totalPoints = khxtPerformanceMembers.getTotalPoints();
		if (null != totalPoints) {
			member.setTotalPoints(totalPoints);
		}
		Date appraiseTime = khxtPerformanceMembers.getAppraiseTime();
		if (null != appraiseTime) {
			member.setAppraiseTime(appraiseTime);
		}
		Short status = khxtPerformanceMembers.getStatus();
		if (null != status) {
			member.setStatus(status);
		}
		dao.update(member);
	}

	@Override
	public KhxtPerformanceMembers get(String id) throws RepsException {
		if (StringUtil.isBlank(id)) {
			throw new RepsException("人员名单ID为空");
		}
		KhxtPerformanceMembers khxtPerformanceMembers = dao.get(id);
		if (null == khxtPerformanceMembers) {
			throw new RepsException("参数异常:人员名单ID无效");
		}
		return khxtPerformanceMembers;
	}

	@Override
	public List<KhxtPerformanceMembers> findByBkhrPersonId(KhxtPerformanceMembers members) {

		return dao.find(members);
	}

	@Override
	public boolean checkAppraiseFinished(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException {
		String sheetId = khxtPerformanceMembers.getSheetId();
		if (StringUtil.isBlank(sheetId)) {
			throw new RepsException("参数异常:考核表ID为空");
		}
		String khrPersonId = khxtPerformanceMembers.getKhrPersonId();
		if (StringUtil.isBlank(khrPersonId)) {
			throw new RepsException("参数异常:考核人ID为空");
		}
		return 0 < dao.count(sheetId, khrPersonId, AppraiseStatus.UN_REPORTED.getId(), AppraiseStatus.REPORTED.getId())
				? false : true;
	}

}
