package com.reps.khxt.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtPerformanceMembersDao;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtKhrProcess;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformancePoint;
import com.reps.khxt.enums.AppraiseStatus;
import com.reps.khxt.enums.MarkStatus;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtKhrProcessService;
import com.reps.khxt.service.IKhxtLevelPersonService;
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

	@Autowired
	private IKhxtLevelPersonService personService;

	@Autowired
	private IKhxtLevelPersonService khxtLevelPersonService;

	@Autowired
	private IKhxtPerformancePointService pointService;

	@Autowired
	private IKhxtAppraiseSheetService khxtAppraiseSheetService;

	@Override
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers khxtPerformanceMembers, boolean eager)
			throws RepsException {
		if (null == khxtPerformanceMembers) {
			throw new RepsException("参数异常");
		}
		/*
		 * String khrPersonId = khxtPerformanceMembers.getKhrPersonId(); if
		 * (StringUtil.isBlank(khrPersonId)) { throw new
		 * RepsException("参数异常:考核人ID为空"); }
		 */
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

	@Override
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers members) throws Exception {
		List<KhxtPerformanceMembers> list = dao.find(members);

		if (null != list && !list.isEmpty()) {
			for (KhxtPerformanceMembers member : list) {
				Hibernate.initialize(member.getPerformancePoints());
			}
		}
		KhxtAppraiseSheet sheet = khxtAppraiseSheetService.get(members.getSheetId(), true);
		// 计算合计
		Map<String, Double> map = calculateScore(sheet);
		// 处理考核人显示
		getJson2String(list, sheet);
		// 处理页面显示指标
		KhxtPerformanceMembers members1 = list.get(0);

		List<KhxtPerformancePoint> KhxtPerformancePoint = members1.getPerformancePoints();
		// 处理页面显示指标和合计
		if (CollectionUtils.isEmpty(KhxtPerformancePoint)) {
			for (KhxtPerformanceMembers formanmembers : list) {
				if (!CollectionUtils.isEmpty(formanmembers.getPerformancePoints())) {
					List<KhxtPerformancePoint> list2 = formanmembers.getPerformancePoints();

					for (KhxtPerformancePoint khxtPerformancePoint2 : list2) {

						KhxtPerformancePoint point = new KhxtPerformancePoint();

						KhxtItem khxtItem = khxtPerformancePoint2.getKhxtItem();

						point.setKhxtItem(khxtItem);
						Double double1 = map.get(khxtItem.getId());
						point.setTotalScore(double1);
						KhxtPerformancePoint.add(point);
					}
					break;
				}
			}
		} else {
			for (KhxtPerformanceMembers performanceMembers : list) {
				List<KhxtPerformancePoint> points = performanceMembers.getPerformancePoints();
				for (KhxtPerformancePoint khxtPerformancePoint : points) {
					String itemId = khxtPerformancePoint.getItemId();
					Double double1 = map.get(itemId);
					khxtPerformancePoint.setTotalScore(double1);
				}
				break;
			}
		}
		return list;
	}

	private List<KhxtPerformanceMembers> getJson2String(List<KhxtPerformanceMembers> list, KhxtAppraiseSheet sheet)
			throws Exception {
		List<Map<String, String>> jsonweight = getJsonWeight(sheet);

		for (KhxtPerformanceMembers members : list) {
			// 根据personid查询
			KhxtLevelPerson person = khxtLevelPersonService.getByPersonId(members.getKhrPersonId());
			if (StringUtils.equals(person.getPersonId(), members.getKhrPerson().getId())) {

				for (Map<String, String> map : jsonweight) {
					if (StringUtils.equals(map.get("levelId"), person.getLevelId())) {
						String name = members.getKhrPerson().getName();
						members.getKhrPerson().setName(name + "(" + map.get("weight") + "%" + ")");
					}
				}
			}
		}

		return list;

	}

	// 计算合计
	private Map<String, Double> calculateScore(KhxtAppraiseSheet sheet) throws Exception {
		// 转换百分比
		NumberFormat nf = NumberFormat.getPercentInstance();

		Map<String, Double> scoreMap = new HashMap<>();

		Set<KhxtItem> item = sheet.getItem();

		List<Map<String, String>> jsonWeight = getJsonWeight(sheet);
		// 封装查询条件
		KhxtPerformancePoint khxtPerformancePoint = new KhxtPerformancePoint();
		KhxtPerformanceMembers members = new KhxtPerformanceMembers();
		members.setSheetId(sheet.getId());

		for (KhxtItem khxtItem : item) {

			// 根据月考核id+分类ID查询评分
			khxtPerformancePoint.setItemId(khxtItem.getId());
			khxtPerformancePoint.setKhxtPerformanceMembers(members);
			List<KhxtPerformancePoint> points = pointService.find(khxtPerformancePoint);
			// 计算总分数
			double score = 0;
			// 遍历级别
			for (Map<String, String> map : jsonWeight) {
				double b = 0;
				int a = 0;
				for (KhxtPerformancePoint point : points) {
					// 查询出该评分属于那个级别
					KhxtPerformanceMembers khxtPerformanceMembers = point.getKhxtPerformanceMembers();
					String personId = khxtPerformanceMembers.getKhrPersonId();
					// 查询该人员属于那个级别
					KhxtLevelPerson levelPerson = khxtLevelPersonService.getByPersonId(personId);
					String personLevelId = levelPerson.getLevelId();

					if (StringUtils.equals(map.get("levelId"), personLevelId)) {
						double parse = (double) nf.parse(map.get("weight") + "%");
						double c = point.getPoint() * parse;
						b += c;
						a++;
					}

				}
				if (a != 0 && b != 0) {
					score += b / a;
				}
			}
			scoreMap.put(khxtItem.getId(), score);

		}
		return scoreMap;
	}

	// 将权重json转化为集合
	private List<Map<String, String>> getJsonWeight(KhxtAppraiseSheet sheet) throws Exception {
		ObjectMapper obj = new ObjectMapper();
		KhxtLevelWeight levelWeight = sheet.getLevelWeight();
		@SuppressWarnings("unchecked")
		List<Map<String, String>> jsonweight = obj.readValue(levelWeight.getWeight(), List.class);
		return jsonweight;
	}

	@Override
	public List<String> findByGroup(KhxtPerformanceMembers members) throws RepsException {
		return dao.findByGroup(members);
	}

	/**
	 * 被考核人查询
	 */
	public List<KhxtPerformanceMembers> query(KhxtPerformanceMembers khxtPerformanceMembers) throws Exception {
		List<KhxtPerformanceMembers> listResult = dao.find(khxtPerformanceMembers);

		List<KhxtPerformanceMembers> listMembers = new ArrayList<>();

		// 过滤重复数据
		Map<String, KhxtPerformanceMembers> map = new LinkedHashMap<>();
		for (KhxtPerformanceMembers members : listResult) {

			map.put(members.getSheetId() + members.getBkhrPersonId(), members);
		}

		for (String key : map.keySet()) {

			KhxtPerformanceMembers members = map.get(key);
			KhxtLevelPerson person = personService.getByPersonId(members.getBkhrPersonId());

			members.setPersonOrganize(person.getOrganize().getName());

			// 计算本月得分
			Map<String, Double> score = calculateScore(members.getAppraiseSheet());
			double total = 0;
			for (String scorekey : score.keySet()) {
				total += score.get(scorekey);
			}
			members.setTotalPoints(total);

			listMembers.add(members);
		}

		return listMembers;
	}

}
