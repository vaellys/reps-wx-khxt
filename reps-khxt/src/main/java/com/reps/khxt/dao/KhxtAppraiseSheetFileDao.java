package com.reps.khxt.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.IGenericDao;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;

@Repository
public class KhxtAppraiseSheetFileDao {

	@Autowired
	private IGenericDao<KhxtAppraiseSheetFile> dao;

	public void save(KhxtAppraiseSheetFile file) {
		dao.save(file);
	}

	public List<KhxtAppraiseSheetFile> findBySheetId(String sheetId) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtAppraiseSheetFile.class);
		if (StringUtils.isBlank(sheetId)) {
			throw new RepsException("考核表ID不能为空");
		}
		dc.add(Restrictions.eq("performanceId", sheetId));
		List<KhxtAppraiseSheetFile> list = dao.findByCriteria(dc);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}

	public void delete(KhxtAppraiseSheetFile appraiseSheetFile) {

		dao.delete(appraiseSheetFile);
	}
	
	public KhxtAppraiseSheetFile get(String id) {
		return dao.get(KhxtAppraiseSheetFile.class, id);
	}
}
