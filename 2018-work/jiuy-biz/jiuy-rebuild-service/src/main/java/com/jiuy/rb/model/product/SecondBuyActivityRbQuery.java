package com.jiuy.rb.model.product; 

import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.ShopActivityStatusEnum;
import lombok.Data;

import java.util.List;

/**
 * SecondBuyActivityRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月11日 上午 11:48:35
 * @Copyright 玖远网络 
*/
@Data
public class SecondBuyActivityRbQuery extends SecondBuyActivityRb {
    /**
     * 大于活动结束时间(传入时间戳)
     */
    private Long gtActivityEndTime;

    /**
     * 商品名简称,超过20省略
     */
    private String simpleProductName;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private String createTimeReadable;
    /**
     * 活动开始时间 yyyy-MM-dd HH:mm:ss
     */
    private String activityStartTimeReadable;
    /**
     * 活动结束时间 yyyy-MM-dd HH:mm:ss
     */
    private String activityEndTimeReadable;
    /**
     * 手动结束时间 yyyy-MM-dd HH:mm:ss
     */
    private String activityHandEndTimeReadable;
    /**
     * 商品橱窗图
     */
    private List<String> showcaseImgList;
    /**
     * 活动状态 1待开始，2进行中，3已结束（手工结束、过期结束）
     */
    private Integer activityState;
    /**
     * 倒计时
     * <p>如果活动未开始: 距离活动开始时间</p>
     * <p>如果活动已开始: 距离活动结束时间</p>
     */
    private Long countDown;
    /**
     * 剩余库存
     */
    private Integer remainProductCount;


    /**
     * 格式化返回前端一些可读的数据,比如时间等
     *
     * @param source source
     * @author Charlie
     * @date 2018/7/30 11:35
     */
    public void formatReadable(SecondBuyActivityRb source) {
        this.simpleProductName = Biz.replaceStr (source.getShopProductName (), 20, "...");
        this.createTimeReadable = Biz.safeTimestamp2Str(source.getCreateTime ());
        this.activityStartTimeReadable = Biz.safeTimestamp2Str (source.getActivityStartTime());
        this.activityEndTimeReadable = Biz.safeTimestamp2Str (source.getActivityEndTime());
        this.activityHandEndTimeReadable = Biz.safeTimestamp2Str (source.getActivityHandEndTime());
        this.showcaseImgList = Biz.jsonStrToListObject (source.getShopProductShowcaseImgs (), List.class, String.class);

        int code = stateCode (this);
        //活动状态
        this.activityState = code;
        //倒计时
        if (code == ShopActivityStatusEnum.TERMINATE.getCode ()) {
            //活动结束
            this.countDown = 0L;
        }
        else if (code == ShopActivityStatusEnum.UNDERWAY.getCode ()) {
            //活动进行中
            countDown = getActivityEndTime () - System.currentTimeMillis ();
        }
        else {
            //活动未开始
            this.countDown = getActivityStartTime () - System.currentTimeMillis ();
        }
        countDown = countDown < 0? 0:countDown;

        this.remainProductCount = this.getActivityProductCount () - (this.getOrderedProductCount () == null ? 0 : this.getOrderedProductCount());
    }

    /**
     * 当前活动状态
     *
     * @return 活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
     * @author Charlie
     * @date 2018/7/30 13:47
     */
    public static int stateCode(SecondBuyActivityRb source) {
        if (source.getActivityHandEndTime ()!=null && source.getActivityHandEndTime ()>0) {
            return ShopActivityStatusEnum.TERMINATE.getCode ();
        }
        else if (source.getActivityEndTime () !=null && source.getActivityEndTime ().compareTo (System.currentTimeMillis ())<=0) {
            return ShopActivityStatusEnum.TERMINATE.getCode ();
        }
        else if (source.getActivityStartTime () != null && source.getActivityStartTime ().compareTo (System.currentTimeMillis ()) <= 0) {
            return ShopActivityStatusEnum.UNDERWAY.getCode ();
        }
        else {
            return ShopActivityStatusEnum.WAITING_2_START.getCode ();
        }
    }


}
