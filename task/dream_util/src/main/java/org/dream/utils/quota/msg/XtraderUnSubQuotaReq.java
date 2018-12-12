package org.dream.utils.quota.msg;

/**
 * Created by yhj on 16/10/27.
 */
public class XtraderUnSubQuotaReq extends AbstractXtraderQuota {
    {
        wAssitantCmd=13;
        MsgType="UnsubQuoteReq";
    }
    public String ExchangeNo;
    public String CommodityNo;
    public String CommodityType="F";
    public String ContractNo1;
    public String ContractNo2;
    public String StrikePrice="";
    public String CallPutFlag="N";
}
