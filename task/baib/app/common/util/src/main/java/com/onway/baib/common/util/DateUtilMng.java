package com.onway.baib.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.onway.common.lang.DateUtils;

/**
 * 
 * @author  weina.chen
 * @version $Id:DateUtilMng.java, 2016年9月29日 下午2:05:29
 */
public class DateUtilMng extends DateUtils{

	/**
	 * 时间戳转date
	 * @param sDate
	 * @param dateFormatString
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateChuo(String sDate, String dateFormatString) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		String dateString = dateFormat.format(Long.parseLong(sDate));
		if (sDate == null) {
            throw new ParseException("length too little", 0);
        }

        return dateFormat.parse(dateString);
	}
}
