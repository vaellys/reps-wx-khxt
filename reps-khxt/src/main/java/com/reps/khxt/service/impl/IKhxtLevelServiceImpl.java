package com.reps.khxt.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtLevelDao;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.service.IKhxtLevelService;

@Service
@Transactional
public class IKhxtLevelServiceImpl implements IKhxtLevelService {

	protected final Logger logger = LoggerFactory.getLogger(IKhxtLevelServiceImpl.class);

	@Autowired
	KhxtLevelDao dao;
	
	@Autowired
	IKhxtLevelPersonService khxtLevelPersonService;

	@Override
	public void save(KhxtLevel khxtLevel) throws RepsException {
		dao.save(khxtLevel);
	}

	@Override
	public void delete(KhxtLevel khxtLevel) throws RepsException {
		if(null == khxtLevel) {
			throw new RepsException("参数异常");
		}
		String id = khxtLevel.getId();
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:级别ID为空");
		}
		if(!khxtLevelPersonService.checkLevelPersonExistInLevel(id)) {
			dao.delete(khxtLevel);
		} else {
			throw new RepsException("删除级别中包含级别人员");
		}
	}

	@Override
	public void update(KhxtLevel khxtLevel) throws RepsException {
		if(null == khxtLevel) {
			throw new RepsException("参数异常");
		}
		KhxtLevel level = dao.get(khxtLevel.getId());
		String name = khxtLevel.getName();
		if(StringUtil.isNotBlank(name)) {
			level.setName(name);
		}
		Short l = khxtLevel.getLevel();
		if(null != l) {
			level.setLevel(l);
		}
		Short power = khxtLevel.getPower();
		if(null != power) {
			level.setPower(power);
		}
		dao.update(level);
	}

	@Override
	public KhxtLevel get(String id) throws RepsException {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:级别ID不能为空");
		}
		KhxtLevel khxtLevel = dao.get(id);
		if(null == khxtLevel) {
			throw new RepsException("参数异常:级别ID无效");
		}
		return dao.get(id);
	}

	@Override
	public ListResult<KhxtLevel> query(int start, int pagesize, KhxtLevel khxtLevel) {
		ListResult<KhxtLevel> listResult = dao.query(start, pagesize, khxtLevel);
		//设置级别人员名字
		setLevelPersonNames(listResult.getList());
		return listResult;
	}
	
	private void setLevelPersonNames(List<KhxtLevel> list) {
		if(null != list && !list.isEmpty()) {
			for (KhxtLevel level : list) {
				level.setPersonNames(khxtLevelPersonService.joinLevelPersonName(level.getId()));
			}
		}
	}

	@Override
	public List<KhxtLevel> findAll() throws RepsException {
		List<KhxtLevel> allList = dao.find();
		setLevelPersonNames(allList);
		return allList;
	}

}
