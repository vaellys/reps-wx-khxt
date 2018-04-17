package com.reps.khxt.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.reps.core.orm.IGenericDao;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtPerformancePoint;

@Repository
public class KhxtPerformancePointDao {
	
	@Autowired
	IGenericDao<KhxtPerformancePoint> dao;
	
	public void save(KhxtPerformancePoint khxtPerformancePoint) {
		dao.save(khxtPerformancePoint);
	}
	
	public void delete(KhxtPerformancePoint khxtPerformancePoint) {
		dao.delete(khxtPerformancePoint);
	}
	
	public void update(KhxtPerformancePoint khxtPerformancePoint) {
		dao.update(khxtPerformancePoint);
	}
	
	public KhxtPerformancePoint get(String id) {
		return dao.get(KhxtPerformancePoint.class, id);
	}

	public List<KhxtPerformancePoint> findByMemberId(String id) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformancePoint.class);
		
		if (StringUtil.isNotBlank(id)) {
				dc.add(Restrictions.eq("memberId", id));
			}else{
				return null;
			}
		List<KhxtPerformancePoint> list = dao.findByCriteria(dc);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list;
	}

	public void deleteByMemberId(String id) {
		List<KhxtPerformancePoint> list = findByMemberId(id);
		if(!CollectionUtils.isEmpty(list)){
			for (KhxtPerformancePoint khxtPerformancePoint : list) {
				delete(khxtPerformancePoint);
			}
		}
	}

}
