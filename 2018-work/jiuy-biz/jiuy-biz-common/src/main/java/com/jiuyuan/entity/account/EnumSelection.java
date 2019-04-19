package com.jiuyuan.entity.account;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jiuyuan.constant.account.UserCoinOperation;

public class EnumSelection implements Serializable {

    private static final long serialVersionUID = -6718137924763381737L;


    private String value;

    private String option;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}



    
}
