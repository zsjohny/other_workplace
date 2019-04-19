package com.e_commerce.miscroservice.crm.utils;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel 文件处理
 */
public class ExcelUtil {
    private static Log logger  = Log.getInstance(ExcelUtil.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    /**
     * contains_in  对应 excel 上的字段
     * contains_in  与实体类相对应
     */
    private static String CONTAINS_IN="businessName,artificialPersonName,businessLicence,businessUrl,position,customerSource," +
            "customerGrade,name,phone,province,city,district,profession,mainBusiness,belonger,lastestRecord";
    /**
     * must_contains_in 为必填项
     * must_contains_in 对应实体类必填项
     */
    private static String MUST_CONTAINS_IN="customerSource,customerGrade,name,phone,province,city,district,profession,mainBusiness,belonger,lastestRecord";

//    public static Map<String, Object> readExcelPOI(Long userId) throws Exception
    public static Map<String, Object> readExcelPOI(MultipartFile file,Long userId,Integer addStatus) throws Exception
    {
        FileInputStream fis = (FileInputStream) file.getInputStream();
        Map<String,Object> jsonObject = new HashMap<>();

        String msg = null;
        List<String> msgList = new ArrayList<>();
        //创建输入流
//        FileInputStream fis = new FileInputStream(new File("g:\\data\\test.xlsx"));
        //通过构造函数传参
//        Workbook workbook = getWorkBook(new File("g:\\data\\test.xlsx"));
        Workbook workbook = getWorkBook(file);
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

        if (row1==null){
            msg="内容为空";
            msgList.add(msg);
            jsonObject.put("msg",msgList);
            return jsonObject;
        }


//        判断 所需字段是否存在
        int cellNum = -1;
        short rowNum = row2.getLastCellNum();
        for (int i=0;i<rowNum;i++){
            Cell cell1 = row2.getCell(i);
            cell1.setCellType(CellType.STRING);
           if (cell1!=null){
               String cellValue = cell1.getStringCellValue();

               if (!CONTAINS_IN.contains(cellValue)){
                   cellNum=i;
                   break;
               }
           }
        }
        if (cellNum!=-1){
            msg="不存在需要的字段";
            msgList.add(msg);
            jsonObject.put("msg",msgList);
            return jsonObject;
        }


        //最后 一列
//        short lastCellNum = row.getLastCellNum();

        //最后一行
        Integer sheetLastRowNum = sheet.getLastRowNum();
        StringBuffer stringBuffer = new StringBuffer();
        List<CustomerPoolAddRequest> list = new ArrayList<>();
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
                CustomerPoolAddRequest customerPoolAddRequest = new CustomerPoolAddRequest();
                for (int j=0;j<rowNum;j++){
                    cell1 = row.getCell(j);
//                    String cellValue = row1.getCell(j).getStringCellValue();
                    String cellValue = row2.getCell(j).getStringCellValue();
                    if (cell1==null&&MUST_CONTAINS_IN.contains(cellValue)){
                        msg="第"+i+"行第"+(j+1)+"列值为空";
                        msgList.add(msg);
                        break;
                    }
                    if (cell1!=null){
                        cell1.setCellType(CellType.STRING);
                        if (CONTAINS_IN.contains(cellValue)){
                            String value = cell1.getStringCellValue();
                            if ("phone".equals(cellValue)){
                                if (!StringUtils.isNumber(value)){
                                    msg="第"+i+"行第"+(j+1)+"电话="+value+"为非数字";
                                    msgList.add(msg);
                                }
                            }
                            Class cal = CustomerPoolAddRequest.class;
                            Field field = cal.getDeclaredField(cellValue);
                            field.setAccessible(true);
                            field.set(customerPoolAddRequest,value);
                        }

                    }
                }
                customerPoolAddRequest.setAddStatus(addStatus);
                Object o = AnnotationUtils.validate(customerPoolAddRequest).get("result");
                if (Boolean.TRUE.equals(o)){
                    customerPoolAddRequest.setUserId(userId);
                    if (addStatus==0){
                        customerPoolAddRequest.setType("未领取");
                    }else
                    if (addStatus==1){
                        customerPoolAddRequest.setType("已领取");
                    }
                    list.add(customerPoolAddRequest);
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



    public static Workbook getWorkBook(MultipartFile file) {
//    public static Workbook getWorkBook(File file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
//        String fileName = file.getName();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
//            FileInputStream is = new FileInputStream(file);
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

    public static void main(String[] args) throws Exception {
        //创建输入流
//        Map jsonObject = readExcelPOI(new File("g:\\data\\test.xlsx"));
//        System.out.println(jsonObject);
//        CustomerPoolAddRequest customerPoolAddRequest = new CustomerPoolAddRequest();
//        customerPoolAddRequest.setPhone("aa");
//        Field fields = customerPoolAddRequest.getClass().getDeclaredField("phone");
//        fields.setAccessible(true);
//        fields.set(customerPoolAddRequest,"va");
//        System.out.println(fields.get(customerPoolAddRequest));
    }

}
