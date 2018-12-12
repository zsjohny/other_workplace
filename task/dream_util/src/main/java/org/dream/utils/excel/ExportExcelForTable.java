/*
 * Copyright (c) 2015 www.cainiu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * www.cainiu.com. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package org.dream.utils.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * 描述:
 *
 * @author  C.C
 * @created 2015年6月11日 下午9:13:38
 * @since   v1.0.0
 */
public class ExportExcelForTable<T> {

	private Logger logger = LoggerFactory.getLogger(ExportExcelForTable.class);

	/**
	 *	
	 * 描述:导出Excel文档 （含图片）
	 *
	 * @author  C.C
	 * @created 2014-5-14 下午3:05:29
	 * @since   v1.0.0 
	 * @param title sheet的标题名
	 * @param headers 表格属性列名数组
	 * @param fieldNames 表头列的名称
	 * @param dataset  需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd"
	 * @return  void
	 */
	public void exportExcel(String title, String[] headers, String[] fieldNames, Collection<T> dataset, OutputStream out, String pattern) {

		//声明一个工作薄。注：HSSFWorkbook为xls文件，XSSFWorkbook为xlsx文件。
		XSSFWorkbook workbook = new XSSFWorkbook();
		//生成一个表格
		XSSFSheet sheet = workbook.createSheet(title);
		//设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		//生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		//设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//生成一个字体
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		//把字体应用到当前的样式
		style.setFont(font);
		//生成并设置另一个样式
		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		//把字体应用到当前的样式
		style2.setFont(font2);

		//生成字体
		XSSFFont font3 = workbook.createFont();
		//font3.setColor(HSSFColor.BLUE.index);

		//声明一个画图的顶级管理器
		//XSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		XSSFDrawing patriarch = sheet.createDrawingPatriarch();
		//定义注释的大小和位置,详见文档
		//HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		XSSFComment comment = patriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		//设置注释内容
		comment.setString(new XSSFRichTextString("可以在POI中添加注释！"));
		//设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("leno");

		//产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		//遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			//利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			for (int i = 0; i < fieldNames.length; i++) {
				XSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				String fieldName = fieldNames[i];
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class<? extends Object> tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					//判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "是";
						if (!bValue) {
							textValue = "否";
						}
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						//有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						//设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						byte[] bsValue = (byte[]) value;
						XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, XSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						//其它数据类型都当作字符串简单处理
						textValue = null != value ? value.toString() : null;
					}
					//如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^//d+(//.//d+)?{1}");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							//是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							XSSFRichTextString richString = new XSSFRichTextString(textValue);
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}

		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			logger.error("IO error when write workbook", e);
		}

	}

}
