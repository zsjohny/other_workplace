package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 小程序标签导航表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-02-11
 */
@TableName("shop_tag_navigation")
public class ShopTagNavigation extends Model<ShopTagNavigation> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 门店id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 标签id
     */
	@TableField("tag_id")
	private Long tagId;
    /**
     * 导航名称
     */
	@TableField("navigation_name")
	private String navigationName;
    /**
     * 导航图片地址
     */
	@TableField("navigation_image")
	private String navigationImage;
	/**
	 * 状态：0正常 -1删除
	 */
	@TableField("status")
	private Integer status;
	/**
	 * 权重
	 */
	@TableField("weight")
	private Integer weight;
	
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 修改时间
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

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public String getNavigationName() {
		return navigationName;
	}

	public void setNavigationName(String navigationName) {
		this.navigationName = navigationName;
	}

	public String getNavigationImage() {
		return navigationImage;
	}

	public void setNavigationImage(String navigationImage) {
		this.navigationImage = navigationImage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
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
