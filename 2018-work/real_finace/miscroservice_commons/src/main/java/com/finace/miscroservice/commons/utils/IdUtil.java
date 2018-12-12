package com.finace.miscroservice.commons.utils;

import com.finace.miscroservice.commons.log.Log;

/**
 * 获取Id的工具类
 */
public class IdUtil {

    private IdUtil() {

    }

    private static final ThreadLocal<Integer> idCache = new ThreadLocal<>();


    private static Log logger = Log.getInstance(IdUtil.class);


    public static void setId(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("所传Id不符合规范 Id={}", id);
            return;
        }
        idCache.set(id);

    }


    public static Integer getId() {

        Integer id = idCache.get();
        if (id == null) {
            logger.warn("当前线程={} 获取不到Id,请检查是否设置", Thread.currentThread().getName());
        } else {
            idCache.remove();
        }

        return id;
    }

//
//    public static void main(String[] args) {
//        int a = 0;
//
//        for (int i = 0; i < 4; i++) {
//
//            int finalI = i;
//            new Thread(() -> {
//                idCache.set(finalI);
//                LockSupport.parkNanos(2000);
//                System.out.println(idCache.get());
//            })
//                    .start();
//
//        }
//
//    }


}


