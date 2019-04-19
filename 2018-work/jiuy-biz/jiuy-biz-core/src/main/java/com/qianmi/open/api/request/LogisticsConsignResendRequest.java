package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.LogisticsConsignResendResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.logistics.consign.resend request
 *
 * @author auto
 * @since 1.0
 */
public class LogisticsConsignResendRequest implements QianmiRequest<LogisticsConsignResendResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 商家物流公司编号
	 */
	private String companyId;

	/** 
	 * 默认当前时间，时间格式：yyyy-MM-dd HH:mm:ss
	 */
	private String deliverTime;

	/** 
	 * 运单号
	 */
	private String outSid;

	/** 
	 * 包裹单号，site为1（云订货）时必填
	 */
	private String packId;

	/** 
	 * 物流费，默认0元
	 */
	private String postFee;

	/** 
	 * 订单渠道来源：1云订货   2云商城 
	 */
	private String site;

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

	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}
	public String getPostFee() {
		return this.postFee;
	}

	public void setSite(String site) {
		this.site = site;
	}
	public String getSite() {
		return this.site;
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
		return "qianmi.cloudshop.logistics.consign.resend";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("company_id", this.companyId);
		txtParams.put("deliver_time", this.deliverTime);
		txtParams.put("out_sid", this.outSid);
		txtParams.put("pack_id", this.packId);
		txtParams.put("post_fee", this.postFee);
		txtParams.put("site", this.site);
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

	public Class<LogisticsConsignResendResponse> getResponseClass() {
		return LogisticsConsignResendResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(companyId, "companyId");
		RequestCheckUtils.checkNotEmpty(outSid, "outSid");
		RequestCheckUtils.checkNotEmpty(tid, "tid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}