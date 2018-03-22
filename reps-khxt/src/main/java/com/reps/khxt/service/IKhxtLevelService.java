package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtLevel;

/**
 * @ClassName: IKhxtLevelService
 * @Description: 级别设置业务类
 * @author qianguobing
 * @date 2018年3月14日 上午9:36:26
 */
public interface IKhxtLevelService {

	/**
	 * 添加级别
	 * 
	 * @param khxtLevel
	 * @throws RepsException
	 */
	public void save(KhxtLevel khxtLevel) throws RepsException;

	/**
	 * 删除级别
	 * 
	 * @param khxtLevel
	 * @throws RepsException
	 */
	public void delete(KhxtLevel khxtLevel) throws RepsException;

	/**
	 * 修改级别
	 * 
	 * @param khxtLevel
	 * @throws RepsException
	 */
	public void update(KhxtLevel khxtLevel) throws RepsException;

	/**
	 * 查询级别
	 * 
	 * @param id
	 * @return khxtLevel
	 * @throws RepsException
	 */
	public KhxtLevel get(String id) throws RepsException;

	/**
	 * 查询级别带分页参数
	 * 
	 * @param start
	 * @param pagesize
	 * @param khxtItem
	 * @return ListResult<KhxtLevel>
	 */
	public ListResult<KhxtLevel> query(int start, int pagesize, KhxtLevel khxtLevel);
	
	/**
	 * 查询所有级别
	 * @return List<KhxtLevel>
	 * @throws RepsException
	 */
	public List<KhxtLevel> findAll() throws RepsException;

}
