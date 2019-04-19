package com.store.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 属性值记录表
 * @author QiuYuefan
 *CREATE TABLE `shop_property_value_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `property_value_id` BIGINT(20) NOT NULL COMMENT '属性值Id',
  `admin_id` BIGINT(20) NOT NULL COMMENT '操作员Id 1:超级管理员',
  `type` TINYINT(4) NOT NULL COMMENT '1:创建 2:删除 3:编辑 4：排序',
  `content` VARCHAR(1024) NOT NULL COMMENT '创建、编辑：json对象，其他：文字',
  `cid` VARCHAR(50) DEFAULT NULL COMMENT '设备Id',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT '客户端ip',
  `platform` TINYINT(4) DEFAULT NULL COMMENT '客户端平台 0:pc 1:android 2:iphone',
  `version` VARCHAR(50) DEFAULT NULL COMMENT '客户端版本',
  `net` TINYINT(4) DEFAULT NULL COMMENT '客户端网络 0:wifi 1:2g 2:3g 4:4g', 
  `create_time` BIGINT(20) DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`Id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='属性值记录表';

 * <p>
 * 属性值记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-10
 */
@TableName("shop_property_value_log")
public class ShopPropertyValueLog extends Model<ShopPropertyValueLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 属性值Id
     */
	@TableField("property_value_id")
	private Long propertyValueId;
    /**
     * 操作员Id 1:超级管理员
     */
	@TableField("admin_id")
	private Long adminId;
    /**
     * 1:创建 2:删除 3:编辑 4：排序
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

	public Long getPropertyValueId() {
		return propertyValueId;
	}

	public void setPropertyValueId(Long propertyValueId) {
		this.propertyValueId = propertyValueId;
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
