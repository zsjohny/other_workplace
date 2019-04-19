package com.jiuyuan.util;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public class ImageUtil {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	/**
	  * 根据图片网络地址获取图片的byte[]类型数据
	  *
	  * @param urlPath
	  *            图片网络地址
	  * @return 图片数据
	  */
	public static byte[] getImageFromURL(String urlPath) { 
		byte[] data = null; 
		InputStream is = null; 
        HttpURLConnection conn = null; 
        try { 
            URL url = new URL(urlPath); 
            conn = (HttpURLConnection) url.openConnection(); 
            conn.setDoInput(true); 
            // conn.setDoOutput(true); 
            conn.setRequestMethod("GET"); 
            conn.setConnectTimeout(6000); 
            is = conn.getInputStream(); 
            if (conn.getResponseCode() == 200) { 
                data = readInputStream(is); 
            } else{ 
                data=null; 
            } 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
            try { 
                if(is != null){ 
                    is.close(); 
                }                
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
            conn.disconnect();           
        } 
        return data; 
    } 
	
	public static byte[] readInputStream(InputStream is) { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[1024]; 
        int length = -1; 
        try { 
            while ((length = is.read(buffer)) != -1) { 
                baos.write(buffer, 0, length); 
            } 
            baos.flush(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        byte[] data = baos.toByteArray(); 
        try { 
            is.close(); 
            baos.close(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return data; 
    } 

       /**
	    * Java将商品详情图和二维码拼接成长图
	 	* @param filepath
	    *  
	    * @return
	 	* @throws Exception
	    */  
	   public static MultipartFile merge(File[] src, String filepath) throws Exception {  
	       //获取需要拼接的图片长度
	       int len = src.length;  
	       //判断拼接图片的张数是否大于0
	       if (len < 1) {  
	           return null;  
	       }  
	       BufferedImage[] images = new BufferedImage[len];  
	       int[][] ImageArrays = new int[len][];  
	       String firstPath = src[src.length-2].getPath();
	       String type = firstPath.substring(firstPath.lastIndexOf(".")+1);
	       logger.info("Java将商品详情图和二维码拼接成长图:type:"+type);
	       int defaultWidth = 0;
	       for (int i = 0; i < len; i++) {  
	           try {  
	               images[i] = ImageIO.read(src[i]);  
	           } catch (Exception e) {  
	               e.printStackTrace();  
	               return null;  
	           }  
	           if(i==0){
	        	   defaultWidth = images[0].getWidth(); //第一张图片的宽度
	           }
	           int width = images[i].getWidth(); 
		       double widthScale = (double)defaultWidth/width;
		       int height = images[i].getHeight(); 
		       int scaleHeight = (int)(height * widthScale);
		       logger.info("缩放比例：width:"+width+";defaultWidth:"+defaultWidth+";scaleHeight："+scaleHeight);
	           if(width!=defaultWidth){//如果宽度和第一张图片不同,以第一张图片为准进行缩放
	        	   String path = src[i].getPath();
	        	   logger.info("Java将商品详情图和二维码拼接成长图第"+i+"张图片宽度和第一张图片不同,路径:"+path);
	        	   String firstPathName = path.substring(0,path.lastIndexOf("."));
	        	   String newFilePath = firstPathName + "_Change" + path.substring(path.lastIndexOf("."));
	        	   //缩放图片
	        	   zoomImage(path, newFilePath, defaultWidth, scaleHeight);
	        	   File newFile = new File(newFilePath);
	        	   src[i] = newFile;
	        	   images[i] = ImageIO.read(newFile);
	        	   width = defaultWidth;
	        	   height = scaleHeight;
	           }
	         
	           
	           
	        // 从图片中读取RGB 像素
	           ImageArrays[i] = new int[width * height];
	           ImageArrays[i] = images[i].getRGB(0, 0, width, height,  ImageArrays[i], 0, width);  
	       }  
	 
	       int dst_height = 0;  
	       int dst_width = images[0].getWidth();  
	     //合成图片像素
	       for (int i = 0; i < images.length; i++) {  
	           dst_width = dst_width > images[i].getWidth() ? dst_width     : images[i].getWidth();  
	           dst_height += images[i].getHeight();  
	       }  
	       //合成后的图片
	       logger.info("宽度:"+dst_width);  
	       logger.info("高度:"+dst_height);  
	       if (dst_height < 1) {  
	    	   logger.info("dst_height < 1");  
	           return null;  
	       }  
	       // 生成新图片   
	       // dst_width = images[0].getWidth();   
           BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,  
                   BufferedImage.TYPE_INT_RGB);  
           int height_i = 0;  
           for (int i = 0; i < images.length; i++) {  
               ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(),  
                       ImageArrays[i], 0, dst_width);  
               height_i += images[i].getHeight();  
           }  
           String wxaqrcodePath = filepath+"/"+UUID.randomUUID().toString()+"."+type;
           File outFile = new File(wxaqrcodePath);  
           ImageIO.write(ImageNew, type, outFile);//写图片 ，输出到硬盘
           MultipartFile file = new JiuyMultipartFile(outFile);
           return file;
	   }  
	   
	   /** 
	     * MultipartFile 转换成File 
	     *  
	     * @param multfile 原文件类型 
	     * @return File 
	     * @throws IOException 
	     */  
	    private File multipartToFile(MultipartFile multfile) throws IOException {  
	        CommonsMultipartFile cf = (CommonsMultipartFile)multfile;   
	        //这个myfile是MultipartFile的  
	        DiskFileItem fi = (DiskFileItem) cf.getFileItem();  
	        File file = fi.getStoreLocation();  
	        //手动创建临时文件  
//	        if(file.length() < CommonConstants.MIN_FILE_SIZE){  
	            File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +   
	                    file.getName());  
	            multfile.transferTo(tmpFile);  
	            return tmpFile;  
//	        }  
//	        return file;  
	    }  
	    
	    //MultipartFile转换为File
	    private static File[] a(MultipartFile[] multfiles){
	    	File[] files = new File[multfiles.length];
	    	for (int i=0;i<multfiles.length;i++) {
	    		CommonsMultipartFile cf = (CommonsMultipartFile)(multfiles[i]);  
		    	DiskFileItem fi = (DiskFileItem) cf.getFileItem(); 
		    	files[i] = fi.getStoreLocation();
			}
	    	return files;
	    }
	    
	    //File转换为MultipartFile
	    private static MultipartFile getMulFileByPath(File file) {  
	        FileItem fileItem = createFileItem(file);  
	        MultipartFile mfile = new CommonsMultipartFile(fileItem);  
	        return mfile;  
	    }  
	  
	    private static FileItem createFileItem(File newfile)  
	    {  
	        FileItemFactory factory = new DiskFileItemFactory(16, null);  
	        String textFieldName = "textField";  
//	        int num = filePath.lastIndexOf(".");  
//	        String extFile = filePath.substring(num);  
	        FileItem item = factory.createItem(textFieldName, "text/plain", true,  
	            "MyFileName_a");  
//	        File newfile = new File(filePath);  
	        int bytesRead = 0;  
	        byte[] buffer = new byte[8192];  
	        try  
	        {  
	            FileInputStream fis = new FileInputStream(newfile);  
	            OutputStream os = item.getOutputStream();  
	            while ((bytesRead = fis.read(buffer, 0, 8192))  
	                != -1)  
	            {  
	                os.write(buffer, 0, bytesRead);  
	            }  
	            os.close();  
	            fis.close();  
	        }  
	        catch (IOException e)  
	        {  
	            e.printStackTrace();  
	        }  
	        return item;  
	    }
	    
//	    public static void zoomImage(String src,String dest,int w,int h) throws Exception {
//	        
//	        double wr=0,hr=0;
//	        File srcFile = new File(src);
//	        ByteArrayOutputStream out = new ByteArrayOutputStream();
//	        File destFile = new File(dest);
//
//	        BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
//	        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板
//	        
//	        wr=w*1.0/bufImg.getWidth();     //获取缩放比例
//	        hr=h*1.0 / bufImg.getHeight();
//
//	        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
//	        Itemp = ato.filter(bufImg, null);
//	        try {
//	            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), out); //写入缩减后的图片
//	            byte[] b = out.toByteArray();  
//
//	            InputStream fis = new ByteArrayInputStream(b);
//	        } catch (Exception ex) {
//	            ex.printStackTrace();
//	        }
//	    }
	    public static void zoomImage(String src,String dest,int w,int h) throws Exception {
	        
	        double wr=0,hr=0;
	        File srcFile = new File(src);
	        File destFile = new File(dest);

	        BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
	        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板
	        
	        wr=w*1.0/bufImg.getWidth();     //获取缩放比例
	        hr=h*1.0 / bufImg.getHeight();

	        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
	        Itemp = ato.filter(bufImg, null);
	        try {
	            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile); //写入缩减后的图片
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
}
