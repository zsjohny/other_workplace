package com.jiuyuan.service.common.area;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Jedis Cache 工具类
 *
 * @author Charlie(唐静)
 * @date 18/06/22
 */
public class JedisUtils{

    private static Logger logger = LoggerFactory.getLogger (JedisUtils.class);
    private static final String REDIS_PROPERTIES = "redis.properties";

    private static String HOST;
    private static int PORT;
    private static String AUTH;
    private static int MAX_ACTIVE;
    private static int MAX_IDLE;
    private static int MAX_WAIT;
    private static int TIMEOUT;
    private static boolean TEST_ON_BORROW;
    private static JedisPool jedisPool;


    /**
     * jedis操作返回码
     */
    private static final String OK = "OK";


    /**
     * 初始化Redis连接池
     */
    static {
        try {
            logger.info ("加载redis配置信息...");
            initRedisConfig ();
            logger.info ("加载redis配置信息成功!!!");
            logger.info ("初始化redis连接池");
            initPool ();
            logger.info ("初始化redis连接池成功!!!");
        } catch (Exception e) {
            logger.error ("初始化Redis出错，" + e);
        }
    }


    /**
     * 根据 redis.properties 初始化redis的配置信息
     *
     * @return java.util.Properties
     * @author Charlie(唐静)
     * @date 2018/6/22 23:43
     */
    private static void initRedisConfig() {
        InputStream in = null;
        try {
            // 获取配置信息
            ClassPathResource resource = new ClassPathResource (REDIS_PROPERTIES);
            in = resource.getInputStream ();
            Properties config = new Properties ();
            config.load (in);
            // 初始化配置
            doInitRedisConfig (config);
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            try {
                if (in != null) {
                    in.close ();
                }
            } catch (IOException ioe) {
                // ignore
            }
        }
    }


    /**
     * 初始化redis配置
     *
     * @param config 配置文件
     * @author Charlie(唐静)
     * @date 2018/6/23 0:01
     */
    private static void doInitRedisConfig(Properties config) {
        HOST = config.getProperty ("redis.servers");
        AUTH = config.getProperty ("redis.pwd");
        PORT = Integer.parseInt (config.getProperty ("redis.port"));
        MAX_IDLE = Integer.parseInt (config.getProperty ("redis.maxIdle"));
        MAX_ACTIVE = Integer.parseInt (config.getProperty ("redis.maxActive"));
        MAX_WAIT = Integer.parseInt (config.getProperty ("redis.maxWait"));
        TEST_ON_BORROW = Boolean.parseBoolean (config.getProperty ("redis.testOnBorrow"));
        TIMEOUT = Integer.parseInt (config.getProperty ("redis.timeout"));

        logger.info ("初始化redis配置信息 servers:" + HOST
                + ",PORT:" + PORT
                + ",MAX_IDLE:" + MAX_IDLE
                + ",MAX_ACTIVE:" + MAX_ACTIVE
                + ",MAX_WAIT:" + MAX_WAIT
                + ",TEST_ON_BORROW:" + TEST_ON_BORROW
                + ",TIMEOUT:" + TIMEOUT
        );
    }



    /**
     * 初始化pool
     *
     * @author Charlie(唐静)
     * @date 2018/6/22 22:51
     */
    private synchronized static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig ();
        config.setMaxIdle (MAX_IDLE);
        config.setMaxWaitMillis (MAX_WAIT);
        config.setTestOnBorrow (TEST_ON_BORROW);
        config.setMaxTotal (MAX_ACTIVE);
        jedisPool = new JedisPool (config, HOST, PORT, TIMEOUT, AUTH);
    }


    /**
     * 加法
     *
     * @param key    key
     * @param number number
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/8/2 13:03
     */
    public static Long incrBy(String key, long number) {
        Jedis jedis = null;
        try {
            jedis = getResource ();
            return jedis.incrBy (key, number);
        } finally {
            returnResource (jedis);
        }
    }


    /**
     * 减法
     *
     * @param key    key
     * @param number number
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/8/2 13:03
     */
    public static Long decrBy(String key, long number) {
        Jedis jedis = null;
        try {
            jedis = getResource ();
            return jedis.decrBy (key, number);
        } finally {
            returnResource (jedis);
        }
    }


    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                value = jedis.get (key);
                value = StringUtils.isNotBlank (value) && ! "nil".equalsIgnoreCase (value) ? value : null;
                logger.debug ("get {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.error ("get {} = {} error = {}", key, value);
        } finally {
            returnResource (jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static Object getObject(String key) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (getBytesKey (key))) {
                value = toObject (jedis.get (getBytesKey (key)));
                logger.debug ("getObject {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn ("getObject{} " + key + "=" + value);
        } finally {
            returnResource (jedis);
        }
        return value;
    }


    /**
     * SETNX actually means "SET if Not eXists".
     *
     * @param key key
     * @param value value
     * @param second second
     * @return 1 if the key was set 0 if the key was not set, -1 if failed
     * @author Charlie
     * @date 2018/8/2 19:19
     */
    public static Long setSimpleIfNoExist(Object key, Object value, int second) {
        Long result = -1L;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            String warpKey = key.toString ();
            result = jedis.setnx (warpKey, value.toString ());
            if (second > 0) {
                jedis.expire (warpKey, second);
            }
            logger.debug ("set {} = {}", key, value);
        } catch (Exception e) {
            logger.error ("redis set缓存失败: key:"+key+",value:"+value+"second:"+second);
        } finally {
            returnResource (jedis);
        }
        return result;
    }



    /**
     * SETNX actually means "SET if Not eXists".
     *
     * @param key key
     * @param value value
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/8/2 19:19
     */
    public static Long setSimpleIfNoExist(Object key, Object value) {
        return setSimpleIfNoExist (key, value, 0);
    }



    /**
     * 设置缓存
     *
     * @param key 键
     * @param value 值
     * @return boolean 是否成功
     * @author Charlie
     * @date 2018/8/2 13:58
     */
    public static boolean setSimple(Object key, Object value) {
        return setSimple(key, value, 0);
    }




    /**
     * 设置缓存
     *
     * @param key 键
     * @param value 值
     * @param second 超时时间，0为不超时 以秒为单位
     * @return boolean 是否成功
     * @author Charlie
     * @date 2018/8/2 13:58
     */
    public static boolean setSimple(Object key, Object value, int second) {
        return OK.equals (set (String.valueOf (key), String.valueOf (value), second));
    }


    /**
     * 设置缓存
     *
     * @param key 键
     * @param value 值
     * @param second 超时时间，0为不超时 以秒为单位
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/2 13:58
     */
    private static String set(String key, String value, int second) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (second > 0) {
                jedis.setex (key, second, value);
            }
            else {
                result = jedis.set (key, value);
            }
            logger.debug ("set {} = {}", key, value);
        } catch (Exception e) {
            logger.error ("redis set缓存失败: key:"+key+",value:"+value+"second:"+second);
        } finally {
            returnResource (jedis);
        }
        return result;
    }




    /**
     * 设置缓存
     *
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/2 13:05
     */
    public static Boolean setObject(String key, Object value, int cacheSeconds) {

        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.set (getBytesKey (key), toBytes (value));
            if (cacheSeconds != 0) {
                jedis.expire (key, cacheSeconds);
            }
            logger.debug ("setObject {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("getObject{} "+ key+"=" + value);
        } finally {
            returnResource (jedis);
        }
        return OK.equals (result);
    }

    /**
     * 获取List缓存
     *
     * @param key 键
     * @return 值
     */
    public static List<String> getList(String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                value = jedis.lrange (key, 0, - 1);
                logger.debug ("getList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn ("getList{} " + key + "=" + value);
        } finally {
            returnResource (jedis);
        }
        return value;
    }

    /**
     * 获取List缓存
     *
     * @param key 键
     * @return 值
     */
    public static List<Object> getObjectList(String key) {
        List<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (getBytesKey (key))) {
                List<byte[]> list = jedis.lrange (getBytesKey (key), 0, - 1);
                value = Lists.newArrayList ();
                for (byte[] bs : list) {
                    value.add (toObject (bs));
                }
                logger.debug ("getObjectList {} = {}", key, value);
            }
        } catch (Exception e) {
//			logger.warn("getObjectList {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return value;
    }

    /**
     * 设置List缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     */
    public static long setList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                jedis.del (key);
            }
            result = jedis.rpush (key, (String[]) value.toArray ());
            if (cacheSeconds != 0) {
                jedis.expire (key, cacheSeconds);
            }
            logger.debug ("setList {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("setList {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 设置List缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (getBytesKey (key))) {
                jedis.del (key);
            }
            List<byte[]> list = Lists.newArrayList ();
            for (Object o : value) {
                list.add (toBytes (o));
            }
            result = jedis.rpush (getBytesKey (key), (byte[][]) list.toArray ());
            if (cacheSeconds != 0) {
                jedis.expire (key, cacheSeconds);
            }
            logger.debug ("setObjectList {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("setObjectList {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static long listAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.rpush (key, value);
            logger.debug ("listAdd {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("listAdd {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 向List缓存中添加值
     *
     * @param key   键
     * @param value 值
     */
    public static long listObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            List<byte[]> list = Lists.newArrayList ();
            for (Object o : value) {
                list.add (toBytes (o));
            }
            result = jedis.rpush (getBytesKey (key), (byte[][]) list.toArray ());
            logger.debug ("listObjectAdd {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("listObjectAdd {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static Set<String> getSet(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                value = jedis.smembers (key);
                logger.debug ("getSet {} = {}", key, value);
            }
        } catch (Exception e) {
//			logger.warn("getSet {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return value;
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public static Set<Object> getObjectSet(String key) {
        Set<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (getBytesKey (key))) {
                value = Sets.newHashSet ();
                Set<byte[]> set = jedis.smembers (getBytesKey (key));
                for (byte[] bs : set) {
                    value.add (toObject (bs));
                }
                logger.debug ("getObjectSet {} = {}", key, value);
            }
        } catch (Exception e) {
//			logger.warn("getObjectSet {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return value;
    }

    /**
     * 设置Set缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     */
    public static long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                jedis.del (key);
            }
            result = jedis.sadd (key, (String[]) value.toArray ());
            if (cacheSeconds != 0) {
                jedis.expire (key, cacheSeconds);
            }
            logger.debug ("setSet {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("setSet {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 设置Set缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     */
    public static long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (getBytesKey (key))) {
                jedis.del (key);
            }
            Set<byte[]> set = Sets.newHashSet ();
            for (Object o : value) {
                set.add (toBytes (o));
            }
            result = jedis.sadd (getBytesKey (key), (byte[][]) set.toArray ());
            if (cacheSeconds != 0) {
                jedis.expire (key, cacheSeconds);
            }
            logger.debug ("setObjectSet {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("setObjectSet {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     *
     * @param key   键
     * @param value 值
     */
    public static long setSetAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.sadd (key, value);
            logger.debug ("setSetAdd {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("setSetAdd {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 向Set缓存中添加值
     *
     * @param key   键
     * @param value 值
     */
    public static long setSetObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            Set<byte[]> set = Sets.newHashSet ();
            for (Object o : value) {
                set.add (toBytes (o));
            }
            result = jedis.rpush (getBytesKey (key), (byte[][]) set.toArray ());
            logger.debug ("setSetObjectAdd {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("setSetObjectAdd {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 获取Map缓存
     *
     * @param key 键
     * @return 值
     */
    public static Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                value = jedis.hgetAll (key);
                logger.debug ("getMap {} = {}", key, value);
            }
        } catch (Exception e) {
//			logger.warn("getMap {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return value;
    }


    /**
     * 设置Map缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     */
    public static String setMap(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                jedis.del (key);
            }
            result = jedis.hmset (key, value);
            if (cacheSeconds != 0) {
                jedis.expire (key, cacheSeconds);
            }
            logger.debug ("setMap {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("setMap {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 设置Map缓存
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间，0为不超时
     */
    public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (getBytesKey (key))) {
                jedis.del (key);
            }
            Map<byte[], byte[]> map = Maps.newHashMap ();
            for (Map.Entry<String, Object> e : value.entrySet ()) {
                map.put (getBytesKey (e.getKey ()), toBytes (e.getValue ()));
            }
            result = jedis.hmset (getBytesKey (key), map);
            if (cacheSeconds != 0) {
                jedis.expire (key, cacheSeconds);
            }
            logger.debug ("setObjectMap {} = {}", key, value);
        } catch (Exception e) {
            logger.warn ("setObjectMap {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key   键
     * @param value 值
     */
    public static String mapPut(String key, Map<String, String> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.hmset (key, value);
            logger.debug ("mapPut {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("mapPut {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 向Map缓存中添加值
     *
     * @param key   键
     * @param value 值
     */
    public static String mapObjectPut(String key, Map<String, Object> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            Map<byte[], byte[]> map = Maps.newHashMap ();
            for (Map.Entry<String, Object> e : value.entrySet ()) {
                map.put (getBytesKey (e.getKey ()), toBytes (e.getValue ()));
            }
            result = jedis.hmset (getBytesKey (key), map);
            logger.debug ("mapObjectPut {} = {}", key, value);
        } catch (Exception e) {
//			logger.warn("mapObjectPut {} = {}", key, value, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key 键
     */
    public static long mapRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.hdel (key, mapKey);
            logger.debug ("mapRemove {}  {}", key, mapKey);
        } catch (Exception e) {
//			logger.warn("mapRemove {}  {}", key, mapKey, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 移除Map缓存中的值
     *
     * @param key 键
     * @param
     * @return
     */
    public static long mapObjectRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.hdel (getBytesKey (key), getBytesKey (mapKey));
            logger.debug ("mapObjectRemove {}  {}", key, mapKey);
        } catch (Exception e) {
//			logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key 键
     */
    public static boolean mapExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.hexists (key, mapKey);
            logger.debug ("mapExists {}  {}", key, mapKey);
        } catch (Exception e) {
//			logger.warn("mapExists {}  {}", key, mapKey, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 判断Map缓存中的Key是否存在
     *
     * @param key 键
     */
    public static boolean mapObjectExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.hexists (getBytesKey (key), getBytesKey (mapKey));
            logger.debug ("mapObjectExists {}  {}", key, mapKey);
        } catch (Exception e) {
//			logger.warn("mapObjectExists {}  {}", key, mapKey, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public static long del(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (key)) {
                result = jedis.del (key);
                logger.debug ("del {}", key);
            }
            else {
                logger.debug ("del {} not exists", key);
            }
        } catch (Exception e) {
            logger.warn ("del {}", key, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public static long delObject(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            if (jedis.exists (getBytesKey (key))) {
                result = jedis.del (getBytesKey (key));
                logger.debug ("delObject {}", key);
            }
            else {
                logger.debug ("delObject {} not exists", key);
            }
        } catch (Exception e) {
            logger.warn ("delObject {}", key, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     *
     * @param key 键
     */
    public static boolean exists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.exists (key);
            logger.debug ("exists {}", key);
        } catch (Exception e) {
            logger.warn ("exists {}", key, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }

    /**
     * 缓存是否存在
     *
     * @param key 键
     */
    public static boolean existsObject(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource ();
            result = jedis.exists (getBytesKey (key));
            logger.debug ("existsObject {}", key);
        } catch (Exception e) {
            logger.warn ("existsObject {}", key, e);
        } finally {
            returnResource (jedis);
        }
        return result;
    }
    /*===================================================================================================================================*/

    /**
     * 获取资源
     *
     * @return redis.clients.jedis.Jedis
     * @author Charlie
     * @date 2018/8/2 12:59
     */
    public static Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource ();
        } catch (JedisException e) {
            logger.warn ("getResource.", e);
            returnResource (jedis);
            throw e;
        }
        return jedis;
    }
    /*===================================================================================================================================*/


    /**
     * 释放资源
     *
     * @param jedis jedis
     * @author Charlie
     * @date 2018/8/2 12:59
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
//            jedisPool.returnResource(jedis);
            jedis.close ();
        }
    }

    /**
     * 获取byte[]类型Key
     *
     * @param object object
     * @return byte[]
     * @author Charlie
     * @date 2018/8/2 12:59
     */
    public static byte[] getBytesKey(Object object) throws UnsupportedEncodingException {
        if (object instanceof String) {
            return ((String) object).getBytes ("UTF-8");
        }
        else {
            return ObjectUtils.serialize (object);
        }

    }

    /**
     * Object转换byte[]类型
     *
     */
    public static byte[] toBytes(Object object) {
        return ObjectUtils.serialize (object);
    }

    /**
     * byte[]型转换Object
     *
     */
    public static Object toObject(byte[] bytes) {
        return ObjectUtils.unserialize (bytes);
    }


    /**
     * @param dbindex
     * @return Object
     * @date 2017-3-8 下午1:57:48
     */

    public static Object dbSize(int dbindex) {
        Jedis jedis = null;
        Object result = null;
        try {
            jedis = getResource ();
            jedis.select (dbindex);
            result = jedis.dbSize ();
            return result;
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            returnResource (jedis);
        }
        return 0;
    }


    public static void set(byte[] serialize, byte[] serialize2, int dbindex) {
        Jedis jedis = null;
        try {
            jedis = getResource ();
            jedis.select (dbindex);
            jedis.set (serialize, serialize2);
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            returnResource (jedis);
        }
    }


    public static byte[] get(byte[] serialize, int dbindex) {
        Jedis jedis = null;
        try {
            jedis = getResource ();
            jedis.select (dbindex);
            return jedis.get (serialize);
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            returnResource (jedis);
        }
        return null;
    }


    public static Object expire(byte[] serialize, int i, int dbindex) {
        Jedis jedis = null;
        try {
            jedis = getResource ();
            jedis.select (dbindex);
            return jedis.expire (serialize, 0);
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            returnResource (jedis);
        }
        return null;
    }


    public static void flushDB(int dbindex) {
        Jedis jedis = null;
        try {
            jedis = getResource ();
            jedis.select (dbindex);
            jedis.flushDB ();
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            returnResource (jedis);
        }
    }


    /**
     * 转换为字节数组
     *
     * @param str
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes ("UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return null;
    }


    static class ObjectUtils{

        /**
         * 注解到对象复制，只复制能匹配上的方法。
         *
         * @param annotation annotation
         * @param object     object
         * @author Charlie
         * @date 2018/8/2 11:45
         */
        public static void annotationToObject(Object annotation, Object object) {

            if (annotation != null) {
                Class<?> annotationClass = annotation.getClass ();
                Class<?> objectClass = object.getClass ();
                for (Method m : objectClass.getMethods ()) {
                    if (StringUtils.startsWith (m.getName (), "set")) {
                        try {
                            String s = StringUtils.uncapitalize (StringUtils.substring (m.getName (), 3));
                            Object obj = annotationClass.getMethod (s).invoke (annotation);
                            if (obj != null && ! "".equals (obj.toString ())) {
                                if (object == null) {
                                    object = objectClass.newInstance ();
                                }
                                m.invoke (object, obj);
                            }
                        } catch (Exception e) {
                            // 忽略所有设置失败方法
                        }
                    }
                }
            }
        }

        /**
         * 序列化对象
         *
         * @param object
         */
        public static byte[] serialize(Object object) {
            ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                if (object != null) {
                    baos = new ByteArrayOutputStream ();
                    oos = new ObjectOutputStream (baos);
                    oos.writeObject (object);
                    return baos.toByteArray ();
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
            return null;
        }

        /**
         * 反序列化对象
         *
         * @param bytes
         */
        public static Object unserialize(byte[] bytes) {
            ByteArrayInputStream bais = null;
            try {
                if (bytes != null && bytes.length > 0) {
                    bais = new ByteArrayInputStream (bytes);
                    ObjectInputStream ois = new ObjectInputStream (bais);
                    return ois.readObject ();
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
            return null;
        }
    }
}

