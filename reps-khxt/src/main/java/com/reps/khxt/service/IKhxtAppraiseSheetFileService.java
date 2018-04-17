package com.reps.khxt.service;

import java.util.List;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.KhxtAppraiseSheetFile;

public interface IKhxtAppraiseSheetFileService {

	/**
	 * 根据考核表id查询考核文件
	 * 
	 * @author Alex
	 * @date 2018年3月29日
	 * @param sheetId
	 * @return
	 * @return KhxtAppraiseSheetFile
	 */
	List<KhxtAppraiseSheetFile> findFileBySheetId(String sheetId);
	
	public KhxtAppraiseSheetFile get(String id) throws RepsException;

}
