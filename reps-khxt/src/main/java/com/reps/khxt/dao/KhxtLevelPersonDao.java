package com.reps.khxt.dao;

import static com.reps.khxt.util.SqlUtil.formatSql;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.reps.core.orm.IGenericDao;
import com.reps.core.orm.IJdbcDao;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.KhxtLevel;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.vo.UserVo;
import com.reps.system.entity.Account;
import com.reps.system.entity.Organize;
import com.reps.system.entity.Person;
import com.reps.system.entity.User;

/**
 * @ClassName: KhxtLevelPersonDao
 * @Description: 级别人员DAO
 * @author qianguobing
 * @date 2018年3月18日 上午11:16:25
 */
@Repository
public class KhxtLevelPersonDao {

	@Autowired
	IGenericDao<KhxtLevelPerson> dao;

	@Autowired
	IJdbcDao jdbcDao;

	public void save(KhxtLevelPerson khxtLevelPerson) {
		dao.save(khxtLevelPerson);
	}

	public void delete(KhxtLevelPerson khxtLevelPerson) {
		dao.delete(khxtLevelPerson);
	}

	public KhxtLevelPerson get(String id) {
		return dao.get(KhxtLevelPerson.class, id);
	}

	public ListResult<KhxtLevelPerson> query(int start, int pagesize, KhxtLevelPerson khxtLevelPerson) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtLevelPerson.class);
		dc.createAlias("khxtLevel", "t");
		if (null != khxtLevelPerson) {
			KhxtLevel khxtLevel = khxtLevelPerson.getKhxtLevel();
			if (null != khxtLevel) {
				String name = khxtLevel.getName();
				if (StringUtil.isNotBlank(name)) {
					dc.add(Restrictions.like("t.name", name, MatchMode.ANYWHERE));
				}
			}
			String personName = khxtLevelPerson.getPersonName();
			if (StringUtil.isNotBlank(personName)) {
				dc.add(Restrictions.like("personName", personName));
			}
		}
		return dao.query(dc, start, pagesize, Order.asc("levelId"));
	}

	@SuppressWarnings({ "unchecked" })
	public ListResult<UserVo> chooseLevelPerson(int start, int pagesize, User user, String levelId) {
		StringBuilder selectSb = new StringBuilder();
		selectSb.append("select distinct new com.reps.khxt.vo.UserVo(p.id, u.identity as userIdentity, p.name, p.sex, o.name as organizeName, o.parentXpath)");
		StringBuilder sbLp = new StringBuilder();
		sbLp.append(" from User u, Account a, Person p, Organize o where u.accountId=a.id and a.personId=p.id and u.organizeId=o.id and u.validRecord = 1");
		if (null != user) {
			Account account = user.getAccount();
			if (null != account) {
				Person person = account.getPerson();
				if (null != person) {
					String name = person.getName();
					if (StringUtil.isNotBlank(name)) {
						sbLp.append(" and p.name like '%");
						sbLp.append(name);
						sbLp.append("%'");
					}
				}

			}
			Organize organize = user.getOrganize();
			if (null != organize) {
				String name = organize.getName();
				if (StringUtil.isNotBlank(name)) {
					sbLp.append(" and o.name like '%");
					sbLp.append(name);
					sbLp.append("%'");
				}
			}
		}
		if (StringUtil.isNotBlank(levelId)) {
			sbLp.append(" and u.account.person.id not in(select lp.personId from KhxtLevelPerson lp where lp.levelId='");
			sbLp.append(levelId);
			sbLp.append("')");
		}
		sbLp.append(" and u.identity in ('10', '20')");
		Long uniqueResult = (Long) dao.getSession().createQuery("select count(distinct p.id)" + sbLp.toString()).uniqueResult();
		sbLp.append(" order by o.parentXpath asc");
		List<UserVo> list = dao.getSession().createQuery(selectSb.toString() + sbLp.toString()).setFirstResult(start).setMaxResults(pagesize).list();
		ListResult<UserVo> result = new ListResult<>();
		result.setList(list);
		result.setCount(uniqueResult);
		return result;
	}
	
	public void batchDelete(String ids) {
		StringBuilder sb = new StringBuilder("delete " + KhxtLevelPerson.class.getName() + " bean");
		sb.append(" where bean.id in (" + formatSql(ids) + ")");
		this.dao.execute(sb.toString());
	}
	
	public List<KhxtLevelPerson> find(KhxtLevelPerson khxtLevelPerson) {
		DetachedCriteria dc = DetachedCriteria.forClass(KhxtLevelPerson.class);
		if (null != khxtLevelPerson) {
			String levelId = khxtLevelPerson.getLevelId();
			if(StringUtil.isNotBlank(levelId)) {
				dc.add(Restrictions.eq("levelId", levelId));
			}
		}
		return dao.findByCriteria(dc);
	}

}
