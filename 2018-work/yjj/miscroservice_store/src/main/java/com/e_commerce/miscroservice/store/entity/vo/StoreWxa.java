package com.e_commerce.miscroservice.store.entity.vo;


import java.io.Serializable;

/**
 * <p>
 * 门店小程序信息记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-07-05
 */
//@TableName("jiuy_store_wxa")
public class StoreWxa {

    private static final long serialVersionUID = 1L;

	//@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商家ID
     */
	//@TableField("store_id")
	private Long storeId;
    /**
     * 授权方APPID
     */
	//@TableField("app_id")
	private String appId;
    /**
     * 授权方昵称
     */
	//@TableField("nick_name")
	private String nickName;
    /**
     * 授权方头像
     */
	//@TableField("head_img")
	private String headImg;
    /**
     * 授权方公众号的原始IDgh_eb5e3a772040
     */
	//@TableField("user_name")
	private String userName;
    /**
     * 授权方所设置的微信号，可能为空
     */
	private String alias;
    /**
     * 小程序二维码图片的URL
     */
	//@TableField("qrcode_url")
	private String qrcodeUrl;
    /**
     * 公众号的主体名称，例：腾讯计算机系统有限公司
     */
	//@TableField("principal_name")
	private String principalName;
    /**
     * 签名描述
     */
	private String signature;
	/**
     * idc
     */
	private String idc;
	
	/**
     * 刷新token
     */
	//@TableField("refresh_token")
	private String refreshToken;
	
	/**
     * 商户号
     */
	//@TableField("mch_id")
	private String mchId;
	
	/**  
  	商户秘钥
     */
	//@TableField("pay_key")
	private String payKey;
	
	
    /**
     * 小程序二维码图片的URL
     */
	//@TableField("authorizer_info_json")
	private String authorizerInfoJson;
    /**
     * 创建时间
     */
	//@TableField("create_time")
	private Long createTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getIdc() {
		return idc;
	}

	public void setIdc(String idc) {
		this.idc = idc;
	}

	public String getAuthorizerInfoJson() {
		return authorizerInfoJson;
	}

	public void setAuthorizerInfoJson(String authorizerInfoJson) {
		this.authorizerInfoJson = authorizerInfoJson;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}


	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getPayKey() {
		return payKey;
	}

	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}

}
