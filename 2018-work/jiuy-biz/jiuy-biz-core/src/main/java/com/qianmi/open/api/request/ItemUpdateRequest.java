package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.update request
 *
 * @author auto
 * @since 1.0
 */
public class ItemUpdateRequest implements QianmiRequest<ItemUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 品牌编号，当商品为自定义商品时可以修改品牌
	 */
	private String brandId;

	/** 
	 * 商品类目id
	 */
	private String cid;

	/** 
	 * 关键词，多个关键词用逗号“，”分开，且最多只能输入5个关键字
	 */
	private String keywords;

	/** 
	 * 商品编号ID
	 */
	private String numIid;

	/** 
	 * 商家的外部编码，当商品为自定义商品时可以修改商家外部编码
	 */
	private String outerId;

	/** 
	 * 商品关联销售渠道，0：关联所有已开通渠道，1：仅云订货，2：仅云商城，3 取消关联所有销售渠道
	 */
	private String site;

	/** 
	 * 商品名称(货柜名称)
	 */
	private String title;

	/** 
	 * 计量单位，当商品为自定义商品时可以修改计量单位
	 */
	private String unit;

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandId() {
		return this.brandId;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCid() {
		return this.cid;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getKeywords() {
		return this.keywords;
	}

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}
	public String getNumIid() {
		return this.numIid;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public String getOuterId() {
		return this.outerId;
	}

	public void setSite(String site) {
		this.site = site;
	}
	public String getSite() {
		return this.site;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return this.title;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return this.unit;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.item.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("brand_id", this.brandId);
		txtParams.put("cid", this.cid);
		txtParams.put("keywords", this.keywords);
		txtParams.put("num_iid", this.numIid);
		txtParams.put("outer_id", this.outerId);
		txtParams.put("site", this.site);
		txtParams.put("title", this.title);
		txtParams.put("unit", this.unit);
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

	public Class<ItemUpdateResponse> getResponseClass() {
		return ItemUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(numIid, "numIid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}