package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.account.BankCardPayDiscount;
import com.jiuyuan.entity.account.UserBankCardSign;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.yujj.dao.mapper.UserBankCardSignMapper;

@Service
public class UserBankCardSignService {
    @Autowired
    private UserBankCardSignMapper userBankCardSignMapper;

    public UserBankCardSign getUserBankCardSign(long userId) {
    	int count= userBankCardSignMapper.getSignCount(userId);
    	if(count>0){
    		return userBankCardSignMapper.getUserBankCardSign(userId);
    		
    	}else{
    		 long time = System.currentTimeMillis();
    		UserBankCardSign userBankCardSign=new UserBankCardSign();
        	userBankCardSign.setUserId(userId);
        	userBankCardSign.setSeq(UtilDate.getOrderNum().substring(8)+(int)(Math.random()*100000));
//        	userBankCardSign.setMobile("");
//        	userBankCardSign.setRskLvl("");
        	userBankCardSign.setUpdateTime(time);
        	userBankCardSign.setCreateTime(time);
        	userBankCardSign.setPno(userId+UtilDate.getOrderNum().substring(7));
        	userBankCardSignMapper.addUserBankCardSign(userBankCardSign);
        	return userBankCardSign;
    	}
    }

    
    
    public int selectUnSignCount(long userId) {
    	int count= userBankCardSignMapper.getUnSignCount(userId);
    	return count;
    }
    
  
    public int updateUserBankCardSign(long userId, int isSigned ) {
    	long time = System.currentTimeMillis();
    	return userBankCardSignMapper.updateUserBankCardSign(userId, isSigned,time);    
    }
    
    public int insertUserBankCardSign(long userId) {
    
    	UserBankCardSign userBankCardSign=new UserBankCardSign();
    	userBankCardSign.setUserId(userId);
    	userBankCardSign.setSeq(UtilDate.getOrderNum().substring(8)+(int)(Math.random()*100000));
//    	userBankCardSign.setMobile("");
//    	userBankCardSign.setRskLvl("");
    	userBankCardSign.setPno(userId+UtilDate.getOrderNum().substring(7));
    	return userBankCardSignMapper.addUserBankCardSign(userBankCardSign);
    	
    	
    }
    
    public int addBankCardPayDiscount(long userId, long orderNo , String discountFlag, double discountAmt, String paymentNo,double payAmt ) {
    	
    		long time = System.currentTimeMillis();
    		BankCardPayDiscount bankCardPayDiscount = new BankCardPayDiscount();
    		bankCardPayDiscount.setOrderNo(orderNo);
    		bankCardPayDiscount.setDiscountAmt(discountAmt);
    		bankCardPayDiscount.setDiscountFlag(discountFlag);
    		bankCardPayDiscount.setPayAmt(payAmt);
    		bankCardPayDiscount.setUserId(userId);
    		bankCardPayDiscount.setCreateTime(time);
    		bankCardPayDiscount.setPaymentNo(paymentNo);
        	int count = userBankCardSignMapper.addBankCardPayDiscount(bankCardPayDiscount);
        	return count;	
    }
    
    public List<BankCardPayDiscount> getBankCardPayDiscountList(long userId) {
    	return userBankCardSignMapper.getBankCardPayDiscountList(userId);
    }
    
    public BankCardPayDiscount getBankCardPayDiscountByOrderNo(long userId, long orderNo) {
    	return userBankCardSignMapper.getBankCardPayDiscountByOrderNo(userId, orderNo);
    }

}
