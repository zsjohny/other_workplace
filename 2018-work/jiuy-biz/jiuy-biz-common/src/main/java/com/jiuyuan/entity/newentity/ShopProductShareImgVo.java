package com.jiuyuan.entity.newentity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/7/17 13:46
 * @Copyright 玖远网络
 */
public class ShopProductShareImgVo{
    static Logger logger = LoggerFactory.getLogger (ShopProductShareImgVo.class);
    public static final String SECOND_ACTIVITY = "抢购价";
    public static final String TEAM_ACTIVITY = "团购价";

    /**
     * 创建以实例
     *
     * @param shopProduct 商品
     * @param showImg     分享图片展示的橱窗图
     * @param wxRQcode    微信二维码
     * @return com.jiuyuan.entity.newentity.ShopProductShareImgVo
     * @author Charlie
     * @date 2018/7/17 14:03
     */
    public static ShopProductShareImgVo createInstance(ShopProduct shopProduct, String showImg, String wxRQcode) {
        ShopProductShareImgVo vo = new ShopProductShareImgVo ();
        vo.shopProduct = shopProduct;
        vo.showImg = showImg;
        vo.wxRQcode = wxRQcode;
        vo.hasActivity = false;
        vo.originalPrice = shopProduct.getPrice () == null ? 0 : shopProduct.getPrice ();
        if (StringUtils.isNotBlank (shopProduct.getName ())) {
            vo.productName = shopProduct.getName ();
        }
        return vo;
    }


    /**
     * 展示的图片
     */
    private String showImg;
    /**
     * 微信二维码
     */
    private String wxRQcode;
    /**
     * 商品实体类
     */
    private ShopProduct shopProduct;
    /**
     * 是否参加活动
     */
    private boolean hasActivity;
    /**
     * 抢购活动结束时间 或 团购活动说明
     */
    private String activityDescription = "";
    /**
     * 商品名称
     */
    private String productName = "";
    /**
     * 活动类型 0:普通商品, 1:限时抢购, 2:拼团活动
     */
    private Integer activityTag;
    /**
     * 原本的价格,即不参与活动的价格
     */
    private Double originalPrice;
    /**
     * 活动价格
     */
    private Double activityPrice;


    public String getShowImg() {
        return showImg;
    }

    public String getWxRQcode() {
        return wxRQcode;
    }

    public ShopProduct getShopProduct() {
        return shopProduct;
    }

    public boolean isHasActivity() {
        return hasActivity;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public String getProductName() {
        return productName;
    }

    /**
     * 活动标签 抢购价,团购价
     */
    public String getActivityTag() {
        if (activityTag == 1) {
            return SECOND_ACTIVITY;
        }
        if (activityTag == 2) {
            return TEAM_ACTIVITY;
        }
        return "";
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public Double getActivityPrice() {
        return activityPrice;
    }

    public String getActivityPriceStr() {
        return new DecimalFormat ("0.00").format (activityPrice);
    }


    public String getOriginalPriceStr() {
        return new DecimalFormat ("0.00").format (originalPrice);
    }

    /**
     * 设置秒杀活动信息
     *
     * @param secondBuyActivity 秒杀活动 nullable
     * @return com.jiuyuan.entity.newentity.ShopProductShareImgVo
     * @author Charlie
     * @date 2018/7/17 14:21
     */
    public ShopProductShareImgVo setActivity(SecondBuyActivity secondBuyActivity) {
        if (secondBuyActivity != null) {
            if (ObjectUtils.nullSafeEquals (secondBuyActivity.getShopProductId (), shopProduct.getId ())) {
                this.hasActivity = true;
                this.activityTag = 1;
                this.activityPrice = secondBuyActivity.getActivityPrice () == null ? 0 : secondBuyActivity.getActivityPrice ();
                Long endTime = secondBuyActivity.getActivityEndTime ();
                String endDate = new SimpleDateFormat ("yyyy/MM/dd HH:mm").format (new Date (endTime));
                this.activityDescription = "截止" + endDate;
            }
            else {
                logger.warn ("分享合成图片:设置秒杀活动信息失败 secondBuyActivityId:"+secondBuyActivity.getId ()+",shopProductId:"+shopProduct.getId ());
            }
        }
        return this;
    }


    /**
     * 设置团购活动信息
     *
     * @param teamBuyActivity 团购活动 nullable
     * @return com.jiuyuan.entity.newentity.ShopProductShareImgVo
     * @author Charlie
     * @date 2018/7/17 14:21
     */
    public ShopProductShareImgVo setActivity(TeamBuyActivity teamBuyActivity) {
        if (teamBuyActivity != null) {
            if (ObjectUtils.nullSafeEquals (teamBuyActivity.getShopProductId (), shopProduct.getId ())) {
                this.hasActivity = true;
                this.activityTag = 2;
                if (StringUtils.isNotBlank (teamBuyActivity.getActivityTitle ())) {
                    this.activityDescription = teamBuyActivity.getActivityTitle ();
                }
                this.activityPrice = teamBuyActivity.getActivityPrice () == null ? 0 : teamBuyActivity.getActivityPrice ();
            }
            else {
                logger.warn ("分享合成图片:设置团购活动信息失败 teamBuyActivityId:"+teamBuyActivity.getId ()+",shopProductId:"+shopProduct.getId ());
            }
        }
        return this;
    }


    @Override
    public String toString() {
        return "showImg='" + showImg + '\'' +
                ", wxRQcode='" + wxRQcode + '\'' +
                ", hasActivity=" + hasActivity +
                ", activityDescription='" + activityDescription + '\'' +
                ", productName='" + productName + '\'' +
                ", activityTag=" + activityTag +
                ", originalPrice=" + originalPrice +
                ", activityPrice=" + activityPrice;
    }

    public int createCode() {
        return this.toString ().hashCode ();
    }

}
