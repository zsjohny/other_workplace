package com.jiuyuan.entity.common;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author Aison
 * @since 2018-04-25
 */
@TableName("yjj_data_dictionary")
public class DataDictionary extends Model<DataDictionary> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键:
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 编码唯一
     */
	private String code;
    /**
     * 分组编码
     */
	@TableField("group_code")
	private String groupCode;
    /**
     * 值
     */
	private String val;
    /**
     * 中文名称
     */
	private String name;
    /**
     * 描述
     */
	private String comment;
    /**
     * 启用状态:0 禁用 1启用
     */
	private Integer status;
    /**
     * 创建人:
     */
	@TableField("create_user_id")
	private String createUserId;
    /**
     * 创建人姓名:
     */
	@TableField("create_user_name")
	private String createUserName;
    /**
     * 创建时间:
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 最后修改人id:
     */
	@TableField("last_user_id")
	private String lastUserId;
    /**
     * 最后修改人名:
     */
	@TableField("last_user_name")
	private String lastUserName;
    /**
     * 最后修改时间:
     */
	@TableField("last_update_time")
	private Date lastUpdateTime;
	@TableField("parent_id")
	private Long parentId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(String lastUserId) {
		this.lastUserId = lastUserId;
	}

	public String getLastUserName() {
		return lastUserName;
	}

	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
