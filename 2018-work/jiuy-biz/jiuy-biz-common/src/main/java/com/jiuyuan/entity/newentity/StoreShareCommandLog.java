package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 分享口令日志,记录打开者信息
 * </p>
 *
 * @author nijin
 * @since 2018-01-23
 */
@TableName("store_share_command_log")
public class StoreShareCommandLog extends Model<StoreShareCommandLog> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 分享口令Id
     */
	@TableField("share_command_id")
	private Long shareCommandId;
    /**
     * 打开者,游客可为空
     */
	@TableField("open_man")
	private String openMan;
    /**
     * 打开时间
     */
	@TableField("open_time")
	private Long openTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShareCommandId() {
		return shareCommandId;
	}

	public void setShareCommandId(Long shareCommandId) {
		this.shareCommandId = shareCommandId;
	}

	public String getOpenMan() {
		return openMan;
	}

	public void setOpenMan(String openMan) {
		this.openMan = openMan;
	}

	public Long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Long openTime) {
		this.openTime = openTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "StoreShareCommandLog{" +
			"id=" + id +
			", shareCommandId=" + shareCommandId +
			", openMan=" + openMan +
			", openTime=" + openTime +
			"}";
	}
}
