/**
 * 
 */
package com.jiuyuan.entity.brand;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

/**
 * @author LWS
 *
 */
public class BrandHomePage extends BaseMeta<Long> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1824936737238442673L;

    @Override
    public Long getCacheId() {
        return id;
    }
    
    
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }
    public long getTemplateInstanceId() {
        return templateInstanceId;
    }
    public void setTemplateInstanceId(long templateInstanceId) {
        this.templateInstanceId = templateInstanceId;
    }
    public String getPageUrl() {
        return pageUrl;
    }
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
    public int getEditable() {
        return editable;
    }
    public void setEditable(int editable) {
        this.editable = editable;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }



    private long id;
    private long partnerId;
    private long templateInstanceId;
    private String pageUrl;
    private int editable;
    private int status;
    private long createTime;
    private long updateTime;

    
}
