package com.finace.miscroservice.getway.util;

import com.finace.miscroservice.commons.enums.DeviceEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.ObjectId;
import com.finace.miscroservice.commons.utils.UidUtils;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.PreDestroy;

/**
 * Uid发放
 */
@Configuration
public class UidDistribute {

    @Autowired
    @Qualifier("uidHashRedisTemplate")
    private HashOperations<String, String, Integer> uidHashRedisTemplate;

    private final String ANDROID_UID_REDIS_KEY = "android:uid:redis";
    private final String IOS_UID_REDIS_KEY = "ios:uid:redis";
    private final String H5_UID_REDIS_KEY = "H5:uid:redis";
    private final String MOBILE_UID_REDIS_KEY = "mobile:uid:redis";
    private final String COMPANY_MOBILE_UUID = "ytj:mobile:uuid";


    private Log log = Log.getInstance(UidDistribute.class);

    /**
     * 重置分布式Id
     */
    public void restCursor() {
        //获得当前分配的总数量
        Integer count = uidHashRedisTemplate.get(COMPANY_MOBILE_UUID, MOBILE_UID_REDIS_KEY);
        if (count == null || count <= 0) {
            count = 0;
        }
        ObjectId.setCursor(count);

    }


    /**
     * 分配Uid
     *
     * @param deviceEnum
     */
    @Synchronized
    public String distribute(DeviceEnum deviceEnum) {
        String uid = "";
        if (deviceEnum == null) {
            log.warn("当前没有传设备标识 无法分配Uid");
            return uid;
        }

        log.info("设备={} 开始获取分配Uid", deviceEnum.toVal());

        //重置自增Id
        restCursor();

        uid = UidUtils.encryptUid(deviceEnum.toVal() + ObjectId.get().toHexString());

        //存储设备的分数量
        savePartCount(deviceEnum);

        int currentTotalCount = ObjectId.getCurrentCounter();
        //存储本次的总量
        saveTotalCount(currentTotalCount);
        log.info("设备={} 成功获取Uid 当前总注册量为={}", deviceEnum.toVal(), currentTotalCount);

        return uid;
    }


    /**
     * 销毁时保存当前总注册量
     */
    @PreDestroy
    public void destroy() {
        //重新注册当前总注册量
        int totalCount = ObjectId.getCurrentCounter();
        if (totalCount > 0) {
            saveTotalCount(totalCount);
            log.info("已经成功保存总注册量={}", totalCount);
        }

    }

    /**
     * 存储总量
     *
     * @param count 总量
     */
    private void saveTotalCount(int count) {
        uidHashRedisTemplate.put(COMPANY_MOBILE_UUID, MOBILE_UID_REDIS_KEY, count);
    }


    /**
     * 存储部分的数量
     *
     * @param deviceEnum 设备标识
     */
    private void savePartCount(DeviceEnum deviceEnum) {

        Integer originalCount;
        switch (deviceEnum) {

            case ANDROID:
                originalCount = uidHashRedisTemplate.get(COMPANY_MOBILE_UUID, ANDROID_UID_REDIS_KEY);

                if (originalCount == null || originalCount <= 0) {
                    originalCount = 0;
                }
                //存储
                uidHashRedisTemplate.put(COMPANY_MOBILE_UUID, ANDROID_UID_REDIS_KEY, ++originalCount);

                break;
            case IOS:
                originalCount = uidHashRedisTemplate.get(COMPANY_MOBILE_UUID, IOS_UID_REDIS_KEY);

                if (originalCount == null || originalCount <= 0) {
                    originalCount = 0;
                }
                //存储
                uidHashRedisTemplate.put(COMPANY_MOBILE_UUID, IOS_UID_REDIS_KEY, ++originalCount);

                break;
            case H5:
                originalCount = uidHashRedisTemplate.get(COMPANY_MOBILE_UUID, H5_UID_REDIS_KEY);

                if (originalCount == null || originalCount <= 0) {
                    originalCount = 0;
                }
                //存储
                uidHashRedisTemplate.put(COMPANY_MOBILE_UUID, H5_UID_REDIS_KEY, ++originalCount);

                break;
            default:
                log.warn("暂时没有支持的设备 ,请添加");
        }

    }


}
