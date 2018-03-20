package com.reps.khxt.service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtLevelWeight;

/**
 * @ClassName: IKhxtLevelWeightService
 * @Description: TODO
 * @author qianguobing
 * @date 2018年3月20日 下午4:50:27
 */
public interface IKhxtLevelWeightService {
	
	/**
	 * 添加级别权重
	 * @param khxtLevelWeight
	 * @throws RepsException
	 */
	public void save(KhxtLevelWeight khxtLevelWeight) throws RepsException;
	
	/**
	 * 删除级别权重
	 * @param khxtLevelWeight
	 * @throws RepsException
	 */
	public void delete(KhxtLevelWeight khxtLevelWeight) throws RepsException;
	
	/**
	 * 修改级别权重
	 * @param khxtLevelWeight
	 * @throws RepsException
	 */
	public void update(KhxtLevelWeight khxtLevelWeight) throws RepsException;
	
	/**
	 * 查询级别权重
	 * @param id
	 * @return khxtLevelWeight
	 * @throws RepsException
	 */
	public KhxtLevelWeight get(String id) throws RepsException;
	
	/**
	 * 查询级别权重带分页参数
	 * @param start
	 * @param pagesize
	 * @param khxtLevelWeight
	 * @return ListResult<KhxtCategory>
	 */
	public ListResult<KhxtLevelWeight> query(int start, int pagesize, KhxtLevelWeight khxtLevelWeight);
	
}
