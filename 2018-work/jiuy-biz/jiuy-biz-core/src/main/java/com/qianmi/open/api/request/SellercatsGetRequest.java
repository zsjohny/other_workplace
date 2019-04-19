package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.SellercatsGetResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.sellercats.get request
 *
 * @author auto
 * @since 1.0
 */
public class SellercatsGetRequest implements QianmiRequest<SellercatsGetResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 需要返回的字段，多个字段之间以逗号隔开
	 */
	private String fields;

	/** 
	 * 父类目编号
	 */
	private String pSellerCid;

	/** 
	 * 类目编号列表
	 */
	private String sellerCids;

	/** 
	 * 站点分类，1：云订货(D2P) 2：云商城(D2C)
	 */
	private String site;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setpSellerCid(String pSellerCid) {
		this.pSellerCid = pSellerCid;
	}
	public String getpSellerCid() {
		return this.pSellerCid;
	}

	public void setSellerCids(String sellerCids) {
		this.sellerCids = sellerCids;
	}
	public String getSellerCids() {
		return this.sellerCids;
	}

	public void setSite(String site) {
		this.site = site;
	}
	public String getSite() {
		return this.site;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.sellercats.get";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("p_seller_cid", this.pSellerCid);
		txtParams.put("seller_cids", this.sellerCids);
		txtParams.put("site", this.site);
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

	public Class<SellercatsGetResponse> getResponseClass() {
		return SellercatsGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}