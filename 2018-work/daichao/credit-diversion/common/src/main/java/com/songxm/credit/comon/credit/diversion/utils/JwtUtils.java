package com.songxm.credit.comon.credit.diversion.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.songxm.credit.comon.credit.diversion.constant.JwtClaim;
import com.songxm.credit.comon.credit.diversion.dto.Token;
import lombok.extern.slf4j.Slf4j;
import moxie.cloud.service.common.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@Slf4j
public class JwtUtils {
    private static final ObjectMapper mapper = JsonUtil.getObjectMapper();
    private static final String ISSUER = "QIUQIU";

    public static Token getToken(Map<String, Object> claims, long expireIn, String secret) {
        log.info("claims:{},exp:{},secret:{}", JSON.toJSONString(claims),expireIn,secret);
        final long iat = System.currentTimeMillis() / 1000L;
        final long exp = iat + expireIn;
        final HashMap<String, Object> jwtClaims = Maps.newHashMap();
        jwtClaims.putAll(claims);
        jwtClaims.put(JwtClaim.iss, ISSUER);
        jwtClaims.put(JwtClaim.iat, iat);
        jwtClaims.put(JwtClaim.exp, exp);
        final JWTSigner signer = new JWTSigner(secret);
        final String jwtToken = signer.sign(jwtClaims);
        Token token = new Token();
        token.setToken(jwtToken);
        token.setExpireIn(expireIn);
        return token;
    }

    public static <T> T getClaim(Map<String, Object> claims, String claimName, Class<T> resultCls) throws Exception {
        Map<String, Object> tokenInfoMap = (Map) claims.get(claimName);
        return mapper.convertValue(tokenInfoMap, resultCls);
    }

    public static Map<String, Object> getClaims(String jwtToken, String secret) throws Exception {
        final JWTVerifier verifier = new JWTVerifier(secret);
        return verifier.verify(jwtToken);
    }
}
