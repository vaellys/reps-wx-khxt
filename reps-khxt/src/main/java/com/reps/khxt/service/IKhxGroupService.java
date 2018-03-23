package com.reps.khxt.service;

import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtGroup;
import com.reps.khxt.entity.KhxtLevelWeight;

/**
 * 考核人分组设置业务类
 * 
 * @author ：Alex
 * @date 2018年3月21日
 */
public interface IKhxGroupService {
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
	 * 添加分组
	 * 
	 * @author Alex
	 * @date 2018年3月21日
	 */
	void save(KhxtGroup khxtLevelWeight);

	/**
	 * 根据id查询分组
	 * 
	 * @param id
	 * @return
	 */
	KhxtGroup get(String id);
}
