package com.reps.khxt.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.reps.core.exception.RepsException;
import com.reps.khxt.vo.CellMergeRange;

public interface IExcelExportService {

	public void export2003(List<Map<String, Object>> resultList, List<CellMergeRange> cellMergeRanges, String[] headers, String sheetName, OutputStream out) throws RepsException; 
}
