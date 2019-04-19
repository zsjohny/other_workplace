package com.jiuyuan.entity.express;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 运费模板
 * </p>
 *
 * @author Aison
 * @since 2018-04-27
 */
@TableName("yjj_express_model")
public class ExpressModel extends Model<ExpressModel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键:
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 省份行政区划编码
     */
	private String province;
    /**
     * 市行政区划编码
     */
	private String city;
    /**
     * 省份名称
     */
	@TableField("province_name")
	private String provinceName;
    /**
     * 城市名称
     */
	@TableField("city_name")
	private String cityName;
    /**
     * 模板名称
     */
	@TableField("model_name")
	private String modelName;
    /**
     * 计价方式:1是重量计算
     */
	@TableField("calculation_way")
	private Integer calculationWay;
    /**
     * 起计费重量
     */
	@TableField("min_weight")
	private BigDecimal minWeight;
    /**
     * 起计重量费用
     */
	@TableField("min_money")
	private BigDecimal minMoney;
    /**
     * 每增加多少重量
     */
	@TableField("each_group_weight")
	private BigDecimal eachGroupWeight;
    /**
     * 每增加多少重量加多少钱
     */
	@TableField("each_group_money")
	private BigDecimal eachGroupMoney;
    /**
     * 商家id
     */
	@TableField("supplier_id")
	private Long supplierId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Integer getCalculationWay() {
		return calculationWay;
	}

	public void setCalculationWay(Integer calculationWay) {
		this.calculationWay = calculationWay;
	}

	public BigDecimal getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(BigDecimal minWeight) {
		this.minWeight = minWeight;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public BigDecimal getEachGroupWeight() {
		return eachGroupWeight;
	}

	public void setEachGroupWeight(BigDecimal eachGroupWeight) {
		this.eachGroupWeight = eachGroupWeight;
	}

	public BigDecimal getEachGroupMoney() {
		return eachGroupMoney;
	}

	public void setEachGroupMoney(BigDecimal eachGroupMoney) {
		this.eachGroupMoney = eachGroupMoney;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
