package com.e_commerce.miscroservice.task.job.jobDispose;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.task.DestoryLive;
import com.e_commerce.miscroservice.commons.entity.task.LiveData;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import com.e_commerce.miscroservice.commons.helper.current.ExecutorService;
import com.e_commerce.miscroservice.commons.helper.current.ExecutorTask;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.DateUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.utils.MapTrunPojo;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums.*;

/**
 * 直播处理中心
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 21:17
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherLiveBus {

    private Log logger = Log.getInstance(TaskDispatcherLiveBus.class);

    @Autowired
    @Qualifier("userHashRedisTemplate")
    private HashOperations<String, String, String> userHashRedisTemplate;

    @Autowired
    @Qualifier("userLiveDataObjHashRedisTemplate")
    private ValueOperations<String, LiveData> userLiveDataObjHashRedisTemplate;
    @Autowired
    @Qualifier("strRedisTemplate")
    private RedisTemplate<String, String> strRedisTemplate;

    @Autowired
    private TaskDispatcherLiveTalkBus taskDispatcherLiveTalkBus;

//    普通直播
    private static String LIVE_COMMON_STORE="live:common:%s";
//    平台直播
    private static String LIVE_SPECIAL_PLATFORM_STORE="live:special:platform:%s";
//     经销商直播
    private static String LIVE_SPECIAL_DISTRIBUTOR_STORE="live:special:distributor:%s";
    //平台 默认id
    private static final String PLATFORM_ID = "yjj_platform";

    //用户人数
    private static String LIVE_MEMBER_LIST="live:member:list:%s";
    //    记录当前场次 直播间 总人数
    private static String LIVE_MEMBER_TOTAL="live:member:total:%s";
    private static String LIVE_RECORD_ROOM="live:record:%s";

    /**
     * 查询所有直播
     * @param dataBase
     */
    public void findAll(DataBase dataBase) {
        logger.info("读取所有直播间={}", dataBase);
        String storeId= String.valueOf(dataBase.getStoreId());
        List<String> list = new ArrayList<>();
        LiveData liveData =MapTrunPojo.jsonParseObj(JSONObject.toJSONString(dataBase.getObj()), LiveData.class);

        if (LIVE_COMMON_SHOP.isMe(liveData.getCode())){
            list = findAllCommonLive(storeId);
        }
        if (LIVE_SPECIAL_PLATFORM.isMe(liveData.getCode())){
            list = findAllSpecialPlatformLive();
        }
        if (LIVE_SPECIAL_DISTRIBUTOR.isMe(liveData.getCode())){
            list = findAllSpecialDistributorLive(String.valueOf(dataBase.getDistributorId()));
        }
        dataBase.getObjectCompletableFuture().complete(Response.success(list));
    }

    /**
     * 读取所有普通直播间
     * @param storeId
     * @return
     */
    public List<String> findAllCommonLive(String storeId){
        List<String> list = userHashRedisTemplate.values(String.format(LIVE_COMMON_STORE, storeId));
        return list;
    }

    /**
     * 读取所有平台直播间
     * @return
     */
    public List<String> findAllSpecialPlatformLive(){
        List<String> list = userHashRedisTemplate.values(String.format(LIVE_SPECIAL_PLATFORM_STORE, PLATFORM_ID));
        return list;
    }

    /**
     * 读取所有供应商直播间
     * @param storeId
     * @return
     */
    public List<String> findAllSpecialDistributorLive(String storeId){
        List<String> list = userHashRedisTemplate.values(String.format(LIVE_SPECIAL_DISTRIBUTOR_STORE, storeId));
        return list;
    }

    /**
     * 查询直播间信息
     * @param dataBase
     */
    public LiveData findLiveInformation(DataBase dataBase) {
        logger.info("读取直播间详情={}", dataBase);
        String data = null;
        LiveData liveData =MapTrunPojo.jsonParseObj(JSONObject.toJSONString(dataBase.getObj()), LiveData.class);

        String storeId = String.valueOf(dataBase.getStoreId());
        String roomId = String.valueOf(dataBase.getRoomId());
        if (LIVE_COMMON_SHOP.isMe(liveData.getCode())){
            data = findCommonLiveInformation(storeId,roomId);
        }
        if (LIVE_SPECIAL_PLATFORM.isMe(liveData.getCode())){
            data = findSpecialPlatformLiveInformation(roomId);
        }
        if (LIVE_SPECIAL_DISTRIBUTOR.isMe(liveData.getCode())){
            data = findSpecialDistributorLiveInformation(String.valueOf(dataBase.getDistributorId()),roomId);
        }
        if (data==null){
            logger.info("用户访问直播间不存在,={}",dataBase);
            CompletableFuture<Object> future = dataBase.getObjectCompletableFuture();
            if (future == null) {
                future = new CompletableFuture<>();
            }
            future.complete( Response.errorMsg("直播间不存在"));
        }
        return MapTrunPojo.jsonParseObj(data, LiveData.class);
//        LiveData liveData = MapTrunPojo.jsonParseObj(data, LiveData.class);
//        dataBase.getObjectCompletableFuture().complete( Response.success(liveData));
    }

    /**
     * 读取普通直播间
     * @param storeId
     * @param roomId
     * @return
     */
    public String findCommonLiveInformation(String storeId,String roomId){
        String data = userHashRedisTemplate.get(String.format(LIVE_COMMON_STORE,storeId),String.valueOf(roomId));
        return data;
    }
    /**
     * 读取平台直播间
     * @param roomId
     * @return
     */
    public String findSpecialPlatformLiveInformation(String roomId){
        String data = userHashRedisTemplate.get(String.format(LIVE_SPECIAL_PLATFORM_STORE,PLATFORM_ID),String.valueOf(roomId));
        return data;
    }
    /**
     * 读取经销商直播间
     * @param storeId
     * @param roomId
     * @return
     */
    public String findSpecialDistributorLiveInformation(String storeId,String roomId){
        String data = userHashRedisTemplate.get(String.format(LIVE_SPECIAL_DISTRIBUTOR_STORE,storeId),String.valueOf(roomId));
        return data;
    }



    public void createLivePre(DataBase dataBase){
        logger.info("新建直播间={}", dataBase);
        if (dataBase.getObj()==null){
            logger.warn("新建直播间数据不存在");
            dataBase.getObjectCompletableFuture().complete( Response.errorMsg("新建直播间数据不存在"));
        }
        LiveData tvCreateData =MapTrunPojo.jsonParseObj(JSONObject.toJSONString(dataBase.getObj()), LiveData.class);
        if (tvCreateData==null){
            logger.warn("创建直播间 参数为空");
            dataBase.getObjectCompletableFuture().complete( Response.errorMsg("创建直播间 参数为空"));
        }
        String storeId = String.valueOf(dataBase.getStoreId());
        String roomId = String.valueOf(dataBase.getRoomId());
        if (LIVE_COMMON_SHOP.isMe(tvCreateData.getCode())){
            createCommonLive(storeId,roomId,tvCreateData);
        }
        if (LIVE_SPECIAL_PLATFORM.isMe(tvCreateData.getCode())){
            createSpecialPlatformLive(roomId,tvCreateData);
        }
        if (LIVE_SPECIAL_DISTRIBUTOR.isMe(tvCreateData.getCode())){
            createSpecialDistributorLive(String.valueOf(dataBase.getDistributorId()),roomId,tvCreateData);
        }
        dataBase.getObjectCompletableFuture().complete(Response.success());

    }

    /**
     * 新建 普通直播间
     * @param storeId
     * @param roomId
     * @param tvCreateData
     */
    public void createCommonLive(String storeId,String roomId,LiveData tvCreateData) {

            userHashRedisTemplate.put(String.format(LIVE_COMMON_STORE, storeId), roomId, JSONObject.toJSONString(tvCreateData));
//            List<String> list = userHashRedisTemplate.values(String.format(LIVE_COMMON_STORE, storeId));
//            System.out.println("ssss="+list);


    }

    /**
     * 创建平台直播间
     * @param roomId
     * @param tvCreateData
     */
    public void createSpecialPlatformLive(String roomId,LiveData tvCreateData) {
        userHashRedisTemplate.put(String.format(LIVE_SPECIAL_PLATFORM_STORE, PLATFORM_ID), roomId, JSONObject.toJSONString(tvCreateData));
//            List<String> list = userHashRedisTemplate.values(String.format(LIVE_COMMON_STORE, storeId));
//            System.out.println("ssss="+list);

    }

    /**
     * 创建供应商直播间
     * @param storeId
     * @param roomId
     * @param tvCreateData
     */
    public void createSpecialDistributorLive(String storeId,String roomId,LiveData tvCreateData) {
        userHashRedisTemplate.put(String.format(LIVE_SPECIAL_DISTRIBUTOR_STORE, storeId), roomId, JSONObject.toJSONString(tvCreateData));
//            List<String> list = userHashRedisTemplate.values(String.format(LIVE_COMMON_STORE, storeId));
//            System.out.println("ssss="+list);

    }

    private ExecutorService executorService = new ExecutorService(1, "destoryLive");



    /**
     * 销毁直播间
     * @param dataBase
     */
    public void destroyLive(DataBase dataBase) {
       logger.info("销毁直播间入口={}",dataBase);
       if (dataBase.getRoomId()==null){
           logger.warn("销毁直播间失败参数错误");
           dataBase.getObjectCompletableFuture().complete( Response.errorMsg("销毁直播间失败参数错误"));
           return;
       }
        LiveData preLive =MapTrunPojo.jsonParseObj(JSONObject.toJSONString(dataBase.getObj()), LiveData.class);
        if (dataBase.getStoreId()==null&&LIVE_COMMON_SHOP.isMe(preLive.getCode())){
            logger.warn("销毁直播间失败参数错误");
            dataBase.getObjectCompletableFuture().complete( Response.errorMsg("销毁直播间失败参数错误"));
            return;
        }

        String storeId = String.valueOf(dataBase.getStoreId());
        String roomId = String.valueOf(dataBase.getRoomId());
        String value = null;
        if (LIVE_COMMON_SHOP.isMe(preLive.getCode())){
            value=destroyCommon(storeId,roomId);

        }
        if (LIVE_SPECIAL_PLATFORM.isMe(preLive.getCode())){
            value=destroySpecialPlatform(roomId);
        }
        if (LIVE_SPECIAL_DISTRIBUTOR.isMe(preLive.getCode())){
            value=destroySpecialDistributor(String.valueOf(dataBase.getDistributorId()),roomId);
        }
        logger.info("销毁直播间value={}", value);
        if (value==null){
            dataBase.getObjectCompletableFuture().complete(Response.errorMsg("操作失败"));
            return;
        }
        LiveData liveData =  MapTrunPojo.jsonParseObj(value, LiveData.class);
        Long  totalMember = userHashRedisTemplate.size(String.format(LIVE_MEMBER_TOTAL,roomId));
        if (totalMember!=null&&totalMember>0){
//            userHashRedisTemplate.delete(String.format(LIVE_MEMBER_TOTAL,roomId) );
            userHashRedisTemplate.getOperations().delete(String.format(LIVE_MEMBER_TOTAL,roomId));
        }
        taskDispatcherLiveTalkBus.clearRoomTalk(Long.valueOf(roomId));
        executorService.addTask(new ExecutorTask() {
            @Override
            public void doJob() {
//                Long size = strRedisTemplate.opsForZSet().size(String.format(LIVE_MEMBER_LIST, roomId));
                List<String> list = userHashRedisTemplate.values(String.format(LIVE_MEMBER_LIST, roomId));
//                Set<String> list = strRedisTemplate.opsForZSet().range(String.format(LIVE_MEMBER_LIST, roomId), 0,size);
                logger.info("填充历史数据={}",list);
                //填充历史记录
                userHashRedisTemplate.put(String.format(LIVE_RECORD_ROOM,roomId), TimeUtils.longFormatString(System.currentTimeMillis()), String.valueOf(list));

            }
        });
        //清空直播间在线人数
//        strRedisTemplate.delete(String.format(LIVE_MEMBER_LIST, roomId));
        Long size = userHashRedisTemplate.size(String.format(LIVE_MEMBER_LIST, roomId));
        if (size!=null&&size>0){
//            strRedisTemplate.opsForZSet().remove(String.format(LIVE_MEMBER_LIST, roomId));
            Set<String> set = userHashRedisTemplate.keys(String.format(LIVE_MEMBER_LIST, roomId));
            logger.info("即将被删除的数据={}", set);
            userHashRedisTemplate.delete(String.format(LIVE_MEMBER_LIST, roomId),set.toArray());
        }
        Long minutes = (System.currentTimeMillis()-liveData.getCreateTime())/1000/60;
        DestoryLive destoryLive = new DestoryLive();
        destoryLive.setMinutes(minutes);
        destoryLive.setTotalMember(totalMember);
        destoryLive.setNickName(liveData.getNickName());
        destoryLive.setIcon(liveData.getMainMap());
        destoryLive.setCreate(liveData.getCreateTime());
        destoryLive.setCurrent(System.currentTimeMillis());

        dataBase.getObjectCompletableFuture().complete(Response.success(destoryLive));
    }


    /**
     * 销毁普通直播间
     * @param storeId
     * @param roomId
     */
    public String destroyCommon(String storeId,String roomId){
        //销毁直播间
        String  value = userHashRedisTemplate.get(String.format(LIVE_COMMON_STORE, storeId), roomId);

        userHashRedisTemplate.delete(String.format(LIVE_COMMON_STORE,storeId),String.valueOf(roomId));
        return value;
    }

    /**
     * 销毁平台直播间
     * @param roomId
     */
    public String destroySpecialPlatform(String roomId){
        //销毁直播间
        String  value =userHashRedisTemplate.get(String.format(LIVE_SPECIAL_PLATFORM_STORE, PLATFORM_ID), roomId);
        userHashRedisTemplate.delete(String.format(LIVE_SPECIAL_PLATFORM_STORE,PLATFORM_ID),String.valueOf(roomId));
        return value;
    }

    /**
     * 销毁经销商直播间
     * @param storeId
     * @param roomId
     */
    public String destroySpecialDistributor(String storeId,String roomId){
        //销毁直播间
        String value = userHashRedisTemplate.get(String.format(LIVE_SPECIAL_DISTRIBUTOR_STORE, storeId), roomId);
        userHashRedisTemplate.delete(String.format(LIVE_SPECIAL_DISTRIBUTOR_STORE,storeId),String.valueOf(roomId));
        return value;

    }


}
