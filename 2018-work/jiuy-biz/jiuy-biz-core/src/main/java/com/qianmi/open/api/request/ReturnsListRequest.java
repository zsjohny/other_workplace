package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.ReturnsListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.returns.list request
 *
 * @author auto
 * @since 1.0
 */
public class ReturnsListRequest implements QianmiRequest<ReturnsListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 退货单创建结束时间 格式yyyy-MM-dd HH：mm:ss
	 */
	private String endCreated;

	/** 
	 * 需要返回的字段
	 */
	private String fields;

	/** 
	 * 买家编号
	 */
	private String memberId;

	/** 
	 * 页码，从0开始
	 */
	private Integer pageNo;

	/** 
	 * 每页条数，最大支持100
	 */
	private Integer pageSize;

	/** 
	 * 退货类型 0 -- 售中 1 -- 售后
	 */
	private Integer returnType;

	/** 
	 * 退货方式
	 */
	private String shipTypeId;

	/** 
	 * 退货单创建开始时间 格式yyyy-MM-dd HH：mm:ss
	 */
	private String startCreated;

	/** 
	 * 订单编号
	 */
	private String tid;

	public void setEndCreated(String endCreated) {
		this.endCreated = endCreated;
	}
	public String getEndCreated() {
		return this.endCreated;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberId() {
		return this.memberId;
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

	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}
	public Integer getReturnType() {
		return this.returnType;
	}

	public void setShipTypeId(String shipTypeId) {
		this.shipTypeId = shipTypeId;
	}
	public String getShipTypeId() {
		return this.shipTypeId;
	}

	public void setStartCreated(String startCreated) {
		this.startCreated = startCreated;
	}
	public String getStartCreated() {
		return this.startCreated;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTid() {
		return this.tid;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.returns.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("end_created", this.endCreated);
		txtParams.put("fields", this.fields);
		txtParams.put("member_id", this.memberId);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("return_type", this.returnType);
		txtParams.put("ship_type_id", this.shipTypeId);
		txtParams.put("start_created", this.startCreated);
		txtParams.put("tid", this.tid);
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

	public Class<ReturnsListResponse> getResponseClass() {
		return ReturnsListResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}