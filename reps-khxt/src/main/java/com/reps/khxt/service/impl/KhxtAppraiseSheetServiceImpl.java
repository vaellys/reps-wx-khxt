package com.reps.khxt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtAppraiseSheetDao;
import com.reps.khxt.dao.KhxtPerformanceMembersDao;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtGroupService;
import com.reps.system.dao.PersonDao;
import com.reps.system.entity.Person;

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
	private IKhxtGroupService groupService;

	@Override
	public ListResult<KhxtAppraiseSheet> query(int start, int pagesize, KhxtAppraiseSheet sheet) {

		return dao.query(start, pagesize, sheet);
	}

	@Override
	public void save(KhxtAppraiseSheet sheet, String[] khrids, String itemIds) throws Exception {
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

		sheet.setAddTime(new Date());
		dao.save(sheet);
		// 添加考核人员名单

		for (String khrid : khrids) {
			List<KhxtGroup> khxtGroup = groupService.getByLvelId(khrid);
			if (CollectionUtils.isEmpty(khxtGroup)) {
				throw new RepsException("考核人员名单生成失败，没有找到考核分组人员，请到分组功能进行设置!");
			}
			for (KhxtGroup group : khxtGroup) {
				if (!StringUtils.equals(group.getBkhrId(), sheet.getBkhr().getId())) {
					throw new RepsException("考核人员名单生成失败，没有找到考核分组人员，请到分组功能进行设置!");
				}
				List<String> khrpersonId = getPersonId(group.getKhr());
				List<String> bkhrpersonId = getPersonId(group.getBkhr());
				for (String id : khrpersonId) {
					Person person = personDao.get(id);

					for (String bkhrPersonId : bkhrpersonId) {
						KhxtPerformanceMembers k = new KhxtPerformanceMembers();
						// 考核表
						k.setSheetId(sheet.getId());
						k.setAppraiseSheet(sheet);
						// 考核人person
						k.setKhrPerson(person);
						k.setKhrPersonId(person.getId());
						// 被考核人person
						k.setBkhrPersonId(bkhrPersonId);
						k.setStatus((short) 0);
						membersDao.save(k);

					}

				}

			}
		}
	}

	@Override
	public void delete(KhxtAppraiseSheet sheet) {
		dao.delete(sheet);
	}

	@Override
	public KhxtAppraiseSheet get(String id, boolean eager) throws RepsException {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:考核ID为空");
		}
		KhxtAppraiseSheet khxtAppraiseSheet = dao.get(id);
		if(null == khxtAppraiseSheet) {
			throw new RepsException("参数异常:考核ID无效");
		}
		if(eager) {
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
	
}
