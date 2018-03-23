package com.reps.khxt.dao;


import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtGroup;

/**
 * 考核分组设置dao
 * 
 * @author ：Alex
 * @date 2018年3月21日
 */
@Repository
public class KxhtGroupDao {

	@Autowired
	IGenericDao<KhxtGroup> dao;

	public ListResult<KhxtGroup> query(int start, int pagesize, KhxtGroup khxtGroup) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtGroup.class);

		return dao.query(dc, start, pagesize, Order.asc("name"));
	}

	public void save(KhxtGroup khxtGroup) {
		dao.save(khxtGroup);
	}

	public KhxtGroup get(String id) {
		
		return dao.get(KhxtGroup.class, id);
	}

}
