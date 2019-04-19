package com.jiuyuan.entity.newentity;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 全局配置表
 * </p>
 *
 * @author 赵兴林
 * @since 2018-03-22
 */
@TableName("yjj_GlobalSetting")
public class GlobalSettingNew extends Model<GlobalSettingNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 属性名
     */
	private String PropertyName;
    /**
     * 属性值
     */
	private String PropertyValue;
    /**
     * 属性值
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
     * 分组的ID
     */
	private Integer GroupId;
    /**
     * 组名
     */
	private String GroupName;
	private String Description;


	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public String getPropertyName() {
		return PropertyName;
	}

	public void setPropertyName(String PropertyName) {
		this.PropertyName = PropertyName;
	}

	public String getPropertyValue() {
		return PropertyValue;
	}

	public void setPropertyValue(String PropertyValue) {
		this.PropertyValue = PropertyValue;
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

	public Integer getGroupId() {
		return GroupId;
	}

	public void setGroupId(Integer GroupId) {
		this.GroupId = GroupId;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String GroupName) {
		this.GroupName = GroupName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
