package com.jiuyuan.entity.store;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商家商品操作日志表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-08-24
 */
@TableName("shop_product_action_log")
public class ProductActionLog extends Model<ProductActionLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 操作人ID
     */
	@TableField("action_user_id")
	private Long actionUserId;
	 /**
     * 操作人名称
     */
	@TableField("action_user_name")
	private String actionUserName;
	 /**
     * 操作人账号
     */
	@TableField("action_user_account")
	private String actionUserAccount;
	
	
	
	
    /**
     * 操作商品ID
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 操作类型:0取消商品VIP、1设置商品VIP
     */
	@TableField("action_type")
	private Integer actionType;
    /**
     * 操作内容
     */
	@TableField("action_content")
	private String actionContent;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;

	 /**
     * 创建时间
     */
	@TableField(exist = false)
	private String createTimeStr;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActionUserId() {
		return actionUserId;
	}

	public void setActionUserId(Long actionUserId) {
		this.actionUserId = actionUserId;
	}

	public String getActionUserName() {
		return actionUserName;
	}

	public void setActionUserName(String actionUserName) {
		this.actionUserName = actionUserName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public String getActionContent() {
		return actionContent;
	}

	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getActionUserAccount() {
		return actionUserAccount;
	}

	public void setActionUserAccount(String actionUserAccount) {
		this.actionUserAccount = actionUserAccount;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

}
