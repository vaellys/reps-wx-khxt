package com.reps.khxt.service.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.reps.core.exception.RepsException;
import com.reps.khxt.dao.KhxtBkhrPointsDao;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtBkhrPoints;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtBkhrPointsService;
import com.reps.khxt.service.IKhxtPerformanceMembersService;

@Transactional
@Service
public class KhxtBkhrPointsServiceImpl implements IKhxtBkhrPointsService {

	@Autowired
	private IKhxtAppraiseSheetService sheetService;

	@Autowired
	private IKhxtPerformanceMembersService membersService;

	@Autowired
	private KhxtBkhrPointsDao bkhrPointsDao;

	@Override
	public void countScore(KhxtPerformanceMembers members) throws Exception {
		/*if (StringUtils.isBlank(members.getSheetId())) {
			throw new RepsException("考核表ID不能为空！");
		}
		KhxtAppraiseSheet sheet = sheetService.get(members.getSheetId());

		if (sheet == null) {
			throw new RepsException("考核表不存在！");
		}

		List<KhxtPerformanceMembers> list = membersService.find(members, false);
		if (CollectionUtils.isEmpty(list)) {
			throw new RepsException("该人员不在考核范围内！");
		}

		Map<String, Double> map = membersService.calculateScore(sheet);
		double score = 0;
		for (String key : map.keySet()) {
			score += map.get(key);
		}
		// 保存合计总分
		KhxtBkhrPoints bkhrPoints = new KhxtBkhrPoints();
		bkhrPoints.setAppraiseSheet(sheet);
		bkhrPoints.setSheetId(sheet.getId());
		bkhrPoints.setTotalPoints((float) score);
		bkhrPoints.setBkhrPersonId(members.getBkhrPersonId());
		bkhrPointsDao.save(bkhrPoints);*/
	}

}
