package com.reps.khxt.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.khxt.dao.KhxtPerformancePointDao;
import com.reps.khxt.entity.KhxtPerformancePoint;
import com.reps.khxt.service.IKhxtPerformancePointService;

@Service
@Transactional
public class KhxtPerformancePointServiceImpl implements IKhxtPerformancePointService {
	
	protected final Logger logger = LoggerFactory.getLogger(KhxtPerformancePointServiceImpl.class);
	
	@Autowired
	KhxtPerformancePointDao dao;

	@Override
	public void save(KhxtPerformancePoint khxtPerformancePoint) throws RepsException {
		dao.save(khxtPerformancePoint);
	}
	

}
