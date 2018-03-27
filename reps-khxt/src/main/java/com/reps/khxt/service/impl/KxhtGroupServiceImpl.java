package com.reps.khxt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reps.core.orm.ListResult;
import com.reps.khxt.dao.KhxtLevelPersonDao;
import com.reps.khxt.dao.KxhtGroupDao;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.service.IKhxtGroupService;

import net.sf.json.JSONArray;

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

	@SuppressWarnings("unchecked")
	@Override
	public KhxtGroup get(String id) throws Exception {
		ObjectMapper obj = new ObjectMapper();
		KhxtGroup khxtGroup = dao.get(id);

		String khr = khxtGroup.getKhr();
		String bkhr = khxtGroup.getBkhr();

		List<Map<String, String>> khrlist = obj.readValue(khr, List.class);

		StringBuilder sbkhr = new StringBuilder();
		StringBuilder khrids = new StringBuilder();
		for (Map<String, String> map : khrlist) {
			String s = map.get("person_name");
			sbkhr.append(s);
			sbkhr.append(",");

			String ids = map.get("person_id");
			khrids.append(ids);
			khrids.append(",");
		}
		List<Map<String, String>> bkhrlist = obj.readValue(bkhr, List.class);
		StringBuilder sbbkhr = new StringBuilder();
		StringBuilder bkhrids = new StringBuilder();
		for (Map<String, String> map : bkhrlist) {
			String s = map.get("person_name");
			sbbkhr.append(s);
			sbbkhr.append(",");

			String ids = map.get("person_id");
			bkhrids.append(ids);
			bkhrids.append(",");
		}
		khxtGroup.setKhr(sbkhr.toString());
		khxtGroup.setKhrIds(khrids.toString());

		khxtGroup.setBkhr(sbbkhr.toString());
		khxtGroup.setBkhrIds(bkhrids.toString());

		return khxtGroup;
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
	public List<KhxtGroup> getByLvelId(String khrid) {
		
		return dao.getByLvelId(khrid);
	}

}
