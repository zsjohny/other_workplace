package org.dream.utils.excel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class ExcelWriter {
	/**
	 * 
	 * @param filepath
	 *            服务端文件地址
	 * @param datalist
	 *            要写入的数据
	 * @return
	 * @throws Exception
	 */
	public static File writeExcel2003(String filepath,
			List<List<String>> datalist) throws Exception {
		if (datalist == null || (datalist != null && datalist.size() <= 0))
			return null;
		return toExcel(datalist, 15000, filepath);
	}

	/**
	 * 2007每个sheet支持100W左右的数据，这里每65535行就新建一个sheet,延续2003每个sheet的数据量
	 * 
	 * @param filepath
	 * @param datalist
	 * @return
	 * @throws Exception
	 */
	public static File writeExcel2007(String filepath,
			List<List<String>> datalist) throws Exception {
		
		if ( datalist == null || (datalist != null && datalist.size() <= 0) )
			return null;
		File file = new File(filepath);
		if (!file.exists()) file.createNewFile();
		FileOutputStream fileOut = new FileOutputStream(file);
		BufferedOutputStream buffout = new BufferedOutputStream(fileOut);
		
		SXSSFWorkbook wb = new SXSSFWorkbook(1000);
		SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("sheet0");
		SXSSFRow row = null;
		SXSSFCell cell = null;
		
		List<String> rowdata = null;
		int rowSize = datalist.size();
		
		for ( int i = 0, j = 0; i < rowSize; i++ ) {
			//每个sheet只有65535行数据
			if ( (i > 0) && (i % 65535) == 0 ) {
				j++;
				sheet = (SXSSFSheet) wb.createSheet("sheet" + j);
			}
			rowdata = datalist.get(0);
			row = (SXSSFRow) sheet.createRow((i % 65535));
			for ( int c = 0; c < rowdata.size(); c++ ) {
				cell = (SXSSFCell) row.createCell(c);
				cell.setCellValue(rowdata.get(c));
			}
			//移除元素  使JVM能够发现并回收
			datalist.remove(0);
		}
		wb.write(buffout);
		buffout.flush();
		fileOut.flush();
		buffout.close();
		fileOut.close();
		datalist.clear();
		
		return file;
	}

	/**
	 * 
	 * @param datalist
	 *            要写入的数据
	 * @param length
	 *            每个sheet要写入的行数 暂定为15000行 超过30000内存压力太大
	 * @param f
	 *            要写入的文件
	 * @throws IOException
	 */
	private static File toExcel(List<List<String>> datalist, int length,
			String f) throws Exception {
		
		File targetFile = new File(f);
		if ( !targetFile.exists() ) targetFile.createNewFile();
		FileOutputStream o = null;
		int n = datalist.size() / length + 1; //创建n个临时的行数为length的xls文件
		File[] srcfile = new File[n];
		File tempfile = null;
		
		// 生成excel
		Workbook book = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		
		for ( int j = 0; j < n; j++ ) { // 循环创建文件
			int start = j * length;
			if ( datalist.size() > start ) {
				book  = new HSSFWorkbook();
				sheet = book.createSheet("sheet0");
				// String tempFilePath = "C://Users//yuanshizhining//Desktop//"+ j + ".xls";
				String tempFilePath = f.substring(0, (f.length() - 4)) + "-"+ j + ".xls";
				tempfile = new File(tempFilePath);
				if (!tempfile.exists()) tempfile.createNewFile();
				srcfile[j] = tempfile;
				o = new FileOutputStream(tempfile); 
				
				for ( int i = 0; i < length && (start + i) < datalist.size(); i++ ) { // 循环每个sheet
					List<String> rowdata = datalist.get(start + i);
					row = sheet.createRow(i);
					for ( int c = 0; c < rowdata.size(); c++ ) { // 循环每一行
						cell = row.createCell(c);
						cell.setCellValue(rowdata.get(c));
					}
				}
				// 写入文件
				book.write(o);
				o.flush();
				o.close();
			}
		}
		datalist.clear();
		// 合并xls文件
		File file = mergeXlsFiles(srcfile, targetFile);
		for (File ff : srcfile) if (ff != null && ff.exists()) ff.delete();
		srcfile = null;
		return file;
	}

	/**
	 * 
	 * @param files
	 *            要合并的文件集合
	 * @param targetFile
	 *            合并后的文件
	 * @return
	 * @throws Exception
	 */
	private static File mergeXlsFiles(File[] files, File targetFile)
			throws Exception {
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 在单元格中居中排放

		for ( int i = 0; i < files.length; i++ ) {
			File f = files[i]; // 取得一个文件
			FileInputStream is = new FileInputStream(f);
			HSSFWorkbook wbs = new HSSFWorkbook(is);
			//根据读出的Excel，创建Sheet
			HSSFSheet sheet = wb.createSheet("sheet" + i);
			// 一直取的是第一个Sheet，因为创建的临时文件中只有一个sheet
			HSSFSheet childSheet = wbs.getSheetAt(0);
			// 循环读取Excel的行
			for (int j = 0; j <= childSheet.getLastRowNum(); j++) {
				//根据读取的行，创建要合并Sheet的行
				HSSFRow r = sheet.createRow(j);
				HSSFRow row = childSheet.getRow(j);
				// 判断是否为空，因为可能出现空行的情况
				if (null != row) {
					// 循环读取列
					for (int k = 0; k < row.getLastCellNum(); k++) {
						// 根据读取的列，创建列
						HSSFCell c = r.createCell(k);
						HSSFCell cell = row.getCell(k);
						// 将值和样式一同赋值给单元格
						String value = "";
						if (null != cell) {
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_NUMERIC: // 数值型
								if (HSSFDateUtil.isCellDateFormatted(cell)) {
									// 如果是Date类型则 ，获取该Cell的Date值
									value = HSSFDateUtil.getJavaDate(
											cell.getNumericCellValue())
											.toString();
								} else {// 纯数字，这里要判断是否为小数的情况，因为整数在写入时会被加上小数点
									String t = cell.getNumericCellValue() + "";
									BigDecimal n = new BigDecimal(
											cell.getNumericCellValue());
									// 判断是否有小数点
									if (t.indexOf(".") < 0) {
										value = n.intValue() + "";
									} else {
										// 数字格式化对象
										NumberFormat nf = NumberFormat
												.getInstance();
										// 小数点最大两位
										nf.setMaximumFractionDigits(2);
										// 执行格式化
										value = nf.format(n.doubleValue());
									}
								}
								break;
							case HSSFCell.CELL_TYPE_STRING: // 字符串型
								value = cell.getRichStringCellValue()
										.toString();
								break;
							case HSSFCell.CELL_TYPE_FORMULA:// 公式型
								// 读公式计算值
								value = String.valueOf(cell
										.getNumericCellValue());
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN:// 布尔
								value = " " + cell.getBooleanCellValue();
								break;
							/* 此行表示该单元格值为空 */
							case HSSFCell.CELL_TYPE_BLANK: // 空值
								value = " ";
								break;
							case HSSFCell.CELL_TYPE_ERROR: // 故障
								value = " ";
								break;
							default:
								value = cell.getRichStringCellValue()
										.toString();
							}
						} else {
							value = " ";
						}
						c.setCellValue(value);
						c.setCellStyle(style);
					}
				} else {
					HSSFCell c = r.createCell(0);
					c.setCellValue(" ");
				}
			}
		}
		// 设置文件放置路径和文件名
		FileOutputStream fileOut = new FileOutputStream(targetFile);
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		
		return targetFile;
	}
}
