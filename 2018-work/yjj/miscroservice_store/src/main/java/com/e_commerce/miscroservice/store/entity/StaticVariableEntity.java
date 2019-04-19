package com.e_commerce.miscroservice.store.entity;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/28 14:07
 * @Copyright 玖远网络
 */
public class StaticVariableEntity {
    /**
     * 默认 页码
     */
    public static Integer DEFAULT_PAGE;

    /**
     * 默认 每页数量
     */
    public static Integer DEFAULT_SIZE;
    //1.仅退款
	public static final int REFUND_TYPE_REFUND = 1;
    //2.退货退款
	public static final int EFUND_TYPE_REFUND_AND_PRODUCT = 2;

    public final static int REFUND_UNDERWAY = 1;

    //platformInterveneState平台介入状态：0未介入、1买家申请平台介入中、2卖家申请平台介入中、3买家申请平台介入结束、4卖家申请平台介入结束
    public static final int PLATFORM_NOT_INTERVENE = 0;//未介入
    public static final int CUSTOMER_PLATFORM_INTERVENE = 1;//1买家申请平台介入中
    public static final int SELLER_PLATFORM_INTERVENE = 2;//2卖家申请平台介入中
    public static final int CLOSE_CUSTOMER_PLATFORM_INTERVENE = 3;//3买家申请平台介入结束
    public static final int CLOSE_SELLER_PLATFORM_INTERVENE = 4;//4卖家申请平台介入结束

}
