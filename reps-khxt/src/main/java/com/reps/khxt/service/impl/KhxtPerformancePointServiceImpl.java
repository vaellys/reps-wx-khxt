package com.reps.khxt.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
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

	@Override
	public void update(KhxtPerformancePoint khxtPerformancePoint) throws RepsException {
		if(null == khxtPerformancePoint) {
			throw new RepsException("参数异常");
		}
		KhxtPerformancePoint point = this.get(khxtPerformancePoint.getId());
		Double p = khxtPerformancePoint.getPoint();
		if(null != p) {
			point.setPoint(p);
		}
		dao.update(point);
	}

	@Override
	public KhxtPerformancePoint get(String id) throws RepsException {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("个人考核评分ID不能为空");
		}
		KhxtPerformancePoint khxtPerformancePoint = dao.get(id);
		if(null == khxtPerformancePoint) {
			throw new RepsException("参数异常:个人考核评分ID无效");
		}
		return khxtPerformancePoint;
	}

}
