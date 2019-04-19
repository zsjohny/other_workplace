package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 供应商公告读取记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-21
 */
@TableName("store_placard_read")
public class PlacardRead extends Model<PlacardRead> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 公告ID
     */
	@TableField("placard_id")
	private Long placardId;
    /**
     * 供应商ID
     */
	@TableField("supplier_id")
	private Long supplierId;
    /**
     * 创建时间
     */
	@TableField("read_time")
	private Long readTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlacardId() {
		return placardId;
	}

	public void setPlacardId(Long placardId) {
		this.placardId = placardId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getReadTime() {
		return readTime;
	}

	public void setReadTime(Long readTime) {
		this.readTime = readTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
