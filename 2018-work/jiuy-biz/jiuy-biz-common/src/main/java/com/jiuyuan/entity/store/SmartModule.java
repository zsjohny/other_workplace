package com.jiuyuan.entity.store;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * app门店智能模块
 * </p>
 *
 * @author Charlie(唐静)
 * @since 2018-05-09
 */
@TableName("store_smart_module")
public class SmartModule extends Model<SmartModule> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 门店用户id
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 模块名称
     */
	@TableField("name")
	private String name;
	/**
	 * 模块编码
	 */
	@TableField("code")
	private String code;
    /**
     * 状态{0:隐藏, 1:显示}
     */
	@TableField("switcher")
	private String switcher;
    /**
     * 排序
     */
	@TableField("sort")
	private Integer sort;

	@TableField("upd_time")
	private Long updTime;
	@TableField("create_time")
	private Long createTime;

	//不映射到db  类似@Transient
	@TableField(exist = false)
	private int imgCount;

	public int getImgCount() {
		return imgCount;
	}

	public void setImgCount(int imgCount) {
		this.imgCount = imgCount;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSwitcher() {
		return switcher;
	}

	public void setSwitcher(String switcher) {
		this.switcher = switcher;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Long updTime) {
		this.updTime = updTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
