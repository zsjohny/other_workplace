package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemImageUploadResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.image.upload request
 *
 * @author auto
 * @since 1.0
 */
public class ItemImageUploadRequest implements QianmiRequest<ItemImageUploadResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * ItemImg结构中的所有字段均可返回，多个字段用”,”分隔。
	 */
	private String fields;

	/** 
	 * 商品图片，最大:1M ，支持的文件类型：gif,jpg,jpeg,png；使用BASE64将图片文件进行编码，得到字符串，然后用“@”字符连接字符串（例：contentStr）和图片文件的格式（例：jpg）；示例：“jpg@contentStr”。
	 */
	private String image;

	/** 
	 * 图片编号
	 */
	private String imgId;

	/** 
	 * 商品编号
	 */
	private String numIid;

	/** 
	 * 图片顺序,默认0；取值范围：0-9
	 */
	private Integer position;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public String getImage() {
		return this.image;
	}

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

	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getPosition() {
		return this.position;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.item.image.upload";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("image", this.image);
		txtParams.put("img_id", this.imgId);
		txtParams.put("num_iid", this.numIid);
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

	public Class<ItemImageUploadResponse> getResponseClass() {
		return ItemImageUploadResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
		RequestCheckUtils.checkNotEmpty(numIid, "numIid");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}