package com.reps.khxt.service;

import java.util.List;
import java.util.Map;

import com.reps.core.exception.RepsException;

public interface IStatService {
	
	public List<Map<String, Object>> computeAssessItem(String sheetId) throws RepsException;

	public Map<String, Object> buildExcelDatas(String sheetId) throws RepsException;

}
