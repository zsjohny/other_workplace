package com.jiuyuan.entity.log;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户登陆表
 * </p>
 *
 * @author Aison
 * @since 2018-05-16
 */
@TableName("yjj_user_login_log_new")
public class UserLoginLogNew extends Model<UserLoginLogNew> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键:
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 设备id
     */
	@TableField("device_id")
	private String deviceId;
    /**
     * 设备名称
     */
	@TableField("device_name")
	private String deviceName;
    /**
     * 版本号
     */
	private String version;
    /**
     * 是否在线:1在线 0下线
     */
	private Integer alive;
    /**
     * 离线时间
     */
	@TableField("offline_time")
	private Long offlineTime;
    /**
     * 平台
     */
	private String platform;
    /**
     * 登陆名:默认手机名
     */
	@TableField("user_name")
	private String userName;
    /**
     * cid
     */
	private String cid;
    /**
     * 登陆方式: 1验证码,2微信,3是支付宝
     */
	@TableField("login_way")
	private Integer loginWay;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getAlive() {
		return alive;
	}

	public void setAlive(Integer alive) {
		this.alive = alive;
	}

	public Long getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Long offlineTime) {
		this.offlineTime = offlineTime;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getLoginWay() {
		return loginWay;
	}

	public void setLoginWay(Integer loginWay) {
		this.loginWay = loginWay;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
