package com.jiuyuan.util;

public class PriceUtil {
    public static String getPriceString(Double price) {
    	String priceNew = null;
		if(price!=null && !("".equals(price))){
			String priceOld = String.format("%.2f",price);
			String[] priceArray = priceOld.split("\\.");
			String priceDouble = priceArray[1];
			if("00".equals(priceDouble)){
				priceNew = priceArray[0];
			}else{
				priceNew = priceArray[0]+"."+priceDouble;
			}
		}
		return priceNew;
    }
}