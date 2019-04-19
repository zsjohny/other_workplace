package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 地推条件规则表
 * 相关bean：UserTimeRule、GroundConditionRule
 * </p>
 * 用户时间阶段规则JSON例：：
{
    "stage1":90,
    "stage2":180,
    "stage3":360
}
说明：stage1:阶段1	value：该阶段指定天数后结束
stage2:阶段2		value：该阶段指定天数后结束
stage3:阶段3		value：该阶段指定天数后结束



 *
 * @author 赵兴林
 * @since 2017-11-14
 */
@TableName("ground_condition_rule")
public class GroundConditionRule extends Model<GroundConditionRule> {

    private static final long serialVersionUID = 1L;

//  规则类型：1(用户时间阶段规则)、2(用户激活条件规则)、3(注册X天后发放奖金)、4地推人员最低提现额
    public static final int USER_TIME_RULE = 1;
   	public static final int USER_ACTIVE_RULE = 2;
    public static final int USER_REG_AFTER_DAY_GRANT_BONUS = 3;
   	public static final int USER_MIN_GET_COST = 4;
//  状态(-1：删除 0：可用  1：禁用)
	public static final int ACTIVE_STATUS_TRUE = 0;
	public static final int ACTIVE_STATUS_DELETE = -1;
	public static final int ACTIVE_STATUS_FALSE = 1;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 规则类型：1(用户时间阶段规则)、2(用户激活条件规则)、3(审核X天后注册奖金入账)、4(地推人员最低提现额)
     */
	@TableField("rule_type")
	private Integer ruleType;
    /**
     * 规则名称
     */
	@TableField("rule_name")
	private String ruleName;
    /**
     * 规则内容JSON，也可非JSON为具体的值
     */
	private String content;
    /**
     * 状态(-1：删除 0：可用  1：禁用)
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
