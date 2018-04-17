package com.reps.khxt.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.khxt.entity.CellMergeRange;
import com.reps.khxt.service.IExcelExportService;

@Service
public class ExcelExportServiceImpl implements IExcelExportService {

	@Override
	public void export2003(List<Map<String, Object>> resultList, List<CellMergeRange> cellMergeRanges, String[] headers, String sheetName, OutputStream out) throws RepsException {
		if (resultList == null) {
			throw new RepsException("生成excel时，接口参数非法！");
		}
		if (headers.length == 0) {
			throw new RepsException("生成excel时，没有设置每列的属性！");
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook();
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			HSSFSheet sheet = wb.createSheet(sheetName);
			HSSFRow firstRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = firstRow.createCell(i);
				cell.setCellStyle(style);
				cell.setCellType(CellType.STRING);
				sheet.setColumnWidth(i, 6144);
				cell.setCellValue(new HSSFRichTextString(headers[i]));
			}
			if ((null != resultList) && (resultList.size() > 0)) {
				for (int j = 0; j < resultList.size(); j++) {
					Map<String, Object> result = resultList.get(j);
					HSSFRow row = sheet.createRow(j + 1);
					Iterator<Object> iterator = result.values().iterator();
					int k = 0;
					while (iterator.hasNext()) {
						HSSFCell cell = row.createCell(k);
						cell.setCellType(CellType.STRING);
						cell.setCellStyle(style);
						cell.setCellValue(new HSSFRichTextString(String.valueOf(iterator.next())));
						k++;
						if (k > 10000) {
							break;
						}
					}
				}
			}
			//设置单元格合并
			if (null != cellMergeRanges && !cellMergeRanges.isEmpty()) {
				for (int i = 0; i < 2; i++) {
					for (CellMergeRange range : cellMergeRanges) {
						CellRangeAddress cra = new CellRangeAddress(range.getFirstRow(), range.getLastRow(), i, i);
						sheet.addMergedRegion(cra);
					}
				}
			}
			wb.write(out);
		} catch (IOException ie) {
			throw new RepsException("生成excel失败！", ie);
		} catch (Exception e) {
			throw new RepsException("生成excel失败！", e);
		} finally {
			if(null != wb) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
