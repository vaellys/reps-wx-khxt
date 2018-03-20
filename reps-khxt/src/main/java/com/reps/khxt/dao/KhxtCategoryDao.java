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
import com.reps.khxt.entity.KhxtCategory;

/**
 * @ClassName: KhxtCategoryDao
 * @Description: 指标分类设置DAO
 * @author qianguobing
 * @date 2018年3月13日 下午5:21:41
 */
@Repository
public class KhxtCategoryDao {
	
	@Autowired
	IGenericDao<KhxtCategory> dao;
	
	public void save(KhxtCategory khxtCategory) {
		dao.save(khxtCategory);
	}
	
	public void delete(KhxtCategory khxtCategory) {
		dao.delete(khxtCategory);
	}
	
	public void update(KhxtCategory khxtCategory) {
		dao.update(khxtCategory);
	}
	
	public KhxtCategory get(String id) {
		return dao.get(KhxtCategory.class, id);
	}

	public ListResult<KhxtCategory> query(int start, int pagesize, KhxtCategory khxtCategory) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtCategory.class);
		if (null != khxtCategory) {
			String name = khxtCategory.getName();
			if(StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}
	
	public List<KhxtCategory> find(KhxtCategory khxtCategory) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtCategory.class);
		if (null != khxtCategory) {
			String name = khxtCategory.getName();
			if(StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.eq("name", name));
			}
		}
		return dao.findByCriteria(dc);
	}
	
}
