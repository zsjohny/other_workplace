package com.e_commerce.miscroservice.crm.utils;

import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于导入Excel 优化版
 * @author hyf
 * @version V1.0
 * @date 2018/9/21 14:41
 * @Copyright 玖远网络
 */
public class ExcelDoUtils {
    private static Log logger  = Log.getInstance(ExcelUtil.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";

    /**
     * 导入
     * @param obj 实体类 与 excel字段相对应
     * @return
     * @throws Exception
     */
    public static Map<String, Object> readExcelPOIT(Object obj) throws Exception
    {
//        FileInputStream fis = (FileInputStream) file.getInputStream();
        Map<String,Object> jsonObject = new HashMap<>();

        String msg = null;
        List<String> msgList = new ArrayList<>();
        //创建输入流
        FileInputStream fis = new FileInputStream(new File("g:\\data\\test.xlsx"));
        //通过构造函数传参
        Workbook workbook = getWorkBookT(new File("g:\\data\\test.xlsx"));
//        Workbook workbook = getWorkBook(file);
        int sheets = workbook.getNumberOfSheets();
        if (sheets<1){
            msg="内容为空";
            msgList.add(msg);
            jsonObject.put("msg",msgList);
            return jsonObject;
        }
        //获取工作标名称
//        for (int i=0;i<sheets;i++){
//            System.out.println(workbook.getSheetName(i));
//
//        }
        //获取工作表 第一张工作表
        Sheet sheet = workbook.getSheetAt(0);
        //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
        //获取第一行
        int firstRowNum = sheet.getFirstRowNum()+2;

        //获取第一行的第一个值进行判断是否为我需要的值
        Row row1 = sheet.getRow(firstRowNum);
        Row row2 = sheet.getRow(firstRowNum-1);

        Class cl = obj.getClass();
        Field[] fs  = cl.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field f : fs ){
            if (stringBuilder.length()==0){
                stringBuilder.append(f.getName());
            }else {
                stringBuilder.append(",");
                stringBuilder.append(f.getName());
            }
        }
        String containsIn = stringBuilder.toString();
        if (row1==null){
            msg="内容为空";
            msgList.add(msg);
            jsonObject.put("msg",msgList);
            return jsonObject;
        }


//        判断 所需字段是否存在
//        int cellNum = -1;
        short rowNum = row2.getLastCellNum();
//        for (int i=0;i<rowNum;i++){
//            Cell cell1 = row2.getCell(i);
//            cell1.setCellType(CellType.STRING);
//            if (cell1!=null){
//                String cellValue = cell1.getStringCellValue();
//
//                if (StringUtils.isNotEmpty(cellValue)&&containsIn.contains(cellValue)){
//                    cellNum=i;
//                    break;
//                }
//            }
//        }
//        if (cellNum==-1){
//            msg="不存在需要的字段";
//            msgList.add(msg);
//            jsonObject.put("msg",msgList);
//            return jsonObject;
//        }


        //最后 一列
//        short lastCellNum = row.getLastCellNum();

        //最后一行
        Integer sheetLastRowNum = sheet.getLastRowNum();
        List<Object> list = new ArrayList<>();
        if (sheetLastRowNum<1){
            msg="数据行不存在";
            msgList.add(msg);
        }
        for (int i=firstRowNum+1;i<sheetLastRowNum+1;i++){
            //获取第i行
            Row row = sheet.getRow(i);
            //获取该列
            Cell cell1 = null;
            if (row!=null){
                Object object = obj.getClass().newInstance();
                BeanUtils.copyProperties(obj,object);
                for (int j=0;j<rowNum;j++){
                    cell1 = row.getCell(j);
//                    String cellValue = row1.getCell(j).getStringCellValue();
                    String cellValue = row2.getCell(j).getStringCellValue();
                    if (StringUtils.isNotEmpty(cellValue)&&cell1==null){
                        msg="第"+(i+1)+"行第"+(j+1)+"列值为空";
                        msgList.add(msg);
                        break;
                    }
                    if (cell1!=null){
                        cell1.setCellType(CellType.STRING);
                        if (StringUtils.isNotEmpty(cellValue)&&containsIn.contains(cellValue)){
                            String value = cell1.getStringCellValue();
                            Class cal = CustomerPoolAddRequest.class;
                            Field field = cal.getDeclaredField(cellValue);
                            field.setAccessible(true);
                            field.set(object,value);
                        }

                    }
                }
                Object o = AnnotationUtils.validate(object).get("result");
                if (Boolean.TRUE.equals(o)){

                    list.add(object);
                }
            }
        }
        logger.warn("msgList={}",msgList);
        jsonObject.put("list",list);
        jsonObject.put("msg",msgList);
        workbook.close();
        fis.close();
        return jsonObject;
    }



    //    public static Workbook getWorkBookT(MultipartFile file) {
    public static Workbook getWorkBookT(File file) {
        //获得文件名
//        String fileName = file.getOriginalFilename();
        String fileName = file.getName();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
//            InputStream is = file.getInputStream();
            FileInputStream is = new FileInputStream(file);
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return workbook;
    }

}
