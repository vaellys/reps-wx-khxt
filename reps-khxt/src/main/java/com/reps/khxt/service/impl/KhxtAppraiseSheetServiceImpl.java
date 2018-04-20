package com.reps.khxt.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.DateUtil;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtAppraiseSheetDao;
import com.reps.khxt.dao.KhxtAppraiseSheetFileDao;
import com.reps.khxt.dao.KhxtKhrProcessDao;
import com.reps.khxt.dao.KhxtLevelDao;
import com.reps.khxt.dao.KhxtLevelWeightDao;
import com.reps.khxt.dao.KhxtPerformanceMembersDao;
import com.reps.khxt.dao.KhxtPerformancePointDao;
import com.reps.khxt.dao.KhxtPerformanceWorkDao;
import com.reps.khxt.dao.KxhtGroupDao;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtKhrProcess;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformancePoint;
import com.reps.khxt.entity.KhxtPerformanceWork;
import com.reps.khxt.enums.AppraiseStatus;
import com.reps.khxt.enums.ProgressStatus;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.system.dao.PersonDao;
import com.reps.system.entity.Person;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class KhxtAppraiseSheetServiceImpl implements IKhxtAppraiseSheetService {

	@Autowired
	private KhxtAppraiseSheetDao dao;

	@Autowired
	private KhxtPerformanceMembersDao membersDao;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private KxhtGroupDao groupDao;

	@Autowired
	private KhxtPerformanceWorkDao workDao;

	@Autowired
	private KhxtAppraiseSheetFileDao fileDao;

	@Autowired
	private KhxtKhrProcessDao processDao;

	@Autowired
	private KhxtKhrProcessDao khrProcessDao;

	@Autowired
	private KhxtPerformancePointDao pointDao;

	@Autowired
	private KhxtLevelDao levelDao;

	@Autowired
	private KhxtLevelWeightDao weightDao;

	@Autowired
	private IKhxtLevelService khxtLevelService;

	@Override
	public ListResult<KhxtAppraiseSheet> query(int start, int pagesize, KhxtAppraiseSheet sheet) throws Exception {
		Map<String, KhxtAppraiseSheet> map = new HashMap<>();

		List<KhxtAppraiseSheet> sheetList = new ArrayList<>();

		ListResult<KhxtAppraiseSheet> result = dao.query(start, pagesize, sheet);
		List<KhxtAppraiseSheet> list = result.getList();

		for (KhxtAppraiseSheet khxtAppraiseSheet : list) {

			// 处理日期
			String endEate = khxtAppraiseSheet.getEndDate();
			khxtAppraiseSheet.setEndDate(DateUtil.formatStrDateTime(endEate, "yyyyMMdd", "yyyy年MM月dd日"));

			String season = khxtAppraiseSheet.getSeason();
			khxtAppraiseSheet.setSeason(DateUtil.formatStrDateTime(season, "yyyyMM", "yyyy年MM月"));
			// 处理权重
			khxtAppraiseSheet.setWeight(getJsonWeight(khxtAppraiseSheet.getLevelWeight()));
			// 考核人
			StringBuilder name = new StringBuilder();
			String khrIds = khxtAppraiseSheet.getKhrId();
			String[] ids = khrIds.split(",");
			for (String id : ids) {
				KhxtLevel khxtLevel = khxtLevelService.get(id);
				name.append(khxtLevel.getName());
				name.append(";");
			}
			name.deleteCharAt(name.length() - 1);
			khxtAppraiseSheet.setKhrName(name.toString());
			map.put(khxtAppraiseSheet.getId(), khxtAppraiseSheet);

		}
		for (String key : map.keySet()) {
			sheetList.add(map.get(key));
		}
		result.setList(sheetList);

		return result;
	}

	@Override
	public List<KhxtAppraiseSheet> count() throws Exception {

		List<KhxtAppraiseSheet> result = dao.find(null);
		if (!CollectionUtils.isEmpty(result)) {
			for (KhxtAppraiseSheet khxtAppraiseSheet : result) {
				// 统计修改考核进度
				int a = 0;
				int b = 0;
				List<KhxtPerformanceMembers> PerformanceMembers = membersDao.findBySheetId(khxtAppraiseSheet.getId());
				Map<String, KhxtPerformanceMembers> map = new HashMap<>();

				for (KhxtPerformanceMembers khxtPerformanceMembers : PerformanceMembers) {
					map.put(khxtPerformanceMembers.getBkhrPersonId(), khxtPerformanceMembers);
				}
				// 过滤箱相同人员名单
				for (String key : map.keySet()) {
					KhxtPerformanceMembers khxtPerformanceMembers = map.get(key);
					if (khxtPerformanceMembers.getStatus() == AppraiseStatus.REPORTED.getId()) {
						a++;
					}
					if (khxtPerformanceMembers.getStatus() == AppraiseStatus.APPRAISED.getId()) {
						a++;
						b++;
					}
				}
				// 考核未完成
				khxtAppraiseSheet.setProgress(ProgressStatus.UN_FINISHED.getId());
				// 考核已经完成
				if (b == khxtAppraiseSheet.getBkhrCount()) {
					khxtAppraiseSheet.setProgress(ProgressStatus.FINISHED.getId());
				}
				// 截止时间
				String endEate = khxtAppraiseSheet.getEndDate();
				DateUtil.formatStrDateTime(endEate, "yyyyMMdd", "yyyy年MM月dd日");
				Calendar calendar = DateUtil.getCalendarFromStr(endEate, "yyyyMMdd");
				// 现在时间
				Date now = new Date();
				if (now.getTime() > calendar.getTime().getTime()) {
					// 考核已结束
					khxtAppraiseSheet.setProgress(ProgressStatus.HAS_OVER.getId());
				}
				khxtAppraiseSheet.setResult("已上报" + a + "人,已打分" + b + "人");
			}
		}
		return result;
	}

	public void update(KhxtAppraiseSheet khxtAppraiseSheet) {
		dao.update(khxtAppraiseSheet);
	}

	@SuppressWarnings("unchecked")
	public String getJsonWeight(KhxtLevelWeight levelWeight) {
		String weight = levelWeight.getWeight();
		if (StringUtil.isNotBlank(weight)) {
			JSONArray levelWeightArray = JSONArray.fromObject(weight);
			StringBuilder sb = new StringBuilder();
			if (null != levelWeightArray && !levelWeightArray.isEmpty()) {
				for (Iterator<JSONObject> iterator = levelWeightArray.iterator(); iterator.hasNext();) {
					JSONObject obj = (JSONObject) iterator.next();

					KhxtLevel khxtLevel = khxtLevelService.get(obj.getString("levelId"));
					String name = khxtLevel.getName();
					String we = obj.getString("weight");
					sb.append(name);
					sb.append(we);
					sb.append("%");
					sb.append(";");
				}
				sb.deleteCharAt(sb.toString().lastIndexOf(";"));
			}
			levelWeight.setWeightDisplay(sb.toString());
		}
		return levelWeight.getWeightDisplay();
	}

	@Override
	public void save(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, List<KhxtAppraiseSheetFile> sheetFile)
			throws Exception {
		// 处理考核人id
		if (khrids == null || khrids.length == 0) {
			throw new RepsException("考核人级别不能为空");
		}
		if (StringUtils.isBlank(itemIds)) {
			throw new RepsException("考核指标不能为空");
		}
		StringBuilder khr = new StringBuilder();
		for (String khrid : khrids) {
			khr.append(khrid);
			khr.append(",");
		}
		khr.deleteCharAt(khr.length() - 1);
		sheet.setKhrId(khr.toString());
		// 处理考核指标
		Set<KhxtItem> set = new HashSet<>();
		String[] split = itemIds.split(",");
		for (String id : split) {
			KhxtItem item = new KhxtItem();
			item.setId(id);
			set.add(item);
		}
		sheet.setItem(set);

		Date date = new Date();
		
		sheet.setBeginDate(DateUtil.formatStrDateTime(sheet.getBeginDate(), "yyyy-MM-dd", "yyyyMMdd"));
		sheet.setEndDate(DateUtil.formatStrDateTime(sheet.getEndDate(), "yyyy-MM-dd", "yyyyMMdd"));

		sheet.setAddTime(date);
		sheet.setBkhrCount(bkhrCount(khrids, sheet.getBkhr().getId()));
		sheet.setProgress(ProgressStatus.UN_FINISHED.getId());
		sheet.setResult("已上报0人,已打分0人");
		dao.save(sheet);
		// 生成考核人员名单
		saveKhxtPerformanceMembers(khrids, sheet);
		// 保存考核文件
		if (!CollectionUtils.isEmpty(sheetFile)) {
			for (KhxtAppraiseSheetFile file : sheetFile) {
				file.setAppraiseSheet(sheet);
				file.setPerformanceId(sheet.getId());
				file.setUploadTime(date);
				fileDao.save(file);
			}
		}
	}

	@Override
	public void update(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, List<KhxtAppraiseSheetFile> file)
			throws Exception {

		if (StringUtils.isBlank(sheet.getId())) {
			throw new RepsException("月考核表ID不能为空！");
		}
		if (StringUtils.isBlank(itemIds)) {
			throw new RepsException("考核指标不能为空！");
		}
		if (StringUtils.isBlank(sheet.getLevelWeight().getId())) {
			throw new RepsException("考核权重不能为空！");
		}
		if (khrids == null || khrids.length == 0) {
			throw new RepsException("考核人不能为空!");
		}
		KhxtAppraiseSheet khxtAppraiseSheet = dao.get(sheet.getId(), true);
		if (khxtAppraiseSheet == null) {
			throw new RepsException("考核表不存在，不能修改！");
		}
		// 处理考核人id
		if (khrids == null || khrids.length == 0) {
			throw new RepsException("考核人级别不能为空");
		}
		if (StringUtils.isBlank(itemIds)) {
			throw new RepsException("考核指标不能为空");
		}
		StringBuilder khr = new StringBuilder();
		for (String khrid : khrids) {
			khr.append(khrid);
			khr.append(",");
		}
		khr.deleteCharAt(khr.length() - 1);
		sheet.setKhrId(khr.toString());
		// 处理考核指标
		Set<KhxtItem> set = new HashSet<>();
		String[] split = itemIds.split(",");
		for (String id : split) {
			KhxtItem item = new KhxtItem();
			item.setId(id);
			set.add(item);
		}
		// 处理日期
		sheet.setBeginDate(DateUtil.formatStrDateTime(sheet.getBeginDate(), "yyyy-MM-dd", "yyyyMMdd"));
		sheet.setEndDate(DateUtil.formatStrDateTime(sheet.getEndDate(), "yyyy-MM-dd", "yyyyMMdd"));
		// 查询考核权重
		KhxtLevelWeight levelWeight = weightDao.get(sheet.getLevelWeight().getId());
		// 判断是否需要修改考核人员名单
		boolean update = false;
		if (!StringUtils.equals(sheet.getKhrId(), khxtAppraiseSheet.getKhrId())) {
			List<KhxtKhrProcess> process = processDao.getBySheetId(sheet.getId());
			for (KhxtKhrProcess khxtKhrProcess : process) {
				processDao.delete(khxtKhrProcess);
				update = true;
			}
		}
		if (update) {
			KhxtLevel level = levelDao.get(sheet.getBkhr().getId());

			KhxtPerformanceMembers formanceMembers = new KhxtPerformanceMembers();
			formanceMembers.setSheetId(sheet.getId());

			List<KhxtPerformanceMembers> list = membersDao.find(formanceMembers);
			for (KhxtPerformanceMembers khxtPerformanceMembers : list) {
				if (khxtPerformanceMembers.getStatus() != AppraiseStatus.UN_REPORTED.getId()) {
					throw new RepsException("已有考核对象上报工作绩效！不能修考核对象");
				}
				membersDao.delete(khxtPerformanceMembers);
			}
			khxtAppraiseSheet.setBkhr(level);
		}

		khxtAppraiseSheet.setName(sheet.getName());
		khxtAppraiseSheet.setSeason(sheet.getSeason());
		khxtAppraiseSheet.setKhrId(sheet.getKhrId());
		khxtAppraiseSheet.setBeginDate(sheet.getBeginDate());
		khxtAppraiseSheet.setEndDate(sheet.getEndDate());
		khxtAppraiseSheet.setLevelWeight(levelWeight);
		khxtAppraiseSheet.setItem(set);
		dao.update(khxtAppraiseSheet);
		// 生成考核名单
		if (update) {
			saveKhxtPerformanceMembers(khrids, khxtAppraiseSheet);
		}
		// 保存考核文件
		if (!CollectionUtils.isEmpty(file)) {
			List<KhxtAppraiseSheetFile> listfile = fileDao.findBySheetId(khxtAppraiseSheet.getId());
			if (!CollectionUtils.isEmpty(listfile)) {
				for (KhxtAppraiseSheetFile khxtAppraiseSheetFile : listfile) {
					for (KhxtAppraiseSheetFile sheetFile : file) {
						if (StringUtils.isNotBlank(sheetFile.getId())) {
							if (!StringUtils.equals(sheetFile.getId(), khxtAppraiseSheetFile.getId())) {
								fileDao.delete(khxtAppraiseSheetFile);
							}
							continue;
						}
						sheetFile.setAppraiseSheet(sheet);
						sheetFile.setPerformanceId(sheet.getId());
						sheetFile.setFileUrl(sheetFile.getFileUrl().split(":")[1]);
						sheetFile.setUploadTime(new Date());
						fileDao.save(sheetFile);
					}
				}
			} else {
				for (KhxtAppraiseSheetFile sheetFile : file) {
					sheetFile.setAppraiseSheet(sheet);
					sheetFile.setPerformanceId(sheet.getId());
					sheetFile.setFileUrl(sheetFile.getFileUrl().split(":")[1]);
					sheetFile.setUploadTime(new Date());
					fileDao.save(sheetFile);
				}
			}
		}

	}

	/**
	 * 查询被考核人数
	 * 
	 * @author Alex
	 * @date 2018年3月29日
	 * @param id
	 * @return
	 * @return Integer
	 * @throws Exception
	 * @throws @throws
	 */
	@SuppressWarnings("unchecked")
	private Integer bkhrCount(String[] khrids, String bkheId) throws Exception {
		ObjectMapper obj = new ObjectMapper();
		if (StringUtils.isBlank(bkheId)) {
			throw new RepsException("级别ID不能为空！");
		}
		List<String> count = new ArrayList<>();
		for (String khrId : khrids) {
			List<KhxtGroup> list = groupDao.getByLvelId(khrId, bkheId);
			if (CollectionUtils.isEmpty(list)) {
				throw new RepsException("没有找到考核分组人员，请到分组功能进行设置!");
			}
			for (KhxtGroup khxtGroup : list) {
				count.add(khxtGroup.getBkhr());
			}
		}
		Map<String, String> map = new HashMap<>();
		for (String bkhr : count) {
			List<Map<String, String>> khrlist = obj.readValue(bkhr, List.class);
			for (Map<String, String> bkhrMap : khrlist) {
				map.put(bkhrMap.get("person_id"), bkhrMap.get("person_id"));
			}
		}

		return map.size();
	}

	private void saveKhxtPerformanceMembers(String[] khrids, KhxtAppraiseSheet sheet) throws Exception {
		for (String khrid : khrids) {
			List<KhxtGroup> khxtGroup = groupDao.getByLvelId(khrid, sheet.getBkhr().getId());
			if (CollectionUtils.isEmpty(khxtGroup)) {
				throw new RepsException("考核人员名单生成失败，没有找到考核分组人员，请到分组功能进行设置!");
			}
			for (KhxtGroup group : khxtGroup) {
				List<String> khrpersonId = getPersonId(group.getKhr());
				List<String> bkhrpersonId = getPersonId(group.getBkhr());
				for (String id : khrpersonId) {
					Person khrperson = personDao.get(id);
					// 保存考核人打分表
					KhxtKhrProcess khr = new KhxtKhrProcess();
					khr.setSheetId(sheet.getId());
					khr.setAppraiseSheet(sheet);
					khr.setKhrPersonId(id);
					khr.setStatus(0);
					khrProcessDao.save(khr);

					for (String bkhrPersonId : bkhrpersonId) {
						Person bkhrPerson = personDao.get(bkhrPersonId);
						KhxtPerformanceMembers k = new KhxtPerformanceMembers();
						// 考核表
						k.setSheetId(sheet.getId());
						k.setAppraiseSheet(sheet);
						// 考核人person
						k.setKhrPerson(khrperson);
						k.setKhrPersonId(khrperson.getId());
						// 被考核人person
						k.setBkhrPersonId(bkhrPerson.getId());
						k.setBkhrPerson(bkhrPerson);
						k.setStatus(AppraiseStatus.UN_REPORTED.getId());
						membersDao.save(k);

					}

				}

			}
		}
	}

	@Override
	public void delete(KhxtAppraiseSheet sheet) throws Exception {
		if (StringUtils.isBlank(sheet.getId())) {
			throw new RepsException("考核表ID不能为空!");
		}
		KhxtAppraiseSheet appraiseSheet = get(sheet.getId());
		if (appraiseSheet == null) {
			throw new RepsException("考核表不存在，不能删除！");
		}
		// 删除考核人打分表
		List<KhxtKhrProcess> process = processDao.getBySheetId(appraiseSheet.getId());
		if (!CollectionUtils.isEmpty(process)) {
			for (KhxtKhrProcess khxtKhrProcess : process) {
				processDao.delete(khxtKhrProcess);
			}
		}
		// 删除考核人员名单
		List<KhxtPerformanceMembers> members = membersDao.findBySheetId(sheet.getId());
		if (!CollectionUtils.isEmpty(members)) {
			for (KhxtPerformanceMembers khxtPerformanceMembers : members) {
				List<KhxtPerformancePoint> list = pointDao.findByMemberId(khxtPerformanceMembers.getId());
				if (!CollectionUtils.isEmpty(list)) {
					throw new RepsException("已有人打分，不能删除！");
				}
				membersDao.delete(khxtPerformanceMembers);
			}
		}

		// 删除工作绩效
		List<KhxtPerformanceWork> work = workDao.findBySheetId(appraiseSheet.getId());
		if (!CollectionUtils.isEmpty(work)) {
			for (KhxtPerformanceWork khxtPerformanceWork : work) {
				workDao.delete(khxtPerformanceWork);
			}
		}
		// 删除考核文件
		List<KhxtAppraiseSheetFile> file = fileDao.findBySheetId(appraiseSheet.getId());
		if (!CollectionUtils.isEmpty(file)) {
			for (KhxtAppraiseSheetFile appraiseSheetFile : file) {

				fileDao.delete(appraiseSheetFile);
			}
		}
		dao.delete(appraiseSheet);
	}

	@Override
	public KhxtAppraiseSheet get(String id, boolean eager) throws RepsException {
		if (StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:考核ID为空");
		}
		KhxtAppraiseSheet khxtAppraiseSheet = dao.get(id);
		if (null == khxtAppraiseSheet) {
			throw new RepsException("参数异常:考核ID无效");
		}
		if (eager) {
			Hibernate.initialize(khxtAppraiseSheet.getItem());
		}
		return khxtAppraiseSheet;
	}

	@SuppressWarnings("unchecked")
	private List<String> getPersonId(String string) throws Exception {
		ObjectMapper obj = new ObjectMapper();
		List<Map<String, String>> khrlist = obj.readValue(string, List.class);
		List<String> list = new ArrayList<>();

		for (Map<String, String> map : khrlist) {
			String personId = map.get("person_id");
			list.add(personId);

		}
		return list;
	}

	@Override
	public void saveWorkPlan(String[] planning, String[] execution, String sheetId, String personId, String appear,
			String workId) throws Exception {
		KhxtPerformanceWork work = new KhxtPerformanceWork();
		if (StringUtils.isEmpty(sheetId)) {
			throw new RepsException("考核ID不能为空");
		}
		KhxtAppraiseSheet sheet = dao.get(sheetId);
		if (sheet == null) {
			throw new RepsException("月考核不存在！");
		}
		if (StringUtils.isBlank(personId)) {
			throw new RepsException("上报人ID不能为空");
		}
		work.setAddTime(new Date());
		work.setExecution(StringUtils.join(execution, ","));
		work.setPlanning(StringUtils.join(planning, ","));
		work.setSheetId(sheet.getId());
		work.setSheet(sheet);
		work.setPersonId(personId);

		if (StringUtils.isNotBlank(workId)) {
			work = workDao.get(workId);
			work.setExecution(StringUtils.join(execution, ","));
			work.setPlanning(StringUtils.join(planning, ","));
			workDao.update(work);
		} else {
			workDao.save(work);
		}

		/**
		 * apper:为空不需要上报
		 * 
		 * apper:不为空需要上报
		 */
		if (StringUtils.isNotBlank(appear)) {
			KhxtPerformanceMembers m = new KhxtPerformanceMembers();
			m.setBkhrPersonId(personId);
			m.setSheetId(sheetId);
			List<KhxtPerformanceMembers> list = membersDao.find(m);
			if (CollectionUtils.isEmpty(list)) {
				throw new RepsException("考核人员明名单不存在");
			}
			for (KhxtPerformanceMembers khxtPerformanceMembers : list) {
				khxtPerformanceMembers.setStatus(AppraiseStatus.REPORTED.getId());
				membersDao.update(khxtPerformanceMembers);
			}
		}
	}

	@Override
	public KhxtAppraiseSheet get(String sheetId) {
		KhxtAppraiseSheet sheet = dao.get(sheetId, true);
		if (sheet == null) {
			throw new RepsException("月考核不存在！");
		}
		return sheet;
	}

	@Override
	public boolean checkWeightExistInSheet(String id) throws RepsException {
		if (StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:请指定权重ID");
		}
		KhxtAppraiseSheet sheet = new KhxtAppraiseSheet();
		KhxtLevelWeight weight = new KhxtLevelWeight();
		weight.setId(id);
		sheet.setLevelWeight(weight);
		List<KhxtAppraiseSheet> list = dao.find(sheet);
		if (null == list || list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
