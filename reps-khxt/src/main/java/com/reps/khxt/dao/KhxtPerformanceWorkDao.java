package com.reps.khxt.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	
	public List<KhxtPerformanceWork> find(KhxtPerformanceWork work) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformanceWork.class);
		if (null != work) {
			String sheetId = work.getSheetId();
			if(null != sheetId) {
				dc.add(Restrictions.eq("sheetId", sheetId));
			}
			String personId = work.getPersonId();
			if(null != personId) {
				dc.add(Restrictions.eq("personId", personId));
			}
		}
		return dao.findByCriteria(dc);
	}
	
}
