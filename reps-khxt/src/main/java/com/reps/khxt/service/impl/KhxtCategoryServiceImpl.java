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
import com.reps.khxt.dao.KhxtCategoryDao;
import com.reps.khxt.entity.KhxtCategory;
import com.reps.khxt.service.IKhxtCategoryService;
import com.reps.khxt.service.IKhxtItemService;

@Service
@Transactional
public class KhxtCategoryServiceImpl implements IKhxtCategoryService {
	
	protected final Logger logger = LoggerFactory.getLogger(KhxtCategoryServiceImpl.class);
	
	@Autowired
	KhxtCategoryDao dao;
	
	@Autowired
	IKhxtItemService khxtItemService;

	@Override
	public void save(KhxtCategory khxtCategory) throws RepsException {
		if(null == khxtCategory) {
			throw new RepsException("数据异常");
		}
		if(!this.checkCategoryNameExists(khxtCategory)) {
			dao.save(khxtCategory);
		} else {
			throw new RepsException("类别名称已存在，请重新输入");
		}
	}

	@Override
	public void delete(KhxtCategory khxtCategory) throws RepsException {
		if(null == khxtCategory) {
			throw new RepsException("参数异常");
		}
		String id = khxtCategory.getId();
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:类别ID为空");
		}
		if(!khxtItemService.checkItemExistInCategory(id)) {
			dao.delete(khxtCategory);
		} else {
			throw new RepsException("删除类别中包含指标");
		}
	}

	@Override
	public void update(KhxtCategory khxtCategory) throws RepsException {
		if(null == khxtCategory) {
			throw new RepsException("参数异常");
		}
		KhxtCategory category = dao.get(khxtCategory.getId());
		String name = khxtCategory.getName();
		if(StringUtil.isNotBlank(name)) {
			category.setName(name);
		}
		String remark = khxtCategory.getRemark();
		if(StringUtil.isNotBlank(remark)) {
			category.setRemark(remark);
		}
		dao.update(category);
	}

	@Override
	public KhxtCategory get(String id) throws RepsException {
		if(StringUtil.isBlank(id)) {
			throw new RepsException("参数异常:类别ID不能为空");
		}
		KhxtCategory khxtCategory = dao.get(id);
		if(null == khxtCategory) {
			throw new RepsException("参数异常:类别ID无效");
		}
		return khxtCategory;
	}

	@Override
	public ListResult<KhxtCategory> query(int start, int pagesize, KhxtCategory khxtCategory) {
		return dao.query(start, pagesize, khxtCategory);
	}

	@Override
	public boolean checkCategoryNameExists(KhxtCategory khxtCategory) throws RepsException {
		String name = khxtCategory.getName();
		if(StringUtil.isNotBlank(name)) {
			List<KhxtCategory> list = dao.find(khxtCategory);
			if(null == list || list.isEmpty()) {
				return false;
			}else {
				return true;
			}
		} 
		return false;
	}

	@Override
	public List<KhxtCategory> findAll() throws RepsException {
		return dao.find(null);
	}
	
}
