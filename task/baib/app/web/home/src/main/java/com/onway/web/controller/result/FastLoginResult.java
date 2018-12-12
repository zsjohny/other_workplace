package com.onway.web.controller.result;

/**
 * 快速登录结果集(额外增加是否设置支付密码属性)
 * 
 * @author guangdong.li
 * @version $Id: FastLoginResult.java, v 0.1 2013-9-12 下午5:53:33 WJL Exp $
 */
public class FastLoginResult extends JsonResult {

    public FastLoginResult(boolean bizSucc) {
        super(bizSucc);
    }

    public FastLoginResult(boolean bizSucc, String errCode, String message) {
        super(bizSucc, errCode, message);
    }

    /** serialVersionUID */
    private static final long serialVersionUID = 5198841746337278942L;

    /** 用户id */
    private String            userId;

    /** 用户实名 */
    private String            realName;

    /** 用户证件 */
    private String            certNo;

    /** 第三方卡银行卡绑定状态 */
    private String            baibdState;

    /** 用户状态 */
    private String            userState;

    /** 设置支付密码(1:已设置    0:未设置  ) */
    private boolean           isPayPwd         = false;

    /** 是否交易过 (true: 交易过  false:未交易过-默认)*/
    private boolean           isTrade          = false;

    /** 是否已设置登录密码 (true: 已设置  false: 未设置-默认)*/
    private boolean           isLoginPwd       = false;

    /** 是否已绑定余额银行卡 */
    private boolean           isBindbaibd       = false;
    
    /**用户登录手机号**/
    private String cell;

    /** 冗余微信端token*/
    private String            token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getbaibdState() {
        return baibdState;
    }

    public void setbaibdState(String baibdState) {
        this.baibdState = baibdState;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public void setTrade(boolean isTrade) {
        this.isTrade = isTrade;
    }

    public boolean isBindbaibd() {
        return isBindbaibd;
    }

    public void setBindbaibd(boolean isBindbaibd) {
        this.isBindbaibd = isBindbaibd;
    }

    public boolean isPayPwd() {
        return isPayPwd;
    }

    public void setPayPwd(boolean isPayPwd) {
        this.isPayPwd = isPayPwd;
    }

    public boolean isLoginPwd() {
        return isLoginPwd;
    }

    public void setLoginPwd(boolean isLoginPwd) {
        this.isLoginPwd = isLoginPwd;
    }

    /**
     * Getter method for property <tt>token</tt>.
     * 
     * @return property value of token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter method for property <tt>token</tt>.
     * 
     * @param token value to be assigned to property token
     */
    public void setToken(String token) {
        this.token = token;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

}
