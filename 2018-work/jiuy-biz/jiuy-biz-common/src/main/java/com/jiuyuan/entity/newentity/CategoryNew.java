package com.jiuyuan.entity.newentity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 商品分类表
 * CREATE TABLE `yjj_Category` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `CategoryName` varchar(60) NOT NULL COMMENT '分类名称',
  `ParentId` bigint(20) DEFAULT '0' COMMENT '分类父id，0表示顶级分类',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常，1隐藏',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `CategoryType` tinyint(4) NOT NULL DEFAULT '0' COMMENT '分类类型：0产品分类，1商家分类，2：虚拟分类',
  `Description` varchar(45) DEFAULT NULL,
  `Weight` int(11) DEFAULT NULL COMMENT '分类权重,相同的parentId情况下,根据权重来排序',
  `IconUrl` varchar(1024) DEFAULT NULL COMMENT '分类图标',
  `IconOnUrl` varchar(1024) DEFAULT NULL COMMENT '分类选中图标',
  `CategoryUrl` varchar(256) DEFAULT NULL COMMENT '分类首页URL',
  `IsDiscount` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否优惠 0:否 1：是',
  `ExceedMoney` decimal(10,2) DEFAULT NULL COMMENT '满额立减条件',
  `MinusMoney` decimal(10,2) DEFAULT NULL COMMENT '满额立减数',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8 COMMENT='商品分类表';
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-17
 */
@TableName("yjj_Category")
public class CategoryNew extends Model<CategoryNew> {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 分类名称
     */
	private String CategoryName;
    /**
     * 分类父id，0表示顶级分类
     */
	private Long ParentId;
    /**
     * 状态:-1删除，0正常，1隐藏
     */
	private Integer Status;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
    /**
     * 分类类型：0产品分类，1商家分类，2：虚拟分类
     */
	private Integer CategoryType;
	private String Description;
    /**
     * 分类权重,相同的parentId情况下,根据权重来排序
     */
	private Integer Weight;
    /**
     * 分类图标
     */
	private String IconUrl;
    /**
     * 分类选中图标
     */
	private String IconOnUrl;
    /**
     * 分类首页URL
     */
	private String CategoryUrl;
    /**
     * 是否优惠 0:否 1：是
     */
	private Integer IsDiscount;
    /**
     * 满额立减条件
     */
	private BigDecimal ExceedMoney;
    /**
     * 满额立减数
     */
	private BigDecimal MinusMoney;
	
	/**
     * 分类等级:0:未知;1:一级;2:二级;3:三级; (0,为历史数据,不再维护)
     */
	private Integer categoryLevel;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String CategoryName) {
		this.CategoryName = CategoryName;
	}

	public Long getParentId() {
		return ParentId;
	}

	public void setParentId(Long ParentId) {
		this.ParentId = ParentId;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	public Integer getCategoryType() {
		return CategoryType;
	}

	public void setCategoryType(Integer CategoryType) {
		this.CategoryType = CategoryType;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public Integer getWeight() {
		return Weight;
	}

	public void setWeight(Integer Weight) {
		this.Weight = Weight;
	}

	public String getIconUrl() {
		return IconUrl;
	}

	public void setIconUrl(String IconUrl) {
		this.IconUrl = IconUrl;
	}

	public String getIconOnUrl() {
		return IconOnUrl;
	}

	public void setIconOnUrl(String IconOnUrl) {
		this.IconOnUrl = IconOnUrl;
	}

	public String getCategoryUrl() {
		return CategoryUrl;
	}

	public void setCategoryUrl(String CategoryUrl) {
		this.CategoryUrl = CategoryUrl;
	}

	public Integer getIsDiscount() {
		return IsDiscount;
	}

	public void setIsDiscount(Integer IsDiscount) {
		this.IsDiscount = IsDiscount;
	}

	public BigDecimal getExceedMoney() {
		return ExceedMoney;
	}

	public void setExceedMoney(BigDecimal ExceedMoney) {
		this.ExceedMoney = ExceedMoney;
	}

	public BigDecimal getMinusMoney() {
		return MinusMoney;
	}

	public void setMinusMoney(BigDecimal MinusMoney) {
		this.MinusMoney = MinusMoney;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

	public Integer getCategoryLevel() {
		return categoryLevel;
	}

	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

}