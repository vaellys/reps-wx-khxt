package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.KhxtPerformanceMembers;

public interface IKhxtPerformanceMembersService {

	/**
	 * 解析人员名单信息JSON
	 * @param memberJson
	 * @throws RepsException
	 */
	public void updateAndParseJson(String memberJson, String itemPointJson) throws RepsException;
	
	/**
	 * 修改人员名单信息
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
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException;

	/**
	 * 查询人员名单信息
	 * @param id
	 * @return KhxtPerformanceMembers
	 * @throws RepsException
	 */
	public KhxtPerformanceMembers get(String id) throws RepsException;
	
}
