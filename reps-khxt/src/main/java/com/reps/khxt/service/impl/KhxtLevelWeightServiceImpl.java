package com.reps.khxt.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtLevelWeightDao;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.service.IKhxtLevelWeightService;

@Service
@Transactional
public class KhxtLevelWeightServiceImpl implements IKhxtLevelWeightService {
	
	protected final Logger logger = LoggerFactory.getLogger(KhxtLevelWeightServiceImpl.class);
	
	@Autowired
	KhxtLevelWeightDao dao;
	
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

	@Override
	public ListResult<KhxtLevelWeight> query(int start, int pagesize, KhxtLevelWeight khxtLevelWeight) {
		return dao.query(start, pagesize, khxtLevelWeight);
	}

}
