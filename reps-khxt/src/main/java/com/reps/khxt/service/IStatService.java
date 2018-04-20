package com.reps.khxt.service;

import java.util.List;
import java.util.Map;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.KhxtPerformanceMembers;

public interface IStatService {
	
	public List<Map<String, Object>> computeAssessItem(KhxtPerformanceMembers member) throws RepsException;

	public Map<String, Object> buildExcelDatas(String sheetId) throws RepsException;

}
