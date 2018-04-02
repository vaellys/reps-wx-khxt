package com.reps.khxt.service;

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
	KhxtAppraiseSheetFile findFileBySheetId(String sheetId);

}
