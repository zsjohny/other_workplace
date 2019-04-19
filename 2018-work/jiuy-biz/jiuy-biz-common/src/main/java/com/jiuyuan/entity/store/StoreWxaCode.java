package com.jiuyuan.entity.store;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 门店小程序代码信息表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-07-21
 */
@TableName("jiuy_store_wxa_code")
public class StoreWxaCode extends Model<StoreWxaCode> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 门店小程序ID
     */
	@TableField("wxa_id")
	private Long wxaId;
    /**
     * 正式版模板号
     */
	@TableField("online_template")
	private String onlineTemplate;
    /**
     * 正式版版本号
     */
	@TableField("online_version")
	private String onlineVersion;
    /**
     * 正式版发布时间
     */
	@TableField("online_release_time")
	private Long onlineReleaseTime;
    /**
     * 体验版模板号
     */
	@TableField("test_template")
	private String testTemplate;
	   /**
     * 体验版版本号
     */
	@TableField("test_version")
	private String testVersion;
	   /**
     * 体验版小程序描述
     */
	@TableField("test_desc")
	private String testDesc;
	
    /**
     * 体验版上传时间
     */
	@TableField("test_upload_time")
	private Long testUploadTime;
    /**
     * 体验版二维码URL
     */
	@TableField("test_qrcode_url")
	private String testQrcodeUrl;
    /**
     * 体验者微信号集合，英文逗号分隔
     */
	@TableField("test_weiXinNums")
	private String testWeiXinNums;
    /**
     * 提交审核时间
     */
	@TableField("submit_audit_time")
	private Long submitAuditTime;
    /**
     * 提交审核时间
     */
	@TableField("submit_audit_admin_id")
	private Long submitAuditAdminId;
    /**
     * 提交审核模板号
     */
	@TableField("audit_template")
	private String auditTemplate;
    /**
     * 提交审核版本号
     */
	@TableField("audit_version")
	private String auditVersion;
    /**
     * 审核状态：0未提交、1审核中、2审核通过、3审核不通过
     */
	@TableField("audit_state")
	private Integer auditState;
    /**
     * 审核反馈时间
     */
	@TableField("audit_back_time")
	private Long auditBackTime;
    /**
     * 审核反馈信息
     */
	@TableField("audit_back_result_msg")
	private String auditBackResultMsg;
    /**
     * 审核反馈JSON数据
     */
	@TableField("audit_back_result_json")
	private String auditBackResultJson;
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

	public Long getWxaId() {
		return wxaId;
	}

	public void setWxaId(Long wxaId) {
		this.wxaId = wxaId;
	}

	public String getOnlineTemplate() {
		return onlineTemplate;
	}

	public void setOnlineTemplate(String onlineTemplate) {
		this.onlineTemplate = onlineTemplate;
	}

	public String getOnlineVersion() {
		return onlineVersion;
	}

	public void setOnlineVersion(String onlineVersion) {
		this.onlineVersion = onlineVersion;
	}

	public Long getOnlineReleaseTime() {
		return onlineReleaseTime;
	}

	public void setOnlineReleaseTime(Long onlineReleaseTime) {
		this.onlineReleaseTime = onlineReleaseTime;
	}

	public String getTestTemplate() {
		return testTemplate;
	}

	public void setTestTemplate(String testTemplate) {
		this.testTemplate = testTemplate;
	}

	public String getTestVersion() {
		return testVersion;
	}

	public void setTestVersion(String testVersion) {
		this.testVersion = testVersion;
	}

	public Long getTestUploadTime() {
		return testUploadTime;
	}

	public void setTestUploadTime(Long testUploadTime) {
		this.testUploadTime = testUploadTime;
	}

	public String getTestQrcodeUrl() {
		return testQrcodeUrl;
	}

	public void setTestQrcodeUrl(String testQrcodeUrl) {
		this.testQrcodeUrl = testQrcodeUrl;
	}

	public String getTestWeiXinNums() {
		return testWeiXinNums;
	}

	public void setTestWeiXinNums(String testWeiXinNums) {
		this.testWeiXinNums = testWeiXinNums;
	}

	public Long getSubmitAuditTime() {
		return submitAuditTime;
	}

	public void setSubmitAuditTime(Long submitAuditTime) {
		this.submitAuditTime = submitAuditTime;
	}

	public Long getSubmitAuditAdminId() {
		return submitAuditAdminId;
	}

	public void setSubmitAuditAdminId(Long submitAuditAdminId) {
		this.submitAuditAdminId = submitAuditAdminId;
	}

	public String getAuditTemplate() {
		return auditTemplate;
	}

	public void setAuditTemplate(String auditTemplate) {
		this.auditTemplate = auditTemplate;
	}

	public String getAuditVersion() {
		return auditVersion;
	}

	public void setAuditVersion(String auditVersion) {
		this.auditVersion = auditVersion;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Long getAuditBackTime() {
		return auditBackTime;
	}

	public void setAuditBackTime(Long auditBackTime) {
		this.auditBackTime = auditBackTime;
	}

	public String getAuditBackResultMsg() {
		return auditBackResultMsg;
	}

	public void setAuditBackResultMsg(String auditBackResultMsg) {
		this.auditBackResultMsg = auditBackResultMsg;
	}

	public String getAuditBackResultJson() {
		return auditBackResultJson;
	}

	public void setAuditBackResultJson(String auditBackResultJson) {
		this.auditBackResultJson = auditBackResultJson;
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

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

}
