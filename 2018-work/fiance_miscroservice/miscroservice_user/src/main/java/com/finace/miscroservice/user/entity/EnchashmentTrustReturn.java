package com.finace.miscroservice.user.entity;

import org.apache.commons.lang.StringUtils;


/**
 * 取现(页面) (异步对账返回)
 * 
 * @ClassName:     EnchashmentTrustPost
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014年11月26日 下午9:22:43 
 * <b>Copyright (c)</b> 一桶金版权所有 <br/>
 */
public class EnchashmentTrustReturn extends BaseTrustReturn {
    
    private String RespType;        //异步返回的 消息类型
    
    private String OrdId;           //订单号
    
    private String UsrCustId;       //用户客户号  
    
    private String TransAmt;        //交易金额
    
    private String RealTransAmt;    //实际到账金额
    
    private String OpenAcctId;      //开户银行账号
    
    private String OpenBankId;      //开户银行代号
    
    private String FeeAmt;          //手续费金额
    
    private String FeeCustId;       //手续费扣款客户号
    
    private String FeeAcctId;       //手续费扣款 子账户号

    private String ServFee;         //商户收取用户的服务费
    
    private String ServFeeAcctId;   //商户子账户号
    
    private String RetUrl;          //页面返回 URL
    
    private String BgRetUrl;        //商户后台应答地址
    
    private String MerPriv;         //商户私有域
    
    private String RespExt;          //入参扩展域
    
    private String ChkValue;        //签名

    public String getRespType() {
        return RespType;
    }

    public void setRespType(String respType) {
        RespType = respType;
    }

    public String getOrdId() {
        return OrdId;
    }

    public void setOrdId(String ordId) {
        OrdId = ordId;
    }

    public String getUsrCustId() {
        return UsrCustId;
    }

    public void setUsrCustId(String usrCustId) {
        UsrCustId = usrCustId;
    }

    public String getTransAmt() {
        return TransAmt;
    }

    public void setTransAmt(String transAmt) {
        TransAmt = transAmt;
    }

    public String getRealTransAmt() {
        return RealTransAmt;
    }

    public void setRealTransAmt(String realTransAmt) {
        RealTransAmt = realTransAmt;
    }

    public String getOpenAcctId() {
        return OpenAcctId;
    }

    public void setOpenAcctId(String openAcctId) {
        OpenAcctId = openAcctId;
    }

    public String getOpenBankId() {
        return OpenBankId;
    }

    public void setOpenBankId(String openBankId) {
        OpenBankId = openBankId;
    }

    public String getFeeAmt() {
        return FeeAmt;
    }

    public void setFeeAmt(String feeAmt) {
        FeeAmt = feeAmt;
    }

    public String getFeeCustId() {
        return FeeCustId;
    }

    public void setFeeCustId(String feeCustId) {
        FeeCustId = feeCustId;
    }

    public String getFeeAcctId() {
        return FeeAcctId;
    }

    public void setFeeAcctId(String feeAcctId) {
        FeeAcctId = feeAcctId;
    }

    public String getServFee() {
        return ServFee;
    }

    public void setServFee(String servFee) {
        ServFee = servFee;
    }

    public String getServFeeAcctId() {
        return ServFeeAcctId;
    }

    public void setServFeeAcctId(String servFeeAcctId) {
        ServFeeAcctId = servFeeAcctId;
    }

    public String getRetUrl() {
        return RetUrl;
    }

    public void setRetUrl(String retUrl) {
        RetUrl = retUrl;
    }

    public String getBgRetUrl() {
        return BgRetUrl;
    }

    public void setBgRetUrl(String bgRetUrl) {
        BgRetUrl = bgRetUrl;
    }

    public String getMerPriv() {
        return MerPriv;
    }

    public void setMerPriv(String merPriv) {
        MerPriv = merPriv;
    }

    public String getRespExt() {
        return RespExt;
    }

    public void setRespExt(String respExt) {
        RespExt = respExt;
    }

    public String getChkValue() {
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();
        
        if(StringUtils.isEmpty(getRespType())){
            
            buffer.append(StringUtils.trimToEmpty(getCmdId())).append(StringUtils.trimToEmpty(getRespCode()))
            .append(StringUtils.trimToEmpty(getMerCustId())).append(StringUtils.trimToEmpty(getOrdId()))
            .append(StringUtils.trimToEmpty(getUsrCustId())).append(StringUtils.trimToEmpty(getTransAmt()))
            .append(StringUtils.trimToEmpty(getOpenAcctId())).append(StringUtils.trimToEmpty(getOpenBankId()))
            .append(StringUtils.trimToEmpty(getFeeAmt())).append(StringUtils.trimToEmpty(getFeeCustId()))
            .append(StringUtils.trimToEmpty(getFeeAcctId())).append(StringUtils.trimToEmpty(getServFee()))
            .append(StringUtils.trimToEmpty(getServFeeAcctId())).append(StringUtils.trimToEmpty(getRetUrl()))
            .append(StringUtils.trimToEmpty(getBgRetUrl())).append(StringUtils.trimToEmpty(getMerPriv()))
            .append(StringUtils.trimToEmpty(getRespExt()));
            
        }else{
            
            buffer.append(StringUtils.trimToEmpty(getRespType())).append(StringUtils.trimToEmpty(getRespCode()))
            .append(StringUtils.trimToEmpty(getMerCustId())).append(StringUtils.trimToEmpty(getOrdId()))
            .append(StringUtils.trimToEmpty(getUsrCustId())).append(StringUtils.trimToEmpty(getTransAmt()))
            .append(StringUtils.trimToEmpty(getOpenAcctId())).append(StringUtils.trimToEmpty(getOpenBankId()))
            .append(StringUtils.trimToEmpty(getRetUrl()))
            .append(StringUtils.trimToEmpty(getBgRetUrl())).append(StringUtils.trimToEmpty(getMerPriv()))
            .append(StringUtils.trimToEmpty(getRespExt()));
        }
        
        String plainStr = buffer.toString();
        
        return plainStr;
    }

    public void setChkValue(String chkValue) {
        ChkValue = chkValue;
    }

    
    
}
