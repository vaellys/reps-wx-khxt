package com.reps.khxt.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reps.core.RepsConstant;
import com.reps.core.commons.Pagination;
import com.reps.core.web.AjaxStatus;
import com.reps.core.web.BaseAction;
import com.reps.khxt.entity.CellMergeRange;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.service.IExcelExportService;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IStatService;

@Controller
@RequestMapping(value = RepsConstant.ACTION_BASE_PATH + "/khxt/assessdetail")
public class StatAction extends BaseAction {
	
	protected final Logger logger = LoggerFactory.getLogger(StatAction.class);
	
	@Autowired
	IStatService statService;
	
	@Autowired
	IKhxtAppraiseSheetService khxtAppraiseSheetService;
	
	@Autowired
	IExcelExportService excelExportService;
	
	@RequestMapping(value = "/stat")
	public Object stat(Pagination pager, String sheetId) {
		ModelAndView mav = getModelAndView("/khxt/assessdetail/stat");
		try {
			List<Map<String, Object>> resultList = statService.computeAssessItem(sheetId);
			KhxtAppraiseSheet khxtAppraiseSheet = khxtAppraiseSheetService.get(sheetId, true);
			mav.addObject("items", khxtAppraiseSheet.getItem());
			mav.addObject("sheetId", sheetId);
			mav.addObject("assessItems", resultList);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("统计失败", e);
			return ajax(AjaxStatus.ERROR, e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/export" })
	public void exportExcel(String sheetId, HttpServletResponse response) {
		OutputStream out = null;
		try {
			KhxtAppraiseSheet khxtAppraiseSheet = khxtAppraiseSheetService.get(sheetId, true);
			Set<KhxtItem> itemList = khxtAppraiseSheet.getItem();
			Map<String, Object> resultMap = statService.buildExcelDatas(sheetId);
			out = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + new String("月考核表".getBytes(), "ISO8859-1") + ".xls");
			List<String> header = new ArrayList<>();
			header.add("序号");
			header.add("被考核人");
			header.add("考核人");
			double itemSum = 0.0;
			if(null != itemList && !itemList.isEmpty()) {
				for (KhxtItem item : itemList) {
					itemSum += item.getPoint();
					header.add(item.getName() + "（" + item.getPoint() + "）");
				}
			}
			header.add("汇总（" + itemSum + "）");
			this.excelExportService.export2003((List<Map<String, Object>>) resultMap.get("result"), (List<CellMergeRange>) resultMap.get("cellRanges"), header.toArray(new String[header.size()]), "月考核表", out);
			out.flush();
		} catch (Exception e) {
			this.logger.error("导出月考核表失败", e);
			if (out != null)
				try {
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
