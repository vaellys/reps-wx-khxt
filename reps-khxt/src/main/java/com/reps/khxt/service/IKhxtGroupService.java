package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtGroup;

/**
 * 考核人分组设置业务类
 * 
 * @author ：Alex
 * @date 2018年3月21日
 */
public interface IKhxtGroupService {
	/**
	 * 分页条件查询考核分组
	 * 
	 * @author Alex
	 * @date 2018年3月21日
	 * @param startRow
	 * @param pageSize
	 * @param khxtGroup
	 * @return
	 * @return ListResult<KhxtGroup>
	 */
	ListResult<KhxtGroup> query(int startRow, int pageSize, KhxtGroup khxtGroup);

	/**
	 * 保存考核分组
	 * 
	 * @author Alex
	 * @date 2018年3月22日
	 * @param group
	 * @param personIds
	 * @return void
	 */
	void save(KhxtGroup group, String khrIds, String bkhrIds);

	/**
	 * 删除考核分组
	 * 
	 * @author Alex
	 * @date 2018年3月22日
	 * @param khxtgroup
	 * @return void
	 */
	void delete(KhxtGroup khxtgroup);

	/**
	 * 根据id查询考核分组
	 * 
	 * @author Alex
	 * @date 2018年3月23日
	 * @param id
	 * @return
	 * @return KhxtGroup
	 * @throws Exception
	 */
	KhxtGroup get(String id) throws Exception;

	/**
	 * 修改考核分组
	 * 
	 * @author Alex
	 * @date 2018年3月24日
	 * @param group
	 * @param khrIds
	 * @param bkhrIds
	 * @return void
	 */
	void update(KhxtGroup group, String khrIds, String bkhrIds);

	/**
	 * 根据考核级id查询分组
	 * 
	 * @author Alex
	 * @date 2018年3月26日
	 * @param khrid
	 * @return
	 * @return KhxtGroup
	 * @throws Exception 
	 */
	List<String> getByLvelId(String khrid, String bkhrid) throws Exception;

	/**
	 * 检查级别是否存在
	 * @param levelId
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkLevelExist(String levelId) throws RepsException;

	/**
	 * 检查人员ID是否存在
	 * @param personId
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkPersonIdExist(String personId) throws RepsException;

}
