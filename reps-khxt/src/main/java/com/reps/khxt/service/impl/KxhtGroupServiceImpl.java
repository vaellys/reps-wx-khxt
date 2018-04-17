package com.reps.khxt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtLevelPersonDao;
import com.reps.khxt.dao.KxhtGroupDao;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.service.IKhxtGroupService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class KxhtGroupServiceImpl implements IKhxtGroupService {
	@Autowired
	private KxhtGroupDao dao;

	@Autowired
	private KhxtLevelPersonDao personDao;

	@Override
	public ListResult<KhxtGroup> query(int start, int pagesize, KhxtGroup khxtGroup) {

		return dao.query(start, pagesize, khxtGroup);
	}

	@Override
	public void save(KhxtGroup group, String khrIds, String bkhrIds) {

		group.setKhr(getJsonKhr(khrIds));
		group.setBkhr(getJsonBkhr(bkhrIds));

		group.setKhrId(group.getKhxtLevel().getId());
		group.setBkhrId(group.getBkhxtLevel().getId());

		dao.save(group);
	}

	private String getJsonBkhr(String bkhrIds) {

		List<Map<String, String>> bkhrlist = new ArrayList<>();
		if (StringUtils.isNotBlank(bkhrIds)) {
			String[] split = bkhrIds.split(",");
			for (String id : split) {
				Map<String, String> bkhrmap = new HashMap<String, String>();
				KhxtLevelPerson person = personDao.getByPersonId(id);
				bkhrmap.put("person_id", person.getPersonId());
				bkhrmap.put("person_name", person.getPersonName());
				bkhrlist.add(bkhrmap);
			}
		}
		return JSONArray.fromObject(bkhrlist).toString();
	}

	private String getJsonKhr(String khrIds) {

		List<Map<String, String>> khrlist = new ArrayList<>();

		if (StringUtils.isNotBlank(khrIds)) {
			String[] split = khrIds.split(",");
			for (String id : split) {
				Map<String, String> khrmap = new HashMap<String, String>();
				KhxtLevelPerson person = personDao.getByPersonId(id);
				khrmap.put("person_id", person.getPersonId());
				khrmap.put("person_name", person.getPersonName());
				khrlist.add(khrmap);
			}
		}
		return JSONArray.fromObject(khrlist).toString();
	}

	@Override
	public void delete(KhxtGroup khxtgroup) {
		if (StringUtils.isNotBlank(khxtgroup.getId())) {
			dao.delete(khxtgroup);
		}
	}

	@Override
	public KhxtGroup get(String id) throws Exception {
		KhxtGroup khxtGroup = dao.get(id);
		// 处理考核人json数据
		String khr = khxtGroup.getKhr();
		StringBuilder sbkhr = new StringBuilder();
		StringBuilder khrids = new StringBuilder();
		getJson2String(khr, sbkhr, khrids);
		sbkhr.deleteCharAt(sbkhr.length() - 1);
		khrids.deleteCharAt(khrids.length() - 1);

		// 处理被考核人json数据
		String bkhr = khxtGroup.getBkhr();
		StringBuilder sbbkhr = new StringBuilder();
		StringBuilder bkhrids = new StringBuilder();
		getJson2String(bkhr, sbbkhr, bkhrids);
		sbbkhr.deleteCharAt(sbbkhr.length() - 1);
		bkhrids.deleteCharAt(bkhrids.length() - 1);

		khxtGroup.setKhr(sbkhr.toString());
		khxtGroup.setKhrIds(khrids.toString());

		khxtGroup.setBkhr(sbbkhr.toString());
		khxtGroup.setBkhrIds(bkhrids.toString());

		return khxtGroup;
	}

	@SuppressWarnings("unchecked")
	public void getJson2String(String json, StringBuilder persnName, StringBuilder personId) throws Exception {
		ObjectMapper obj = new ObjectMapper();
		List<Map<String, String>> personlist = obj.readValue(json, List.class);
		for (Map<String, String> map : personlist) {
			String s = map.get("person_name");
			persnName.append(s);
			persnName.append(",");

			String ids = map.get("person_id");
			if (personId != null) {
				personId.append(ids);
				personId.append(",");
			}
		}
	}

	@Override
	public void update(KhxtGroup group, String khrIds, String bkhrIds) {
		group.setKhr(getJsonKhr(khrIds));
		group.setBkhr(getJsonBkhr(bkhrIds));

		group.setKhrId(group.getKhxtLevel().getId());
		group.setBkhrId(group.getBkhxtLevel().getId());
		dao.update(group);
	}

	@Override
	public List<String> getByLvelId(String khrids, String bkhrid) throws Exception {

		String[] levelkhrid = khrids.split(",");
		StringBuilder khrpersonName = new StringBuilder();
		StringBuilder bkhrpersonName = new StringBuilder();

		if (khrids != null & khrids.length() > 0 && StringUtils.isNotBlank(bkhrid)) {
			Map<String, String> khrMap = new HashMap<>();
			Map<String, String> bkhrMap = new HashMap<>();

			for (String khrid : levelkhrid) {
				// 查询分组
				List<KhxtGroup> list = dao.getByLvelId(khrid, bkhrid);
				for (KhxtGroup khxtGroup : list) {
					String khr = khxtGroup.getKhr();
					String bkhr = khxtGroup.getBkhr();
					getPersonMap(khr, khrMap);
					getPersonMap(bkhr, bkhrMap);
				}
			}
			// 过滤相同人员
			for (String key : khrMap.keySet()) {
				khrpersonName.append(khrMap.get(key));
				khrpersonName.append("，");
			}
			khrpersonName.deleteCharAt(khrpersonName.length() - 1);
			
			// 过滤相同人员
			for (String key : bkhrMap.keySet()) {
				bkhrpersonName.append(bkhrMap.get(key));
				bkhrpersonName.append("，");
			}
			bkhrpersonName.deleteCharAt(bkhrpersonName.length() - 1);
		}

		List<String> personNameList = new ArrayList<>();
		personNameList.add(khrpersonName.toString());
		personNameList.add(bkhrpersonName.toString());
		return personNameList;
	}

	// 过滤重复人员
	@SuppressWarnings({ "unchecked" })
	private void getPersonMap(String json, Map<String, String> personMap) throws Exception {
		ObjectMapper obj = new ObjectMapper();
		List<Map<String, String>> personlist = obj.readValue(json, List.class);
		for (Map<String, String> map : personlist) {
			String name = map.get("person_name");
			String id = map.get("person_id");
			personMap.put(id, name);
		}
	}

	@Override
	public boolean checkLevelExist(String levelId) throws RepsException {
		if (StringUtil.isBlank(levelId)) {
			throw new RepsException("参数异常:级别ID为空");
		}
		return 0 < dao.countLevelIds(levelId) ? true : false;
	}

	@Override
	public boolean checkPersonIdExist(String personId) throws RepsException {
		if (StringUtil.isBlank(personId)) {
			throw new RepsException("参数异常:人员ID为空");
		}
		List<KhxtGroup> groupList = dao.find(null);
		if (null != groupList && !groupList.isEmpty()) {
			for (KhxtGroup group : groupList) {
				if (checkPersonId(group.getKhr(), personId) || checkPersonId(group.getBkhr(), personId)) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean checkPersonId(String source, String personId) throws RepsException {
		if (StringUtil.isNotBlank(source)) {
			JSONArray sourceArray = JSONArray.fromObject(source);
			if (null != sourceArray && !sourceArray.isEmpty()) {
				for (Iterator<JSONObject> iterator = sourceArray.iterator(); iterator.hasNext();) {
					JSONObject sourceObj = iterator.next();
					String sourcePersonId = sourceObj.getString("person_id");
					if (personId.equals(sourcePersonId)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
