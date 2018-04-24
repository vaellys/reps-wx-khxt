package com.reps.khxt.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.khxt.entity.KhxtBkhrPoints;

@Repository
public class KhxtBkhrPointsDao {

	@Autowired
	private IGenericDao<KhxtBkhrPoints> dao;

	public void save(KhxtBkhrPoints bkhrPoints) {
		dao.save(bkhrPoints);
	}
}
