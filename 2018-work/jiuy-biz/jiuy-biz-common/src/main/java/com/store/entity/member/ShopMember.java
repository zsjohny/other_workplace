package com.store.entity.member;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 小程序会员表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-07-01
 */
@TableName("shop_member")
public class ShopMember extends Model<ShopMember> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
     * 商家Id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 手机号码
     */
	@TableField("bind_phone")
	private String bindPhone;
    /**
     * 微信UId
     */
	@TableField("bind_weixin")
	private String bindWeixin;
	/**
     * 备注名称
     */
	@TableField("memo_name")
	private String memoName;
	@TableField("sex")
	private Integer sex;
	
	
    /**
     * 用户昵称
     */
	@TableField("user_nickname")
	private String userNickname;
    /**
     * 用户头像
     */
	@TableField("user_icon")
	private String userIcon;
    /**
     * 用户二维码
     */
	@TableField("minicode_url")
	private String minicodeUrl;
    /**
     * 最后会员发送消息时间
     */
	@TableField("last_message_time")
	private Long lastMessageTime;
    /**
     *  最后会员发送消息类型：消息类型：0文本text、1图片image、2链接link、3语音消息voice、4视频消息video、5小视频消息shortvideo、6地理位置消息location、9事件event
     */
	@TableField("last_message_type")
	private Integer lastMessageType;
    /**
     * 最后会员发送消息显示内容
     */
	@TableField("last_message_content")
	private String lastMessageContent;
    /**
     * 未读消息数量
     */
	@TableField("not_read_message_count")
	private Integer notReadMessageCount;
    /**
     * 1,2,3
     */
	@TableField("tag_ids")
	private String tagIds;
    /**
     * 状态 -1:删除 0:正常 1:取消关注
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
	/**
	 * 注册来源
	 */
	@TableField("source")
	private String source;

	/**
	 * 店中店openId
	 */
	@TableField("in_shop_open_id")
	private String inShopOpenId;
	/**
	 * 店中店用户id
	 */
	@TableField("in_shop_member_id")
	private Long inShopMemberId;



	public String getInShopOpenId() {
		return inShopOpenId;
	}

	public void setInShopOpenId(String inShopOpenId) {
		this.inShopOpenId = inShopOpenId;
	}

	public Long getInShopMemberId() {
		return inShopMemberId;
	}

	public void setInShopMemberId(Long inShopMemberId) {
		this.inShopMemberId = inShopMemberId;
	}




	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBindPhone() {
		return bindPhone;
	}

	public void setBindPhone(String bindPhone) {
		this.bindPhone = bindPhone;
	}

	public String getBindWeixin() {
		return bindWeixin;
	}

	public void setBindWeixin(String bindWeixin) {
		this.bindWeixin = bindWeixin;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getMinicodeUrl() {
		return minicodeUrl;
	}

	public void setMinicodeUrl(String minicodeUrl) {
		this.minicodeUrl = minicodeUrl;
	}

	public Long getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(Long lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

	public Integer getLastMessageType() {
		return lastMessageType;
	}

	public void setLastMessageType(Integer lastMessageType) {
		this.lastMessageType = lastMessageType;
	}

	public String getLastMessageContent() {
		return lastMessageContent;
	}

	public void setLastMessageContent(String lastMessageContent) {
		this.lastMessageContent = lastMessageContent;
	}

	public Integer getNotReadMessageCount() {
		return notReadMessageCount;
	}

	public void setNotReadMessageCount(Integer notReadMessageCount) {
		this.notReadMessageCount = notReadMessageCount;
	}

	public String getTagIds() {
		return tagIds;
	}

	public void setTagIds(String tagIds) {
		this.tagIds = tagIds;
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

	public String getMemoName() {
		return memoName;
	}

	public void setMemoName(String memoName) {
		this.memoName = memoName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "ShopMember [id=" + id + ", storeId=" + storeId + ", bindPhone=" + bindPhone + ", bindWeixin="
				+ bindWeixin + ", memoName=" + memoName + ", userNickname=" + userNickname + ", userIcon=" + userIcon
				+ ", minicodeUrl=" + minicodeUrl + ", lastMessageTime=" + lastMessageTime + ", lastMessageType="
				+ lastMessageType + ", lastMessageContent=" + lastMessageContent + ", notReadMessageCount="
				+ notReadMessageCount + ", tagIds=" + tagIds + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
