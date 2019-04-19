package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.D2pLogisticsSendResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.d2p.logistics.send request
 *
 * @author auto
 * @since 1.0
 */
public class D2pLogisticsSendRequest implements QianmiRequest<D2pLogisticsSendResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * admin卖家物流公司编号
	 */
	private String companyId;

	/** 
	 * 物流公司名称
	 */
	private String companyName;

	/** 
	 * 发货时间
	 */
	private String deliverTime;

	/** 
	 * 运单号，具体一个物流公司真实的运单号码
	 */
	private String outSid;

	/** 
	 * 包裹编号
	 */
	private String packId;

	/** 
	 * 发货包裹清单，包含商品单编号和商品实际发货数量，格式：oid:num;oid:num
	 */
	private String packItems;

	/** 
	 * 物流费用，单位 元，整数部分不超过999999，精确到2位小数
	 */
	private String postFee;

	/** 
	 * 交易中ship_type_id 卖家发货方式编号 self:自提 express：快递 virtual：虚拟发货 post:平邮 ems:EMS
	 */
	private String shipTypeId;

	/** 
	 * 订单编号
	 */
	private String tid;

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyId() {
		return this.companyId;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyName() {
		return this.companyName;
	}

	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}
	public String getDeliverTime() {
		return this.deliverTime;
	}

	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}
	public String getOutSid() {
		return this.outSid;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}
	public String getPackId() {
		return this.packId;
	}

	public void setPackItems(String packItems) {
		this.packItems = packItems;
	}
	public String getPackItems() {
		return this.packItems;
	}

	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}
	public String getPostFee() {
		return this.postFee;
	}

	public void setShipTypeId(String shipTypeId) {
		this.shipTypeId = shipTypeId;
	}
	public String getShipTypeId() {
		return this.shipTypeId;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTid() {
		return this.tid;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.d2p.logistics.send";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("company_id", this.companyId);
		txtParams.put("company_name", this.companyName);
		txtParams.put("deliver_time", this.deliverTime);
		txtParams.put("out_sid", this.outSid);
		txtParams.put("pack_id", this.packId);
		txtParams.put("pack_items", this.packItems);
		txtParams.put("post_fee", this.postFee);
		txtParams.put("ship_type_id", this.shipTypeId);
		txtParams.put("tid", this.tid);
		if(udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new QianmiHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<D2pLogisticsSendResponse> getResponseClass() {
		return D2pLogisticsSendResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(companyId, "companyId");
		RequestCheckUtils.checkNotEmpty(outSid, "outSid");
		RequestCheckUtils.checkNotEmpty(shipTypeId, "shipTypeId");
		RequestCheckUtils.checkNotEmpty(tid, "tid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}