package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.account.BankCardPayDiscount;
import com.jiuyuan.entity.account.UserBankCardSign;

@DBMaster
public interface UserBankCardSignMapper {

	UserBankCardSign getUserBankCardSign(@Param("userId") long userId);
	
	int getSignCount(@Param("userId") long userId);
	
	int getUnSignCount(@Param("userId") long userId);

    int addUserBankCardSign(UserBankCardSign userBankCardSign);

    int updateUserBankCardSign(@Param("userId") long userId, @Param("isSigned") int isSigned, @Param("time") long time);
    
    int addBankCardPayDiscount(BankCardPayDiscount bankCardPayDiscount);
    
    List<BankCardPayDiscount> getBankCardPayDiscountList(@Param("userId") long userId);
    
    BankCardPayDiscount getBankCardPayDiscountByOrderNo(@Param("userId") long userId, @Param("orderNo") long orderNo);
}
