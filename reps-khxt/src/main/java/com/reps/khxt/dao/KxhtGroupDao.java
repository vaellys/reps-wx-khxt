package com.reps.khxt.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtItem;

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

	public void save(KhxtGroup group) {
		dao.save(group);
	}

	public void delete(KhxtGroup khxtgroup) {
		dao.delete(khxtgroup);
	}

	public KhxtGroup get(String id) {
		if (StringUtils.isNotBlank(id)) {
			KhxtGroup group = dao.get(KhxtGroup.class, id);
			return group;
		}
		return null;
	}

	public void update(KhxtGroup group) {
		dao.update(group);
	}

	public List<KhxtGroup> getByLvelId(String khrid) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtGroup.class);
		if (StringUtils.isNotBlank(khrid)) {
			dc.add(Restrictions.eq("khrId", khrid));
		}
		List<KhxtGroup> list = dao.findByCriteria(dc);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}

}
