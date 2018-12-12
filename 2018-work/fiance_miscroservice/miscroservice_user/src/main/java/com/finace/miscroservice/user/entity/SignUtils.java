/**
 * 说明： 配置信息仅供Demo使用，开发请根据实际情况配置
 *
 *汇付天下有限公司
 *
 * Copyright (c) 2006-2013 ChinaPnR,Inc.All Rights Reserved.
 */
package com.finace.miscroservice.user.entity;

import chinapnr.SecureLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Configuration
@Component
@RefreshScope
public class SignUtils implements Serializable {
    private static final long  serialVersionUID        = 3640874934537168392L;

    /** MD5签名类型 **/
    public static final String SIGN_TYPE_MD5           = "M";

    /** RSA签名类型 **/
    public static final String SIGN_TYPE_RSA           = "R";

    /** RSA验证签名成功结果 **/
    public static final int RAS_VERIFY_SIGN_SUCCESS = 0;

    /** 商户客户号 **/
//    public static final String RECV_MER_ID             = "530617";//测试
//    public static final String RECV_MER_ID             = "830755";//生产

    /** 商户公钥文件地址 **/
//  public static final String MER_PUB_KEY_PATH        = "/var/lib/apache-tomcat-7.0.12/webapps/ROOT/key/PgPubk.key";
//  public static final String MER_PUB_KEY_PATH        = "/var/svns/ytj3.0/ytj/trunk/WebRoot/cskey/PgPubk.key";

/** 商户私钥文件地址 **/
//   public static final String MER_PRI_KEY_PATH        = "/var/lib/apache-tomcat-7.0.12/webapps/ROOT/key/MerPrK830755.key";
//   public static final String MER_PRI_KEY_PATH        = "/var/svns/ytj3.0/ytj/trunk/WebRoot/cskey/MerPrK530617.key";


    private static String recmerid;
    private static String prikeypath;
    private static String pubkeypath;

    @Value("${user.chinapnr.recmerid}")
    public void setRecmerid(String recmerid) {
        SignUtils.recmerid = recmerid;
    }

    @Value("${user.chinapnr.prikeypath}")
    public void setPrikeypath(String prikeypath) {
        SignUtils.prikeypath = prikeypath;
    }

    @Value("${user.chinapnr.pubkeypath}")
    public void setPubkeypath(String pubkeypath) {
        SignUtils.pubkeypath = pubkeypath;
    }

    /**
     * RSA方式加签
     *
     * @param forEncryptionStr
     * @return
     * @throws Exception
     */
    public static String encryptByRSA(String forEncryptionStr) throws Exception {
        SecureLink sl = new SecureLink();
        int result = sl.SignMsg(recmerid , prikeypath, forEncryptionStr);
        if (result < 0) {
            // 打印日志
            throw new Exception();
        }
        return sl.getChkValue();
    }

    public static boolean verifyByRSA(String forEncryptionStr, String chkValue)throws Exception {
        try {
            int verifySignResult = new SecureLink().VeriSignMsg(pubkeypath, forEncryptionStr, chkValue);
            return verifySignResult == RAS_VERIFY_SIGN_SUCCESS;
        } catch (Exception e) {
            // 打印日志
            throw new Exception();
        }
    }
}
