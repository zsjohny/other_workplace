package com.store.entity.coupon;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 优惠券模板记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-08-14
 */
@TableName("shop_coupon_template_log")
public class ShopCouponTemplateLog extends Model<ShopCouponTemplateLog> {

    private static final long serialVersionUID = 1L;
//    1:创建 2:删除 3:停止发放
    public static final int type_set = 1;
    public static final int type_del = 2;
    public static final int type_stop = 3;
	

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 优惠券模板Id
     */
	@TableField("coupon_template_id")
	private Long couponTemplateId;
    /**
     * 操作员Id 1:超级管理员
     */
	@TableField("admin_id")
	private Long adminId;
    /**
     * 1:创建 2:删除 3:停止发放
     */
	private Integer type;
    /**
     * 创建、编辑：json对象，其他：文字
     */
	private String content;
    /**
     * 设备Id
     */
	private String cid;
    /**
     * 客户端ip
     */
	private String ip;
    /**
     * 客户端平台 0:pc 1:android 2:iphone
     */
	private Integer platform;
    /**
     * 客户端版本
     */
	private String version;
    /**
     * 客户端网络 0:wifi 1:2g 2:3g 4:4g
     */
	private Integer net;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getCouponTemplateId() {
		return couponTemplateId;
	}

	public void setCouponTemplateId(Long couponTemplateId) {
		this.couponTemplateId = couponTemplateId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getNet() {
		return net;
	}

	public void setNet(Integer net) {
		this.net = net;
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

}
