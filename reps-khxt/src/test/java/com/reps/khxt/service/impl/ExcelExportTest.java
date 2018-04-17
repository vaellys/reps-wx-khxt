package com.reps.khxt.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;


public class ExcelExportTest {

	public static void main(String[] args) throws IOException {
		
		List<Map<String, String>> datas = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("serial", "1");
		map.put("province", "湖北省松滋市");
		map.put("department", "研发部");
		map.put("username", "张飞");
		datas.add(map);
		
		Map<String, String> map2 = new HashMap<>();
		map2.put("serial", "1");
		map2.put("province", "北京");
		map2.put("department", "研发部");
		map2.put("username", "李四");
		datas.add(map2);
		
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("区县工作小组名单");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((short) 0);
		 // 第四步，创建单元格，并设置值表头 设置表头居中  
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		// 创建一个居中格式       
		HSSFCell cell = row.createCell((short) 0);  
		cell.setCellValue("序号"); 
		cell.setCellStyle(style);
		cell = row.createCell((short) 1); 
		cell.setCellValue("区县");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("部门");
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 3); 
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		
		for (int i = 0; i < datas.size(); i++) {
			 row = sheet.createRow((int) i + 1);
			 HSSFCell createCell = row.createCell((short) 0);
			 createCell.setCellStyle(style);
			 createCell.setCellValue(datas.get(i).get("serial"));
			 row.createCell((short) 1).setCellValue(datas.get(i).get("province"));
			 row.createCell((short) 2).setCellValue(datas.get(i).get("department"));   
		     row.createCell((short) 3).setCellValue(datas.get(i).get("username"));   
		     
		}
		     CellRangeAddress cra = new CellRangeAddress(1, 2, 0, 0);   
		     sheet.addMergedRegion(cra);    
		
		File out = new File("D:\\testexcel.xls");
		wb.write(out);
	}
	
	/*public static void main(String[] args) throws Exception {
		FileOutputStream fos=new FileOutputStream("D:\\13.xls");  
        
        Workbook wb=new HSSFWorkbook();  
          
        Sheet sheet=wb.createSheet();  
         
         * 设定合并单元格区域范围 
         *  firstRow  0-based 
         *  lastRow   0-based 
         *  firstCol  0-based 
         *  lastCol   0-based 
           
        CellRangeAddress cra=new CellRangeAddress(0, 3, 3, 9);        
          
        //在sheet里增加合并单元格  
        sheet.addMergedRegion(cra);  
          
        Row row = sheet.createRow(0);  
          
        Cell cell_1 = row.createCell(3);  
          
        cell_1.setCellValue("When you're right , no one remembers, when you're wrong ,no one forgets .");  
          
        //cell 位置3-9被合并成一个单元格，不管你怎样创建第4个cell还是第5个cell…然后在写数据。都是无法写入的。  
        Cell cell_2 = row.createCell(10);  
          
        cell_2.setCellValue("what's up ! ");  
          
        wb.write(fos);  
          
        fos.close();
	}*/
	
	

}
