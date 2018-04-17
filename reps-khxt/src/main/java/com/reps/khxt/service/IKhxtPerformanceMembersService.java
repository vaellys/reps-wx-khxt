package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtPerformanceMembers;

public interface IKhxtPerformanceMembersService {

	/**
	 * 解析人员名单信息JSON
	 * 
	 * @param memberJson
	 * @param itemPointJson
	 * @param khxtPerformanceMembers
	 * @throws RepsException
	 */
	public void updateAndParseJson(String memberJson, String itemPointJson, KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException;

	/**
	 * 修改人员名单信息
	 * 
	 * @param khxtPerformanceMembers
	 * @throws RepsException
	 */
	public void update(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException;

	/**
	 * 获取考核人员名单信息
	 * 
	 * @param khxtPerformanceMembers
	 * @return List<KhxtPerformanceMembers>
	 * @throws RepsException
	 */
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers khxtPerformanceMembers, boolean eager) throws RepsException;

	/**
	 * 查询人员名单信息
	 * 
	 * @param id
	 * @return KhxtPerformanceMembers
	 * @throws RepsException
	 */
	public KhxtPerformanceMembers get(String id) throws RepsException;

	/**
	 * 检查考核人打分是否都完成
	 * 
	 * @param khxtPerformanceMembers
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkAppraiseFinished(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException;

	/**
	 * 条件查询人员名单
	 * 
	 * @author Alex
	 * @date 2018年4月8日
	 * @param members
	 * @return
	 * @return List<KhxtPerformanceMembers>
	 */
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers members);
	
	public List<String> findByGroup(KhxtPerformanceMembers members) throws RepsException;

	/**
	 * 分页条件查询
	 * 
	 * @author Alex
	 * @date 2018年4月13日
	 * @param startRow
	 * @param pageSize
	 * @param khxtPerformanceMembers
	 * @return
	 * @return List<KhxtPerformanceMembers>
	 */
	public ListResult<KhxtPerformanceMembers> query(int startRow, int pageSize, KhxtPerformanceMembers khxtPerformanceMembers);

}
