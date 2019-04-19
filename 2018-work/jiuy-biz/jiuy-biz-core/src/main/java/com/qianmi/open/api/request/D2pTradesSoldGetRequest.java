package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.D2pTradesSoldGetResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.d2p.trades.sold.get request
 *
 * @author auto
 * @since 1.0
 */
public class D2pTradesSoldGetRequest implements QianmiRequest<D2pTradesSoldGetResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 订单完成状态, -1:全部, 0:进行中, 1:已完成, 2:已作废
	 */
	private String completeStatus;

	/** 
	 * 查询交易创建的结束时间，格式：yyyy-MM-dd HH:mm:ss
	 */
	private String endCreated;

	/** 
	 * trade交易结构中的所有字段均可返回，多个字段用”,”分隔，请按需获取,获取order所有字段只需要传orders,如只需要部分字段，请按以下格式:order.oid,order.price
	 */
	private String fields;

	/** 
	 * 订单流程状态，只能是以下几种状态中的一种，不传则查询全部。pending_audit_trade：待订单审核，pending_audit_finance：待财务审核，pending_pack：待出库，pending_deliver：待发货，pending_receive：待收货确认，received：已收货。
	 */
	private String flowStatus;

	/** 
	 * 页码，取值范围：大于等于0的整数，默认0
	 */
	private Integer pageNo;

	/** 
	 * 每页条数，取值范围：大于0的整数，最大100，默认50
	 */
	private Integer pageSize;

	/** 
	 * 订单支付状态, -1:全部, 0:未支付, 1:已支付, 2:已退款, 4:部分退款
	 */
	private String payStatus;

	/** 
	 * 查询交易的创建开始时间，格式：yyyy-MM-dd HH:mm:ss，只能查询三个月之内的交易信息
	 */
	private String startCreated;

	/** 
	 * 订单类型:0自营 ，1代销，不指定会查询所有
	 */
	private Integer tradeFlag;

	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}
	public String getCompleteStatus() {
		return this.completeStatus;
	}

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

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getFlowStatus() {
		return this.flowStatus;
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

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPayStatus() {
		return this.payStatus;
	}

	public void setStartCreated(String startCreated) {
		this.startCreated = startCreated;
	}
	public String getStartCreated() {
		return this.startCreated;
	}

	public void setTradeFlag(Integer tradeFlag) {
		this.tradeFlag = tradeFlag;
	}
	public Integer getTradeFlag() {
		return this.tradeFlag;
	}

    public Long getTimestamp() {
    	return this.timestamp;
    }
    public void setTimestamp(Long timestamp) {
    	this.timestamp = timestamp;
    }

	public String getApiMethodName() {
		return "qianmi.cloudshop.d2p.trades.sold.get";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("complete_status", this.completeStatus);
		txtParams.put("end_created", this.endCreated);
		txtParams.put("fields", this.fields);
		txtParams.put("flow_status", this.flowStatus);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("pay_status", this.payStatus);
		txtParams.put("start_created", this.startCreated);
		txtParams.put("trade_flag", this.tradeFlag);
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

	public Class<D2pTradesSoldGetResponse> getResponseClass() {
		return D2pTradesSoldGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}