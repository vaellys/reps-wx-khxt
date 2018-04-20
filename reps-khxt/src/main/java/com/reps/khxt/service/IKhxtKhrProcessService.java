package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtKhrProcess;

public interface IKhxtKhrProcessService {
	
	/**
	 * 更新打分情况状态
	 * @param khxtKhrProcess
	 * @throws RepsException
	 */
	public void updateStatus(KhxtKhrProcess khxtKhrProcess) throws RepsException;
	
	/**
	 * 查询考核人打分情况列表
	 * @param khxtKhrProcess
	 * @return List<KhxtKhrProcess>
	 * @throws RepsException
	 */
	public List<KhxtKhrProcess> find(KhxtKhrProcess khxtKhrProcess) throws RepsException;
	
	/**
	 * 检查用户是否是考核人
	 * @param sheetId
	 * @param personId
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkKhr(String sheetId, String personId) throws RepsException;

	/**
	 * 检查用户是否已经打完分
	 * @param sheetId
	 * @param personId
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkCompletedMarking(String sheetId, String personId) throws RepsException;

	/**
	 * 考核人列表查询
	 * @param start
	 * @param pagesize
	 * @param process
	 * @return
	 * @throws RepsException
	 */
	public ListResult<KhxtKhrProcess> query(int start, int pagesize, KhxtKhrProcess process) throws RepsException;

}
