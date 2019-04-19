package com.jiuyuan.entity.common;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 行政区划表
 * </p>
 *
 * @author Aison
 * @since 2018-04-28
 */
@TableName("yjj_area")
public class Area extends Model<Area> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 省份编码
     */
	@TableField("province_code")
	private String provinceCode;
    /**
     * 省份名称
     */
	@TableField("province_name")
	private String provinceName;
    /**
     * 城市编码
     */
	@TableField("city_code")
	private String cityCode;
    /**
     * 城市名称
     */
	@TableField("city_name")
	private String cityName;
    /**
     * 县编码
     */
	@TableField("county_code")
	private String countyCode;
    /**
     * 县名称
     */
	@TableField("county_name")
	private String countyName;
    /**
     * 分组
     */
	@TableField("group_code")
	private String groupCode;
    /**
     * 分组名称
     */
	@TableField("group_name")
	private String groupName;

	/**
	 * 简称
	 */
	@TableField("short_name")
	private String shortName;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
