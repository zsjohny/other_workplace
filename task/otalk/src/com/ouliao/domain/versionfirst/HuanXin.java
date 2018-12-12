package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

@Table(name = "huanxin")
@Entity
public class HuanXin {
	private Integer HuaXinId;
	private Integer ownerId;
	private String huaXinUUid;
	private String huaXinName;

	private String pass;
	private Date creatTime;
	private int version;

	@Version
	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@GeneratedValue
	@Id
	public Integer getHuaXinId() {
		return HuaXinId;
	}

	public void setHuaXinId(Integer huaXinId) {
		HuaXinId = huaXinId;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@Column(length = 512)
	public String getHuaXinUUid() {
		return huaXinUUid;
	}

	public void setHuaXinUUid(String huaXinUUid) {
		this.huaXinUUid = huaXinUUid;
	}

	public String getHuaXinName() {
		return huaXinName;
	}

	public void setHuaXinName(String huaXinName) {
		this.huaXinName = huaXinName;
	}

	@Column(length = 216)
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

}
