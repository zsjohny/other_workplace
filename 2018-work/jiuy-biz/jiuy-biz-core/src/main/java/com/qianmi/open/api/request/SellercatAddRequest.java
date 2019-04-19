package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.SellercatAddResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.sellercat.add request
 *
 * @author auto
 * @since 1.0
 */
public class SellercatAddRequest implements QianmiRequest<SellercatAddResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 类目名
	 */
	private String name;

	/** 
	 * 父目录编号,不传则默认顶级目录
	 */
	private String pSellerCid;

	/** 
	 * 产品线 1: 云订货(D2P) 2: 云商城(D2C) 默认 1
	 */
	private String site;

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setpSellerCid(String pSellerCid) {
		this.pSellerCid = pSellerCid;
	}
	public String getpSellerCid() {
		return this.pSellerCid;
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
		return "qianmi.cloudshop.sellercat.add";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("name", this.name);
		txtParams.put("p_seller_cid", this.pSellerCid);
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

	public Class<SellercatAddResponse> getResponseClass() {
		return SellercatAddResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(name, "name");
		RequestCheckUtils.checkNotEmpty(pSellerCid, "pSellerCid");
		RequestCheckUtils.checkNotEmpty(site, "site");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}