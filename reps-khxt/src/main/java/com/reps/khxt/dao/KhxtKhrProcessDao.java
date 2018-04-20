package com.reps.khxt.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtKhrProcess;
import com.reps.system.entity.Person;

/**
 * 考核人打分情况表
 * 
 * @author ：Alex
 * @date 2018年3月29日
 */
@Repository
public class KhxtKhrProcessDao {

	@Autowired
	private IGenericDao<KhxtKhrProcess> dao;

	public void save(KhxtKhrProcess khr) {
		dao.save(khr);
	}

	public List<KhxtKhrProcess> getBySheetId(String sheetId) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtKhrProcess.class);
		if (StringUtils.isNotBlank(sheetId)) {
			dc.add(Restrictions.eq("sheetId", sheetId));
		}
		return dao.findByCriteria(dc);
	}

	public void delete(KhxtKhrProcess khxtKhrProcess) {
		dao.delete(khxtKhrProcess);
	}
	
	public void update(KhxtKhrProcess khxtKhrProcess) {
		dao.update(khxtKhrProcess);
	}
	
	public KhxtKhrProcess get(String id) {
		return dao.get(KhxtKhrProcess.class, id);
	}
	
	public List<KhxtKhrProcess> find(KhxtKhrProcess khxtKhrProcess) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtKhrProcess.class);
		if (null != khxtKhrProcess) {
			Integer status = khxtKhrProcess.getStatus();
			if(null != status) {
				dc.add(Restrictions.eq("status", status));
			}
			String khrPersonId = khxtKhrProcess.getKhrPersonId();
			if(StringUtil.isNotBlank(khrPersonId)) {
				dc.add(Restrictions.eq("khrPersonId", khrPersonId));
			}
			String sheetId = khxtKhrProcess.getSheetId();
			if(StringUtil.isNotBlank(sheetId)) {
				dc.add(Restrictions.eq("sheetId", sheetId));
			}
		}
		return dao.findByCriteria(dc);
	}
	
	public ListResult<KhxtKhrProcess> query(int start, int pagesize, KhxtKhrProcess process) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtKhrProcess.class);
		dc.createAlias("khrPerson", "p");
		dc.createAlias("appraiseSheet", "sheet");
		if (null != process) {
			String beginDate = process.getBeginDate();
			if(StringUtil.isNotBlank(beginDate)) {
				dc.add(Restrictions.ge("sheet.beginDate", beginDate));
			}
			String endDate = process.getEndDate();
			if(StringUtil.isNotBlank(endDate)) {
				dc.add(Restrictions.le("sheet.endDate", endDate));
			}
			Integer status = process.getStatus();
			if(null != status) {
				dc.add(Restrictions.eq("status", status));
			}
			String khrPersonId = process.getKhrPersonId();
			if(StringUtil.isNotBlank(khrPersonId)) {
				dc.add(Restrictions.eq("khrPersonId", khrPersonId));
			}
			String sheetId = process.getSheetId();
			if(StringUtil.isNotBlank(sheetId)) {
				dc.add(Restrictions.eq("sheetId", sheetId));
			}
			Person khrPerson = process.getKhrPerson();
			if(null != khrPerson) {
				String name = khrPerson.getName();
				if(StringUtil.isNotBlank(name)) {
					dc.add(Restrictions.like("p.name", name, MatchMode.ANYWHERE));
				}
			}
		}
		return dao.query(dc, start, pagesize);
	}
	
	public void updateStatus(KhxtKhrProcess khxtKhrProcess) {
		String hql = "update " + KhxtKhrProcess.class.getName() + " p set p.status=:status where p.sheetId=:sheetId and p.khrPersonId=:khrPersonId"; 
		dao.getSession().createQuery(hql).setInteger("status", khxtKhrProcess.getStatus()).setString("sheetId", khxtKhrProcess.getSheetId()).setString("khrPersonId", khxtKhrProcess.getKhrPersonId()).executeUpdate();
	}
	
}
