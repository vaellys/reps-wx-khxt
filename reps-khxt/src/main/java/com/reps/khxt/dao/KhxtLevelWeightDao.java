package com.reps.khxt.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtLevelWeight;

/**
 * 
 * @ClassName: KhxtLevelWeightDao
 * @Description: 级别权重DAO
 * @author qianguobing
 * @date 2018年3月20日 下午4:47:01
 */
@Repository
public class KhxtLevelWeightDao {
	
	@Autowired
	IGenericDao<KhxtLevelWeight> dao;
	
	public void save(KhxtLevelWeight khxtLevelWeight) {
		dao.save(khxtLevelWeight);
	}
	
	public void delete(KhxtLevelWeight khxtLevelWeight) {
		dao.delete(khxtLevelWeight);
	}
	
	public void update(KhxtLevelWeight khxtLevelWeight) {
		dao.update(khxtLevelWeight);
	}
	
	public KhxtLevelWeight get(String id) {
		return dao.get(KhxtLevelWeight.class, id);
	}

	public ListResult<KhxtLevelWeight> query(int start, int pagesize, KhxtLevelWeight khxtLevelWeight) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtLevelWeight.class);
		if (null != khxtLevelWeight) {
			String name = khxtLevelWeight.getName();
			if(StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}
	
	public List<KhxtLevelWeight> find(KhxtLevelWeight khxtLevelWeight) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtLevelWeight.class);
		if (null != khxtLevelWeight) {
			String name = khxtLevelWeight.getName();
			if(StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.eq("name", name));
			}
		}
		return dao.findByCriteria(dc);
	}
	
}
