package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-13
 */
@TableName("yjj_Tag")
public class TagNew extends Model<TagNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 标签名
     */
	private String Name;
    /**
     * 显示优先级，越大越靠前
     */
	private Integer Priority;
    /**
     * 所属标签组id，-1为自身是标签组
     */
	private Long GroupId;
    /**
     * 描述
     */
	private String Description;
    /**
     * 该标签组内包含标签的个数，仅当groupId为-1下有效
     */
	private Integer Count;
    /**
     * -1：删除 0：正常
     */
	private Integer Status;
	private Long CreateTime;
	private Long UpdateTime;
    /**
     * 推荐时间0:不推荐
     */
	private Long topTime;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Integer getPriority() {
		return Priority;
	}

	public void setPriority(Integer Priority) {
		this.Priority = Priority;
	}

	public Long getGroupId() {
		return GroupId;
	}

	public void setGroupId(Long GroupId) {
		this.GroupId = GroupId;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public Integer getCount() {
		return Count;
	}

	public void setCount(Integer Count) {
		this.Count = Count;
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

	public Long getTopTime() {
		return topTime;
	}

	public void setTopTime(Long topTime) {
		this.topTime = topTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
