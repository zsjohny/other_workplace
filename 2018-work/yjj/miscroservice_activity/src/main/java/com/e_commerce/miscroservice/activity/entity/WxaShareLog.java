package com.e_commerce.miscroservice.activity.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 分享记录表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 05:37:31
 */
@Data
@Table("yjj_wxa_share_log")
public class WxaShareLog {
 
	/**
	 * 主键
	 */
	@Id
	private Long id;
 
	/**
	 * 分享者id
	 */
	@Column(value = "member_id",commit = "分享者id",length = 20)
	private Long memberId;
 
	/**
	 * 商品/活动id
	 */  
	@Column(value = "target_id", commit = "商品/活动id",length = 20)
	private Long targetId;

	@Column(value = "be_shared_member_id", length = 20, commit = "被分享者id", defaultVal = "0")
	private Long beSharedMemberId;
 
	/**
	 * 分享类型:2 商品分享,1活动分享,3优惠券分享,4小程序分享,10下级粉丝分享
	 */  
	@Column(value = "share_type", commit = "分享类型:2 商品分享,1活动分享,3优惠券分享,4小程序分享,10下级粉丝分享", length = 4)
	private Integer shareType;  
 
	/**
	 * 描述
	 */  
	@Column(value = "description", commit = "描述", length = 500)
	private String description;

	/**
	 * 0没有收益,1有收益(受每日收益次数限制),2有收益(不受收益次数限制)
	 */
	@Column(value = "earnings_type", commit = "收益类型", length = 4, defaultVal = "1")
	private Integer earningsType;



	/**
	 * 创建时间
	 */
	@Column(value = "create_time", commit = "创建时间",dateGeneStrategy = DbHandler.DateGeneStrategy.CREATE)
	private Timestamp createTime;


 }