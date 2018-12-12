/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.web.controller.result;


/**
 * 支付参数结果集
 * 
 * @author guangdong.li
 * @version $Id: ServiceFeeResult.java, v 0.1 2016年4月18日 下午5:15:59 guangdong.li Exp $
 */
public class SysConfigResult extends JsonResult {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * @param bizSucc
     */
    public SysConfigResult(boolean bizSucc) {
        super(bizSucc);
    }

    /** 微信*/
    private String appid;

    private String partnerid;

    private String appsecret;

    private String mchid;
    
    
    
    /** 支付宝*/
    private String partner;

    private String seller;

    private String privateKey;
    
    private String authUrl;

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    /**
     * Getter method for property <tt>appid</tt>.
     * 
     * @return property value of appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * Setter method for property <tt>appid</tt>.
     * 
     * @param appid value to be assigned to property appid
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * Getter method for property <tt>partnerid</tt>.
     * 
     * @return property value of partnerid
     */
    public String getPartnerid() {
        return partnerid;
    }

    /**
     * Setter method for property <tt>partnerid</tt>.
     * 
     * @param partnerid value to be assigned to property partnerid
     */
    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    /**
     * Getter method for property <tt>appsecret</tt>.
     * 
     * @return property value of appsecret
     */
    public String getAppsecret() {
        return appsecret;
    }

    /**
     * Setter method for property <tt>appsecret</tt>.
     * 
     * @param appsecret value to be assigned to property appsecret
     */
    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    /**
     * Getter method for property <tt>mchid</tt>.
     * 
     * @return property value of mchid
     */
    public String getMchid() {
        return mchid;
    }

    /**
     * Setter method for property <tt>mchid</tt>.
     * 
     * @param mchid value to be assigned to property mchid
     */
    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    /**
     * Getter method for property <tt>partner</tt>.
     * 
     * @return property value of partner
     */
    public String getPartner() {
        return partner;
    }

    /**
     * Setter method for property <tt>partner</tt>.
     * 
     * @param partner value to be assigned to property partner
     */
    public void setPartner(String partner) {
        this.partner = partner;
    }

    /**
     * Getter method for property <tt>seller</tt>.
     * 
     * @return property value of seller
     */
    public String getSeller() {
        return seller;
    }

    /**
     * Setter method for property <tt>seller</tt>.
     * 
     * @param seller value to be assigned to property seller
     */
    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * Getter method for property <tt>privateKey</tt>.
     * 
     * @return property value of privateKey
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * Setter method for property <tt>privateKey</tt>.
     * 
     * @param privateKey value to be assigned to property privateKey
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}
