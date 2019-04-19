package com.e_commerce.miscroservice.store.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NumberUtil {
	//生成订单号,时间戳+随机n位数
	public static String genOrderNo(Integer num){
		return genOrderNo(num,false);
	}
	
	//生成订单号，时间戳+随机n位数,是否包含字母
	public static String genOrderNo(Integer num, boolean hasLetter){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuffer sf = new StringBuffer();
		sf.append(sdf.format(new Date()));
	    String randomNum = genRandomNum(num,hasLetter);
	    sf.append(randomNum);
		return sf.toString();
	}
	

    //生成随机数
	public static String genRandomNum(Integer num, boolean hasLetter) {
		StringBuffer sf = new StringBuffer();
		String nums = "0123456789";
		if(hasLetter){
			nums+="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}
		for(int i = 0;i<num;i++){
			int index = (int)(Math.random()*nums.length());
			sf.append(nums.charAt(index));
		}
		return sf.toString();
	}
	public static void main(String[] args) {
		System.out.println(genOrderNo(3));
	}
}
