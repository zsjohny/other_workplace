package org.dream.utils.quota.msg;

/**
 * @author Boyce
 * 2016年8月12日 下午2:39:51 
 */
public class XtraderSubQuotaReq extends AbstractXtraderQuota{
	{
		wAssitantCmd=10;
		MsgType="SubQuoteReq";
	}
	public String ExchangeNo;
	public String CommodityNo;
	public String CommodityTYpe="F";
	public String ContractNo1;
	public String ContractNo2;
	public String StrikePrice="";
	public String CallPutFlag="N";
	
}
