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
import com.reps.khxt.entity.KhxtItem;

/**
 * @ClassName: KhxtItemDao
 * @Description: 指标定义DAO
 * @author qianguobing
 * @date 2018年3月13日 下午5:21:41
 */
@Repository
public class KhxtItemDao {
	
	@Autowired
	IGenericDao<KhxtItem> dao;
	
	public void save(KhxtItem khxtItem) {
		dao.save(khxtItem);
	}
	
	public void delete(KhxtItem khxtItem) {
		dao.delete(khxtItem);
	}
	
	public void update(KhxtItem khxtItem) {
		dao.update(khxtItem);
	}
	
	public KhxtItem get(String id) {
		return dao.get(KhxtItem.class, id);
	}

	public ListResult<KhxtItem> query(int start, int pagesize, KhxtItem khxtItem) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtItem.class);
		dc.createAlias("khxtCategory", "t");
		if (null != khxtItem) {
			String name = khxtItem.getName();
			if(StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
			}
			String categoryId = khxtItem.getCategoryId();
			if (StringUtil.isNotBlank(categoryId)) {
				dc.add(Restrictions.eq("t.id", categoryId));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}
	
	public List<KhxtItem> find(KhxtItem khxtItem) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtItem.class);
		if (null != khxtItem) {
			String name = khxtItem.getName();
			if(StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.eq("name", name));
			}
			String categoryId = khxtItem.getCategoryId();
			if(StringUtil.isNotBlank(categoryId)) {
				dc.add(Restrictions.eq("categoryId", categoryId));
			}
		}
		return dao.findByCriteria(dc);
	}
	
}
