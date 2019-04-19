package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 分享口令记录表
 * </p>
 *
 * @author nijin
 * @since 2018-01-22
 */
@TableName("store_share_command_record")
public class StoreShareCommandRecord extends Model<StoreShareCommandRecord> {

    private static final long serialVersionUID = 1L;
    
    public static final long VALID_TIME = 90L*24*60*60*1000;


    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 商品Id
     */
	@TableField("product_id")
	private Long productId;
    /**
     * 分享时间
     */
	@TableField("share_time")
	private Long shareTime;
    /**
     * 分享口令内容
     */
	@TableField("share_command_content")
	private String shareCommandContent;
    /**
     * 分享口令
     */
	@TableField("share_command")
	private String shareCommand;
    /**
     * 分享者ID,可为空
     */
	@TableField("share_man")
	private String shareMan;
    /**
     * 分享有效期
     */
	@TableField("share_deadline")
	private Long shareDeadline;
    /**
     * 打开次数
     */
	@TableField("open_times")
	private Long openTimes;
	/**
	 * 解析次数
	 */
	@TableField("parse_times")
	private Long parseTimes;



	public Long getParseTimes() {
		return parseTimes;
	}

	public void setParseTimes(Long parseTimes) {
		this.parseTimes = parseTimes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getShareTime() {
		return shareTime;
	}

	public void setShareTime(Long shareTime) {
		this.shareTime = shareTime;
	}

	public String getShareCommandContent() {
		return shareCommandContent;
	}

	public void setShareCommandContent(String shareCommandContent) {
		this.shareCommandContent = shareCommandContent;
	}

	public String getShareCommand() {
		return shareCommand;
	}

	public void setShareCommand(String shareCommand) {
		this.shareCommand = shareCommand;
	}

	public String getShareMan() {
		return shareMan;
	}

	public void setShareMan(String shareMan) {
		this.shareMan = shareMan;
	}

	public Long getShareDeadline() {
		return shareDeadline;
	}

	public void setShareDeadline(Long shareDeadline) {
		this.shareDeadline = shareDeadline;
	}

	public Long getOpenTimes() {
		return openTimes;
	}

	public void setOpenTimes(Long openTimes) {
		this.openTimes = openTimes;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "StoreShareCommandRecord{" +
			"id=" + id +
			", productId=" + productId +
			", shareTime=" + shareTime +
			", shareCommandContent=" + shareCommandContent +
			", shareCommand=" + shareCommand +
			", shareMan=" + shareMan +
			", shareDeadline=" + shareDeadline +
			", openTimes=" + openTimes +
			",parseTimes="+parseTimes+
			"}";
	}
}
