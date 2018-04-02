package com.reps.khxt.service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;

public interface IKhxtAppraiseSheetService {

	/**
	 * 分页条件查询
	 * 
	 * @author Alex
	 * @date 2018年3月23日
	 * @param startRow
	 * @param pageSize
	 * @param sheet
	 * @return
	 * @return ListResult<KhxtAppraiseSheet>
	 */
	ListResult<KhxtAppraiseSheet> query(int startRow, int pageSize, KhxtAppraiseSheet sheet, boolean count);

	/**
	 * 添加月考核
	 * 
	 * @author Alex
	 * @date 2018年3月25日
	 * @param sheet
	 * @param itemIds
	 * @param khrids
	 * @param file
	 * @return void
	 * @throws Exception
	 */
	void save(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, KhxtAppraiseSheetFile file) throws Exception;

	/**
	 * 删除月考核
	 * 
	 * @author Alex
	 * @date 2018年3月25日
	 * @param sheet
	 * @return void
	 * @throws Exception
	 */
	void delete(KhxtAppraiseSheet sheet) throws Exception;

	/**
	 * 查询月考核
	 * 
	 * @param id
	 * @return KhxtAppraiseSheet
	 * @throws RepsException
	 */
	KhxtAppraiseSheet get(String id, boolean eager) throws RepsException;

	/**
	 * 保存工作计划
	 * 
	 * @author Alex
	 * @date 2018年3月27日
	 * @param planning
	 * @param execution
	 * @param sheetId
	 * @param appear
	 * @param work
	 * @return void
	 */
	void saveWorkPlan(String[] planning, String[] execution, String sheetId, String personId, String appear,
			String workId);

	KhxtAppraiseSheet get(String sheetId);

	/**
	 * 修改月考核制度表
	 * 
	 * @author Alex
	 * @date 2018年3月29日
	 * @param sheet
	 * @param khrids
	 * @param itemIds
	 * @param file
	 * @return void
	 * @throws Exception
	 */
	void update(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, KhxtAppraiseSheetFile file) throws Exception;

	/**
	 * 检查级别权重是否被引用
	 * @param id
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkWeightExistInSheet(String id) throws RepsException;

}
