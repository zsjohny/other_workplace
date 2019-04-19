package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemSellercatsListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.sellercats.list request
 *
 * @author auto
 * @since 1.0
 */
public class ItemSellercatsListRequest implements QianmiRequest<ItemSellercatsListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 需返回字段列表。返回多个字段时，以逗号分隔。
	 */
	private String fields;

	/** 
	 * 商品编号，多个商品以”,“号分隔,每次不超过50条商品
	 */
	private String numIids;

	/** 
	 * 商品挂靠的销售渠道， 1:云订货(D2P)，2：云商城(D2C)  
	 */
	private String site;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setNumIids(String numIids) {
		this.numIids = numIids;
	}
	public String getNumIids() {
		return this.numIids;
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
		return "qianmi.cloudshop.item.sellercats.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("num_iids", this.numIids);
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

	public Class<ItemSellercatsListResponse> getResponseClass() {
		return ItemSellercatsListResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
		RequestCheckUtils.checkNotEmpty(numIids, "numIids");
		RequestCheckUtils.checkNotEmpty(site, "site");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}