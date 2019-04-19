package com.jiuy.rb.service.common;

public interface ICacheService {

    public Object getCommon(String groupKey, String key);

    public long incrCommon(String groupKey, String key, long size);





    // ==> Charlie ******************************************************************************

    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    Boolean setSimple(Object key, Object value);


    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @param second 存活时间
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    Boolean setSimple(Object key, Object value, int second);



    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @param second 存活时间
     * @return 1 if the key was set 0 if the key was not set, -1 if failed
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    Long setSimpleIfNoExist(Object key, Object value, int second);



    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @return 1 if the key was set 0 if the key was not set, 失败:null
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    Long setSimpleIfNoExist(Object key, Object value);



    /**
     * 通过key获取值
     *
     * @param key key
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/2 15:53
     */
    String get(String key);



    /**
     * 减法
     *
     * @param key key
     * @param number number
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/8/2 15:57
     */
    Long decr(String key, long number);



    /**
     * 加法
     *
     * @param key key
     * @param number number
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/8/2 15:57
     */
    Long incr(String key, long number);



    /**
     * 存入一个复合对象
     *
     * @param key key
     * @param value value
     * @param second 存活时间
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/2 16:25
     */
    Boolean setObject(String key, Object value, int second);

    // ****************************************************************************** <== Charlie
}
