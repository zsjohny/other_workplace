package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemPropsUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.props.update request
 *
 * @author auto
 * @since 1.0
 */
public class ItemPropsUpdateRequest implements QianmiRequest<ItemPropsUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * ItemProp结构中的所有字段均可返回，多个字段用”,”分隔; 默认值：pid,pname,prop_vals。
	 */
	private String fields;

	/** 
	 * 规格项，规格值被商品使用不可修改,名称不可重复。
	 */
	private String pid;

	/** 
	 * 规格项名
	 */
	private String pname;

	/** 
	 * 修改规格值.数据格式为（规格值1,规格值2,规格值3)，规格值之间用','隔开。
	 */
	private String vnames;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPid() {
		return this.pid;
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
		return "qianmi.cloudshop.item.props.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("pid", this.pid);
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

	public Class<ItemPropsUpdateResponse> getResponseClass() {
		return ItemPropsUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(pid, "pid");
		RequestCheckUtils.checkNotEmpty(vnames, "vnames");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}