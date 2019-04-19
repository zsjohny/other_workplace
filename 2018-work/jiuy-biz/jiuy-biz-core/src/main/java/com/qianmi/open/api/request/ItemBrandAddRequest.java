package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemBrandAddResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.brand.add request
 *
 * @author auto
 * @since 1.0
 */
public class ItemBrandAddRequest implements QianmiRequest<ItemBrandAddResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 品牌描述
	 */
	private String brandDesc;

	/** 
	 * 品牌名称
	 */
	private String brandName;

	/** 
	 * 新增品牌的信息，返回字段参照ItemBrand结构，多个字段用”,”分隔；
	 */
	private String fields;

	/** 
	 * 品牌图片，最大:1M ，支持的文件类型：gif,jpg,jpeg,png；注：使用BASE64将图片文件进行编码，得到字符串，然后用“@”字符连接字符串（例：contentStr）和图片文件的格式（例
	 */
	private String logo;

	/** 
	 * 排序
	 */
	private String position;

	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}
	public String getBrandDesc() {
		return this.brandDesc;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandName() {
		return this.brandName;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getLogo() {
		return this.logo;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	public String getPosition() {
		return this.position;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.item.brand.add";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("brand_desc", this.brandDesc);
		txtParams.put("brand_name", this.brandName);
		txtParams.put("fields", this.fields);
		txtParams.put("logo", this.logo);
		txtParams.put("position", this.position);
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

	public Class<ItemBrandAddResponse> getResponseClass() {
		return ItemBrandAddResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(brandName, "brandName");
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}