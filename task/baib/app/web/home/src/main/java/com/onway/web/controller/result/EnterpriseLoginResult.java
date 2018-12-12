/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.web.controller.result;



/**
 * 客户端登录结果集
 * 
 * @author guangdong.li
 * @version $Id: EmpLoginResult.java, v 0.1 2016年4月11日 下午10:28:00 guangdong.li Exp $
 */
public class EnterpriseLoginResult extends JsonResult {

    /**  */
    private static final long   serialVersionUID = 1L;

    /** 企业姓名*/
    private String              enterpriseName;

    /** 企业用户id*/
    private String              enterpriseId;
   
    /**企业类型  **/
    private String              enterpriseType;
    
    /**企业用户推广二维码**/
    private String               qRCode;
    
    /**登陆账号（即手机号）   **/
    private String             loginName;
    

    /**
     * @param bizSucc
     * @param errCode
     * @param message
     */
    public EnterpriseLoginResult(boolean bizSucc, String errCode, String message) {
        super(bizSucc, errCode, message);
    }


    public String getEnterpriseName() {
        return enterpriseName;
    }


    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }


    public String getEnterpriseId() {
        return enterpriseId;
    }


    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }


    public String getEnterpriseType() {
        return enterpriseType;
    }


    public void setEnterpriseType(String enterpriseType) {
        this.enterpriseType = enterpriseType;
    }


    public String getLoginName() {
        return loginName;
    }


    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


    public String getqRCode() {
        return qRCode;
    }


    public void setqRCode(String qRCode) {
        this.qRCode = qRCode;
    }

}
