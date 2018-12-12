package com.finace.miscroservice.user.entity;

import org.apache.commons.lang.StringUtils;

/**
 * 取现(页面) (提交)
 *
 * @ClassName:     EnchashmentTrustPost
 * @Description:
 *
 * @author         cannavaro
 * @version        V1.0
 * @Date           2014年11月26日 下午9:22:43
 * <b>Copyright (c)</b> 一桶金版权所有 <br/>
 */
public class EnchashmentTrustPost extends BaseTrustPost{

    private String OrdId;           //订单号

    private String UsrCustId;       //用户客户号

    private String TransAmt;        //交易金额

    private String ServFee;         //商户收取服务费金 额

    private String ServFeeAcctId;   //商户子账户号

    private String OpenAcctId;      //开户银行账号

    private String RetUrl;          //页面返回 URL

    private String BgRetUrl;        //商户后台应答地址

    private String Remark;          //备注

    private String CharSet;         //编码集

    private String MerPriv;         //商户私有域

    private String ReqExt;          //入参扩展域

    private String FeeObjFlag;      //手续费收取对象

    private String FeeAcctId;       //手续费收取子账户

    private String CashChl;         //取现渠道

    private String ChkValue;        //签名

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

    public String getOpenAcctId() {
        return OpenAcctId;
    }

    public void setOpenAcctId(String openAcctId) {
        OpenAcctId = openAcctId;
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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCharSet() {
        return CharSet;
    }

    public void setCharSet(String charSet) {
        CharSet = charSet;
    }

    public String getMerPriv() {
        return MerPriv;
    }

    public void setMerPriv(String merPriv) {
        MerPriv = merPriv;
    }

    public String getReqExt() {
        return ReqExt;
    }

    public void setReqExt(String reqExt) {
        ReqExt = reqExt;
    }

    public String getFeeObjFlag() {
        return FeeObjFlag;
    }

    public void setFeeObjFlag(String feeObjFlag) {
        FeeObjFlag = feeObjFlag;
    }

    public String getFeeAcctId() {
        return FeeAcctId;
    }

    public void setFeeAcctId(String feeAcctId) {
        FeeAcctId = feeAcctId;
    }

    public String getCashChl() {
        return CashChl;
    }

    public void setCashChl(String cashChl) {
        CashChl = cashChl;
    }

    public String getChkValue() {
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();

        buffer.append(StringUtils.trimToEmpty(getVersion())).append(StringUtils.trimToEmpty(getCmdId()))
            .append(StringUtils.trimToEmpty(getMerCustId())).append(StringUtils.trimToEmpty(getOrdId()))
            .append(StringUtils.trimToEmpty(getUsrCustId())).append(StringUtils.trimToEmpty(getTransAmt()))
            .append(StringUtils.trimToEmpty(getServFee())).append(StringUtils.trimToEmpty(getServFeeAcctId()))
            .append(StringUtils.trimToEmpty(getOpenAcctId())).append(StringUtils.trimToEmpty(getRetUrl()))
            .append(StringUtils.trimToEmpty(getBgRetUrl())).append(StringUtils.trimToEmpty(getRemark()))
            .append(StringUtils.trimToEmpty(getMerPriv())).append(StringUtils.trimToEmpty(getReqExt()));

        String plainStr = buffer.toString();

        String chkstr = "";
        try {
            chkstr = SignUtils.encryptByRSA(plainStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chkstr;
    }

    public void setChkValue(String chkValue) {
        ChkValue = chkValue;
    }


}
