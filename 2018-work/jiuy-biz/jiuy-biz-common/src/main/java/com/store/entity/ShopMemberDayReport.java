package com.store.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 会员记录表
 * @author QiuYuefan
CREATE TABLE `shop_member_day_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store_id` bigint(20) DEFAULT NULL COMMENT '商家ID',
  `pv_count` int(11) DEFAULT '0' COMMENT 'PV数',
  `register_count` int(11) DEFAULT '0' COMMENT '注册数',
  `collection_count` int(11) DEFAULT '0' COMMENT '收藏数',
  `create_time` bigint(20) DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `unq_strDate` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员相关信息日报表';

 * <p>
 * 会员相关信息日报表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-10
 */
@TableName("shop_member_day_report")
public class ShopMemberDayReport extends Model<ShopMemberDayReport> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * PV数
     */
	@TableField("pv_count")
	private Integer pvCount;
    /**
     * 注册数
     */
	@TableField("register_count")
	private Integer registerCount;
    /**
     * 收藏数
     */
	@TableField("collection_count")
	private Integer collectionCount;
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

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Integer getPvCount() {
		return pvCount;
	}

	public void setPvCount(Integer pvCount) {
		this.pvCount = pvCount;
	}

	public Integer getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
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

}
