package com.reps.khxt.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtLevelWeight;

@Repository
public class KhxtAppraiseSheetDao {

	@Autowired
	private IGenericDao<KhxtAppraiseSheet> dao;

	public ListResult<KhxtAppraiseSheet> query(int start, int pagesize, KhxtAppraiseSheet sheet) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtAppraiseSheet.class);
		if (sheet.isCheckKhr()) {
			dc.createAlias("khrProcessList", "s");
		}
		if (null != sheet) {
			String name = sheet.getName();
			if (StringUtil.isNotBlank(name)) {
				dc.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
			}
			String khrId = sheet.getKhrId();
			if (StringUtil.isNotBlank(khrId)) {
				dc.add(Restrictions.like("khrId", khrId, MatchMode.ANYWHERE));
			}
			String season = sheet.getSeason();
			if (StringUtil.isNotBlank(season)) {
				dc.add(Restrictions.eq("season", season));
			}
			String bkhrId = sheet.getBkhrId();
			if (StringUtil.isNotBlank(bkhrId)) {
				dc.add(Restrictions.eq("bkhrId", bkhrId));
			}
			Short progress = sheet.getProgress();
			if (progress != null) {
				dc.add(Restrictions.eq("progress", progress));
			}
			if (sheet.isCheckKhr()) {
				// 查询考核人打分情况
				String khrPersonId = sheet.getKhrPersonId();
				if (StringUtil.isNotBlank(khrPersonId)) {
					dc.add(Restrictions.eq("s.khrPersonId", khrPersonId));
				}
				Integer status = sheet.getStatus();
				if (null != status) {
					dc.add(Restrictions.eq("s.status", status));
				}
			}

		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}

	public void save(KhxtAppraiseSheet sheet) {
		dao.save(sheet);
	}

	public void delete(KhxtAppraiseSheet sheet) throws Exception {

		dao.delete(sheet);
	}

	public KhxtAppraiseSheet get(String sheetId) {
		KhxtAppraiseSheet sheet = dao.get(KhxtAppraiseSheet.class, sheetId);
		return sheet;
	}

	public KhxtAppraiseSheet get(String sheetId, boolean b) {
		KhxtAppraiseSheet sheet = dao.get(KhxtAppraiseSheet.class, sheetId);
		if (b) {
			Hibernate.initialize(sheet.getItem());
		}

		return sheet;
	}

	public void update(KhxtAppraiseSheet khxtAppraiseSheet) {
		dao.update(khxtAppraiseSheet);
	}

	public List<KhxtAppraiseSheet> find(KhxtAppraiseSheet khxtAppraiseSheet) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtAppraiseSheet.class);
		dc.createAlias("levelWeight", "w");
		if (null != khxtAppraiseSheet) {
			KhxtLevelWeight levelWeight = khxtAppraiseSheet.getLevelWeight();
			if (null != levelWeight) {
				String id = levelWeight.getId();
				if (StringUtil.isNotBlank(id)) {
					dc.add(Restrictions.eq("w.id", id));
				}
			}
		}
		return dao.findByCriteria(dc);
	}

}
