package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 快递公司管理
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-25
 */
@TableName("yjj_ExpressSupplier")
public class ExpressSupplier extends Model<ExpressSupplier> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 快递供应商中文名
     */
	private String CnName;
    /**
     * 快递供应商英文名
     */
	private String EngName;
    /**
     * 网上查询快递信息，快递信息链接前缀 
     */
	private String QueryLink;
    /**
     * 状态 -1：删除，0：正常
     */
	private Integer Status;
	private Long CreateTime;
	private Long UpdateTime;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getCnName() {
		return CnName;
	}

	public void setCnName(String CnName) {
		this.CnName = CnName;
	}

	public String getEngName() {
		return EngName;
	}

	public void setEngName(String EngName) {
		this.EngName = EngName;
	}

	public String getQueryLink() {
		return QueryLink;
	}

	public void setQueryLink(String QueryLink) {
		this.QueryLink = QueryLink;
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

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
