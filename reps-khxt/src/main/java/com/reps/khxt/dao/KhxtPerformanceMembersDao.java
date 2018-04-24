package com.reps.khxt.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.IGenericDao;
import com.reps.core.util.DateUtil;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.system.entity.Person;

/**
 * 
 * @ClassName: KhxtPerformanceMembersDao
 * @Description: 考核人员名单DAO
 * @author qianguobing
 * @date 2018年3月24日 下午2:15:18
 */
@Repository
public class KhxtPerformanceMembersDao {

	@Autowired
	IGenericDao<KhxtPerformanceMembers> dao;

	public void save(KhxtPerformanceMembers khxtPerformanceMembers) {
		dao.save(khxtPerformanceMembers);
	}

	public void delete(KhxtPerformanceMembers khxtPerformanceMembers) {
		dao.delete(khxtPerformanceMembers);
	}

	public void update(KhxtPerformanceMembers khxtPerformanceMembers) {
		dao.update(khxtPerformanceMembers);
	}

	public KhxtPerformanceMembers get(String id) {
		return dao.get(KhxtPerformanceMembers.class, id);
	}

	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers khxtPerformanceMembers) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformanceMembers.class);
		dc.createAlias("appraiseSheet", "p");
		dc.createAlias("bkhrPerson", "person");
		dc.createAlias("khrPerson", "k");
		if (null != khxtPerformanceMembers) {
			String khrPersonId = khxtPerformanceMembers.getKhrPersonId();
			if (StringUtil.isNotBlank(khrPersonId)) {
				dc.add(Restrictions.eq("khrPersonId", khrPersonId));
			}
			Short status = khxtPerformanceMembers.getStatus();
			if (null != status) {
				dc.add(Restrictions.eq("status", status));
			}
			String sheetId = khxtPerformanceMembers.getSheetId();
			if (null != sheetId) {
				dc.add(Restrictions.eq("sheetId", sheetId));
			}
			String bkhrPersonId = khxtPerformanceMembers.getBkhrPersonId();
			if (null != bkhrPersonId) {
				dc.add(Restrictions.eq("bkhrPersonId", bkhrPersonId));
			}
			String khrPersonName = khxtPerformanceMembers.getKhrPersonName();
			if(StringUtil.isNotBlank(khrPersonName)) {
				dc.add(Restrictions.like("k.name", khrPersonName, MatchMode.ANYWHERE));
			}
			KhxtAppraiseSheet sheet = khxtPerformanceMembers.getAppraiseSheet();
			if (sheet != null) {
				String name = sheet.getName();
				if (StringUtils.isNotBlank(name)) {
					dc.add(Restrictions.eq("p.name", name));
				}
				String season = sheet.getSeason();
				if (StringUtils.isNotBlank(season)) {
					dc.add(Restrictions.eq("p.season", season));
				}
				String beginDate = sheet.getBeginDate();
				if (StringUtils.isNotBlank(beginDate)) {
					dc.add(Restrictions.eq("p.beginDate",DateUtil.formatStrDateTime(beginDate, "yyyy年MM月dd日", "yyyyMMdd")));
				}
				String endEate = sheet.getEndDate();
				if (StringUtils.isNotBlank(endEate)) {
					dc.add(Restrictions.eq("p.endDate",	DateUtil.formatStrDateTime(endEate, "yyyy年MM月dd日", "yyyyMMdd")));
				}
			}
			Person bkhrPerson = khxtPerformanceMembers.getBkhrPerson();
			if (bkhrPerson != null) {
				String name = bkhrPerson.getName();
				if (StringUtil.isNotBlank(name)) {
					dc.add(Restrictions.eq("person.name", name));
				}
			}
			dc.addOrder(Order.desc("totalPoints"));
		}
		return dao.findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public List<String> findByGroup(KhxtPerformanceMembers khxtPerformanceMembers) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformanceMembers.class);
		dc.createAlias("appraiseSheet", "s");
		dc.createAlias("bkhrPerson", "b");
		dc.setProjection(Projections.groupProperty("bkhrPersonId"));
		if (null != khxtPerformanceMembers) {
			String sheetId = khxtPerformanceMembers.getSheetId();
			if (StringUtil.isNotBlank(sheetId)) {
				dc.add(Restrictions.eq("s.id", sheetId));
			}
			String bkhrPersonName = khxtPerformanceMembers.getBkhrPersonName();
			if(StringUtil.isNotBlank(bkhrPersonName)) {
				dc.add(Restrictions.like("b.name", bkhrPersonName, MatchMode.ANYWHERE));
			}
		}
		return dc.getExecutableCriteria(dao.getSession()).list();
	}

	public List<KhxtPerformanceMembers> findBySheetId(String sheetId) {

		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformanceMembers.class);
		if (StringUtils.isBlank(sheetId)) {
			throw new RepsException("考核表ID不能为空");
		}
		dc.add(Restrictions.eq("sheetId", sheetId));
		List<KhxtPerformanceMembers> list = dao.findByCriteria(dc);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}

	public int count(String sheetId, String personId, Short... status) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtPerformanceMembers.class);
		if (StringUtil.isNotBlank(sheetId)) {
			dc.add(Restrictions.eq("sheetId", sheetId));
		}
		if (StringUtil.isNotBlank(personId)) {
			dc.add(Restrictions.eq("khrPersonId", personId));
		}
		if (null != status) {
			dc.add(Restrictions.in("status", status));
		}
		return dao.getRowCount(dc).intValue();
	}
}
