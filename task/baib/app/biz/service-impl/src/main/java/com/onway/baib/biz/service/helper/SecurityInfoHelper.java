/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.biz.service.helper;

import com.onway.baib.core.model.base.result.UserTokenResult;



/**
 * 安全信息管理组件
 * 
 * @author guangdong.li
 * @version $Id: SecurityInfoHelper.java, v 0.1 23 Feb 2016 15:17:41 guangdong.li Exp $
 */
public interface SecurityInfoHelper {

    /**
     * 检查支付密码是否正确
     * 
     * @param userId
     * @param payPassword
     * @param version
     * @param appType
     * @param encodeType
     * @param tokenResult
     * @return
     */
    public boolean checkPayPwd(String userId, String payPassword);

    /**
     * 检查支付密码是否正确
     * 
     * @param userId
     * @param payPassword
     * @param version
     * @param appType
     * @param encodeType
     * @param tokenResult
     * @return
     */
    public boolean checkPayPassword(String userId, String payPassword, String version,
                                    String appType, UserTokenResult tokenResult);

    /**
     * 解密银行卡
     * 
     * @param userId
     * @param bankAccount
     * @param appType
     * @param tokenResult
     * @return
     */
    public String decryStr(String userId, String bankAccount, String appType,
                           UserTokenResult tokenResult);
}
