package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemSkuPriceUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.sku.price.update request
 *
 * @author auto
 * @since 1.0
 */
public class ItemSkuPriceUpdateRequest implements QianmiRequest<ItemSkuPriceUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * sku所属商品编号
	 */
	private String numIid;

	/** 
	 * sku售价, 单位: 元, 两位小数
	 */
	private String price;

	/** 
	 * sku编号
	 */
	private String skuId;

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}
	public String getNumIid() {
		return this.numIid;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return this.price;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSkuId() {
		return this.skuId;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.item.sku.price.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("num_iid", this.numIid);
		txtParams.put("price", this.price);
		txtParams.put("sku_id", this.skuId);
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

	public Class<ItemSkuPriceUpdateResponse> getResponseClass() {
		return ItemSkuPriceUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(numIid, "numIid");
		RequestCheckUtils.checkNotEmpty(price, "price");
		RequestCheckUtils.checkNotEmpty(skuId, "skuId");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}