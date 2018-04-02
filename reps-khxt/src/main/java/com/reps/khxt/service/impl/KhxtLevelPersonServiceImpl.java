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
import com.reps.khxt.dao.KhxtLevelPersonDao;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.service.IKhxtGroupService;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.vo.UserVo;
import com.reps.system.entity.Account;
import com.reps.system.entity.Organize;
import com.reps.system.entity.Person;
import com.reps.system.entity.User;
import com.reps.system.service.IAccountService;
import com.reps.system.service.IPersonService;
import com.reps.system.service.IUserService;

@Service
@Transactional
public class KhxtLevelPersonServiceImpl implements IKhxtLevelPersonService {

	protected final Logger logger = LoggerFactory.getLogger(KhxtLevelPersonServiceImpl.class);

	@Autowired
	KhxtLevelPersonDao dao;

	@Autowired
	IUserService userService;

	@Autowired
	IPersonService personService;

	@Autowired
	IAccountService accountService;
	
	@Autowired
	IKhxtGroupService khxtGroupService;

	@Override
	public void save(KhxtLevelPerson khxtLevelPerson) throws RepsException {
		dao.save(khxtLevelPerson);
	}

	@Override
	public void saveAll(KhxtLevelPerson khxtLevelPerson) throws RepsException {
		String levelId = khxtLevelPerson.getLevelId();
		if (StringUtil.isBlank(levelId)) {
			throw new RepsException("参数异常:级别ID为空");
		}
		for (String id : khxtLevelPerson.getPersonIds().split(",")) {
			KhxtLevelPerson lp = new KhxtLevelPerson();
			// 设置级别ID
			KhxtLevel khxtLevel = new KhxtLevel();
			khxtLevel.setId(levelId);
			lp.setKhxtLevel(khxtLevel);
			lp.setPersonId(id);
			// 获取相关联person
			Person person = personService.get(id);
			lp.setPersonName(person.getName());
			lp.setPersonSex(person.getSex());
			// 设置机构ID
			Organize o = new Organize();
			Account account = accountService.getByPersonId(id, true);
			User user = userService.getByAccountId(account.getId()).get(0);
			o.setId(user.getOrganizeId());
			lp.setOrganize(o);
			this.save(lp);
		}
	}

	@Override
	public void delete(KhxtLevelPerson khxtLevelPerson) throws RepsException {
		dao.delete(khxtLevelPerson);
	}

	@Override
	public KhxtLevelPerson get(String id) throws RepsException {
		return dao.get(id);
	}

	@Override
	public ListResult<KhxtLevelPerson> query(int start, int pagesize, KhxtLevelPerson khxtLevelPerson) {
		return dao.query(start, pagesize, khxtLevelPerson);
	}

	@Override
	public ListResult<UserVo> choosePerson(int start, int pagesize, User user, String levelId) throws RepsException {
		return dao.chooseLevelPerson(start, pagesize, user, levelId);
	}

	@Override
	public void deleteAll(String ids) throws RepsException {
		if (StringUtil.isNotBlank(ids)) {
			for (String id : ids.split(",")) {
				KhxtLevelPerson khxtLevelPerson = dao.get(id);
				if(null != khxtLevelPerson) {
					if(khxtGroupService.checkPersonIdExist(khxtLevelPerson.getPersonId())) {
						throw new RepsException("该人员已经被分组");
					}
				}
			}
			dao.batchDelete(ids);
		}
	}

	@Override
	public boolean checkLevelPersonExistInLevel(String levelId) throws RepsException {
		if (StringUtil.isBlank(levelId)) {
			throw new RepsException("参数异常:请指定级别ID");
		}
		KhxtLevelPerson lp = new KhxtLevelPerson();
		lp.setLevelId(levelId);
		List<KhxtLevelPerson> levelPersonList = dao.find(lp);
		if (null == levelPersonList || levelPersonList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String joinLevelPersonName(String levelId) throws RepsException {
		if (StringUtil.isBlank(levelId)) {
			throw new RepsException("参数异常:请指定级别ID");
		}
		StringBuilder sb = new StringBuilder();
		KhxtLevelPerson lp = new KhxtLevelPerson();
		lp.setLevelId(levelId);
		List<KhxtLevelPerson> list = dao.find(lp);
		if (null != list && !list.isEmpty()) {
			for (KhxtLevelPerson khxtLevelPerson : list) {
				sb.append(khxtLevelPerson.getPersonName());
				sb.append(",");
			}
			sb.deleteCharAt(sb.toString().lastIndexOf(","));
		}
		return sb.toString();
	}

	@Override
	public List<KhxtLevelPerson> findLevelPerson(KhxtLevelPerson khxtLevelPerson) {

		return dao.find(khxtLevelPerson);
	}

	@Override
	public KhxtLevelPerson getByPersonId(String id) {

		return dao.getByPersonId(id);
	}

}
