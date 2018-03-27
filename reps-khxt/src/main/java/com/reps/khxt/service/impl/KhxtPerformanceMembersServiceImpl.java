package com.reps.khxt.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtPerformanceMembersDao;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformancePoint;
import com.reps.khxt.enums.AppraiseStatus;
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
	
	@Override
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException {
		if (null == khxtPerformanceMembers) {
			throw new RepsException("参数异常");
		}
		String khrPersonId = khxtPerformanceMembers.getKhrPersonId();
		if (StringUtil.isBlank(khrPersonId)) {
			throw new RepsException("参数异常:考核人ID为空");
		}
		/*Short status = khxtPerformanceMembers.getStatus();
		if (null == status) {
			throw new RepsException("参数异常:考核状态为空");
		}*/
		return dao.find(khxtPerformanceMembers);
	}

	protected boolean checkKhr(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException {
		if(StringUtil.isNotBlank(khxtPerformanceMembers.getKhrPersonId())) {
			List<KhxtPerformanceMembers> find = dao.find(khxtPerformanceMembers);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateAndParseJson(String memberJson, String itemPointJson) throws RepsException {
		if(StringUtil.isBlank(memberJson) || StringUtil.isBlank(itemPointJson)) {
			throw new RepsException("参数异常");
		}
		
		JSONArray memberArray = JSONArray.fromObject(memberJson);
		if(null != memberArray && !memberArray.isEmpty()) {
			for (Iterator<JSONObject> iterator = memberArray.iterator(); iterator.hasNext();) {
				JSONObject memberObj = iterator.next();
				KhxtPerformanceMembers member = new KhxtPerformanceMembers();
				member.setId(memberObj.getString("memberId"));
				member.setAppraiseTime(new Date());
				member.setTotalPoints(memberObj.getDouble("totalPoint"));
				member.setStatus(AppraiseStatus.APPRAISED.getId());
				//更新人员名单状态信息
				update(member);
			}
		}
		
		JSONArray itemPointArray = JSONArray.fromObject(itemPointJson);
		if(null != itemPointArray && !itemPointJson.isEmpty()) {
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
				khxtPerformancePointService.save(point);
			}
		}
		
	}

	@Override
	public void update(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException {
		if(null == khxtPerformanceMembers) {
			throw new RepsException("参数异常");
		}
		KhxtPerformanceMembers member = this.get(khxtPerformanceMembers.getId());
		Double totalPoints = khxtPerformanceMembers.getTotalPoints();
		if(null != totalPoints) {
			member.setTotalPoints(totalPoints);
		}
		Date appraiseTime = khxtPerformanceMembers.getAppraiseTime();
		if(null != appraiseTime) {
			member.setAppraiseTime(appraiseTime);
		}
		Short status = khxtPerformanceMembers.getStatus();
		if(null != status) {
			member.setStatus(status);
		}
		dao.update(member);
	}

	@Override
	public KhxtPerformanceMembers get(String id) throws RepsException {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("ID为空");
		}
		KhxtPerformanceMembers khxtPerformanceMembers = dao.get(id);
		if(null == khxtPerformanceMembers) {
			throw new RepsException("参数异常:ID无效");
		}
		return khxtPerformanceMembers;
	}

}
