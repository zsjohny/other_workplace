package com.yujj.entity.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/17 6:32
 * @Copyright 玖远网络
 */
public class ValidTimeQueueHelper{

    static Logger logger = LoggerFactory.getLogger(ValidTimeQueueHelper.class);

    /**
     * 创建对象
     * @return com.store.service.YjjMemberService.MemberValidTimeQueue
     * @author Charlie
     * @date 2018/8/16 11:11
     */
    public static ValidTimeQueueHelper instance(YjjMember member) {
        return new ValidTimeQueueHelper (member.getId (), member.getValidTimeQueue (), member.getType (), member.getEndTime ());
    }

    private ValidTimeQueueHelper(Long memberId, String validTimeQueue, Integer memberPackageType, Long endTime) {
        this.memberId = memberId;
        this.validTimeQueue = validTimeQueue;
        this.memberPackageType = memberPackageType;
        this.endTime = endTime;
    }

    private Long memberId;
    /**
     * 有效期
     */
    private String validTimeQueue;
    /**
     * 套餐类型
     */
    private Integer memberPackageType;
    /**
     * 结束时间
     */
    private Long endTime;

    /**
     *
     * @param memberPackageType 套餐类型
     * @param timeUnit 时间单位 1年2月3日4时5分
     * @param value 时间单位
     * @return com.yujj.entity.product.ValidTimeQueueHelper
     * @author Charlie
     * @date 2018/9/26 11:29
     */
    public ValidTimeQueueHelper add(Integer memberPackageType, int timeUnit, Integer value) {
        /*一切以新系统为主, 老系统的业务是, 当前只有APP端免费的会员才能续费, 其他的都不会走这里的逻辑
         * 这里的逻辑只用了一部分
         */
        //加之前先过滤一下有历史数据
        clearExpirationTime ();
        if (endTime == 0) {
            //过期会员
            Calendar calendar = addTime (Calendar.getInstance (), timeUnit, value);
            this.endTime = calendar.getTimeInMillis ();
            validTimeQueue = buildValidTimeQueue (endTime, memberPackageType);
            this.memberPackageType = memberPackageType;
        }
        else {
            //会员续费
            if (this.memberPackageType == 4) {
                //如果是0元续费, 直接覆盖, 不记录以前的状态
                Calendar calendar = addTime (Calendar.getInstance (), timeUnit, value);
                this.endTime = calendar.getTimeInMillis ();
                validTimeQueue = buildValidTimeQueue (endTime, memberPackageType);
                this.memberPackageType = memberPackageType;
                return this;
            }
            //非0元续费
            int start = validTimeQueue.lastIndexOf ("[") + 1;
            int end = validTimeQueue.lastIndexOf ("]");
            String lastEndTimeInfo = validTimeQueue.substring (start, end);
            String[] timeSplit = lastEndTimeInfo.split (":");
            long lastEndTime = Long.parseLong (timeSplit[0]);
            Calendar calendar = Calendar.getInstance ();
            calendar.setTimeInMillis (lastEndTime);
            calendar = addTime (Calendar.getInstance (), timeUnit, value);
            validTimeQueue += buildValidTimeQueue (calendar.getTimeInMillis (), memberPackageType);
        }
        return this;
    }

    /**
     * 追加时间
     *
     * @param timeUnit 时间单位
     * @param value 时间长度
     * @return java.util.Calendar
     * @author Charlie
     * @date 2018/9/26 11:39
     */
    public static Calendar addTime(Calendar calendar, int timeUnit, Integer value) {
        int calendarTimeUnit ;
        switch (timeUnit) {
            case 1 :
                calendarTimeUnit = Calendar.YEAR;break;
            case 2 :
                calendarTimeUnit = Calendar.MONTH;break;
            case 3 :
                calendarTimeUnit = Calendar.DATE;break;
            case 4 :
                calendarTimeUnit = Calendar.HOUR_OF_DAY;break;
            case 5 :
                calendarTimeUnit = Calendar.MINUTE;break;
                default:
                    calendarTimeUnit = Calendar.YEAR;
                    logger.error ("未知的日期配置 timeUnit:{}",timeUnit);
        }
        calendar.add (calendarTimeUnit, value);
        return calendar;
    }


    /**
     * 清除过期时间, 没有则不处理
     *
     * @author Charlie
     * @date 2018/8/16 11:32
     */
    public void clearExpirationTime() {
        if (! isHasDirtyEndTime ()) {
            //没有脏数据
            return;
        }

        //有脏数据待处理
        String expireData = buildValidTimeQueue(this.endTime, this.memberPackageType);
        if (! validTimeQueue.contains (expireData)) {
            logger.error ("理用户会员账号过期时间异常,数据不匹配 memberId[{}].validTimeQueue[{}].endTime[{}].memberPackageType[{}]", memberId, validTimeQueue, endTime, memberId);
            throw new RuntimeException ("处理用户会员账号过期时间异常,数据不匹配 endTime:"+endTime+",validTimeQueue"+validTimeQueue+",memberId:"+memberId);
        }
        validTimeQueue = validTimeQueue.substring (expireData.length (), validTimeQueue.length ());
        if (validTimeQueue.length () > 0) {
            //新的时间
            String next =validTimeQueue .substring (validTimeQueue.indexOf ("[")+1, validTimeQueue.indexOf ("]"));
            String[] val = next.split (":");
            this.endTime = Long.parseLong (val[0]);
            this.memberPackageType = Integer.parseInt (val[1]);
        }
        else {
            //没有续费的服务
            this.endTime = 0L;
        }
    }



    /**
     * 是否有脏数据
     *
     * @return true 是
     * @author Charlie
     * @date 2018/8/16 11:38
     */
    public boolean isHasDirtyEndTime() {
        boolean hasDirtyEndTime = true;
        if (this.endTime > System.currentTimeMillis ()) {
            //没有过期数据
            hasDirtyEndTime = false;
        }
        if (this.endTime == 0) {
            //过期数据已被处理
            hasDirtyEndTime = false;
        }
        return hasDirtyEndTime;
    }


    public String getValidTimeQueue() {
        return validTimeQueue;
    }

    public Integer getMemberPackageType() {
        return memberPackageType;
    }

    public Long getEndTime() {
        return endTime;
    }

    public static String buildValidTimeQueue(Long endTime, Integer memberPackageType) {
        return "[" + endTime + ":" + memberPackageType + "]";
    }
}

