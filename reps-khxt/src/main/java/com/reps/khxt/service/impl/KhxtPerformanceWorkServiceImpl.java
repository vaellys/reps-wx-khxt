package com.reps.khxt.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtPerformanceWorkDao;
import com.reps.khxt.entity.KhxtPerformanceWork;
import com.reps.khxt.service.IKhxtPerformanceWorkService;

@Service
@Transactional
public class KhxtPerformanceWorkServiceImpl implements IKhxtPerformanceWorkService {
	
	protected final Logger logger = LoggerFactory.getLogger(KhxtPerformanceWorkServiceImpl.class);
	
	@Autowired
	private KhxtPerformanceWorkDao dao;

	@Override
	public List<KhxtPerformanceWork> find(KhxtPerformanceWork khxtPerformanceWork) throws RepsException {
		if(null == khxtPerformanceWork) {
			throw new RepsException("参数异常");
		}
		String sheetId = khxtPerformanceWork.getSheetId();
		if(StringUtil.isBlank(sheetId)) {
			throw new RepsException("参数异常:考核表ID为空");
		}
		String personId = khxtPerformanceWork.getPersonId();
		if(StringUtil.isBlank(personId)) {
			throw new RepsException("参数异常:被考核人ID为空");
		}
		return dao.find(khxtPerformanceWork);
	}

}
