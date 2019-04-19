package com.e_commerce.miscroservice.order.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 小程序会员收货地址表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-11
 */
//@TableName("shop_member_delivery_address")
	@Data
public class ShopMemberDeliveryAddress  {



    /**
     * 主键id
     */
	//@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 小程序会员id
     */
	//@TableField("shop_member_id")
	private Long shopMemberId;
	
	//@TableField("store_id")
	private Long storeId;
    /**
     * 联系人姓名
     */
	//@TableField("linkman_name")
	private String linkmanName;
    /**
     * 手机号码
     */
	//@TableField("phone_number")
	private String phoneNumber;
    /**
     * 所在地区
     */
	private String location;
    /**
     * 详细地址
     */
	private String address;
    /**
     * 状态 0：删除，1：正常
     */
	private Integer status;
    /**
     * 最近一次使用1：是 0：否
     */
	//@TableField("last_used_time")
	private Long lastUsedTime;
    /**
     * 创建时间
     */
	//@TableField("create_time")
	private Long createTime;
    /**
     * 修改时间
     */
	//@TableField("update_time")
	private Long updateTime;

	/**
	 * 默认状态
	 */
	private Integer defaultStatus;

}
