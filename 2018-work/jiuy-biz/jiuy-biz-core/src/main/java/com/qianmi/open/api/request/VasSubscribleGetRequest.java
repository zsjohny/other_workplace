package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.VasSubscribleGetResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.vas.subscrible.get request
 *
 * @author auto
 * @since 1.0
 */
public class VasSubscribleGetRequest implements QianmiRequest<VasSubscribleGetResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 应用收费代码（服务编码），从控制台 服务管理-收费管理-收费项目列表 能够获得该服务的应用收费代码
	 */
	private String articleCode;

	/** 
	 * 千米商家编码
	 */
	private String buyerId;

	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}
	public String getArticleCode() {
		return this.articleCode;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerId() {
		return this.buyerId;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.vas.subscrible.get";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("article_code", this.articleCode);
		txtParams.put("buyer_id", this.buyerId);
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

	public Class<VasSubscribleGetResponse> getResponseClass() {
		return VasSubscribleGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(articleCode, "articleCode");
		RequestCheckUtils.checkNotEmpty(buyerId, "buyerId");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}