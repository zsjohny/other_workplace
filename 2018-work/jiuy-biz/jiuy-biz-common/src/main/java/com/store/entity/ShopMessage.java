package com.store.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 客服消息
 * CREATE TABLE `shop_Message` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `AdminId` bigint(20) NOT NULL COMMENT '客服Id',
  `MemberId` bigint(20) NOT NULL COMMENT '会员Id',
  `Type` tinyint(4) DEFAULT '0' COMMENT '0:客服聊天 1:群发',
  `Message` varchar(256) NOT NULL COMMENT '内容 格式待定',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `UpdateTime` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客服聊天记录表';
 * @author Administrator
 *https://mapperhelper.github.io/docs/2.use/

 * <p>
 * 客服聊天记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-10
 */
@TableName("shop_message")
public class ShopMessage extends Model<ShopMessage> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
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
     * 会员Id
     */
	@TableField("member_id")
	private Long memberId;
    /**
     * 会员名称
     */
	@TableField("member_name")
	private String memberName;
    /**
     * 会员头像
     */
	@TableField("member_headimg")
	private String memberHeadimg;
    /**
     * 发送类型：0客服单发会员、1会员单发客服、2客服群发会员
     */
	@TableField("send_type")
	private Integer sendType;
    /**
     * 消息类型：0文本text、1图片image、2链接link、3语音消息voice、4视频消息video、5小视频消息shortvideo、6地理位置消息location、9事件event
     */
	@TableField("message_type")
	private Integer messageType;
    /**
     * 消息类型：0文本text、1图片image、2链接link、3语音消息voice、4视频消息video、5小视频消息shortvideo、6地理位置消息location、9事件event
     */
	@TableField("message_type_code")
	private String messageTypeCode;
    /**
     * 文本消息内容
     */
	@TableField("text_content")
	private String textContent;
    /**
     * 图片消息url
     */
	@TableField("image_pic_url")
	private String imagePicUrl;
    /**
     * 图片消息媒体Id
     */
	@TableField("image_media_id")
	private String imageMediaId;
    /**
     * 链接消息标题
     */
	@TableField("link_title")
	private String linkTitle;
    /**
     * 链接消息描述
     */
	@TableField("link_description")
	private String linkDescription;
    /**
     * 链接消息url
     */
	@TableField("link_url")
	private String linkUrl;
    /**
     * 语音消息媒体Id
     */
	@TableField("voice_media_id")
	private String voiceMediaId;
    /**
     * 语音消息语音格式，如amr，speex等
     */
	@TableField("voice_format")
	private String voiceFormat;
    /**
     * 视频消息媒体Id
     */
	@TableField("video_media_id")
	private String videoMediaId;
    /**
     * 视频消息缩略图的媒体id
     */
	@TableField("video_thumb_media_id")
	private String videoThumbMediaId;
    /**
     * 小视频消息媒体Id
     */
	@TableField("shortvideo_media_id")
	private String shortvideoMediaId;
    /**
     * 小视频消息缩略图的媒体id
     */
	@TableField("shortvideo_thumb_media_id")
	private String shortvideoThumbMediaId;
    /**
     * 地理位置消息维度
     */
	@TableField("location_x")
	private String locationX;
    /**
     * 地理位置消息经度
     */
	@TableField("location_y")
	private String locationY;
    /**
     * 地理位置消息大小
     */
	@TableField("location_scale")
	private String locationScale;
    /**
     * 地理位置消息信息
     */
	@TableField("location_label")
	private String locationLabel;
    /**
     * 发送结果状态：0（成功）、1（失败）
     */
	@TableField("send_State")
	private Integer sendState;
    /**
     * 微信发送返回JSON
     */
	@TableField("weixin_return_json")
	private String weixinReturnJson;
    /**
     * 微信消息ID
     */
	@TableField("weixin_msg_id")
	private String weixinMsgId;
    /**
     * 阅读状态:0未读，1已读
     */
	@TableField("read_state")
	private Integer readState;
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
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;


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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberHeadimg() {
		return memberHeadimg;
	}

	public void setMemberHeadimg(String memberHeadimg) {
		this.memberHeadimg = memberHeadimg;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getImagePicUrl() {
		return imagePicUrl;
	}

	public void setImagePicUrl(String imagePicUrl) {
		this.imagePicUrl = imagePicUrl;
	}

	public String getImageMediaId() {
		return imageMediaId;
	}

	public void setImageMediaId(String imageMediaId) {
		this.imageMediaId = imageMediaId;
	}

	public String getLinkTitle() {
		return linkTitle;
	}

	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}

	public String getLinkDescription() {
		return linkDescription;
	}

	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getVoiceMediaId() {
		return voiceMediaId;
	}

	public void setVoiceMediaId(String voiceMediaId) {
		this.voiceMediaId = voiceMediaId;
	}

	public String getVoiceFormat() {
		return voiceFormat;
	}

	public void setVoiceFormat(String voiceFormat) {
		this.voiceFormat = voiceFormat;
	}

	public String getVideoMediaId() {
		return videoMediaId;
	}

	public void setVideoMediaId(String videoMediaId) {
		this.videoMediaId = videoMediaId;
	}

	public String getVideoThumbMediaId() {
		return videoThumbMediaId;
	}

	public void setVideoThumbMediaId(String videoThumbMediaId) {
		this.videoThumbMediaId = videoThumbMediaId;
	}

	public String getShortvideoMediaId() {
		return shortvideoMediaId;
	}

	public void setShortvideoMediaId(String shortvideoMediaId) {
		this.shortvideoMediaId = shortvideoMediaId;
	}

	public String getShortvideoThumbMediaId() {
		return shortvideoThumbMediaId;
	}

	public void setShortvideoThumbMediaId(String shortvideoThumbMediaId) {
		this.shortvideoThumbMediaId = shortvideoThumbMediaId;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getLocationScale() {
		return locationScale;
	}

	public void setLocationScale(String locationScale) {
		this.locationScale = locationScale;
	}

	public String getLocationLabel() {
		return locationLabel;
	}

	public void setLocationLabel(String locationLabel) {
		this.locationLabel = locationLabel;
	}

	public Integer getSendState() {
		return sendState;
	}

	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}

	public String getWeixinReturnJson() {
		return weixinReturnJson;
	}

	public void setWeixinReturnJson(String weixinReturnJson) {
		this.weixinReturnJson = weixinReturnJson;
	}

	public String getWeixinMsgId() {
		return weixinMsgId;
	}

	public void setWeixinMsgId(String weixinMsgId) {
		this.weixinMsgId = weixinMsgId;
	}

	public Integer getReadState() {
		return readState;
	}

	public void setReadState(Integer readState) {
		this.readState = readState;
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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
