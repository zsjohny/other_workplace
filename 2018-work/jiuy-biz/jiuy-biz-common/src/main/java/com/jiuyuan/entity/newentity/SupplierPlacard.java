package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 供应商公告表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-14
 */
@TableName("store_supplier_placard")
public class SupplierPlacard extends Model<SupplierPlacard> {

    private static final long serialVersionUID = 1L;
    //	公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
    public static final int type_general = 0;//0普通公告
	public static final int type_apply = 1;//1报名公告
	
	public static final String type_name_general = "普通公告";//
	public static final String type_name_apply = "报名公告";//
	
    //	公告状态:0已发布、1通知中、2已停止
	public static final int state_publish = 0;//0已发布
	public static final int state_notify = 1;//1通知中
	public static final int state_stop = 2 ;//2已停止
	public static final String state_name_publish = "已发布";//1已发布
	public static final String state_name_notify = "通知中";//2通知中
	public static final String state_name_stop = "停止";//3已停止
	

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 公告标题
     */
	private String title;
    /**
     * 公告类型:0普通公告（报名关闭）、1报名公告（报名开启）
     */
	private Integer type;
    /**
     * 公告状态:0已发布、1通知中、2已停止
     */
	private Integer state;
    /**
     * 公告内容
     */
	private String content;
    /**
     * 系统弹窗通知的总数
     */
	private Integer notifyCount;
    /**
     * 阅读数
     */
	private Integer readCount;
    /**
     * 发布时间
     */
	@TableField("publish_time")
	private Long publishTime;
    /**
     * 停止时间
     */
	@TableField("stop_time")
	private Long stopTime;
	/**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
	
	 /**
     * 发布类型：0立即、1定时
     */
     @TableField("publish_type")
	private Integer publishType;
	
     /**
      * 是否发送站内通知：0不通知、1通知
      */
      @TableField("is_send_advice")
 	private Integer isSendAdvice;
	
	/**
     * 报名结束时间
     */
	@TableField("apply_end_time")
	private Long applyEndTime;
	

    /**
     * 创建人ID
     */
	@TableField("admin_id")
	private Long adminId;


	/**
     * 已读供应商ID集合，英文逗号分隔
     */
	@TableField("is_read_supplier_ids")
	private String isReadSupplierIds;
	
	
	
	
	
	
	
	
	
	
	public String getIsReadSupplierIds() {
		return isReadSupplierIds;
	}

	public void setIsReadSupplierIds(String isReadSupplierIds) {
		this.isReadSupplierIds = isReadSupplierIds;
	}

	public Integer getIsSendAdvice() {
		return isSendAdvice;
	}

	public void setIsSendAdvice(Integer isSendAdvice) {
		this.isSendAdvice = isSendAdvice;
	}

	public Integer getPublishType() {
		return publishType;
	}

	public void setPublishType(Integer publishType) {
		this.publishType = publishType;
	}

	

	public Long getApplyEndTime() {
		return applyEndTime;
	}

	public void setApplyEndTime(Long applyEndTime) {
		this.applyEndTime = applyEndTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getNotifyCount() {
		return notifyCount;
	}

	public void setNotifyCount(Integer notifyCount) {
		this.notifyCount = notifyCount;
	}

	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}

	public Long getStopTime() {
		return stopTime;
	}

	public void setStopTime(Long stopTime) {
		this.stopTime = stopTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	
	public String buildStateName() {
		if(this.state == state_publish){
			return state_name_publish;
		}else if(this.state == state_notify){
			return state_name_notify;
		}else if(this.state == state_stop){
			return state_name_stop;
		}
		return "";
	}
	
	

	public String buildTypeName(Integer type) {
		if(type == type_general){
			return type_name_general;
		}else if(type == type_apply){
			return type_name_apply;
		}
		return "";
	}
	/**
	 * 报名活动状态:0报名进行中、1报名已经结束、2无
	 * @return
	 */
	public int buildApplyState() {
		long time =  System.currentTimeMillis();
		int applyState = 2;
		if(this.type == this.type_apply){
			if(this.applyEndTime >= time){
				applyState = 0;
			}else{
				applyState = 1;
			};
		}
		return applyState;
	}
	/**
	 * 报名活动状态:0报名进行中、1报名已经结束、2无
	 * @return
	 */
	public String buildApplyStateName() {
		int applyState = buildApplyState();
		if(applyState == 0){
			return "报名进行中";
		}else if(applyState == 1){
			return "报名已经结束";
		}else{
			return "无";
		}
	}
	/**
	 * 阅读状态：0未读、1已读
	 * @param readState
	 * @return
	 */
	public String buildReadStateName(int readState) {
			if(readState == 1){
				return "已读";
			}else{
				return "未读";
			}
	 }

}
