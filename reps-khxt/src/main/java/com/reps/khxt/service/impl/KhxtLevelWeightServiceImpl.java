package com.reps.khxt.service.impl;

import java.util.Iterator;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtLevelWeightDao;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.service.IKhxtLevelService;
import com.reps.khxt.service.IKhxtLevelWeightService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class KhxtLevelWeightServiceImpl implements IKhxtLevelWeightService {
	
	protected final Logger logger = LoggerFactory.getLogger(KhxtLevelWeightServiceImpl.class);
	
	@Autowired
	KhxtLevelWeightDao dao;
	
	@Autowired
	IKhxtLevelService khxtLevelService;
	
	@Override
	public void save(KhxtLevelWeight khxtLevelWeight) throws RepsException {
		dao.save(khxtLevelWeight);
	}

	@Override
	public void delete(KhxtLevelWeight khxtLevelWeight) throws RepsException {
		dao.delete(khxtLevelWeight);
	}

	@Override
	public void update(KhxtLevelWeight khxtLevelWeight) throws RepsException {
		if(null == khxtLevelWeight) {
			throw new RepsException("参数异常");
		}
		KhxtLevelWeight levelWeght = dao.get(khxtLevelWeight.getId());
		String name = khxtLevelWeight.getName();
		if(StringUtil.isNotBlank(name)) {
			levelWeght.setName(name);
		}
		Short visible = khxtLevelWeight.getVisible();
		if(null != visible) {
			levelWeght.setVisible(visible);
		}
		String weight = khxtLevelWeight.getWeight();
		if(StringUtil.isNotCN(weight)) {
			levelWeght.setWeight(weight);
		}
		Integer year = khxtLevelWeight.getYear();
		if(null != year) {
			levelWeght.setYear(year);
		}
		dao.update(levelWeght);
	}

	@Override
	public KhxtLevelWeight get(String id) throws RepsException {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:级别权重ID不能为空");
		}
		KhxtLevelWeight khxtLevelWeight = dao.get(id);
		if(null == khxtLevelWeight) {
			throw new RepsException("参数异常:级别权重ID无效");
		}
		return khxtLevelWeight;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<KhxtLevelWeight> query(int start, int pagesize, KhxtLevelWeight khxtLevelWeight) {
		ListResult<KhxtLevelWeight> listResult = dao.query(start, pagesize, khxtLevelWeight);
		for (KhxtLevelWeight levelWeight : listResult.getList()) {
			String weight = levelWeight.getWeight();
			if(StringUtil.isNotBlank(weight)) {
				JSONArray levelWeightArray = JSONArray.fromObject(weight);
				StringBuilder sb = new StringBuilder();
				if(null != levelWeightArray && !levelWeightArray.isEmpty()) {
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
		}
		return listResult;
	}

	@Override
	public void copy(String id) throws RepsException {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:级别权重ID为空");
		}
		dao.insert(id);
	}

}
