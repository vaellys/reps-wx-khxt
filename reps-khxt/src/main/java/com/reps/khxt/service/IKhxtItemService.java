package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtItem;

/**
 * 
 * @ClassName: IKhxtItemService
 * @Description: 指标操作业务类
 * @author qianguobing
 * @date 2018年3月14日 上午9:34:41
 */
public interface IKhxtItemService {

	/**
	 * 添加指标
	 * @param khxtItem
	 * @throws RepsException
	 */
	public void save(KhxtItem khxtItem) throws RepsException;
	
	/**
	 * 删除指标
	 * @param khxtItem
	 * @throws RepsException
	 */
	public void delete(KhxtItem khxtItem) throws RepsException;

	/**
	 * 修改指标
	 * @param khxtItem
	 * @throws RepsException
	 */
	public void update(KhxtItem khxtItem) throws RepsException;

	/**
	 * 查询指标
	 * @param id
	 * @return khxtItem
	 * @throws RepsException
	 */
	public KhxtItem get(String id, boolean eager) throws RepsException;

	/**
	 * 查询指标带分页参数
	 * @param start
	 * @param pagesize
	 * @param khxtItem
	 * @return ListResult<KhxtItem>
	 */
	public ListResult<KhxtItem> query(int start, int pagesize, KhxtItem khxtItem) throws RepsException;

	/**
	 * 查询指标列表
	 * @param cid
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkItemExistInCategory(String cid) throws RepsException;

	/**
	 * 检查指标名称是否存在
	 * @param khxtItem
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkItemNameExists(KhxtItem khxtItem) throws RepsException;

	/**
	 * 查询所有指标
	 * 
	 * @author Alex
	 * @date 2018年3月25日
	 * @return
	 * @return List<KhxtItem>
	 */
	public List<KhxtItem> findAll();

}
