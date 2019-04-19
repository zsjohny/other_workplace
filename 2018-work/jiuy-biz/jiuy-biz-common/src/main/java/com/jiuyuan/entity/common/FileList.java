package com.jiuyuan.entity.common;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author Aison
 * @since 2018-04-27
 */
@TableName("yjj_file_list")
public class FileList extends Model<FileList> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键:
     */
	private Long id;
    /**
     * 文件地址:
     */
	private String url;
    /**
     * 文件位置:1 oss 2  fastdfs
     */
	private Integer position;
    /**
     * 文件用途: 1商品橱窗文件，2商品
     */
	@TableField("img_type")
	private Integer imgType;
    /**
     * 文件id
     */
	@TableField("file_id")
	private String fileId;
    /**
     * 文件名称
     */
	@TableField("file_name")
	private String fileName;
    /**
     * 上传时间:
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 上传人id:
     */
	@TableField("update_user_id")
	private String updateUserId;
    /**
     * 上传人名字:
     */
	@TableField("update_user_name")
	private String updateUserName;
    /**
     * 最后修改时间:
     */
	@TableField("last_update_time")
	private Date lastUpdateTime;
    /**
     * 最后修改人id:
     */
	@TableField("last_update_user_id")
	private String lastUpdateUserId;
    /**
     * 最后修改人名:
     */
	@TableField("last_update_user_name")
	private String lastUpdateUserName;
    /**
     * 文件code
     */
	@TableField("file_code")
	private String fileCode;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getImgType() {
		return imgType;
	}

	public void setImgType(Integer imgType) {
		this.imgType = imgType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	public String getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	public void setLastUpdateUserName(String lastUpdateUserName) {
		this.lastUpdateUserName = lastUpdateUserName;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
