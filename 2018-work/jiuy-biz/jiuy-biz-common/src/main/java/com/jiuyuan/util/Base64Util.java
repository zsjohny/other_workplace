/**
 * 
 */
package com.jiuyuan.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * @author LWS
 *
 */
public class Base64Util {
	
	private static final Logger logger = Logger.getLogger(Base64Util.class);
	// 加密  
    public static String encode(String str) {  
        byte[] b = null;  
        String s = null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
        	logger.error(e); 
        }  
        if (b != null) {  
            s = new String(new Base64().encode(b));  
        }  
        return s;  
    }  
  
    // 解密  
    public static String decode(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
            try {  
                b = new Base64().decode(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
            	logger.error(e); 
            }  
        }  
        return result;  
    }  
    
    	/**
	  	* 将网络图片进行Base64位编码
	  	* 
	  	* @param imgUrl
	  	*			图片的url路径，如http://.....xx.jpg
	  	* @return
  	  	*/
    	public static String encodeImgageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    		ByteArrayOutputStream outputStream = null;
		    try {
		      BufferedImage bufferedImage = ImageIO.read(imageUrl);
		      outputStream = new ByteArrayOutputStream();
		      ImageIO.write(bufferedImage, "jpg", outputStream);
		    } catch (MalformedURLException e1) {
		      e1.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    // 对字节数组Base64编码
		    return Base64.encodeBase64String(outputStream.toByteArray());
    	}
}
