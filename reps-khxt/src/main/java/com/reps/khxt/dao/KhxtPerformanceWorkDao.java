package com.reps.khxt.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.IGenericDao;
import com.reps.khxt.entity.KhxtPerformanceWork;

/**
 * 工作计划Dao
 * 
 * @author ：Alex
 * @date 2018年3月27日
 */
@Repository
public class KhxtPerformanceWorkDao {
	@Autowired
	IGenericDao<KhxtPerformanceWork> dao;

	public void save(KhxtPerformanceWork work) {
		dao.save(work);
	}

	public List<KhxtPerformanceWork> findBySheetId(String sheetId) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformanceWork.class);
		if (StringUtils.isBlank(sheetId)) {
			throw new RepsException("考核表ID不能为空");
		}
		dc.add(Restrictions.eq("sheetId", sheetId));
		List<KhxtPerformanceWork> list = dao.findByCriteria(dc);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}

	public void delete(KhxtPerformanceWork khxtPerformanceWork) {
		dao.delete(khxtPerformanceWork);
	}

	public List<KhxtPerformanceWork> find(KhxtPerformanceWork work) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformanceWork.class);
		if (null != work) {
			String sheetId = work.getSheetId();
			if (null != sheetId) {
				dc.add(Restrictions.eq("sheetId", sheetId));
			}
			String personId = work.getPersonId();
			if (null != personId) {
				dc.add(Restrictions.eq("personId", personId));
			}
		}
		return dao.findByCriteria(dc);
	}

	public KhxtPerformanceWork get(String id) {

		return dao.get(KhxtPerformanceWork.class, id);
	}

	public void update(KhxtPerformanceWork work) {
		dao.update(work);
	}
}
