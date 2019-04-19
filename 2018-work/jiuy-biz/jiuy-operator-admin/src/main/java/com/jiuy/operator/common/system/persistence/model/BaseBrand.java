package com.jiuy.operator.common.system.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 品牌表
 * </p>
 *
 * @author nijin
 * @since 2017-10-18
 */
@TableName("yjj_Brand")
public class BaseBrand extends Model<BaseBrand> {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌表主键
     */
	@TableId(value="Id", type= IdType.AUTO)
	private Integer Id;
	private Long BrandId;
    /**
     * 品牌名称
     */
	private String BrandName;
    /**
     * 品牌logo
     */
	private String Logo;
    /**
     * 状态:1:禁用,0:正常,-1:删除
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
	private String CnName;
	private String Description;
    /**
     * 权重-排序
     */
	private Integer Weight;
    /**
     * 品牌标识
     */
	private String BrandIdentity;
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
     * 品牌商品款号前缀
     */
	@TableField("cloth_number_prefix")
	private String clothNumberPrefix;


	public Integer getId() {
		return Id;
	}

	public void setId(Integer Id) {
		this.Id = Id;
	}

	public Long getBrandId() {
		return BrandId;
	}

	public void setBrandId(Long BrandId) {
		this.BrandId = BrandId;
	}

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String BrandName) {
		this.BrandName = BrandName;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String Logo) {
		this.Logo = Logo;
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

	public String getCnName() {
		return CnName;
	}

	public void setCnName(String CnName) {
		this.CnName = CnName;
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

	public String getBrandIdentity() {
		return BrandIdentity;
	}

	public void setBrandIdentity(String BrandIdentity) {
		this.BrandIdentity = BrandIdentity;
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

	public String getClothNumberPrefix() {
		return clothNumberPrefix;
	}

	public void setClothNumberPrefix(String clothNumberPrefix) {
		this.clothNumberPrefix = clothNumberPrefix;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

	@Override
	public String toString() {
		return "YjjBrand{" +
			"Id=" + Id +
			", BrandId=" + BrandId +
			", BrandName=" + BrandName +
			", Logo=" + Logo +
			", Status=" + Status +
			", CreateTime=" + CreateTime +
			", UpdateTime=" + UpdateTime +
			", CnName=" + CnName +
			", Description=" + Description +
			", Weight=" + Weight +
			", BrandIdentity=" + BrandIdentity +
			", IsDiscount=" + IsDiscount +
			", ExceedMoney=" + ExceedMoney +
			", MinusMoney=" + MinusMoney +
			", clothNumberPrefix=" + clothNumberPrefix +
			"}";
	}
}
