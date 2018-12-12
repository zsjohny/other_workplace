package com.finace.miscroservice.commons.utils.tools;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;



public class Utils {

	/**
	 * 
	 * @param p 本金
	 * @param r 月利率
	 * @return  每月还款金额
	 */

	public static double Mrpi(double p,double r,int mn){
		double mr=r/12;
		
		double aprPow=Math.pow(1+mr,mn);
		
		double monPay=p*mr*aprPow/(aprPow-1);
		
		return monPay;
		
	}
	
	/**
	 * 
	 * 三个变量x,y,z  
     *      x代表提现金额 y代表现在净资产减15天内的充值总值z代表提现手续费
     *      1.  0≤x ≤1500 或者 y<15000
     * 无论y为何值    z=0.002x
     *       2.  y≥x
     *      1500<x ≤30000     z=3
     *       30000<x ≤50000    z=5
     *       3. y <x
     *       1500<y ≤30000     z=3+(x-y)0.002
     *       30000<y ≤50000    z=5+(x-y)0.002
	 * 
	 * 
	 * @param x x代表提现金额 
	 * @param y y代表现在净资产减15天内的充值总值
	 * @param r r代表提现手续费率
	 * @param maxCash
	 * @return 提现手续费
	 */
	public static double GetCashFee(double x,double y,double r,double maxCash){
		if(x<=1500||y<=1500){
			return r*x;
		}else if(y>=x){
			if(x>1500&&x<=30000){
				return 3.0;
			}else{
				return 5;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	/**
	 * 莲花财富提现规则
	 */
	public static double getCashFeeForlhd(double x,double y,double r,double maxCash){
		 if(y>=x){
			if(x>0&&x<=30000){
				return 3.0;
			}else{
				return 5;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	/**
	 * 中融资本的提现费用计算公式
	 * @param x
	 * @param y
	 * @param r
	 * @param maxCash
	 * @return
	 */
	public static double getCashFeeForZRZB(double x,double y,double r,double maxCash){
		if(x<=1500||y<=1500){
			return r*x;
		}else if(y>=x){
			if(x>1500&&x<=30000){
				return 3.0;
			}else{
				return 5;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	/**
	 * 及时雨提现规则
	 */
	public static double getCashFeeForJJY(double x,double y,double r,double maxCash){
		/*if(x<y){
			if (x>=0) {
				return (y-x)*r;
			}else {
				return y*r;
			}
		}else{
			return 0;
		}*/
		if(y>=x){
			if(x>0&&x<=30000){
				return 3.0;
			}else{
				return 5;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	/**
	 * 山水聚宝提现规则
	 */
	public static double getCashFeeForSSJB(double x,double y,double r,double maxCash){
		 if(y>=x){
			return 0;
		}else{
			if(y>0){
				return (x-y)*r;
			}else{
				return x*r;
			}
		}
	}
	/**
	 * 
	 * @param x
	 * @param y
//	 * @param r
//	 * @param maxCash
	 * @return
	 */
	public static double getCashFeeForHqd(double money, double x,double y,int type){
		if(type==1){
			
			return money*x;
			
		}else{
			
			return money*(x+y);
		}
	}
	/**
	 * 中融资本新提现规则  
	 * @param x
	 * @param r
	 * @param money
	 * @return
	 */
	public static double getCashFeeForZrzbZero(double x,double r,double money){
		if(x<money){
			if (x>=0) {
				return (money-x)*r;
			}else {
				return money*r;
			}
		}else{
			return 0;
		}
	}
	
	public static double GetLargeCashFee(double x,double y,double r,double maxCash){
		if(y>=500000){
			return x/10000;
		}else if(y<20000){
			if(y<0){
				y=0;
			}
			return y/10000+(x-y)*r;
		}else{
			if(y>=x){
				return x/10000;
			}else{
				return y/10000+(x-y)*r;
			}
		}
	}
	
	/**
	 * 徽贷提现规则  
	 * @param x
	 * @param r
	 * @param money
	 * @return
	 */
	public static double getCashFeeForHuidai(double money,double x,double r){
		if(x<money){
			if (x>=0) {
				return (money-x)*r;
			}else {
				return money*r;
			}
		}else{
			return 0;
		}
	}
	
	
	/**
	 * 提现规则
	 * @param x   待计算的提现金额
//	 * @param y   免费额度
	 * @param r   提现费率
//	 * @param maxCash 最大提现金额
	 * @param type      1：15日之内或者2：15日之后
	 * @return
	 */
	public static double getCashFeeForHrd(double x,double r,int type){
		if(type==1){
			return r*x;
		}else if(type==2){
			if(x>30000){
				return 5;
			}else{
				return 3;
			}
			
		}else{
			return 0;
		}
		
	}
	
	/**
	 * 
	 * @Description:  雄猫软件
	 * @param:        @param x
	 * @param:        @param y
	 * @param:        @param r
	 * @param:        @param maxCash
	 * @param:        @return   
	 * @return:       double   
	 * @throws
	 */
	public static double getCashFeeForZrzbZero(double x,double y,double r,double maxCash){
		if(y>=x){
			if(x>1500&&x<=30000){
				return 0;
			}else{
				return 0;
			}
		}else{
			if(y<=30000){
				return 3+(x-y)*r;
			}else{
				return 5+(x-y)*r;
			}
		}
	}
	
	public static String formateRate(String rateStr){  
        if(rateStr.indexOf(".") != -1){  
            //获取小数点的位置  
            int num = 0;  
            num = rateStr.indexOf(".");  
              
            //获取小数点后面的数字 是否有两位 不足两位补足两位  
            String dianAfter = rateStr.substring(0,num+1);  
            String afterData = rateStr.replace(dianAfter, "");  
            if(afterData.length() < 2){  
               afterData = afterData + "0" ;  
            }else{  
                afterData = afterData;  
            }  
            return rateStr.substring(0,num) + "." + afterData.substring(0,2);  
         }else{  
           if(rateStr == "1"){  
              return "100";  
           }else{  
               return rateStr;  
           }  
         }  
	}
	
	/**
	 * 获取double型的几位小数
	 * @param d
	 * @param count
	 * @return
	 */
	public static double getDouble(double d, int count){
		BigDecimal b = new BigDecimal(d);
		double df = b.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
		return df;
	}  
	public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
		System.out.println(getDouble(8.2356,2));
		System.out.println(getDouble(8.2326,2));
		System.out.println(URLDecoder.decode("642309700%40qq.com", "UTF-8"));
		String startTime =String.valueOf(System.currentTimeMillis() / 1000);
		String  endTime =TimeUtil.getTime(TimeUtil.getEndTime(startTime, 0, -7, 0, 0, 0));
		System.out.println(startTime);
		System.out.println(endTime);
		System.out.println(TimeUtil.getStr(startTime));
		System.out.println(TimeUtil.getStr(endTime));
		System.out.println(System.currentTimeMillis());
		
	}
}
