package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 公告申请审核表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-20
 */
@TableName("store_placard_apply_audit")
public class PlacardApplyAudit extends Model<PlacardApplyAudit> {

    private static final long serialVersionUID = 1L;
    
//  公告申请状态：0待审核、1已通过、2已拒绝
	public static int  state_wait_audit = 0;//0待审核
	public static int  state_wait_yes = 1;//1已通过
	public static int  state_wait_no = 2;//2已拒绝
	
	public String buildStateName() {
		if(this.state == state_wait_audit){
			return "待审核";
		}else if(this.state == state_wait_yes){
			return "已通过";
		}else if(this.state == state_wait_no){
			return "已拒绝";
		} 
		return "";
	}

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 公告申请ID
     */
	@TableField("placard_apply_id")
	private Long placardApplyId;
    /**
     * 公告ID
     */
	@TableField("placard_id")
	private Long placardId;
    /**
     * 供应商ID
     */
	@TableField("supplier_id")
	private Long supplierId;
    /**
     * 公告申请状态：0待审核、1已通过、2已拒绝
     */
	private Integer state;
    /**
     * 审核说明
     */
	@TableField("title_note")
	private String titleNote;
	 /**
     * 审核时间
     */
	@TableField("audit_time")
	private Long auditTime;
	 /**
     * 审核人
     */
	@TableField("apply_user_name")
	private String applyUserName;
	
	 /**
     * 审核人ID
     */
	@TableField("apply_user_name_id")
	private Long applyUserNameId;
	
    /**
     * 报名信息
     */
	@TableField("apply_coutent")
	private String applyCoutent;
    /**
     * 申请时间
     */
	@TableField("apply_time")
	private Long applyTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlacardApplyId() {
		return placardApplyId;
	}

	public void setPlacardApplyId(Long placardApplyId) {
		this.placardApplyId = placardApplyId;
	}

	public Long getPlacardId() {
		return placardId;
	}

	public void setPlacardId(Long placardId) {
		this.placardId = placardId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getTitleNote() {
		return titleNote;
	}

	public void setTitleNote(String titleNote) {
		this.titleNote = titleNote;
	}

	public Long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Long auditTime) {
		this.auditTime = auditTime;
	}

	public String getApplyCoutent() {
		return applyCoutent;
	}

	public void setApplyCoutent(String applyCoutent) {
		this.applyCoutent = applyCoutent;
	}

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public Long getApplyUserNameId() {
		return applyUserNameId;
	}

	public void setApplyUserNameId(Long applyUserNameId) {
		this.applyUserNameId = applyUserNameId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
