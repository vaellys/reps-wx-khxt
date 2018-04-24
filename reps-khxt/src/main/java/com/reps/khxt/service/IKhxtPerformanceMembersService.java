package com.reps.khxt.service;

import java.util.List;
import java.util.Map;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.KhxtAppraiseSheet;
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
	public void updateAndParseJson(String memberJson, String itemPointJson,
			KhxtPerformanceMembers khxtPerformanceMembers) throws RepsException;

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
	public List<KhxtPerformanceMembers> find(KhxtPerformanceMembers khxtPerformanceMembers, boolean eager)
			throws RepsException;

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

	public List<String> findByGroup(KhxtPerformanceMembers members) throws RepsException;

	/**
	 * 查询被考核人得分详情
	 * 
	 * @param member
	 * @return KhxtPerformanceMembers
	 * @throws RepsException
	 */
	KhxtPerformanceMembers findBkhrScoring(KhxtPerformanceMembers member) throws RepsException;

	/**
	 * 被考核人查询
	 * 
	 * @author Alex
	 * @date 2018年4月23日
	 * @param khxtPerformanceMembers
	 * @return
	 * @throws Exception
	 * @return List<KhxtPerformanceMembers>
	 */
	List<KhxtPerformanceMembers> bkhrList(KhxtPerformanceMembers khxtPerformanceMembers) throws Exception;

	/**
	 * 计算合计总分
	 * 
	 * @author Alex
	 * @date 2018年4月23日
	 * @param sheet
	 * @return
	 * @throws Exception
	 * @return Map<String,Double>
	 */
	public Map<String, Double> calculateScore(KhxtAppraiseSheet sheet) throws Exception;

	/**
	 * 得分详情
	 * 
	 * @author Alex
	 * @date 2018年4月23日
	 * @param members
	 * @return
	 * @throws Exception
	 * @return List<KhxtPerformanceMembers>
	 */
	List<KhxtPerformanceMembers> scoringDetails(KhxtPerformanceMembers members) throws Exception;

}
