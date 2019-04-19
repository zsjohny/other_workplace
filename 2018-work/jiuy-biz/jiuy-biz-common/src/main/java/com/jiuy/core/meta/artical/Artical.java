package com.jiuy.core.meta.artical;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

public class Artical extends BaseMeta<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	
	private String title;
	
	private String content;
	
	private long weight;
	
	private int status;
	
	private long updateTime;

	private long createTime;
	
	private long aRCategoryId;
	
	private long pageView;
	
	private String abstracts;
	
	private String promotionImg;
	
	private String interfaceTitle;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public long getaRCategoryId() {
		return aRCategoryId;
	}

	public void setaRCategoryId(long aRCategoryId) {
		this.aRCategoryId = aRCategoryId;
	}

	public long getPageView() {
		return pageView;
	}

	public void setPageView(long pageView) {
		this.pageView = pageView;
	}
	
	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getPromotionImg() {
		return promotionImg;
	}

	public void setPromotionImg(String promotionImg) {
		this.promotionImg = promotionImg;
	}
	
	public String getInterfaceTitle() {
		return interfaceTitle;
	}

	public void setEscapeContent(String content) {
		this.content = StringEscapeUtils.unescapeHtml4(content);
	}
	
	@JsonIgnore
	public String getEscapeContent() {
		return StringEscapeUtils.escapeHtml4(content);
	}
	
	public void setInterfaceTitle(String interfaceTitle) {
		this.interfaceTitle = interfaceTitle;
	}

	@Override
	public Long getCacheId() {
		return id;
	}

    public String getUpdateTimeToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(updateTime));
    }

}
