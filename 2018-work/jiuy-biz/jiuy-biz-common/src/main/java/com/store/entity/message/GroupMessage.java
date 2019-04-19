package com.store.entity.message;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 客服群发记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-24
 */
@TableName("shop_group_message")
public class GroupMessage extends Model<GroupMessage> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
     * 商家Id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 客服Id
     */
	@TableField("admin_id")
	private Long adminId;
    /**
     * 客服名称
     */
	@TableField("admin_name")
	private String adminName;
    /**
     * 客服头像
     */
	@TableField("admin_headimg")
	private String adminHeadimg;
    /**
     * 发送总会员数量
     */
	@TableField("send_total_member_count")
	private Integer sendTotalMemberCount;
    /**
     * 发送总会员ID集合
     */
	@TableField("send_total_member_ids")
	private String sendTotalMemberIds;
    /**
     * 发送成功会员数量
     */
	@TableField("send_seccess_member_count")
	private Integer sendSeccessMemberCount;
    /**
     * 发送成功会员ID集合
     */
	@TableField("send_seccess_member_ids")
	private String sendSeccessMemberIds;
    /**
     * 发送失败会员数量
     */
	@TableField("send_fail_member_count")
	private Integer sendFailMemberCount;
    /**
     * 发送失败会员ID集合
     */
	@TableField("send_fail_member_ids")
	private String sendFailMemberIds;
    /**
     * 内容
     */
	private String content;
    /**
     * 消息类型：0文本、1图片、2链接
     */
	@TableField("message_type")
	private Integer messageType;
    /**
     * 状态:-1删除，0正常
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminHeadimg() {
		return adminHeadimg;
	}

	public void setAdminHeadimg(String adminHeadimg) {
		this.adminHeadimg = adminHeadimg;
	}

	public Integer getSendTotalMemberCount() {
		return sendTotalMemberCount;
	}

	public void setSendTotalMemberCount(Integer sendTotalMemberCount) {
		this.sendTotalMemberCount = sendTotalMemberCount;
	}

	public String getSendTotalMemberIds() {
		return sendTotalMemberIds;
	}

	public void setSendTotalMemberIds(String sendTotalMemberIds) {
		this.sendTotalMemberIds = sendTotalMemberIds;
	}

	public Integer getSendSeccessMemberCount() {
		return sendSeccessMemberCount;
	}

	public void setSendSeccessMemberCount(Integer sendSeccessMemberCount) {
		this.sendSeccessMemberCount = sendSeccessMemberCount;
	}

	public String getSendSeccessMemberIds() {
		return sendSeccessMemberIds;
	}

	public void setSendSeccessMemberIds(String sendSeccessMemberIds) {
		this.sendSeccessMemberIds = sendSeccessMemberIds;
	}

	public Integer getSendFailMemberCount() {
		return sendFailMemberCount;
	}

	public void setSendFailMemberCount(Integer sendFailMemberCount) {
		this.sendFailMemberCount = sendFailMemberCount;
	}

	public String getSendFailMemberIds() {
		return sendFailMemberIds;
	}

	public void setSendFailMemberIds(String sendFailMemberIds) {
		this.sendFailMemberIds = sendFailMemberIds;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

}
