package com.qianmi.open.api.request;

import com.qianmi.open.api.tool.util.RequestCheckUtils;
import java.util.Map;

import com.qianmi.open.api.QianmiRequest;
import com.qianmi.open.api.tool.util.QianmiHashMap;
import com.qianmi.open.api.response.RefundsApplyListResponse;
import com.qianmi.open.api.ApiRuleException;

/**
 * API: qianmi.cloudshop.refunds.apply.list request
 *
 * @author auto
 * @since 1.0
 */
public class RefundsApplyListRequest implements QianmiRequest<RefundsApplyListResponse> {

    private Map<String, String> headerMap = new QianmiHashMap();
	private QianmiHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	 * 申请退货/退款编号
	 */
	private String applyId;

	/** 
	 * 申请状态 1-待审核 2-已审核通过 3-已收到退货 4-已退款 5-已完成 6-审核未通过
	 */
	private Integer applyState;

	/** 
	 * 查询截止时间_申请单创建时间
	 */
	private String endCreated;

	/** 
	 * 需返回字段列表，返回多个字段时，以逗号分隔。
	 */
	private String fields;

	/** 
	 * 商品名称
	 */
	private String itemName;

	/** 
	 * 会员昵称
	 */
	private String memberNick;

	/** 
	 * 用户类型 1个人 4分销商
	 */
	private String memberType;

	/** 
	 * 是否需要退款 1需要，0不需要
	 */
	private Integer needRefund;

	/** 
	 * 是否需要退货 1需要，0不需要
	 */
	private Integer needReturn;

	/** 
	 * 页码，大于等于0的整数，默认值0
	 */
	private Integer pageNo;

	/** 
	 * 每页条数，最大支持100，默认50
	 */
	private Integer pageSize;

	/** 
	 * 查询开始时间_申请单创建时间
	 */
	private String startCreated;

	/** 
	 * 订单编号
	 */
	private String tid;

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
	}
	public Integer getApplyState() {
		return this.applyState;
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

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemName() {
		return this.itemName;
	}

	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}
	public String getMemberNick() {
		return this.memberNick;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberType() {
		return this.memberType;
	}

	public void setNeedRefund(Integer needRefund) {
		this.needRefund = needRefund;
	}
	public Integer getNeedRefund() {
		return this.needRefund;
	}

	public void setNeedReturn(Integer needReturn) {
		this.needReturn = needReturn;
	}
	public Integer getNeedReturn() {
		return this.needReturn;
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
		return "qianmi.cloudshop.refunds.apply.list";
	}

	public Map<String, String> getTextParams() {
		QianmiHashMap txtParams = new QianmiHashMap();
		txtParams.put("apply_id", this.applyId);
		txtParams.put("apply_state", this.applyState);
		txtParams.put("end_created", this.endCreated);
		txtParams.put("fields", this.fields);
		txtParams.put("item_name", this.itemName);
		txtParams.put("member_nick", this.memberNick);
		txtParams.put("member_type", this.memberType);
		txtParams.put("need_refund", this.needRefund);
		txtParams.put("need_return", this.needReturn);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
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

	public Class<RefundsApplyListResponse> getResponseClass() {
		return RefundsApplyListResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(fields, "fields");
    }

	public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}