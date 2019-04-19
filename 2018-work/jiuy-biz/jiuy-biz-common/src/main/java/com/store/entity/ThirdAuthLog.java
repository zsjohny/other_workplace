package com.store.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 小程序三方授权记录表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-07-04
 */
@TableName("jiuy_third_auth_log")
public class ThirdAuthLog extends Model<ThirdAuthLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * adminid： null表示是通过微信自己的小程序网页处理的， 其他代表管理员Id
     */
	@TableField("admin_id")
	private Long adminId;
    /**
     * 第三方平台appid
     */
	@TableField("third_app_id")
	private String thirdAppId;
    /**
     * unauthorized是取消授权，updateauthorized是更新授权，authorized是授权成功通知
     */
	@TableField("info_type")
	private String infoType;
    /**
     * 公众号或小程序appid
     */
	@TableField("authorizer_app_id")
	private String authorizerAppId;
    /**
     * 授权码（code）
     */
	@TableField("authorization_code")
	private String authorizationCode;
    /**
     * 过期时间
     */
	@TableField("authorization_code_expired_time")
	private Integer authorizationCodeExpiredTime;
    /**
     * 授权创建时间
     */
	@TableField("authorizer_create_time")
	private Long authorizerCreateTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getThirdAppId() {
		return thirdAppId;
	}

	public void setThirdAppId(String thirdAppId) {
		this.thirdAppId = thirdAppId;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getAuthorizerAppId() {
		return authorizerAppId;
	}

	public void setAuthorizerAppId(String authorizerAppId) {
		this.authorizerAppId = authorizerAppId;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public Integer getAuthorizationCodeExpiredTime() {
		return authorizationCodeExpiredTime;
	}

	public void setAuthorizationCodeExpiredTime(Integer authorizationCodeExpiredTime) {
		this.authorizationCodeExpiredTime = authorizationCodeExpiredTime;
	}

	public Long getAuthorizerCreateTime() {
		return authorizerCreateTime;
	}

	public void setAuthorizerCreateTime(Long authorizerCreateTime) {
		this.authorizerCreateTime = authorizerCreateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
