package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemcatUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.itemcat.update request
 *
 * @author auto
 * @since 1.0
 */
public class ItemcatUpdateRequest implements QianmiRequest<ItemcatUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 需要修改的类目id
	 */
	private String cid;

	/** 
	 * 修改的类目名称
	 */
	private String name;

	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCid() {
		return this.cid;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.itemcat.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("cid", this.cid);
		txtParams.put("name", this.name);
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

	public Class<ItemcatUpdateResponse> getResponseClass() {
		return ItemcatUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(cid, "cid");
		RequestCheckUtils.checkNotEmpty(name, "name");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}