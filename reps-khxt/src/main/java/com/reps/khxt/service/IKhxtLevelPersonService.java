package com.reps.khxt.service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.vo.UserVo;
import com.reps.system.entity.User;

/**
 * @ClassName: IKhxtLevelPersonService
 * @Description: 级别人员业务类
 * @author qianguobing
 * @date 2018年3月14日 上午9:36:26
 */
public interface IKhxtLevelPersonService {
	
	/**
	 * 添加级别人员
	 * @param khxtLevelPerson
	 * @throws RepsException
	 */
	public void save(KhxtLevelPerson khxtLevelPerson) throws RepsException;
	
	/**
	 * 保存多个级别人员
	 * @param list
	 * @throws RepsException
	 */
	public void saveAll(KhxtLevelPerson khxtLevelPerson) throws RepsException;
	
	/**
	 * 删除级别人员
	 * @param khxtLevelPerson
	 * @throws RepsException
	 */
	public void delete(KhxtLevelPerson khxtLevelPerson) throws RepsException;
	
	/**
	 * 根据条件删除所有级别人员
	 * @param ids
	 * @throws RepsException
	 */
	public void deleteAll(String ids) throws RepsException;
	
	/**
	 * 查询级别人员
	 * @param id
	 * @return khxtLevelPerson
	 * @throws RepsException
	 */
	public KhxtLevelPerson get(String id) throws RepsException;
	
	/**
	 * 查询级别人员带分页参数
	 * @param start
	 * @param pagesize
	 * @param khxtItem
	 * @return ListResult<KhxtLevelPerson>
	 */
	public ListResult<KhxtLevelPerson> query(int start, int pagesize, KhxtLevelPerson khxtLevelPerson);

	/**
	 * 选择人员信息
	 * @param start
	 * @param pagesize
	 * @param user
	 * @return ListResult<Map<String, Object>>
	 * @throws RepsException
	 */
	public ListResult<UserVo> choosePerson(int start, int pagesize, User user, String levelId) throws RepsException;
	
	/**
	 * 检查级别中是否包含级别人员
	 * @param levelId
	 * @return
	 * @throws RepsException
	 */
	public boolean checkLevelPersonExistInLevel(String levelId) throws RepsException;
	
	/**
	 * 以,拼接级别人员名字
	 * @param levelId
	 * @return String
	 * @throws RepsException
	 */
	public String joinLevelPersonName(String levelId) throws RepsException;
	

}
