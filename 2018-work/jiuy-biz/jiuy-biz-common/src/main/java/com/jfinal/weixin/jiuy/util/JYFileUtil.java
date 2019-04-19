package com.jfinal.weixin.jiuy.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JYFileUtil {


	private static final Logger logger = LoggerFactory.getLogger(JYFileUtil.class);
	/** 
     * 从网络Url中下载文件 
     * @param urlStr 
     * @param fileName 
     * @param savePath 
     * @throws IOException 
     */  
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{  
    	logger.info("从网络Url中下载文件，urlStr:"+urlStr+",fileName:"+fileName+",savePath:"+savePath);
        URL url = new URL(urlStr);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        conn.setConnectTimeout(3*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //获取自己数组  
        byte[] getData = readInputStream(inputStream);      
  
        //文件保存位置  
        File saveDir = new File(savePath);  
        if(!saveDir.exists()){  
            saveDir.mkdir();  
        }  
        File file = new File(saveDir+File.separator+fileName);      
        FileOutputStream fos = new FileOutputStream(file);       
        fos.write(getData);   
        if(fos!=null){  
            fos.close();    
        }  
        if(inputStream!=null){  
            inputStream.close();  
        }  
  
    	logger.info("从网络Url中下载文件成功，urlStr:"+urlStr+",fileName:"+fileName+",savePath:"+savePath);
        
        System.out.println("info:"+url+" download success");   
  
    } 
    
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }    
  
    public static void main(String[] args) {  
    	File directory = new File(""); 
    	try {
			directory.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //得到的是C:/ 
        try{  
            downLoadFromUrl("http://wx.qlogo.cn/mmopen/NB8ZmN2uvfxwQqaqOVIVYQo3LcIqYbfnEAhz1siaIGbF2HVTWavxvPkTbHwiae7NW7gCYJnNTCVKgS1VWeNj9u5uldB3c9eRCZ/0",  
                    "1111111111111111111.jpg","e:/");  
        }catch (Exception e) {  
            // TODO: handle exception  
        }  
    }  
}
