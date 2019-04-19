package com.jiuyuan.util.nosql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import com.yujj.util.StringUtil;

/**
 * 
 * @author zenghaibo
 *
 * redis工具类
 */
public class RedisUtil {
    private static final Logger LOG = Logger.getLogger(RedisUtil.class);
    private static Map<String, Object> map = new HashMap();
    protected static JedisPool connectionPool;
    private static Properties config = new Properties();
//    public  final String REDIS_HOST = config.getProperty("redis.servers", "").trim();
//    public static final String REDIS_PORT = config.getProperty("redis.port", "").trim();
//    public static final String REDIS_PWD = config.getProperty("redis.pwd", "");
//    public static final String 	MAX_WAIT = config.getProperty("maxWait", "");
    static {
            InputStream in = Object.class.getResourceAsStream("/constants.properties");
            try {
                config.load(in);

                String REDIS_HOST = config.getProperty("redis.servers", "").trim();
                String REDIS_PORT = config.getProperty("redis.port", "").trim();
                String REDIS_PWD = config.getProperty("redis.pwd", "");
                String 	MAX_WAIT = config.getProperty("maxWait", "");

                System.out.println("REDIS_HOST="+REDIS_HOST);
                System.out.println("REDIS_PORT="+REDIS_PORT);
                System.out.println("REDIS_HOST="+REDIS_PWD);


                if (StringUtil.isNotEmpty(REDIS_HOST) && StringUtil.isNotEmpty(REDIS_PORT)) {
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxIdle(50);
                    config.setTestOnBorrow(true);
                    connectionPool = new JedisPool(config, REDIS_HOST, Integer.parseInt(REDIS_PORT), Protocol.DEFAULT_TIMEOUT, REDIS_PWD);
                     LOG.info(String.format("Redis : %s:%s", REDIS_HOST, REDIS_PORT));
                } else {
                    LOG.warn("Redis : 未配置Redis,使用HashMap");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    public static String get(String key) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.get(key);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            return (String) map.get(key);
        }
    }

    /**
     *
     * @param key
     * @param seconds
     *            秒
     * @return
     */
    public static boolean set(String key, String value, int seconds) {
        if (connectionPool != null) {
            Jedis jedis = connectionPool.getResource();
            try {
                jedis.setex(key, seconds, value);
                return true;
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            map.put(key, value);
            return true;
        }

    }

    public static boolean hmset(String key, Map<String, String> data) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                jedis.hmset(key, data);
                return true;
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            map.put(key, data);
            return true;
        }
    }

    public static Map<String, String> hgetAll(String key) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.hgetAll(key);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            return (Map<String, String>) map.get(key);
        }
    }

    public static boolean hset(String key, String field, String value) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                jedis.hset(key, field, value);
                return true;
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            Map<String, String> tmp = (Map<String, String>) map.get(key);
            if(tmp==null){
                tmp=new HashMap();
                map.put(key, tmp);
            }
            tmp.put(field, value);
            return true;
        }
    }

    public static String hget(String key, String filed) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.hget(key, filed);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            Map tmp = (Map<String, String>) map.get(key);
            return (String) tmp.get(filed);
        }
    }

    public static List<String> hmget(String key, String... fields) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.hmget(key, fields);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            Map tmp = (Map<String, String>) map.get(key);
            List<String> list = new ArrayList();
            for (String f : fields) {
                list.add((String) tmp.get(f));
            }
            return list;
        }
    }

    public static Object eval(String script, String... paremeters) {

        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.eval(script, 0, paremeters);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            return null;
        }
    }

    public static Long incrBy(String key, long i) {

        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.incrBy(key, i);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            if (map.containsKey(key)) {
                Long v = (Long) map.get(key);
                map.put(key, v + i);
                return v + i;
            } else {
                map.put(key, i);
                return new Long(i);
            }
        }
    }

    public static Long decrBy(String key, long i) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.decrBy(key, i);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            if (map.containsKey(key)) {
                Long v = (Long) map.get(key);
                map.put(key, v - i);
                return v - i;
            } else {
                map.put(key, -i);
                return new Long(-i);
            }
        }
    }

    public static Long del(String name) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.del(name);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            map.remove(name);
            return null;
        }
    }

    public static boolean set(String key, String value) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                jedis.set(key, value);
                return true;
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            map.put(key, value);
            return true;
        }

    }

    public static long incr(String key, int seconds) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                long i = jedis.incr(key);
                if (i <= 1) {
                    jedis.expire(key, seconds);
                }
                return i;
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            Integer i = (Integer) map.get(key);
            if (i == null) {
                i = 1;
                map.put(key, i);
            } else {
                map.put(key, i + 1);
            }
            return i;
        }
    }

    public static Object getObj(String key) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.get(key);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            return (Object) map.get(key);
        }
    }

    public static String rpop(String key) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.rpop(key);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            return (String) map.get(key);
        }
    }

    public static Long hdel(String key, String ... fields) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.hdel(key, fields);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        } else {
            return null;
        }

    }

    public static long expire(String key, int seconds) {
        if (connectionPool != null) {

            Jedis jedis = connectionPool.getResource();
            try {
                return jedis.expire(key, seconds);
            } finally {
                connectionPool.returnResourceObject(jedis);
            }
        }
        return 0;
    }

public static void main(String[] args) {
        int EXPIRE_TIME = 1000 * 60 * 60 * 24 * 1;
        String keyValue = "";
        RedisUtil.set("zhb10","一起努力！",EXPIRE_TIME);
        keyValue = RedisUtil.get("zhb10");
        System.out.println("keyValue=" + keyValue);
}
}
