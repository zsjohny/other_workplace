package com.finace.miscroservice.user.entity;


import org.springframework.beans.factory.annotation.Autowired;

/**
 * 提交基础参数
 * 
 * @ClassName:     BaseTrustPost
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014年11月28日 上午11:34:11 
 * <b>Copyright (c)</b> 一桶金版权所有 <br/>
 */
public class BaseTrustReturn {

    private String CmdId;           //消息类型
    
    private String RespCode;        //应答返回码
    
    private String RespDesc;        //应答描述
    
    private String MerCustId;   //商户客户号(测试环境6000060001228050/生产6000060039052925)

    public String getCmdId() {
        return CmdId;
    }

    public void setCmdId(String cmdId) {
        CmdId = cmdId;
    }

    public String getRespCode() {
        return RespCode;
    }

    public void setRespCode(String respCode) {
        RespCode = respCode;
    }

    public String getRespDesc() {
        return RespDesc;
    }

    public void setRespDesc(String respDesc) {
        RespDesc = respDesc;
    }

    public String getMerCustId() {
        return MerCustId;
    }

    public void setMerCustId(String merCustId) {
        MerCustId = merCustId;
    }

    
}
