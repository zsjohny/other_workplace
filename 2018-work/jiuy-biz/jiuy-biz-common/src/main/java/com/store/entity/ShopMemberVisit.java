package com.store.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 会员访问表
 * @author QiuYuefan
CREATE TABLE shop_member_visit (
id bigint(20) NOT NULL AUTO_INCREMENT,
member_id bigint(20) DEFAULT ‘0’ COMMENT ‘会员id’,
related_id bigint(20) DEFAULT ‘0’ COMMENT ‘关联id（根据type进行区分）’,
type tinyint(4) DEFAULT ‘0’ COMMENT ‘类型：0商品’,
create_time bigint(20) DEFAULT ‘0’ COMMENT ‘创建时间’,
PRIMARY KEY (id),
UNIQUE KEY uk_uid_rid_type (member_id,related_id,type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=’会员访问表’;

 * <p>
 * 会员访问表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-10
 */
@TableName("shop_member_visit")
public class ShopMemberVisit extends Model<ShopMemberVisit> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 会员id
     */
	@TableField("member_id")
	private Long memberId;
    /**
     * 关联id（根据type进行区分）
     */
	@TableField("related_id")
	private Long relatedId;
    /**
     * 类型：0商品
     */
	private Integer type;
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
    /**
     * 小程序appId
     */
	@TableField("app_id")
	private String appId;
    /**
     * 会员浏览次数
     */
	private Integer count;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
