/**
 * 
 */
package com.jiuy.web.controller.util;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * @author lucyxu0607
 *
 */
public class QrcodeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrcodeUtils.class);

    private static Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(1);

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
    }

    public static void generate(String path, String text, int height, int width, String filename, String format) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            File outputFile = new File(path + File.separator + filename + "." + format);
            OutputStream os = new FileOutputStream(outputFile);
            MatrixToImageWriter.writeToStream(bitMatrix, format, os);
            IOUtils.closeQuietly(os);
        } catch (WriterException | IOException e) {
            LOGGER.error("qrcode generate fail", e);
        }
    }
    
    public static void main(String... aaa) {
    	System.out.println("开始");
        QrcodeUtils.generate("E:\\qrcode\\frontpage", "http://wxaproxylocal.yujiejie.com/myCustomer/myCustomer_apply?proxyUserId=45", 1181, 1181, "二维码", "png");
        System.out.println("结束");
//        QrcodeUtils qrcodeUtils = new QrcodeUtils();  
//        qrcodeUtils.getFile(); 
    }
    
    
    public static HttpServletResponse getFile(HttpServletRequest request, HttpServletResponse response, String url, String fileName, int width, int high) {        
    	try {            
    		 BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, high, hints);
    		 BufferedImage image =  MatrixToImageWriter.toBufferedImage(bitMatrix);
    		 
    		 ByteArrayOutputStream out = new ByteArrayOutputStream();  
             ImageIO.write(image, "png", out);  
             byte[] b = out.toByteArray();  

             InputStream fis = new ByteArrayInputStream(b); 
             byte[] buffer = new byte[fis.available()];           
             fis.read(buffer);           
             fis.close();            
             response.reset();            
             // 设置response的Header            
             fileName += ".png";
             response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));           
             response.addHeader("Content-Length", "" + b.length);           
             OutputStream toClient = new BufferedOutputStream(response.getOutputStream());           
             response.setContentType("application/octet-stream");            
             toClient.write(buffer);            
             toClient.flush();            
             toClient.close();        
    	} catch (IOException ex) {            
    		ex.printStackTrace();        
		}  catch (WriterException e) {
			e.printStackTrace();
		}      
    	return response;    
    }
    
    
}
