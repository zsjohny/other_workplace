package com.jiuy.rb.model.product;

import com.jiuy.base.annotation.FieldName;
import com.jiuy.base.annotation.IgnoreCopy;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.ShopActivityStatusEnum;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * TeamBuyActivityRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 *
 * @author Aison
 * @version V1.0
 * @date 2018年07月11日 上午 11:48:50
 * @Copyright 玖远网络
 */
@Data
public class TeamBuyActivityRbQuery extends TeamBuyActivityRb{

    /**
     * 成团条件类型 人数成团(3.7.9以前版本)
     */
    @IgnoreCopy
    public static final int CONDITION_TYPE_USER = 1;
    /**
     * 成团条件类型  件数成团
     */
    @IgnoreCopy
    public static final int CONDITION_TYPE_PRODUCT = 2;



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
     * 还差团购人数
     */
    private Integer differUserCount;
    /**
     * 还差团购件数
     */
    private Integer differProductCount;
    /**
     * 倒计时
     */
    private Long countDown;


    /**
     * 格式化返回前端一些可读的数据,比如时间等
     *
     * @param source source
     * @author Charlie
     * @date 2018/7/30 11:35
     */
    public void formatReadable(TeamBuyActivityRb source) {
        this.simpleProductName = Biz.replaceStr (source.getShopProductName (), 20, "...");
        this.createTimeReadable = (source.getCreateTime () == null || source.getCreateTime () == 0)? "" : Biz.safeTimestamp2Str (source.getCreateTime ());
        this.activityStartTimeReadable = (source.getActivityStartTime () == null || source.getActivityStartTime () == 0)? "" : Biz.safeTimestamp2Str (source.getActivityStartTime ());
        this.activityEndTimeReadable = (source.getActivityEndTime () == null || source.getActivityEndTime () == 0)? "" : Biz.safeTimestamp2Str (source.getActivityEndTime ());
        this.activityHandEndTimeReadable = (source.getActivityHandEndTime () == null || source.getActivityHandEndTime () == 0)? "" : Biz.safeTimestamp2Str (source.getActivityHandEndTime ());
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
        //差件数,人数
        if (ObjectUtils.nullSafeEquals (this.getConditionType (), CONDITION_TYPE_PRODUCT)) {
            this.differProductCount = this.getMeetProductCount () - this.getOrderedProductCount ();
            this.differProductCount = differProductCount < 0 ? 0 : differProductCount;
            this.differUserCount = 0;
        }
        else {
            this.differProductCount = 0;
            this.differUserCount = this.getUserCount () - this.getActivityMemberCount ();
            this.differUserCount = differUserCount < 0 ? 0 : differUserCount;
        }
    }


    /**
     * 当前活动状态
     *
     * @return 活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
     * @author Charlie
     * @date 2018/7/30 13:47
     */
    public static int stateCode(TeamBuyActivityRb team) {
        if (team.getActivityHandEndTime () != null && team.getActivityHandEndTime () > 0) {
            return ShopActivityStatusEnum.TERMINATE.getCode ();
        }
        else if (team.getActivityEndTime () != null && team.getActivityEndTime ().compareTo (System.currentTimeMillis ()) <= 0) {
            return ShopActivityStatusEnum.TERMINATE.getCode ();
        }
        else if (team.getActivityStartTime () != null && team.getActivityStartTime ().compareTo (System.currentTimeMillis ()) <= 0) {
            return ShopActivityStatusEnum.UNDERWAY.getCode ();
        }
        else {
            return ShopActivityStatusEnum.WAITING_2_START.getCode ();
        }
    }
} 
