package com.reps.khxt.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtItemDao;
import com.reps.khxt.entity.KhxtCategory;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.service.IKhxtItemService;

@Service
@Transactional
public class KhxtItemServiceImpl implements IKhxtItemService {

	protected final Logger logger = LoggerFactory.getLogger(KhxtItemServiceImpl.class);

	@Autowired
	KhxtItemDao dao;

	@Override
	public boolean save(KhxtItem khxtItem) throws RepsException {
		if (null == khxtItem) {
			throw new RepsException("数据异常");
		}
		if (!this.checkItemNameExists(khxtItem)) {
			dao.save(khxtItem);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void delete(KhxtItem khxtItem) throws RepsException {
		if (null == khxtItem) {
			throw new RepsException("参数异常");
		}
		KhxtItem item = this.get(khxtItem.getId(), true);
		if (null != item.getSheets() && !item.getSheets().isEmpty()) {
			throw new RepsException("该指标已经被考核表所引用！");
		} else {
			dao.delete(item);
		}
	}

	@Override
	public void update(KhxtItem khxtItem) throws RepsException {
		if (null == khxtItem) {
			throw new RepsException("参数异常");
		}
		KhxtItem item = dao.get(khxtItem.getId());
		String name = khxtItem.getName();
		if (StringUtil.isNotBlank(name)) {
			item.setName(name);
		}
		Double point = khxtItem.getPoint();
		if (null != point) {
			item.setPoint(point);
		}
		KhxtCategory khxtCategory = khxtItem.getKhxtCategory();
		if (null != khxtCategory) {
			item.setKhxtCategory(khxtCategory);
		}
		dao.update(item);
	}

	@Override
	public KhxtItem get(String id, boolean eager) throws RepsException {
		if (StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:类别ID不能为空");
		}
		KhxtItem khxtItem = dao.get(id);
		if (null == khxtItem) {
			throw new RepsException("参数异常:类别ID无效");
		}
		if (eager) {
			Hibernate.initialize(khxtItem.getSheets());
		}
		return khxtItem;
	}

	@Override
	public ListResult<KhxtItem> query(int start, int pagesize, KhxtItem khxtItem) {
		return dao.query(start, pagesize, khxtItem);
	}

	@Override
	public boolean checkItemExistInCategory(String cid) throws RepsException {
		if (StringUtil.isBlank(cid)) {
			throw new RepsException("参数异常:请指定类别ID");
		}
		KhxtItem item = new KhxtItem();
		item.setCategoryId(cid);
		List<KhxtItem> itemList = dao.find(item);
		if (null == itemList || itemList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean checkItemNameExists(KhxtItem khxtItem) throws RepsException {
		String name = khxtItem.getName();
		if (StringUtil.isNotBlank(name)) {
			List<KhxtItem> list = dao.find(khxtItem);
			if (null != list && !list.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<KhxtItem> findAll() {

		return dao.find(null);
	}

}
