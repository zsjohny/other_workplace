package com.onway.baib.core.model.jsonresult;

import java.util.Date;

public class PostCommentInfo {

    private final static long serialVersionUID = 1L;

    private String            userName;

    /**
     * This property corresponds to db column <tt>ID</tt>.
     */
    private Integer           commandId;

    /**
     * This property corresponds to db column <tt>OBJECT_ID</tt>.
     */
    private String            objectId;

    /**
     * This property corresponds to db column <tt>OTHER_ID</tt>.
     */
    private String            otherId;

    /**
     * This property corresponds to db column <tt>CONTENT</tt>.
     */
    private String            content;

    /**
     * This property corresponds to db column <tt>C_TIME</tt>.
     */
    private Date              CTime;

    /**
     * This property corresponds to db column <tt>GMT_CREATE</tt>.
     */
    private Date              gmtCreate;

    /**
     * This property corresponds to db column <tt>GMT_MODIFID</tt>.
     */
    private Date              gmtModifid;

    /**
     * This property corresponds to db column <tt>MEMO</tt>.
     */
    private String            memo;

    /**
     * This property corresponds to db column <tt>TYPE</tt>.
     */
    private String            type;

    /**
     * This property corresponds to db column <tt>C_DELETE</tt>.
     */
    private String            CDelete;

    /**
     * This property corresponds to db column <tt>AMOUNT</tt>.
     */
    private long              amount;

    /**
     * This property corresponds to db column <tt>PAY_STATUS</tt>.
     */
    private String            payStatus;

    /**
     * This property corresponds to db column <tt>ADOPT</tt>.
     */
    private String            adopt;
    
    private String userType;

    public Integer getCommandId() {
        return commandId;
    }

    public void setCommandId(Integer commandId) {
        this.commandId = commandId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCTime() {
        return CTime;
    }

    public void setCTime(Date cTime) {
        CTime = cTime;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModifid() {
        return gmtModifid;
    }

    public void setGmtModifid(Date gmtModifid) {
        this.gmtModifid = gmtModifid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCDelete() {
        return CDelete;
    }

    public void setCDelete(String cDelete) {
        CDelete = cDelete;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getAdopt() {
        return adopt;
    }

    public void setAdopt(String adopt) {
        this.adopt = adopt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
