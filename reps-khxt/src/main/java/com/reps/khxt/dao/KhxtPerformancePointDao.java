package com.reps.khxt.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.khxt.entity.KhxtPerformancePoint;

/**
 * 
 * @ClassName: KhxtPerformancePointDao
 * @Description: TODO
 * @author qianguobing
 * @date 2018年3月27日 下午2:04:32
 */
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

}
