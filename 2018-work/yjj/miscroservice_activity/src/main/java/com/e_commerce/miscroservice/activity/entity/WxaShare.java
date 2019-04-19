package com.e_commerce.miscroservice.activity.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 分享关系表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 05:13:44
 */
@Data
@Table("yjj_wxa_share")
public class WxaShare{
	@Id
	private Long id;
 
	/**
	 * 分享者
	 */
	@Column(value = "source_user", commit = "分享者", length = 20, defaultVal = "0")
	private Long sourceUser;

    /**
     * 渠道商用户id
     */
	@Column(value = "channel_user_id", commit = "渠道商用户id", length = 20, defaultVal = "0")
	private Long channelUserId;

	/**
	 * 被邀请者
	 */
	@Column(value = "target_user", commit = "被邀请者", length = 20, isNUll = false)
	private Long targetUser;
 
	/**
	 * 微信id
	 */
	@Column(value = "wx_id", commit = "微信id", length = 64, defaultVal = "")
	private String wxId;
 
	/**
	 * 微信名称
	 */
	@Column(value = "wx_nickname", commit = "微信名称", length = 64, defaultVal = "")
	private String wxNickname;
 
	/**
	 * 微信头像
	 */
	@Column(value = "wx_header_portrait", commit = "微信头像", length = 255, defaultVal = "")
	private String wxHeaderPortrait;
 
	/**
	 * 创建时间
	 */
	@Column(value = "create_time", commit = "创建时间",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Date createTime;
 
	/**
	 * 1活动分享(预留),2 商品分享,3优惠券分享(预留),4分享小程序,5渠道商分享
	 */
	@Column(value = "share_type", commit = "邀请类型:1活动分享(预留),2 商品分享,3优惠券分享(预留),4分享小程序", isNUll = false)
	private Integer shareType;


	/**
	 * 粉丝类型:-1历史(玖币)粉丝, 0普通粉丝, 1有效粉丝(获得分享收益)
	 * <p>当前的有效粉丝: 是被邀请的新用户, 且通过后台判断(性别女), 为有效粉丝</p>
	 */
	@Column( value = "fans_type", commit = "粉丝类型:-1历史(玖币)粉丝, 0普通粉丝, 1有效粉丝(获得分享收益)", defaultVal = "1", length = 4)
	private Integer fansType;



 }