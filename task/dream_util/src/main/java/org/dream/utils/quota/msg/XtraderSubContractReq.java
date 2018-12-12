package org.dream.utils.quota.msg;

/**
 * @author Boyce
 *         2016年8月12日 下午2:39:51
 */
public class XtraderSubContractReq extends AbstractXtraderQuota {
    {
        wAssitantCmd = 8;
        MsgType = "ContractInfoReq";
    }

    public String ExchangeNo;
    public String CommodityNo;
    public String CommodityType = "F";

}
