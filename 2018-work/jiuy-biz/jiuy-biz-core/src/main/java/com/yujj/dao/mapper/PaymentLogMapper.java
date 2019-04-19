/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.Map;

import com.jiuyuan.dao.annotation.DBMaster;

/**
 * @author LWS
 *
 */
@DBMaster
public interface PaymentLogMapper {

//    int createBeforePay(Map<String,Object> params);
    
    int updateAfterPay(Map<String,String> callbackParams);
}
