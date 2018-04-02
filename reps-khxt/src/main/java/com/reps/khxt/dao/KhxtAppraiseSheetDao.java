package com.reps.khxt.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;
import com.reps.khxt.entity.KhxtKhrProcess;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformanceWork;
import com.reps.khxt.enums.AppraiseStatus;

@Repository
public class KhxtAppraiseSheetDao {

	@Autowired
	private IGenericDao<KhxtAppraiseSheet> dao;

	@Autowired
	private KhxtAppraiseSheetFileDao fileDao;

	@Autowired
	private KhxtPerformanceMembersDao membesDao;

	@Autowired
	private KhxtPerformanceWorkDao workDao;

	@Autowired
	private KhxtKhrProcessDao processDao;

	public ListResult<KhxtAppraiseSheet> query(int start, int pagesize, KhxtAppraiseSheet sheet) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtAppraiseSheet.class);
		dc.createAlias("khrProcessList", "s");
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
			Integer progress = sheet.getProgress();
			if (progress != null) {
				dc.add(Restrictions.eq("progress", progress));
			}
			//查询考核人打分情况
			String khrPersonId = sheet.getKhrPersonId();
			if(StringUtil.isNotBlank(khrPersonId)) {
				dc.add(Restrictions.eq("s.khrPersonId", khrPersonId));
			}
			Integer status = sheet.getStatus();
			if(null != status) {
				dc.add(Restrictions.eq("s.status", status));
			}

		}
		return dao.query(dc, start, pagesize, Order.asc("name"));
	}

	public void save(KhxtAppraiseSheet sheet) {
		dao.save(sheet);
	}

	public void delete(KhxtAppraiseSheet sheet) throws Exception {

		if (StringUtils.isBlank(sheet.getId())) {
			throw new RepsException("考核表ID不恩为空!");
		}
		KhxtAppraiseSheet appraiseSheet = get(sheet.getId());
		if (appraiseSheet == null) {
			throw new RepsException("考核表不存在，不能删除！");
		}
		// 截止时间
		String endEate = appraiseSheet.getEndEate();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date end = format.parse(endEate);
		// 现在时间
		Date now = new Date();

		if (end.getTime() < now.getTime()) {
			throw new RepsException("月考核填报时间已结束，不能删除！");
		}
		// 删除工作绩效
		List<KhxtPerformanceWork> work = workDao.findBySheetId(sheet.getId());
		if (!CollectionUtils.isEmpty(work)) {
			for (KhxtPerformanceWork khxtPerformanceWork : work) {
				workDao.delete(khxtPerformanceWork);
			}
		}
		// 删除考核文件
		List<KhxtAppraiseSheetFile> file = fileDao.findBySheetId(sheet.getId());
		if (!CollectionUtils.isEmpty(file)) {
			for (KhxtAppraiseSheetFile appraiseSheetFile : file) {
				fileDao.delete(appraiseSheetFile);
			}
		}
		// 删除的考核人员名单
		List<KhxtPerformanceMembers> members = membesDao.findBySheetId(sheet.getId());
		if (!CollectionUtils.isEmpty(members)) {
			for (KhxtPerformanceMembers khxtPerformanceMembers : members) {
				if (khxtPerformanceMembers.getStatus() != AppraiseStatus.UN_REPORTED.getId()) {
					throw new RepsException("已经有人上报或打分，不能删除！");
				}
				membesDao.delete(khxtPerformanceMembers);
			}
		}
		// 删除考核人打分表
		List<KhxtKhrProcess> process = processDao.getBySheetId(sheet.getId());
		if (!CollectionUtils.isEmpty(process)) {
			for (KhxtKhrProcess khxtKhrProcess : process) {
				processDao.delete(khxtKhrProcess);
			}
		}

		dao.delete(appraiseSheet);
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
		if(null != khxtAppraiseSheet) {
			KhxtLevelWeight levelWeight = khxtAppraiseSheet.getLevelWeight();
			if(null != levelWeight) {
				String id = levelWeight.getId();
				if(StringUtil.isNotBlank(id)) {
					dc.add(Restrictions.eq("w.id", id));
				}
			}
		}
		return dao.findByCriteria(dc);
	}

}
