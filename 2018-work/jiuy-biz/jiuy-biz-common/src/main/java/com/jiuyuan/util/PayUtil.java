package com.jiuyuan.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.StaticApplicationContext;

public class PayUtil {
	
	public static void main(String[] arg){
		System.out.println("formatPrice"+formatPrice(1));
	}
	
    public static String formatPrice(int priceInCents) {
        if (priceInCents == 0) {
            return "0";
        }
        String sign = priceInCents > 0 ? "" : "-";
        int abs = Math.abs(priceInCents);
        return sign + (abs / 100) + "." + StringUtils.leftPad(String.valueOf(abs % 100), 2, '0');
    }
}
