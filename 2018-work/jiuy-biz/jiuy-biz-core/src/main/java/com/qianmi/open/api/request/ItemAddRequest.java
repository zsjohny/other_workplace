package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemAddResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.item.add request
 *
 * @author auto
 * @since 1.0
 */
public class ItemAddRequest implements QianmiRequest<ItemAddResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 商品的品牌编号
	 */
	private String brandId;

	/** 
	 * 商品的品牌名称
	 */
	private String brandName;

	/** 
	 * 商品类目
	 */
	private String cid;

	/** 
	 * 商品来源
	 */
	private String dataSource;

	/** 
	 * 商品描述
	 */
	private String desc;

	/** 
	 * 所发布商品的商品详情，返回字段参照Item商品结构，多个字段用”,”分隔；
	 */
	private String fields;

	/** 
	 * 规格开关，默认启用
	 */
	private Boolean hasProps;

	/** 
	 * 商品主图，最大:1M ，支持的文件类型：gif,jpg,jpeg,png；使用BASE64将图片文件进行编码，得到字符串，然后用“@”字符连接字符串（例：contentStr）和图片文件的格式（例：jpg）；示例：“jpg@contentStr”。
	 */
	private String image;

	/** 
	 *  外部商品编号，不超过32位。
	 */
	private String outerId;

	/** 
	 * 默认0：关联所有已开通的销售渠道，1仅云订货，2仅云商城，3：不关联任何销售渠道
	 */
	private String site;

	/** 
	 * 商品的sku信息JSON字符串；其中，sku可用字段：price（价格），quantity（库存），cost_price（成本价），outer_id（外部编号），barcode（条形码），副标题（sell_point），规格（sku_props）；sku_props的price和quantity必传，cost_price默认值为0。
	 */
	private String skusJson;

	/** 
	 * 商品名称
	 */
	private String title;

	/** 
	 * 商品的计量单位
	 */
	private String unit;

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandId() {
		return this.brandId;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandName() {
		return this.brandName;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCid() {
		return this.cid;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getDataSource() {
		return this.dataSource;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDesc() {
		return this.desc;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setHasProps(Boolean hasProps) {
		this.hasProps = hasProps;
	}
	public Boolean getHasProps() {
		return this.hasProps;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public String getImage() {
		return this.image;
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

	public void setSkusJson(String skusJson) {
		this.skusJson = skusJson;
	}
	public String getSkusJson() {
		return this.skusJson;
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
		return "qianmi.cloudshop.item.add";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("brand_id", this.brandId);
		txtParams.put("brand_name", this.brandName);
		txtParams.put("cid", this.cid);
		txtParams.put("dataSource", this.dataSource);
		txtParams.put("desc", this.desc);
		txtParams.put("fields", this.fields);
		txtParams.put("has_props", this.hasProps);
		txtParams.put("image", this.image);
		txtParams.put("outer_id", this.outerId);
		txtParams.put("site", this.site);
		txtParams.put("skus_json", this.skusJson);
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

	public Class<ItemAddResponse> getResponseClass() {
		return ItemAddResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
		RequestCheckUtils.checkNotEmpty(title, "title");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}