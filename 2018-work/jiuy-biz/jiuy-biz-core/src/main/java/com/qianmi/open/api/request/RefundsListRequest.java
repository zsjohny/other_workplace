package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.RefundsListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.refunds.list request
 *
 * @author auto
 * @since 1.0
 */
public class RefundsListRequest implements QianmiRequest<RefundsListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 退款单创建时间_结束时间, 格式: yyyy-MM-dd HH:mm:ss
	 */
	private String endCreated;

	/** 
	 * 需返回字段列表，返回多个字段时，以逗号分隔。
	 */
	private String fields;

	/** 
	 * 会员编号
	 */
	private String memberId;

	/** 
	 * 页码，大于等于0的整数，默认值0
	 */
	private Integer pageNo;

	/** 
	 * 每页条数，取大于0的整数，最大值100，默认值50
	 */
	private Integer pageSize;

	/** 
	 * 退款单号
	 */
	private String refundId;

	/** 
	 * 退款类型 0-售中 1-售后
	 */
	private String refundType;

	/** 
	 * 退款方式编号  OLP-在线支付 BTP-转账汇款  GRP-现金  OBP-余额支付 YX_OSP-积分支付
	 */
	private String refundTypeId;

	/** 
	 * 退款单创建时间_开始时间, 格式: yyyy-MM-dd HH:mm:ss
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

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	public String getRefundId() {
		return this.refundId;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	public String getRefundType() {
		return this.refundType;
	}

	public void setRefundTypeId(String refundTypeId) {
		this.refundTypeId = refundTypeId;
	}
	public String getRefundTypeId() {
		return this.refundTypeId;
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
		return "qianmi.cloudshop.refunds.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("end_created", this.endCreated);
		txtParams.put("fields", this.fields);
		txtParams.put("member_id", this.memberId);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("refund_id", this.refundId);
		txtParams.put("refund_type", this.refundType);
		txtParams.put("refund_type_id", this.refundTypeId);
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

	public Class<RefundsListResponse> getResponseClass() {
		return RefundsListResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}