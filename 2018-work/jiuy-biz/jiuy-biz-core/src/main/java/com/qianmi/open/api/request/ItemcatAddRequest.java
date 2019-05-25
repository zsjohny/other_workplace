package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemcatAddResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.itemcat.add request
 *
 * @author auto
 * @since 1.0
 */
public class ItemcatAddRequest implements QianmiRequest<ItemcatAddResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 目录名称
	 */
	private String name;

	/** 
	 * 父类目id，不传则默认顶级目录
	 */
	private String parentCid;

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setParentCid(String parentCid) {
		this.parentCid = parentCid;
	}
	public String getParentCid() {
		return this.parentCid;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.itemcat.add";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("name", this.name);
		txtParams.put("parent_cid", this.parentCid);
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

	public Class<ItemcatAddResponse> getResponseClass() {
		return ItemcatAddResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(name, "name");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}