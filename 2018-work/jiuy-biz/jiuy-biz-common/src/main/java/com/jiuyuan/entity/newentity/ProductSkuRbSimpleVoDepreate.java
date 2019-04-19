package com.jiuyuan.entity.newentity;



/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/8 22:09
 * @Copyright 玖远网络
 */
public class ProductSkuRbSimpleVoDepreate{

    /**
     * skuId
     */
    private Long id;

    /**
     * 库存
     */
    private Integer remainCount;

    /**
     * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     */
    private Integer status;

    /**
     * 颜色ID
     */
    private Long colorId;

    /**
     * 尺码ID
     */
    private Long sizeId;

    /**
     * 颜色名称
     */
    private String colorName;

    /**
     * 尺码名称
     */
    private String sizeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
}
