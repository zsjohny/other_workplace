package com.store.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 会员收藏表
 * @author QiuYuefan
CREATE TABLE shop_member_favorite (
id bigint(20) NOT NULL AUTO_INCREMENT,
member_id bigint(20) NOT NULL COMMENT ‘会员id’,
related_id bigint(20) DEFAULT ‘0’ COMMENT ‘关联id（根据type进行区分）’,
type tinyint(4) DEFAULT ‘0’ COMMENT ‘类型：0商品’,
status tinyint(4) DEFAULT ‘0’ COMMENT ‘状态：0正常，-1删除’,
create_time bigint(20) DEFAULT ‘0’ COMMENT ‘创建时间’,
update_time bigint(20) DEFAULT ‘0’ COMMENT ‘更新时间’,
PRIMARY KEY (Id),
UNIQUE KEY uk_uid_rid_type (member_id,related_id,type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=’会员收藏表’;
 */
public class MiniappMemberFavorite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7036216284190315795L;

	private Long id;

	/**
	 * 会员Id
	 */
    private Long memberId;

    /**
     * 关联id（根据type进行区分）
     */
    private Long relatedId;
    
    /**
     * 类型：0商品
     */
    private Integer type;
    
    /**
     * 状态：0正常，-1删除
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 更新时间
     */
    private Long updateTime;

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

}