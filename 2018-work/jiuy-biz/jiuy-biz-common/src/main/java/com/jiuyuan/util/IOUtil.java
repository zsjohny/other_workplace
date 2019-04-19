/**
 * 
 */
package com.jiuyuan.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

/**
 * 网络以及file 以及流相关的都放在这里
 * @author zhuliming
 *
 */
public class IOUtil {

	private static final Logger logger = Logger.getLogger(IOUtil.class);
	
	/**************
	 * 关闭文件对象
	 * @param reader
	 */
	public static final void closeReaderWithoutException(final BufferedReader reader){
		if(reader != null){
			try {
				reader.close();
			} catch (IOException ex) {
				logger.error("Fail to close reader after reading file: ex="+ex.getMessage(), ex);	
			}
		}
	}
	
	public static final void closeStreamWithoutException(final InputStream in, final OutputStream out){
		if(in != null){
			try {
				in.close();
			} catch (IOException ex) {
				logger.error("Fail to close InputStream: ex="+ex.getMessage(), ex);	
			}
		}
		if(out != null){
			try {
				out.close();
			} catch (IOException ex) {
				logger.error("Fail to close OutputStream: ex="+ex.getMessage(), ex);	
			}
		}
	}	
	
	/************
	 * 获取换行符
	 * @return
	 */
	public static final String lineSeparator(){
		return System.getProperty("line.separator", "\n"); 
	}
		
	/**
	 * @Title: isValidFileName
	 * @Description: 判断是否是合法的文件名
	 * @param fileName
	 * @return
	 * @throws
	 */
	public static boolean isValidFileName(String fileName){
		if (fileName == null || fileName.length() > 255 || !fileName.contains(".")){
	    	return false;
	    }else{
	    	return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
	    }
	}
	
	/**
	 * @Title: isXlsFileName
	 * @Description: 判断是否合法的EXCEL文件
	 * @param fileName
	 * @return
	 * @throws
	 */
	public static boolean isXlsFileName(String fileName){
		return fileName.matches("^.+\\.(?i)((xls)|(xlsx))$");
	}
	
	/**
	 * @Title: isXlsxFile
	 * @Description: 判断是否为07版EXCEL文件
	 * @param fileName
	 * @return
	 * @throws
	 */
	public static boolean isXlsxFile(String fileName){
		return fileName.matches("^.+\\.(?i)(xlsx)$");
	}
	
	/**
	 * 将InputStream转换成ByteArrayOutputStream
	 * @return
	 */
	public static ByteArrayOutputStream convertInputStreamToByteArrayOutputStream(InputStream inputStream){
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];  
			int len;  
			while ((len = inputStream.read(buffer, 0, 1024)) > -1 ) {  
				byteArrayOutputStream.write(buffer, 0, len);  
			}
			byteArrayOutputStream.flush();
			return byteArrayOutputStream;
		} catch (IOException e) {
			logger.error("Failed to convert InputStream to ByteArrayOutputStream with IOException : " + e, e);
			return null;
		} 
	}
}