package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.SellercatUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.sellercat.update request
 *
 * @author auto
 * @since 1.0
 */
public class SellercatUpdateRequest implements QianmiRequest<SellercatUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 修改的类目名称
	 */
	private String name;

	/** 
	 * 需要修改的类目id
	 */
	private String sellerCid;

	/** 
	 * 产品线 1: 云订货(D2P) 2: 云商城(D2C)
	 */
	private String site;

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setSellerCid(String sellerCid) {
		this.sellerCid = sellerCid;
	}
	public String getSellerCid() {
		return this.sellerCid;
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
		return "qianmi.cloudshop.sellercat.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("name", this.name);
		txtParams.put("seller_cid", this.sellerCid);
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

	public Class<SellercatUpdateResponse> getResponseClass() {
		return SellercatUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(name, "name");
		RequestCheckUtils.checkNotEmpty(sellerCid, "sellerCid");
		RequestCheckUtils.checkNotEmpty(site, "site");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}