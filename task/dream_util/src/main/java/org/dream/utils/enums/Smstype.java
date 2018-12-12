package org.dream.utils.enums;

import java.util.Arrays;

public enum Smstype {
	register("SMS_10606593");//注册验证码
	
	public String value;
	
	
	
	private Smstype(String value) {
		this.value = value;
		
	}
	public static void main(String[] args) {
		System.out.println(Smstype.values());
		 for(Smstype smstype:Smstype.values()){
			 if(smstype.toString().equals("register")){
				 System.out.println(3);
				 System.out.println(smstype.value);
			 }
			 
			 System.out.println(smstype);
		    }
	}
	

}
