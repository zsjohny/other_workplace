package com.jiuyuan.entity.newentity.ground;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 地推人员客户阶段变化计划表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-15
 */
@TableName("ground_customer_stage_change")
public class GroundCustomerStageChange extends Model<GroundCustomerStageChange> {
	private static final long serialVersionUID = 1L;
  

	
    
    public GroundCustomerStageChange() {
		super();
	}


	
    public static final Integer ADD_CUSTOMER = 1;
    public static final Integer DELETE_CUSTOMER = -1;
    
    
    

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
	@TableField("super_ids")
	private String superIds;
    /**
     * 地推用户id
     */
	@TableField("ground_user_id")
	private Long groundUserId;
    /**
     * 门店用户id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 计划阶段变化时间，时间戳，精确到毫秒
     */
	@TableField("stage_change_time")
	private Long stageChangeTime;
    /**
     * 计划阶段变化日期，格式例：20170909
     */
	@TableField("stage_change_date")
	private Integer stageChangeDate;
    /**
     * 计划变化客户数据：1增加、-1减少
     */
	@TableField("change_count")
	private Long changeCount;
    /**
     * 阶段类型：1(第一阶段)、2(第二阶段)、3第三阶段、4其他阶段
     */
	@TableField("stage_type")
	private Integer stageType;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;

	  public GroundCustomerStageChange(String superIds, Long groundUserId, Long storeId, Long stageChangeTime,
				Integer stageChangeDate, Long changeCount, Integer stageType, Long createTime) {
			super();
			this.superIds = superIds;
			this.groundUserId = groundUserId;
			this.storeId = storeId;
			this.stageChangeTime = stageChangeTime;
			this.stageChangeDate = stageChangeDate;
			this.changeCount = changeCount;
			this.stageType = stageType;
			this.createTime = createTime;
		}
	  
	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getSuperIds() {
		return superIds;
	}

	public void setSuperIds(String superIds) {
		this.superIds = superIds;
	}

	public Long getGroundUserId() {
		return groundUserId;
	}

	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getStageChangeTime() {
		return stageChangeTime;
	}

	public void setStageChangeTime(Long stageChangeTime) {
		this.stageChangeTime = stageChangeTime;
	}

	public Integer getStageChangeDate() {
		return stageChangeDate;
	}

	public void setStageChangeDate(Integer stageChangeDate) {
		this.stageChangeDate = stageChangeDate;
	}

	public Long getChangeCount() {
		return changeCount;
	}

	public void setChangeCount(Long changeCount) {
		this.changeCount = changeCount;
	}

	public Integer getStageType() {
		return stageType;
	}

	public void setStageType(Integer stageType) {
		this.stageType = stageType;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
