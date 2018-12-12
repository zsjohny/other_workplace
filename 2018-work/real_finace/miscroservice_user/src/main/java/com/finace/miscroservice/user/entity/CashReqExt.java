package com.finace.miscroservice.user.entity;

/**
 * 提现申请ReqExt内容补充
 * 
 * @ClassName:     CashReqExt
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2015年8月21日 下午3:17:19 
 * <b>Copyright (c)</b> 一桶金版权所有 <br/>
 */
public class CashReqExt {

    private String FeeObjFlag;
    
    private String FeeAcctId;
    
    private String CashChl;

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
    
    
}
