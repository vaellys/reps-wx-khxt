package com.reps.khxt.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KxhtGroupDao;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtLevelWeight;
import com.reps.khxt.service.IKhxGroupService;

@Service
@Transactional
public class KxhtGroupServiceImpl implements IKhxGroupService {
	@Autowired
	private KxhtGroupDao dao;

	@Override
	public ListResult<KhxtGroup> query(int start, int pagesize, KhxtGroup khxtGroup) {

		return dao.query(start, pagesize, khxtGroup);
	}

	@Override
	public void save(KhxtGroup khxtGroup) {
		dao.save(khxtGroup);
	}

	@Override
	public KhxtGroup get(String id) {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:考核分组ID不能为空");
		}
		KhxtGroup KhxtGroup = dao.get(id);
		if(null == KhxtGroup) {
			throw new RepsException("参数异常:考核分组ID无效");
		}
		return KhxtGroup;
	}

}
