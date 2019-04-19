package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.SkuImageDeleteResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.sku.image.delete request
 *
 * @author auto
 * @since 1.0
 */
public class SkuImageDeleteRequest implements QianmiRequest<SkuImageDeleteResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 图片编号
	 */
	private String imgId;

	/** 
	 * 商品编号
	 */
	private String numIid;

	/** 
	 * sku编号
	 */
	private String skuId;

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public String getImgId() {
		return this.imgId;
	}

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}
	public String getNumIid() {
		return this.numIid;
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
		return "qianmi.cloudshop.sku.image.delete";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("img_id", this.imgId);
		txtParams.put("num_iid", this.numIid);
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

	public Class<SkuImageDeleteResponse> getResponseClass() {
		return SkuImageDeleteResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(imgId, "imgId");
		RequestCheckUtils.checkNotEmpty(numIid, "numIid");
		RequestCheckUtils.checkNotEmpty(skuId, "skuId");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}