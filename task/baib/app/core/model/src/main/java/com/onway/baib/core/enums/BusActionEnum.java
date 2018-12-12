package com.onway.baib.core.enums;

/**
 * 业务动作流
 * 
 * @author guangdong.li
 * @version $Id: BusActionEnum.java, v 0.1 14 Jan 2016 16:47:52 guangdong.li Exp $
 */
public enum BusActionEnum {

    /** 当前页面 */
    CURRENT_PAGE("CURRENT_PAGE", "0"),

    /** (申购，赎回，充值，提现)订单订购页面 */
    INIT_ORDER("INIT_ORDER", "1"),

    /** 实名页面 */
    REALNAME_IDENTIFY("REALNAME_IDENTIFY", "2"),

    /** 授权/鉴权  */
    AUTHENTICATION("AUTHENTICATION", "3"),

    /** 补全用户信息 */
    SUPPLY_ADDRESS("SUPPLY_ADDRESS", "4"),

    /** 绑卡 */
    BIND_CARD("BIND_CARD", "5"),

    /** 设置密码 */
    SET_PSW("SET_PSW", "6"),

    /** 收银台确认页 */
    CASHIER("CASHIER", "7"),

    /** 订单成功页 */
    ORDER_FINISH("ORDER_FINISH", "8"),

    /** 快捷支付页 */
    QUICK_PAY("QUICK_PAY", "9"), ;
    ;

    private String action;

    private String code;

    /**
     * @param action
     * @param renderDes
     * @param code
     */
    private BusActionEnum(String action, String code) {
        this.action = action;
        this.code = code;
    }

    /**
     * Getter method for property <tt>action</tt>.
     * 
     * @return property value of action
     */
    public String getAction() {
        return action;
    }

    /**
     * Setter method for property <tt>action</tt>.
     * 
     * @param action value to be assigned to property action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property <tt>code</tt>.
     * 
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }
}
