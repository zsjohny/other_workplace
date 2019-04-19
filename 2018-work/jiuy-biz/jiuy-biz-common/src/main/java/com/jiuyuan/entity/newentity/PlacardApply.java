package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 公告申请表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-14
 */
@TableName("store_placard_apply")
public class PlacardApply extends Model<PlacardApply> {

    private static final long serialVersionUID = 1L;
//    公告申请状态：0待审核、1已通过、2已拒绝
	public static int  state_wait_audit = 0;//0待审核
	public static int  state_audit_yes = 1;//1已通过
	public static int  state_audit_no = 2;//2已拒绝
	
	public String buildStateName() {
		if(this.state == state_wait_audit){
			return "待审核";
		}else if(this.state == state_audit_yes){
			return "已通过";
		}else if(this.state == state_audit_no){
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
     * 公告ID
     */
	@TableField("placard_id")
	private Long placardId;
    /**
     * 公告标题
     */
	@TableField("placard_title")
	private String placardTitle;
	/**
     * 供应商ID
     */
	@TableField("supplier_id")
	private Long supplierId;
	/**
     * 供应商名称
     */
	@TableField("supplier_name")
	private String supplierName;
   
    /**
     * 品牌ID
     */
	@TableField("brand_id")
	private Long brandId;
    /**
     * 品牌名称
     */
	@TableField("brand_name")
	private String brandName;
    /**
     * 公告申请状态：0待审核、1已通过、2已拒绝
     */
	private Integer state;
    /**
     * 报名信息
     */
	private String coutent;
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
     * 申请时间
     */
	@TableField("apply_time")
	private Long applyTime;
	
	/**
     * 报名结束时间
     */
	@TableField("apply_end_time")
	private Long applyEndTime;


	
	
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

	public Long getPlacardId() {
		return placardId;
	}

	public void setPlacardId(Long placardId) {
		this.placardId = placardId;
	}

	public String getPlacardTitle() {
		return placardTitle;
	}

	public void setPlacardTitle(String placardTitle) {
		this.placardTitle = placardTitle;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}



	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCoutent() {
		return coutent;
	}

	public void setCoutent(String coutent) {
		this.coutent = coutent;
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

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}
	
	
	

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	

}
