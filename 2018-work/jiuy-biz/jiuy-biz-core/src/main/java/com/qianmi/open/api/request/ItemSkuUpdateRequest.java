package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemSkuUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.sku.update request
 *
 * @author auto
 * @since 1.0
 */
public class ItemSkuUpdateRequest implements QianmiRequest<ItemSkuUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 商品级别的条形码
	 */
	private String barcode;

	/** 
	 * 成本价，单位元，保留2位小数
	 */
	private String costPrice;

	/** 
	 * sku的市场价，单位元，保留2位小数
	 */
	private String marketPrice;

	/** 
	 * 商品编号Id
	 */
	private String numIid;

	/** 
	 * SKU对应的商家编码
	 */
	private String outerId;

	/** 
	 * 属于这个sku的商品的售价，保留2位小数，单位元
	 */
	private String price;

	/** 
	 * 属于这个sku的商品的数量
	 */
	private String quantity;

	/** 
	 * 副标题(卖点)
	 */
	private String sellPoint;

	/** 
	 * sku编号 ，g开头
	 */
	private String skuId;

	/** 
	 * sku的重量,单位kg,最多支持3位小数
	 */
	private String weight;

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBarcode() {
		return this.barcode;
	}

	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getCostPrice() {
		return this.costPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getMarketPrice() {
		return this.marketPrice;
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

	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return this.price;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getQuantity() {
		return this.quantity;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public String getSellPoint() {
		return this.sellPoint;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSkuId() {
		return this.skuId;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getWeight() {
		return this.weight;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.item.sku.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("barcode", this.barcode);
		txtParams.put("cost_price", this.costPrice);
		txtParams.put("market_price", this.marketPrice);
		txtParams.put("num_iid", this.numIid);
		txtParams.put("outer_id", this.outerId);
		txtParams.put("price", this.price);
		txtParams.put("quantity", this.quantity);
		txtParams.put("sell_point", this.sellPoint);
		txtParams.put("sku_id", this.skuId);
		txtParams.put("weight", this.weight);
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

	public Class<ItemSkuUpdateResponse> getResponseClass() {
		return ItemSkuUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(numIid, "numIid");
		RequestCheckUtils.checkNotEmpty(skuId, "skuId");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}