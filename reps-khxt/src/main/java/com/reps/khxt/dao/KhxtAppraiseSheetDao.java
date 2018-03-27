package com.reps.khxt.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtAppraiseSheet;

@Repository
public class KhxtAppraiseSheetDao {

	@Autowired
	IGenericDao<KhxtAppraiseSheet> dao;

	public ListResult<KhxtAppraiseSheet> query(int start, int pagesize, KhxtAppraiseSheet sheet) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtAppraiseSheet.class);
		if (null != sheet) {
			String name = sheet.getName();
			if (StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.eq("name", name));
			}
			String season = sheet.getSeason();
			if (StringUtil.isNotBlank(season)) {
				dc.add(Restrictions.eq("season", season));
			}
			String bkhrId = sheet.getBkhrId();
			if (StringUtil.isNotBlank(bkhrId)) {
				dc.add(Restrictions.eq("bkhrId", bkhrId));
			}

		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}

	public void save(KhxtAppraiseSheet sheet) {
		dao.save(sheet);
	}

	public void delete(KhxtAppraiseSheet sheet) {
		if (StringUtils.isBlank(sheet.getId())) {
			throw new RepsException("删除对象不能为空");
		}
		dao.delete(sheet);
	}
	
	public KhxtAppraiseSheet get(String id) {
		return dao.get(KhxtAppraiseSheet.class, id);
	}

}
