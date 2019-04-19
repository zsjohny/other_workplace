package com.e_commerce.miscroservice.store.entity.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员认证不通过原因预设
 * </p>
 *
 * @author nijin
 * @since 2017-12-14
 */
//@TableName("shop_store_auth_reason")
	@Data
public class ShopStoreAuthReason  {

    private static final long serialVersionUID = 1L;

	//@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 认证审核不通过原因
     */
	//@TableField("no_pass_reason")
	private String noPassReason;
    /**
     * 创建时间
     */
	//@TableField("create_time")
	private Long createTime;
    /**
     * 更新时间
     */
	//@TableField("update_time")
	private Long updateTime;
    /**
     * 拒绝原因类型：0：门店认证1：商品认证
     */
	//@TableField("TYPE")
	private Integer type;
	/**
	 * 权重
	 */
	//@TableField("weight")
	private Integer weight;
	/**
	 * 是否删除
	 */
	//@TableField("is_delete")
	private Integer isDelete;


}
