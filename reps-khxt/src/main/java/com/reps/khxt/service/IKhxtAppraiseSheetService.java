package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;
import com.reps.khxt.entity.KhxtLevelWeight;

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
	 * @throws Exception
	 */
	ListResult<KhxtAppraiseSheet> query(int startRow, int pageSize, KhxtAppraiseSheet sheet) throws Exception;
	List<KhxtAppraiseSheet> count() throws Exception;
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
	void save(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, List<KhxtAppraiseSheetFile> file)
			throws Exception;

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
	 * @throws Exception 
	 */
	void saveWorkPlan(String[] planning, String[] execution, String sheetId, String personId, String appear,
			String workId) throws Exception;

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
	void update(KhxtAppraiseSheet sheet, String[] khrids, String itemIds, List<KhxtAppraiseSheetFile> file) throws Exception;
	void update(KhxtAppraiseSheet khxtAppraiseSheet);
	/**
	 * 检查级别权重是否被引用
	 * 
	 * @param id
	 * @return boolean
	 * @throws RepsException
	 */
	public boolean checkWeightExistInSheet(String id) throws RepsException;

	/**
	 * 
	 * @author Alex
	 * @date 2018年4月12日
	 * @param levelWeight
	 * @return
	 * @return String
	 */
	public String getJsonWeight(KhxtLevelWeight levelWeight);
}
