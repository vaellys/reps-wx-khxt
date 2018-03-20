package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtCategory;

/**
 * @ClassName: IKhxtCategoryService
 * @Description: 指标类别操作业务类
 * @author qianguobing
 * @date 2018年3月14日 上午9:35:29
 */
public interface IKhxtCategoryService {
	
	/**
	 * 添加指标类别
	 * @param khxtCategory
	 * @throws RepsException
	 */
	public void save(KhxtCategory khxtCategory) throws RepsException;
	
	/**
	 * 删除指标类别
	 * @param khxtCategory
	 * @throws RepsException
	 */
	public void delete(KhxtCategory khxtCategory) throws RepsException;
	
	/**
	 * 修改指标类别
	 * @param khxtCategory
	 * @throws RepsException
	 */
	public void update(KhxtCategory khxtCategory) throws RepsException;
	
	/**
	 * 查询指标类别
	 * @param id
	 * @return KhxtCategory
	 * @throws RepsException
	 */
	public KhxtCategory get(String id) throws RepsException;
	
	/**
	 * 查询指标类别带分页参数
	 * @param start
	 * @param pagesize
	 * @param khxtCategory
	 * @return ListResult<KhxtCategory>
	 */
	public ListResult<KhxtCategory> query(int start, int pagesize, KhxtCategory khxtCategory);
	
	/**
	 * 检查类别名称的唯一性
	 * @param khxtCategory
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkCategoryNameExists(KhxtCategory khxtCategory) throws RepsException;
	
	/**
	 * 查询所有类别
	 * @return List<KhxtCategory>
	 * @throws RepsException
	 */
	public List<KhxtCategory> findAll() throws RepsException;

}
