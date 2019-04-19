package com.e_commerce.miscroservice.task.job.jobDispose;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.entity.task.MemberAudienceData;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.MapTrunPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 直播间用户行为直播处理中心
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 21:17
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherLiveUserBehaviorBus {

    private Log logger = Log.getInstance(TaskDispatcherLiveUserBehaviorBus.class);
    @Autowired
    @Qualifier("userHashRedisTemplate")
    private HashOperations<String, String, String> userHashRedisTemplate;

    @Autowired
    @Qualifier("defaultRedisTemplate")
    private RedisTemplate<String, String> defaultRedisTemplate;
    @Autowired
    @Qualifier("strRedisTemplate")
    private RedisTemplate<String, String> strRedisTemplate;

//    记录用户当前进入的直播间
    private static String LIVE_MEMBER="live:member:%s";
    //    记录当前场次 直播间 在线人数
    private static String LIVE_MEMBER_LIST="live:member:list:%s";
//    记录当前场次 直播间 总人数
    private static String LIVE_MEMBER_TOTAL="live:member:total:%s";

    /**
     * 用户进入直播间
     * @param dataBase
     */
    public void intoRoom(DataBase dataBase) {
        logger.info("插入直播间人数+1，data={}",dataBase);
        //当前数量
//        Long size = strRedisTemplate.opsForZSet().size(String.format(LIVE_MEMBER_LIST,dataBase.getRoomId()));
//        strRedisTemplate.opsForZSet().add(String.format(LIVE_MEMBER_LIST,dataBase.getRoomId()),JSONObject.toJSONString(dataBase.getObj()), size+1);
        userHashRedisTemplate.put(String.format(LIVE_MEMBER_LIST,dataBase.getRoomId()),String.valueOf(dataBase.getMemberId()),JSONObject.toJSONString(dataBase.getObj()));
        //        记录 进入直播间
        userHashRedisTemplate.put(String.format(LIVE_MEMBER_TOTAL,dataBase.getRoomId()), String.valueOf(dataBase.getStoreId()), String.valueOf(dataBase.getMemberId()));
        //存储 用户当前访问
        String currentIntoRoom =  defaultRedisTemplate.opsForValue().get(String.format(LIVE_MEMBER,dataBase.getMemberId()));
        if(currentIntoRoom!=null){
            logger.info("当前进入的房间={}",currentIntoRoom);
            defaultRedisTemplate.opsForValue().set(String.format(LIVE_MEMBER,dataBase.getMemberId()), String.valueOf(dataBase.getRoomId()));
            //上一个直播间
            dataBase.setRoomId(Long.valueOf(currentIntoRoom));
            outRoom(dataBase);
        }
        dataBase.getObjectCompletableFuture().complete(Response.success());

    }


    /**
     * 退出直播间
     * @param dataBase
     */
    public void outRoom(DataBase dataBase) {
        logger.info("退出直播间={}", dataBase);
        //删除当前
        strRedisTemplate.opsForZSet().remove(String.format(LIVE_MEMBER_LIST,dataBase.getRoomId()),JSONObject.toJSONString(dataBase.getObj()));
        dataBase.getObjectCompletableFuture().complete(Response.success());
    }

    /**
     * 当前直播在线列表
     * @param dataBase
     */
    public void findMemberList(DataBase dataBase) {
        logger.info("当前直播在线列表={}", dataBase);
        Long size = strRedisTemplate.opsForZSet().size(String.format(LIVE_MEMBER_LIST,dataBase.getRoomId()));
        Set<String> list  = strRedisTemplate.opsForZSet().range(String.format(LIVE_MEMBER_LIST,dataBase.getRoomId()), dataBase.getPageNumber(),dataBase.getPageSize());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", size);
        jsonObject.put("list", list);
        dataBase.getObjectCompletableFuture().complete(Response.success(jsonObject));

    }

}
