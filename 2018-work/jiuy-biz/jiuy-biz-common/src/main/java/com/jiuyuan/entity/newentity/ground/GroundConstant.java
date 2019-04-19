package com.jiuyuan.entity.newentity.ground;

/**
 * <p>
 * 地推相关公共常量
 * </p>
 */
public class GroundConstant {

    private static final long serialVersionUID = 1L;

//	 奖金类型 0:个人 1:管理       奖金来源类型：0个人、1团体    
	public static final int BONUS_TYPE_ONESELF = 0;//0:个人
	public static final int BONUS_TYPE_TEAM = 1;//1:管理
	
    //奖金类型：1(门店注册奖金)、2（门店激活） 、3（第一阶段门店交易奖金）、4（第二阶段门店交易奖金）、5（第三阶段门店交易奖金）
    public static final int BONUS_TYPE_REGISTER = 1;//1(门店注册奖金)
    public static final int BONUS_TYPE_ACTIVE = 2;//2（门店激活）
    public static final int BONUS_TYPE_FIRST_STAGE = 3;//3（第一阶段门店交易奖金）
    public static final int BONUS_TYPE_SECOND_STAGE = 4;//4（第二阶段门店交易奖金）
    public static final int BONUS_TYPE_THIRD_STAGE = 5;//5（第三阶段门店交易奖金）
    
	// 阶段类型：1(第一阶段)、2(第二阶段)、3第三阶段、4其他阶段
	public static final Integer STAGE_TYPE_FIRST = 1;//1(第一阶段)
    public static final Integer STAGE_TYPE_SECOND = 2;//2(第二阶段)
    public static final Integer STAGE_TYPE_THIRD = 3;//3第三阶段
    public static final Integer STAGE_TYPE_OTHER = 4;//4其他阶段

}
