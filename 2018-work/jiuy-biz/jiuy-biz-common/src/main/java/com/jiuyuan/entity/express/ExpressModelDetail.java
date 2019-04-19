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
 * 运费模板详情
 * </p>
 *
 * @author Aison
 * @since 2018-05-02
 */
@TableName("yjj_express_model_detail")
public class ExpressModelDetail extends Model<ExpressModelDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键:
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 目标省份
     */
	@TableField("target_province")
	private String targetProvince;
    /**
     * 市行政区划编码
     */
	@TableField("target_city")
	private String targetCity;
    /**
     * 省份名称
     */
	@TableField("target_province_name")
	private String targetProvinceName;
    /**
     * 城市名称
     */
	@TableField("target_city_name")
	private String targetCityName;
    /**
     * 模板id
     */
	@TableField("model_id")
	private Long modelId;
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
	private String group;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTargetProvince() {
		return targetProvince;
	}

	public void setTargetProvince(String targetProvince) {
		this.targetProvince = targetProvince;
	}

	public String getTargetCity() {
		return targetCity;
	}

	public void setTargetCity(String targetCity) {
		this.targetCity = targetCity;
	}

	public String getTargetProvinceName() {
		return targetProvinceName;
	}

	public void setTargetProvinceName(String targetProvinceName) {
		this.targetProvinceName = targetProvinceName;
	}

	public String getTargetCityName() {
		return targetCityName;
	}

	public void setTargetCityName(String targetCityName) {
		this.targetCityName = targetCityName;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
