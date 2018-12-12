package com.finace.miscroservice.commons.utils.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class BKCMD5 {
	/**
	 * 创建MD5字段
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String md5(String value) {
		
		String temp = null;
		try{
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(value.getBytes());
			byte[] digest = alg.digest();
			temp = byte2hex(digest);
		}
		catch(Exception e){
			//
		}
		return temp.toLowerCase();
	}
	
	/**
	 * 创建MD5字段
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String md5GB2312(String value) {
		
		String temp = null;
		try{
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(value.getBytes("gb2312"));
			byte[] digest = alg.digest();
			temp = byte2hex(digest);
		}
		catch(Exception e){
			//
		}
		return temp;
	}
	
	
	/**
	 * 创建MD5字段
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String md5GBK(String value) {
		
		String temp = null;
		try{
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(value.getBytes("gbk"));
			byte[] digest = alg.digest();
			temp = byte2hex(digest);
		}
		catch(Exception e){
			//
		}
		return temp;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2hex(byte[] bytes) {
		String hs = "";
		String stmp = "";
		for (int i = 0; i < bytes.length; i++) {
			stmp = (Integer.toHexString(bytes[i] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
	
	private String utf8Togb2312(String str){
	      StringBuffer sb = new StringBuffer();
	      for(int i=0; i<str.length(); i++) {
	          char c = str.charAt(i);
	          switch (c) {
	             case '+':
	                 sb.append(' ');
	             break;
	             case '%':
	                 try {
	                      sb.append((char)Integer.parseInt(
	                      str.substring(i+1,i+3),16));
	                 }
	                 catch (NumberFormatException e) {
	                     throw new IllegalArgumentException();
	                }
	                i += 2;
	                break;
	             default:
	                sb.append(c);
	                break;
	           }
	      }
	      // Undo conversion to external encoding
	      String result = sb.toString();
	      String res=null;
	      try{
	          byte[] inputBytes = result.getBytes("8859_1");
	          res= new String(inputBytes,"UTF-8");
	      }
	      catch(Exception e){}
	      return res;
	}

	public static void main(String args[]) throws UnsupportedEncodingException {
		//e0ea2386593728f2a1bdaaf9a6304b7f
//		String ss = "382110140862652OrderID=654a1b1c2d2e3_LotID=41_LotIssue=91206_LotMoney=6_LotCode=SXP|1=ÉÏµ¥,2=ÏÂµ¥,3=ÉÏË«|µ¥¹Ø_LotMulti=1_Attach=other_OneMoney=2a8b8c8d8e8f8g8h8";
//		String str = new String(ss.getBytes("UTF-8"));
//		String str1 = new String(str.getBytes("GB2312"));
//		String ss = "123456";
//		MD5 md5 = new MD5();
//		String value = md5.md5(ss);
//		System.out.println("value : " + value.toUpperCase());
	}
}
