package org.dream.utils.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ExcelHelper {
	// excel2003扩展名
	private static final String EXCEL03_EXTENSION = ".xls";
	// excel2007扩展名
	private static final String EXCEL07_EXTENSION = ".xlsx";
	
	// 获取sheet列表
	public static List<String> getSheetList(File file,String extension) {
		PoiExcelHelper helper = getPoiExcelHelper(extension);			
		ArrayList<String> sheets = helper.getSheetList(file);
		return sheets;
	}
	
	// Excel读取
	public static ArrayList<ArrayList<String>> readExcel(File file,String extension, int sheetIndex) {
		PoiExcelHelper helper = getPoiExcelHelper(extension);
		ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> data = null;
		// 读取excel文件数据
		if(sheetIndex < 0){
			List<String> sheets =  getSheetList(file,extension);
			if( sheets != null )
			for(int i = 0; i < sheets.size(); i++ ){
				data = helper.readExcel(file,i);
				dataList.addAll(data);
			}
		}else{
			dataList = helper.readExcel(file,sheetIndex);
		}
        return dataList;
	}
	
	public static File writeExcel2003(String filepath,
			List<List<String>> datalist) throws Exception {
		return ExcelWriter.writeExcel2003(filepath, datalist);
	}

	public static File writeExcel2007(String filepath,
			List<List<String>> datalist) throws Exception {
		return ExcelWriter.writeExcel2007(filepath, datalist);
	}
	
	// 获取Excel处理类
	private static PoiExcelHelper getPoiExcelHelper(String extension) {
		PoiExcelHelper helper = null;
		if(extension.equals(EXCEL07_EXTENSION)) {
			helper = new PoiExcel2k7Helper();
		}else if(extension.equals(EXCEL03_EXTENSION)){
			helper = new PoiExcel2k3Helper();
		}
		return helper;
	}
	
}
