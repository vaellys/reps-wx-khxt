package com.reps.khxt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reps.khxt.dao.KhxtBkhrPointsDao;
import com.reps.khxt.entity.KhxtBkhrPoints;
import com.reps.khxt.service.IKhxtBkhrPointsService;

@Service
@Transactional
public class KhxtBkhrPointsServiceImpl implements IKhxtBkhrPointsService {

	@Autowired
	private KhxtBkhrPointsDao bkhrPointsDao;

	@Override
	public void save(KhxtBkhrPoints bkhrPoints) {
		KhxtBkhrPoints points = bkhrPointsDao.getBySheetId(bkhrPoints.getSheetId());
		if (points != null) {
			if (points.getTotalPoints() != bkhrPoints.getTotalPoints()) {
				points.setTotalPoints(bkhrPoints.getTotalPoints());
				bkhrPointsDao.update(points);
			}
		} else {
			bkhrPointsDao.save(bkhrPoints);
		}
	}

}
