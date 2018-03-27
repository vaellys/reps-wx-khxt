package com.reps.khxt.service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.khxt.entity.KhxtAppraiseSheet;

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
	ListResult<KhxtAppraiseSheet> query(int startRow, int pageSize, KhxtAppraiseSheet sheet);

	/**
	 * 添加月考核
	 * 
	 * @author Alex
	 * @date 2018年3月25日
	 * @param sheet
	 * @param itemIds
	 * @param khrids
	 * @return void
	 * @throws Exception 
	 */
	void save(KhxtAppraiseSheet sheet, String[] khrids, String itemIds) throws Exception;

	/**
	 * 删除月考核
	 * 
	 * @author Alex
	 * @date 2018年3月25日
	 * @param sheet
	 * @return void
	 */
	void delete(KhxtAppraiseSheet sheet);
	
	/**
	 * 查询月考核
	 * @param id
	 * @return KhxtAppraiseSheet
	 * @throws RepsException
	 */
	KhxtAppraiseSheet get(String id, boolean eager) throws RepsException;

}
