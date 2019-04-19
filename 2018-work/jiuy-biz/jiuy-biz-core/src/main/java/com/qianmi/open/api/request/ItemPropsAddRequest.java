package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemPropsAddResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.props.add request
 *
 * @author auto
 * @since 1.0
 */
public class ItemPropsAddRequest implements QianmiRequest<ItemPropsAddResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * ItemProp结构中的所有字段均可返回，多个字段用”,”分隔； 默认值：pid,pname,prop_vals。
	 */
	private String fields;

	/** 
	 * 规格项
	 */
	private String pname;

	/** 
	 * 规格值，多个值用”,”隔开,数量不能超过50个
	 */
	private String vnames;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPname() {
		return this.pname;
	}

	public void setVnames(String vnames) {
		this.vnames = vnames;
	}
	public String getVnames() {
		return this.vnames;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.item.props.add";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("pname", this.pname);
		txtParams.put("vnames", this.vnames);
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

	public Class<ItemPropsAddResponse> getResponseClass() {
		return ItemPropsAddResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(pname, "pname");
		RequestCheckUtils.checkNotEmpty(vnames, "vnames");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}