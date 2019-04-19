package com.e_commerce.miscroservice.task.job.jobDispose;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.task.entity.ShopInData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 店中店处理中心
 * @author hyf
 * @version V1.0
 * @date 2018/12/10 21:16
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherShopBus {
    private Log logger = Log.getInstance(TaskDispatcherShopBus.class);

    @Autowired
    @Qualifier("hashRedisTemplate")
    private  RedisTemplate  hashRedisTemplate;

    private static String BUS_SHOP_ID_MEMBER="bus:shop:%s:%s";

    public void busSelect(DataBase dataBase){
        logger.info("处理中心");
        Map<String,String> map = hashRedisTemplate.opsForHash().entries(String.format(BUS_SHOP_ID_MEMBER,String.valueOf(dataBase.getId()),dataBase.getMemberId()));
        if (map==null){
            logger.info("用户访问记录不存在");
            dataBase.getObjectCompletableFuture().complete( Response.success());
        }
        Object[] values = map.values().toArray();
        Object[] keys = map.keySet().toArray();
        keys =bubbleSort(values,keys);
        dataBase.getObjectCompletableFuture().complete( Response.success(keys));
    }

//    从大到小排序
    public static Object[] bubbleSort(Object[] numbers,Object[] keys)
    {
        Object temp = null;
//        Object tempKey = null;
        int size = numbers.length;
        for(int i = 0 ; i < size-1; i ++)
        {
            for(int j = 0 ;j < size-1-i ; j++)
            {
                Object objCurrent = numbers[j];
                Object objNext = numbers[j+1];
                if (objCurrent instanceof ShopInData){
                    ShopInData current = (ShopInData)objCurrent;
                    ShopInData next = (ShopInData)objNext;
                    if( Long.parseLong(String.valueOf(current.getCurrentTime()))< Long.parseLong(String.valueOf(next.getCurrentTime())))  //交换两数位置
                    {
                        temp = numbers[j];
                        numbers[j] = numbers[j+1];
                        numbers[j+1] = temp;
                        //交换位置
//                    tempKey=keys[j];
//                    keys[j]=keys[j+1];
//                    keys[j+1] = tempKey;
                    }
                }

            }
        }
        return numbers;
    }


    public void busUpdate(DataBase msg)
    {
//        if (msg.getObj() instanceof ShopInData){
//        ShopMember map = (ShopMember) msg.getObj();
//            map.get("");
            logger.info("类型匹配成功={}",msg.getObj());
            ShopInData shopInData = (ShopInData)msg.getObj();
            shopInData.setCurrentTime(System.currentTimeMillis());
            hashRedisTemplate.opsForHash().put(String.format(BUS_SHOP_ID_MEMBER,msg.getId(),msg.getMemberId()),String.valueOf(msg.getRoomId()), JSONObject.toJSONString(shopInData));

//        }
        msg.getObjectCompletableFuture().complete( Response.success());

    }
}
