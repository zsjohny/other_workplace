package com.finace.miscroservice.commons.utils;

import com.finace.miscroservice.commons.log.Log;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * jwt的认证
 */
public class JwtToken {


    public static final int EXPIRE_TIME = 24 * 60;
    public static final long SOON_EXPIRE_TIME = 1000 * 60 * 10L;

    private static final String SIGN_KEY = "com.finace.miscroservice";
    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String TOKEN = "token";
    public static final String AUTH_SUFFIX = "auth";


    /**
     * token失效标识
     */
    public static final int INVALID_LOAD_SIGN = 0;


    private static Log logger = Log.getInstance(JwtToken.class);

    /**
     * 生成token
     *
     * @param id  用户id
     * @param uid 用户设备号
     * @return
     */
    public static String toToken(Integer id, String uid) {
        id = Optional.ofNullable(id).orElse(-1);
        uid = Optional.ofNullable(uid).orElse(UUID.randomUUID().toString());
        Calendar calendar = Calendar.getInstance();
        Date nowTime = calendar.getTime();
        calendar.add(Calendar.HOUR, EXPIRE_TIME);
//        calendar.add(Calendar.MINUTE, 5);
        Map<String, Object> map = new HashMap<>();
        map.put(ID, id);
        map.put(UID, uid);

        return Jwts.
                builder().setClaims(map).setIssuedAt(nowTime).setExpiration(calendar.getTime()).signWith(SignatureAlgorithm.HS256, (SIGN_KEY)).compact();

    }


    /**
     * 解密token
     *
     * @param token token
     * @param uid   用户设备号
     * @return
     */
    public static Integer parseToken(String token, String uid) {
        Integer id = null;
        if (token == null || token.length() == 0 || uid == null || uid.isEmpty()) {
            logger.warn("用户所传token参数为空");
            return id;
        }
        Claims body = null;

        try {
            body = Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {

            if (e instanceof ExpiredJwtException) {
                logger.warn("uid={} token={}已经失效", uid, token);
                return INVALID_LOAD_SIGN;
            }

            logger.error("用户所传token={},解析异常", token, e);

        }
        if (body == null || body.isEmpty()) {
            logger.warn("用户所传token={}解密失败", token);
            return id;
        }

        Object res = body.get(ID);


        if (res == null) {
            logger.warn("用户所传token={}中不包含{}", token, ID);
            return id;
        }


        Object uids = body.get(UID);

        if (uids == null) {
            logger.warn("用户所传token={}中不包含{}", token, ID);
            return id;
        }
        logger.info("jwt的认证 用户所传token={},uid={},对比uids={}",token,uid,uids);
        if (!uid.equals(uids)) {
            logger.warn("用户{}所传的uid不一致", res);
            return id;
        }

        id = (Integer) res;

        long interval = body.getExpiration().getTime() - System.currentTimeMillis();

        String saveToken = token;

        if (interval <= SOON_EXPIRE_TIME) {
            logger.info("用户{}的token时间还剩={} 小于时间={} 即将失效 进行自动刷新", res, interval, SOON_EXPIRE_TIME);
            saveToken = toToken(id, uid);
            //保存修改信息
            TokenOperateUtil.modify(id, saveToken);
        }

        refreshToken(saveToken);

        return id;

    }


    /**
     * 执行即将过世的任务
     *
     * @param token 用户token
     */
    private static void refreshToken(String token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        response.addHeader(TOKEN, token);


    }


}
