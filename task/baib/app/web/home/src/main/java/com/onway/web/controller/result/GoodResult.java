package com.onway.web.controller.result;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.onway.common.lang.Money;

public class GoodResult {
    private static final long serialVersionUID = 741231858441822688L;
    private int id;
    private String code;
    private String categoryId;
    private String name;
    private String imageAUrl;
    private String imageBUrl;
    private String imageCUrl;
    private String imageDUrl;
    private Money originalPrice;
    private Money discountPrice;
    private Date gmtCreate;
    private JSONObject labelJson;
    
    private JSONObject parameterJson;
    
    
    private  int tatolNum;
    private int orderNum;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageAUrl() {
        return imageAUrl;
    }
    public void setImageAUrl(String imageAUrl) {
        this.imageAUrl = imageAUrl;
    }
    public String getImageBUrl() {
        return imageBUrl;
    }
    public void setImageBUrl(String imageBUrl) {
        this.imageBUrl = imageBUrl;
    }
    public String getImageCUrl() {
        return imageCUrl;
    }
    public void setImageCUrl(String imageCUrl) {
        this.imageCUrl = imageCUrl;
    }
    public String getImageDUrl() {
        return imageDUrl;
    }
    public void setImageDUrl(String imageDUrl) {
        this.imageDUrl = imageDUrl;
    }
    public Money getOriginalPrice() {
        return originalPrice;
    }
    public void setOriginalPrice(Money originalPrice) {
        this.originalPrice = originalPrice;
    }
    public Money getDiscountPrice() {
        return discountPrice;
    }
    public void setDiscountPrice(Money discountPrice) {
        this.discountPrice = discountPrice;
    }
    public int getTatolNum() {
        return tatolNum;
    }
    public void setTatolNum(int tatolNum) {
        this.tatolNum = tatolNum;
    }
    public int getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
    public JSONObject getLabelJson() {
        return labelJson;
    }
    public void setLabelJson(JSONObject labelJson) {
        this.labelJson = labelJson;
    }
    public JSONObject getParameterJson() {
        return parameterJson;
    }
    public void setParameterJson(JSONObject parameterJson) {
        this.parameterJson = parameterJson;
    }
    public Date getGmtCreate() {
        return gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
    @Override
    public String toString() {
        return "GoodsInfo [id=" + id + ", code=" + code + ", categoryId=" + categoryId + ", name="
               + name + ", imageAUrl=" + imageAUrl + ", imageBUrl=" + imageBUrl + ", imageCUrl="
               + imageCUrl + ", imageDUrl=" + imageDUrl + ", originalPrice=" + originalPrice
               + ", discountPrice=" + discountPrice + ", gmtCreate=" + gmtCreate + ", labelJson="
               + labelJson + ", parameterJson=" + parameterJson + ", tatolNum=" + tatolNum
               + ", orderNum=" + orderNum + "]";
    }
         
    
}
