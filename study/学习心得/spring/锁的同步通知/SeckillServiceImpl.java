package com.test.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.security.Timestamp;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service("seckillService")
public class SeckillServiceImpl implements ISeckillService {
    /**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
    private Lock lock = new ReentrantLock(true);//互斥锁 参数默认false，不公平锁
    @Autowired
    private DynamicQuery dynamicQuery;

    @Override
    @Transactional
    public Result startSeckilLock(long seckillId, long userId) {
        try {
            lock.lock();
            //这里、不清楚为啥、总是会被超卖101、难道锁不起作用、lock是同一个对象
            //------------------因为事物的存在 导致 不会再同一个事物单元
            String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
            Object object =  dynamicQuery.nativeQueryObject(nativeSql, new Object[]{seckillId});
            Long number =  ((Number) object).longValue();
            if(number>0){
                nativeSql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=?";
                dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[]{seckillId});
                SuccessKilled killed = new SuccessKilled();
                killed.setSeckillId(seckillId);
                killed.setUserId(userId);
                killed.setState(Short.parseShort(number+""));
                killed.setCreateTime(new Timestamp(new Date().getTime()));
                dynamicQuery.save(killed);
            }else{
                return Result.error(SeckillStatEnum.END);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }
}
