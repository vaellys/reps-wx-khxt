package com.reps.khxt.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtPerformanceMembers;

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
		if (null != khxtPerformanceMembers) {
			String khrPersonId = khxtPerformanceMembers.getKhrPersonId();
			if(StringUtil.isNotBlank(khrPersonId)) {
				dc.add(Restrictions.eq("khrPersonId", khrPersonId));
			}
			Short status = khxtPerformanceMembers.getStatus();
			if(null != status) {
				dc.add(Restrictions.eq("status", status));
			}
			String sheetId = khxtPerformanceMembers.getSheetId();
			if(null != sheetId) {
				dc.add(Restrictions.eq("sheetId", sheetId));
			}
		}
		return dao.findByCriteria(dc);
	}
	
}
