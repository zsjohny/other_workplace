package com.finace.miscroservice.commons.utils;

import com.finace.miscroservice.commons.log.Log;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.data.redis.core.HashOperations;

import java.util.concurrent.TimeUnit;

import static com.finace.miscroservice.commons.enums.RedisKeyEnum.USER_LOAD_STATUS_KEY;
import static com.finace.miscroservice.commons.utils.JwtToken.EXPIRE_TIME;
import static com.finace.miscroservice.commons.utils.JwtToken.SOON_EXPIRE_TIME;

/**
 * token操作的工具
 */
public class TokenOperateUtil {

    private TokenOperateUtil() {

    }


    private static Log log = Log.getInstance(TokenOperateUtil.class);
    private static final String LINK = ",";

    //用户操作的redis
    private static HashOperations<String, String, String> userLoadRedisTemplate;

    private static final Object LOCK = new Object();


    /**
     * 保存token
     *
     * @param id    用户的Id
     * @param token 用户的token
     */
    public static void save(Integer id, String token) {

        if (isNullOrEmpty(id, token)) {
            return;
        }
        try {
            HashOperations<String, String, String> redisTemplate = getRedisTemplate();

            redisTemplate.put(USER_LOAD_STATUS_KEY.toKey(), id.toString(), token);

        } catch (Exception e) {
            log.error("用户={}保存token 出现异常", id, e);
        }


    }

    private static final int NOT_EXISTS = -1;

    private static LoadingCache<Integer, Integer> modifyRecord = CacheBuilder.newBuilder().expireAfterAccess(SOON_EXPIRE_TIME, TimeUnit.MILLISECONDS).
            build(new CacheLoader<Integer, Integer>() {
                @Override
                public Integer load(Integer key) throws Exception {
                    return NOT_EXISTS;
                }
            });

    /**
     * 修改token
     *
     * @param id    用户的Id
     * @param token 用户的token
     */
    public static void modify(Integer id, String token) {

        if (isNullOrEmpty(id, token)) {
            return;
        }
        try {
            if (modifyRecord.getUnchecked(id) > NOT_EXISTS) {
                log.warn("时间间隔={} 用户={}已经刷新过token 此为非法操作", EXPIRE_TIME, id);
                return;
            }

            String saveData = token;

            HashOperations<String, String, String> redisTemplate = getRedisTemplate();


            //因为是单个用户的Id 所以这里不涉及原子性问题
            String tokens = redisTemplate.get(USER_LOAD_STATUS_KEY.toKey(), id.toString());


            if (tokens != null && token.split(LINK).length <= 1) {

                saveData = saveData + LINK + tokens;

            }

            redisTemplate.put(USER_LOAD_STATUS_KEY.toKey(), id.toString(), saveData);


            modifyRecord.put(id, id);
        } catch (Exception e) {
            log.error("用户={}修改token 出现异常", id, e);
        }


    }

    /**
     * 验证token
     *
     * @param id          用户的Id
     * @param verifyToken 用户待验证的token
     */
    public static Boolean verify(Integer id, String verifyToken) {
        Boolean passFlag = Boolean.FALSE;

        if (isNullOrEmpty(id, verifyToken)) {
            return passFlag;
        }

        try {

            HashOperations<String, String, String> redisTemplate = getRedisTemplate();

            String tokens = redisTemplate.get(USER_LOAD_STATUS_KEY.toKey(), id.toString());


            if (tokens != null && !tokens.contains(verifyToken)) {
                log.info("用户={} token不存在列表集合中,为非法登陆", id);

            } else {
                passFlag = Boolean.TRUE;
                log.info("用户={} 为正常登陆", id);
            }

        } catch (Exception e) {
            passFlag = Boolean.TRUE;
            log.error("用户={}验证token出现问题,降级处理", id, e);
        }

        return passFlag;


    }

    /**
     * 校验参数是非法
     *
     * @param id    用户的Id
     * @param token 用户的token
     * @return
     */
    private static Boolean isNullOrEmpty(Integer id, String token) {
        Boolean isNullOrEmptyFlag = Boolean.TRUE;

        if (id == null || id < 0 || token == null || token.isEmpty()) {
            log.warn("所传参数为空id={} token={}", id, token);
            return isNullOrEmptyFlag;
        }

        isNullOrEmptyFlag = Boolean.FALSE;

        return isNullOrEmptyFlag;
    }


    private static HashOperations<String, String, String> getRedisTemplate() {
        if (userLoadRedisTemplate == null) {
            synchronized (LOCK) {
                userLoadRedisTemplate = (HashOperations<String, String, String>) ApplicationContextUtil.getBean("userLoadRedisTemplate");
            }
        }
        return userLoadRedisTemplate;


    }


}
