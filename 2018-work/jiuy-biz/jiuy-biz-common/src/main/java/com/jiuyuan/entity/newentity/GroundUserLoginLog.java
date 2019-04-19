package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户登录记录
 * </p>
 *
 * @author nijin
 * @since 2017-11-11
 */
@TableName("ground_user_login_log")
public class GroundUserLoginLog extends Model<GroundUserLoginLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	private Long userid;
    /**
     * 登录IP
     */
	private String ip;
    /**
     * 客户端类型
     */
	@TableField("client_type")
	private String clientType;
    /**
     * 客户端版本
     */
	@TableField("client_version")
	private String clientVersion;
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

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
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

	@Override
	public String toString() {
		return "GroundUserLoginLog{" +
			"id=" + id +
			", userid=" + userid +
			", ip=" + ip +
			", clientType=" + clientType +
			", clientVersion=" + clientVersion +
			", createTime=" + createTime +
			"}";
	}
}
