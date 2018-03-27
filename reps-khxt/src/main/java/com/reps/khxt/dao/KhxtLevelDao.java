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
import com.reps.khxt.entity.KhxtLevel;

/**
 * @ClassName: KhxtLevelDao
 * @Description: 级别设置DAO
 * @author qianguobing
 * @date 2018年3月13日 下午5:21:41
 */
@Repository
public class KhxtLevelDao {

	@Autowired
	IGenericDao<KhxtLevel> dao;

	public void save(KhxtLevel khxtLevel) {
		dao.save(khxtLevel);
	}

	public void delete(KhxtLevel khxtLevel) {
		dao.delete(khxtLevel);
	}

	public void update(KhxtLevel khxtLevel) {
		dao.update(khxtLevel);
	}

	public KhxtLevel get(String id) {
		return dao.get(KhxtLevel.class, id);
	}

	public ListResult<KhxtLevel> query(int start, int pagesize, KhxtLevel khxtLevel) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtLevel.class);
		if (null != khxtLevel) {
			String name = khxtLevel.getName();
			if (StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("level"));
	}

	public List<KhxtLevel> find() {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtLevel.class);
		dc.addOrder(Order.asc("level"));
		return dao.findByCriteria(dc);
	}

	public List<KhxtLevel> findByPower(Short[] a) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtLevel.class);

		dc.add(Restrictions.in("power", a));
		return dao.findByCriteria(dc);
	}

}
