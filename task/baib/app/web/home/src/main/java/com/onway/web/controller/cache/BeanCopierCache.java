package com.onway.web.controller.cache;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.cglib.beans.BeanCopier;

import org.apache.log4j.Logger;

import com.onway.baib.common.service.enums.BaibCoreResultCodeEnum;
import com.onway.baib.common.service.exception.BaibCoreException;
import com.onway.platform.common.utils.LogUtil;

/**
 * BeanCopier 对象拷贝器缓存
 * 
 * @author wwf
 * @version $Id: BeanCopierCache.java, v 0.1 2016年9月9日 上午9:42:47 Administrator Exp $
 */
public class BeanCopierCache {

    /**
     * logger
     */
    protected static final Logger               logger           = Logger
                                                                     .getLogger(BeanCopierCache.class);

    /**
     *BeanCopier对象容器
     */
    public static final Map<String, BeanCopier> beanCopierVessel = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 读写锁
     */
    private static ReentrantReadWriteLock       rw               = new ReentrantReadWriteLock();
    /**
     * 读锁
     */
    @SuppressWarnings("unused")
    private static Lock                         readLock         = rw.readLock();
    /**
     * 写锁
     */
    private static Lock                         writeLock        = rw.writeLock();

    /**
     * 获取 对象拷贝器
     *    Class<?> clazz
     * @param request
     * @param info
     * @return
     */
    public static BeanCopier get(Class<?> fromBean, Class<?> toBean,boolean useConverter) {
        String key = fromBean.toString() + toBean.toString();
        if (!beanCopierVessel.containsKey(key)) {
            writeLock.lock();
            try {
                if (!beanCopierVessel.containsKey(key)) {

                    BeanCopier bc = BeanCopier.create(fromBean, toBean, useConverter);
                    if (null == bc) {
                        LogUtil.error(logger, MessageFormat.format(
                            "通过来源对象和目标对象创建BeanCopier异常，fromBean:{0},toBean：{1}", new Object[] {
                                    fromBean.toString(), toBean.toString() }));
                        throw new BaibCoreException(BaibCoreResultCodeEnum.CREATE_BEANCOPIER_ERROR,
                            BaibCoreResultCodeEnum.CREATE_BEANCOPIER_ERROR.getMessage());
                    }
                    beanCopierVessel.put(key, bc);
                }
            } finally {
                writeLock.unlock();
            }
        }
        return beanCopierVessel.get(key);
    }
}
