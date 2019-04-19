package com.jiuyuan.entity.newentity;

/**
 * 地推条件规则--用户时间阶段规则
 * 
 * 相关bean：UserTimeRule、GroundConditionRule
 * @author 赵兴林
 * @since 2017-11-14
 */
public class UserTimeRule{

	
    /**
     * 条件名称：stage1:阶段1	value：该阶段指定天数后结束
     */
	private int stage1;
    /**
     *  stage2:阶段2		value：该阶段指定天数后结束
     */
	private int stage2;
    /**
     * stage3:阶段3		value：该阶段指定天数后结束
     */
	private int stage3;
	
	public UserTimeRule(int stage1,int stage2,int stage3){
		this.stage1 = stage1;
		this.stage2 = stage2;
		this.stage3 = stage3;
	}

	public int getStage1() {
		return stage1;
	}

	public void setStage1(int stage1) {
		this.stage1 = stage1;
	}

	public int getStage2() {
		return stage2;
	}

	public void setStage2(int stage2) {
		this.stage2 = stage2;
	}

	public int getStage3() {
		return stage3;
	}

	public void setStage3(int stage3) {
		this.stage3 = stage3;
	}
	
	
}
