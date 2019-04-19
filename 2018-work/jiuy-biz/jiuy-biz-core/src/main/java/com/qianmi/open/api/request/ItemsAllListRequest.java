package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ItemsAllListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.items.all.list request
 *
 * @author auto
 * @since 1.0
 */
public class ItemsAllListRequest implements QianmiRequest<ItemsAllListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 商品品牌ID
	 */
	private String brandId;

	/** 
	 * 商品类目ID
	 */
	private String cid;

	/** 
	 * 需返回字段列表，如商品名称、价格等。返回多个字段时，以逗号分隔。
	 */
	private String fields;

	/** 
	 * 排序格式：column:asc/desc，column可选值：cid(标准类目编号)、num(商品数量)，brand_id(品牌编号)，type_id(商品类型编号)
	 */
	private String orderBy;

	/** 
	 * 页码，大于等于0的整数，默认值0
	 */
	private Integer pageNo;

	/** 
	 * 每页条数，取大于0的整数，最大值50，默认值50
	 */
	private Integer pageSize;

	/** 
	 * 库存预警状态
	 */
	private String stockWarn;

	/** 
	 * 商品类型ID
	 */
	private String typeId;

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

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOrderBy() {
		return this.orderBy;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageNo() {
		return this.pageNo;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setStockWarn(String stockWarn) {
		this.stockWarn = stockWarn;
	}
	public String getStockWarn() {
		return this.stockWarn;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeId() {
		return this.typeId;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.items.all.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("brand_id", this.brandId);
		txtParams.put("cid", this.cid);
		txtParams.put("fields", this.fields);
		txtParams.put("order_by", this.orderBy);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("stock_warn", this.stockWarn);
		txtParams.put("type_id", this.typeId);
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

	public Class<ItemsAllListResponse> getResponseClass() {
		return ItemsAllListResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}