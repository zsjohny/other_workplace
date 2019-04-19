package com.e_commerce.miscroservice.task.job;

import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.task.job.jobDispose.TaskDispatcherLiveBus;
import com.e_commerce.miscroservice.task.job.jobDispose.TaskDispatcherLiveTalkBus;
import com.e_commerce.miscroservice.task.job.jobDispose.TaskDispatcherLiveUserBehaviorBus;
import com.e_commerce.miscroservice.task.job.jobDispose.TaskDispatcherShopBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;


/**
 * 总处理中心
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 21:17
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherBus {

    private Log logger = Log.getInstance(TaskDispatcherBus.class);

    @Autowired
    @Qualifier("userHashRedisTemplate")
    private HashOperations<String, String, String> userHashRedisTemplate;

    @Autowired
    private TaskDispatcherShopBus taskDispatcherShopBus;
    @Autowired
    private TaskDispatcherLiveBus taskDispatcherLiveBus;
    @Autowired
    private TaskDispatcherLiveTalkBus taskDispatcherLiveTalkBus;
    @Autowired
    private TaskDispatcherLiveUserBehaviorBus taskDispatcherLiveUserBehaviorBus;


    /**
     * Live处理
     * @param dataBase
     */
    public void liveBus(DataBase dataBase) {

        TaskTypeEnums type = TaskTypeEnums.create(dataBase.getCode());
        switch (type) {
            //查找直播间详情
            case LIVE_SHOP_READ:
                dataBase.getObjectCompletableFuture().complete(Response.success(taskDispatcherLiveBus.findLiveInformation(dataBase)));
                break;
            //创建直播间
            case LIVE_SHOP_CREATE:
                taskDispatcherLiveBus.createLivePre(dataBase);
                break;
            //销毁直播间
            case LIVE_SHOP_DELETE:
                taskDispatcherLiveBus.destroyLive(dataBase);
                break;
            //查询直播列表
            case LIVE_READ_ALL_SHOP:
                taskDispatcherLiveBus.findAll(dataBase);
                break;
            //进入直播间
            case LIVE_ROOM_MEMBER_INTO:
                taskDispatcherLiveUserBehaviorBus.intoRoom(dataBase);
                break;
            //退出直播间
            case LIVE_ROOM_MEMBER_OUT:
                taskDispatcherLiveUserBehaviorBus.outRoom(dataBase);
            //当前在线用户列表
            case LIVE_ROOM_MEMBER_LIST:
                taskDispatcherLiveUserBehaviorBus.findMemberList(dataBase);
                break;

            //直播间发言
            case LIVE_TALK_SEND:
                taskDispatcherLiveTalkBus.sendTalk(dataBase);
                break;
            //直播间拉取其他用户发言
            case LIVE_TALK_RECEIVE:
                taskDispatcherLiveTalkBus.receiveTalk(dataBase);
                break;
            default:
                logger.warn("操作类型为空");
                return;
        }


      /*  Integer mType = dataBase.getCode();
        if (mType == null) {
            logger.warn("操作类型为空");
            return;
        }
        if (LIVE_COMMON_SHOP_READ.isMe(mType)||LIVE_SPECIAL_PLATFORM_SHOP_READ.isMe(mType)||LIVE_SPECIAL_DISTRIBUTOR_SHOP_READ.isMe(mType)){
            //查找直播间详情
            taskDispatcherLiveBus.findLiveInformation(dataBase);
        }else if (LIVE_COMMON_SHOP_CREATE.isMe(mType)||LIVE_SPECIAL_PLATFORM_SHOP_CREATE.isMe(mType)||LIVE_SPECIAL_DISTRIBUTOR_SHOP_CREATE.isMe(mType)){
            //创建直播间
            taskDispatcherLiveBus.createLivePre(dataBase);
        }else if (LIVE_COMMON_SHOP_DELETE.isMe(mType)||LIVE_SPECIAL_PLATFORM_SHOP_DELETE.isMe(mType)||LIVE_SPECIAL_DISTRIBUTOR_SHOP_DELETE.isMe(mType)){
            //销毁直播间
            taskDispatcherLiveBus.destroyLive(dataBase);
        }else if (LIVE_READ_ALL_COMMON_SHOP.isMe(mType)||LIVE_READ_ALL_SPECIAL_PLATFORM.isMe(mType)||LIVE_READ_ALL_SPECIAL_DISTRIBUTOR.isMe(mType)){
            //查询直播列表
            taskDispatcherLiveBus.findAll(dataBase);
        }else if (LIVE_ROOM_MEMBER_INTO.isMe(mType)){
            //进入直播间
            taskDispatcherLiveUserBehaviorBus.intoRoom(dataBase);
        }else if (LIVE_ROOM_MEMBER_OUT.isMe(mType)){
//            退出直播间
            taskDispatcherLiveUserBehaviorBus.outRoom(dataBase);
        }else if (LIVE_ROOM_MEMBER_LIST.isMe(mType)){
            //当前在线用户列表
            taskDispatcherLiveUserBehaviorBus.findMemberList(dataBase);
        }
        else {
            logger.warn("没有符合的 处理类型");
        }
*/
    }

    /**
     * shop处理
     * @param dataBase
     */
    public void shopBus(DataBase dataBase) {
        if (dataBase.getCode()!=null&&dataBase.getCode().equals(TaskTypeEnums.TYPE_SHOP_READ.getKey())){
            taskDispatcherShopBus.busSelect(dataBase);

        }else {
            logger.warn("没有符合的 处理类型");
        }

    }

}
