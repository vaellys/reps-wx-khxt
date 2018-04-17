package com.reps.khxt.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reps.core.exception.RepsException;
import com.reps.khxt.dao.KhxtAppraiseSheetFileDao;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;
import com.reps.khxt.service.IKhxtAppraiseSheetFileService;

@Service
@Transactional
public class KhxtAppraiseSheetFileServiceImpl implements IKhxtAppraiseSheetFileService {

	@Autowired
	private KhxtAppraiseSheetFileDao dao;

	@Override
	public List<KhxtAppraiseSheetFile> findFileBySheetId(String sheetId) {
		if (StringUtils.isBlank(sheetId)) {
			throw new RepsException("考核表ID不能为空！");
		}
		List<KhxtAppraiseSheetFile> list = dao.findBySheetId(sheetId);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list;
	}

	@Override
	public KhxtAppraiseSheetFile get(String id) throws RepsException {
		return dao.get(id);
	}

}
