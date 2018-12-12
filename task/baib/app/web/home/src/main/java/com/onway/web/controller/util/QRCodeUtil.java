package com.onway.web.controller.util;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.onway.web.controller.AbstractJsonController;

/*
 * 本地生成带参数二维码工具
 * QRurl:二维码中对应的网址（或对应文本）
 * QRimgPath:生成的二维码要保存的路径
 */
public class QRCodeUtil extends AbstractJsonController
{ 
    public static String  QRImage(String QRurl,String QRimgPath) 
    { 
        
      /**
       * 二维码生成方式1
       */
//        String text = "http://www.puyueinfo.cn";  
        int width = 300;  
        int height = 300;  
        //二维码的图片格式  
        String format = "jpg";  
        Hashtable hints = new Hashtable();  
        //内容所使用编码  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(QRurl,  
                    BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            System.out.println(""+e);
        }  
        String QRImgPath=System.currentTimeMillis()+"_QR.jpg";
        //生成二维码  
        File outputFile = new File(QRimgPath+QRImgPath);  
        
        try {
            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
        } catch (IOException e) {
            System.out.println(""+e);
        } 
        
        return QRImgPath;
        
       /**
        * 生成二维码方式二
        */
//        String qrcodeContent = "http://www.puyueinfo.cn/"; 
//        ByteArrayOutputStream out = QRCode.from(qrcodeContent).to(ImageType.PNG).stream();
//        try 
//        { 
//            String imgUrl="http://nxpp.wangdaidiandian.com/distribution_image/";
//            System.out.println(""+imgUrl);
////            uploadImage(out, IMAGE_PATH);
//            FileOutputStream fout = new FileOutputStream(new File(imgUrl+"QR_Code.jpg")); 
//            fout.write(out.toByteArray());            
//            fout.flush(); 
//            fout.close(); 
//        } 
//        catch (FileNotFoundException e) 
//        { 
//            e.printStackTrace(); 
//        } 
//        catch (IOException e) 
//        { 
//            e.printStackTrace(); 
//        } 
      } 
    } 
