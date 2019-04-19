package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.SkusQuantityBatchUpdateResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.skus.quantity.batch.update request
 *
 * @author auto
 * @since 1.0
 */
public class SkusQuantityBatchUpdateRequest implements QianmiRequest<SkusQuantityBatchUpdateResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * sku库存批量修改入参，用于指定一批sku和每个sku的库存修改值。格式为skuId:quantity;skuId:quantity。当全量更新库存时，quantity必须为大于等于0的正整数；当增量更新库存时，quantity为整数，可小于等于0。若增量更新时传入的库存为负数，则负数与实际库存之和不能小于0
	 */
	private String skuidQuantity;

	/** 
	 * 库存更新方式，1全量更新 2增量更新。默认全量更新
	 */
	private String type;

	public void setSkuidQuantity(String skuidQuantity) {
		this.skuidQuantity = skuidQuantity;
	}
	public String getSkuidQuantity() {
		return this.skuidQuantity;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.skus.quantity.batch.update";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("skuid_quantity", this.skuidQuantity);
		txtParams.put("type", this.type);
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

	public Class<SkusQuantityBatchUpdateResponse> getResponseClass() {
		return SkusQuantityBatchUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(skuidQuantity, "skuidQuantity");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}