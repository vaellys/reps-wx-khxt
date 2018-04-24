package com.reps.khxt.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.reps.core.orm.IGenericDao;
import com.reps.khxt.entity.KhxtBkhrPoints;

@Repository
public class KhxtBkhrPointsDao {

	@Autowired
	private IGenericDao<KhxtBkhrPoints> dao;

	public void save(KhxtBkhrPoints bkhrPoints) {
		dao.save(bkhrPoints);
	}

	public KhxtBkhrPoints getBySheetId(String sheetId) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtBkhrPoints.class);
		dc.createAlias("appraiseSheet", "sheet");
		if (StringUtils.isBlank(sheetId)) {
			return null;
		}
		dc.add(Restrictions.eq("sheetId", sheetId));
		List<KhxtBkhrPoints> list = dao.findByCriteria(dc);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}

	public void update(KhxtBkhrPoints points) {
		dao.update(points);
	}
}
