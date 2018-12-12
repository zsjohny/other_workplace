package com.goldplusgold.td.sltp.core.auth;

import com.goldplusgold.td.sltp.core.auth.constant.LoginParamsEnum;
import com.goldplusgold.td.sltp.core.auth.constant.PlatformEnum;
import com.goldplusgold.td.sltp.core.auth.constant.TokenInfo;
import com.goldplusgold.td.sltp.core.auth.exception.JwtTokenException;
import com.goldplusgold.td.sltp.core.auth.util.OperationCookie;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.goldplusgold.td.sltp.core.auth.exception.JwtTokenException.Info.CREATE_JWT_PARAM_MISS;
import static com.goldplusgold.td.sltp.core.auth.exception.JwtTokenException.Info.VERIFY_JWT_ERROR;
import static com.goldplusgold.td.sltp.core.auth.exception.JwtTokenException.Info.VERIFY_JWT_PARAM_MISS;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Jwt工具类
 * <p>
 * iss(issuer)：JWT的签发者(可选)
 * sub(subject)：该JWT所面向的用户(可选)
 * aud(audience)：接收该JWT的一方(可选)
 * exp(expiration)：过期时间（时间戳，可选）
 * nbf(notBefore)：如果当前时间在nbf里的时间之前，则Token不被接受；是否使用是可选的
 * iat(issuedAt)：在什么时候签发(可选)
 * <p>
 * 创建JWT时必带参数:
 * <p>
 * userId： 用户ID
 * userName： 用户名
 * platform： 客户端平台(android，ios，pc，h5)
 * clientId:推送ID,如果是web,h5可以为空
 * imei： 客户端ID，web与h5可以为空，android与ios都是deviceId
 */
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static final String key = "6YeR5pyJ6YeR"; //金专家base64(目前与TD共用)

    /**
     * 签发jwt token
     */
    public static String createJWT(String userId,
                                   String userName,
                                   String platform,
                                   String imei,
                                   String clientId,
                                   long expirTime) throws JwtTokenException {

        try {

            checkArgument(StringUtils.isNotEmpty(userId));
            checkArgument(StringUtils.isNotEmpty(userName));
            checkArgument(StringUtils.isNotEmpty(platform));
            checkArgument(expirTime > 0);

            if (PlatformEnum.ANDROID.toName().equals(platform) || PlatformEnum.IOS.toName().equals(platform)) {
                checkArgument(StringUtils.isNotEmpty(imei));
                checkArgument(StringUtils.isNotEmpty(clientId));
            }

        } catch (IllegalArgumentException e) {
            logger.error(CREATE_JWT_PARAM_MISS.toInfo());
            //TODO： 异常显示信息未定义
            throw new JwtTokenException(CREATE_JWT_PARAM_MISS.toCode(), CREATE_JWT_PARAM_MISS.toInfo(), e, "");

        }

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + expirTime;
        Date exp = new Date(expMillis);

        Map<String, Object> params = new HashMap<>();
        params.put(LoginParamsEnum.JZJUSERID.toName(), userId);
        params.put(LoginParamsEnum.JZJUSERNAME.toName(), userName);
        params.put(LoginParamsEnum.PLATFORM.toName(), platform);
        params.put(LoginParamsEnum.IMEI.toName(), StringUtils.isNotEmpty(imei) ? imei : "");
        params.put(LoginParamsEnum.CLIENTID.toName(), StringUtils.isNotEmpty(clientId) ? clientId : "");
        params.put(TokenInfo.EXPIRATION, exp);

        String jwtString = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, key)
                .setExpiration(exp)
                .setIssuedAt(now)
                .setClaims(params)
                .compact();

        logger.debug("createJWT(): {}", jwtString);

        return jwtString;
    }


    /**
     * 验证jwt token
     */
    public static HttpServletRequest verifyJWT(String jwtString,
                                               HttpServletRequest request) throws JwtTokenException {

        Claims claims;

        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtString).getBody();
        } catch (MissingClaimException | IncorrectClaimException e) {
            logger.error(VERIFY_JWT_ERROR.toInfo());
            //TODO： 将显示的错误信息抽取到常量类
            throw new JwtTokenException(VERIFY_JWT_ERROR.toCode(), VERIFY_JWT_ERROR.toInfo(), e, "请重新登录金专家.");
        }

        checkArgument(claims != null);

        Object userIdObj = claims.get(LoginParamsEnum.JZJUSERID.toName());
        String userId = (String) Optional.ofNullable(userIdObj).orElse("");

        Object userNameObj = claims.get(LoginParamsEnum.JZJUSERNAME.toName());
        String userName = (String) Optional.ofNullable(userNameObj).orElse("");

        Object platformObj = claims.get(LoginParamsEnum.PLATFORM.toName());
        String platform = (String) Optional.ofNullable(platformObj).orElse("");

        Object imeiObj = claims.get(LoginParamsEnum.IMEI.toName());
        String imei = (String) Optional.ofNullable(imeiObj).orElse("");

        Object clientIdObj = claims.get(LoginParamsEnum.CLIENTID.toName());
        String clientId = (String) Optional.ofNullable(clientIdObj).orElse("");

        Object expirationObj = claims.get(TokenInfo.EXPIRATION);
        Long expiration = (Long) Optional.ofNullable(expirationObj).orElse("");

        try {
            checkArgument(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(userName) &&
                    StringUtils.isNotEmpty(platform) && expiration > 0);
        } catch (IllegalArgumentException e) {
            logger.error(VERIFY_JWT_PARAM_MISS.toInfo());
            throw new JwtTokenException(VERIFY_JWT_PARAM_MISS.toCode(), VERIFY_JWT_PARAM_MISS.toInfo(), e, "请重新登录金专家.");
        }

        setRequestAttribute(userId,
                userName,
                platform,
                imei,
                clientId,
                expiration,
                request);

        return request;
    }

    //设置请求属性值
    private static void setRequestAttribute(String userId,
                                            String userName,
                                            String platform,
                                            String imei,
                                            String clientId,
                                            Long expiration,
                                            HttpServletRequest request) {

        request.setAttribute(LoginParamsEnum.JZJUSERID.toName(), userId);
        request.setAttribute(LoginParamsEnum.JZJUSERNAME.toName(), userName);
        request.setAttribute(LoginParamsEnum.PLATFORM.toName(), platform);
        request.setAttribute(LoginParamsEnum.IMEI.toName(), imei);
        request.setAttribute(LoginParamsEnum.CLIENTID.toName(), clientId);
        request.setAttribute(TokenInfo.EXPIRATION, expiration);
    }


    /**
     * 从cookie中提取json web token(JWT)
     */
    public static String getJWTTokenFromCookie(HttpServletRequest request) {

        return OperationCookie.getCookieValue(request, TokenInfo.HTTP_TD_RESPONSE_HEADER_TOKEN_NAME);

    }


    /**
     * 从header中提取json web token(JWT)
     */
    public static String getJWTTokenFromHeader(HttpServletRequest request) {

        String jwtToken = null;
        String auth = request.getHeader(TokenInfo.HTTP_HEADER_AUTHORIZATION);
        if (StringUtils.isNotEmpty(auth)) {
            jwtToken = StringUtils.removeStart(auth, TokenInfo.HTTP_HEADER_AUTHORIZATION_BEARER).trim();
        }
        return jwtToken;
    }

    /**
     * 从header中提取用户登录金专家的时间戳
     */
    public static String getJZJLoginTimeStampFromHeader(HttpServletRequest request){
        return request.getHeader(TokenInfo.HTTP_HEADER_JZJ_LOGIN_TIME_STAMP);
    }

    /**
     * 从cookie中提取用户登录金专家的时间戳
     */
    public static String getJZJLoginTimeStampFromCookie(HttpServletRequest request){
        return OperationCookie.getCookieValue(request, TokenInfo.HTTP_HEADER_JZJ_LOGIN_TIME_STAMP);
    }
}
