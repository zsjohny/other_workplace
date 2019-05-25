package com.songxm.credit.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.moxie.commons.BaseJwtUtils;
import com.songxm.credit.comon.credit.diversion.constant.JwtClaim;
import com.songxm.credit.comon.credit.diversion.enums.TokenType;
import com.songxm.credit.comon.credit.diversion.utils.JwtUtils;
import com.songxm.credit.config.BaseConfig;
import lombok.extern.slf4j.Slf4j;
import moxie.cloud.service.common.util.JsonUtil;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticator {

    public JwtAuthInfo authenticate(String token) {
        token = token.indexOf(32) < 0 ? token : token.substring(token.indexOf(32) + 1).trim();
        String aud = (String) BaseJwtUtils.getTokenInfo(token).get(JwtClaim.aud);
        String loginNo = (String) BaseJwtUtils.getTokenInfo(token).get(JwtClaim.loginNo);
        if (null == loginNo) {
            log.info("serviceName:{}, token:{}", aud, token);
        } else {
            log.info("loginNo:{}, token:{}", loginNo, token);
        }
        String jwt = token.indexOf(32) < 0 ? token : token.substring(token.lastIndexOf(' ') + 1, token.length());
        try {
            List<String> jwtParts = Splitter.on(".").limit(3).splitToList(jwt);
            if (jwtParts.size() != 3) {
                return null;
            }
            byte[] decodedBytes = Base64.getUrlDecoder().decode(jwtParts.get(1));
            String claimStr = new String(decodedBytes, Charsets.UTF_8);
            ImmutablePair<String, TokenType> tokenTypeAndVersion = getTokenTypeAndVersion(claimStr);
            if (tokenTypeAndVersion.getLeft() == null || tokenTypeAndVersion.getRight() == null) {
                return null;
            }
            Map<String, Object> claims = JwtUtils.getClaims(jwt,
                    BaseConfig.TOKEN_SECURECT);
            log.info("token校验成功");
            return new JwtAuthInfo(claims);
        } catch (Exception e) {
            log.error("token非法-[{}]: {}", token, e.getMessage());
        }
        return null;
    }

    private ImmutablePair<String, TokenType> getTokenTypeAndVersion(String claimStr) throws Exception {
        JsonNode node = JsonUtil.getObjectMapper().readTree(claimStr);
        TokenType tokenType = null;
        if (node.has(JwtClaim.type)) {
            String type = node.path(JwtClaim.type).asText();
            tokenType = Enum.valueOf(TokenType.class, type);
        }
        String secretVersion = null;
        if (node.has(JwtClaim.secVer)) {
            secretVersion = node.path(JwtClaim.secVer).asText();
        }
        return ImmutablePair.of(secretVersion, tokenType);
    }

    public static void main(String args[]) throws Exception {
        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwcm9kdWN0Q29kZSI6ImEyOTYwOWNkYjU2YjQ4Yzc4ZmU2ZTFkZDY3ZmIwZGE0Iiwib3JnQ29kZSI6IjYyMTkzMjY3MTMwMjQ1MTIiLCJpc3MiOiJYSU5CQU5HIiwic2VjVmVyIjoiMSIsInR5cGUiOiJTRVJWSUNFIiwiZXhwIjoxNTE2NTM3MDIyLCJpYXQiOjE0ODQ5MTQ2MjIsImp0aSI6Ijg0ZTdjYzJhMjU5MjQ5Y2ViY2M2YmE5YTU1NjU4ODJlIn0.w5ABNoDIpb4uyib8XDCJ29DzxkH0yZfUNQOCG0Srm0M";
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwcm9kdWN0Q29kZSI6ImEyOTYwOWNkYjU2YjQ4Yzc4ZmU2ZTFkZDY3ZmIwZGE0Iiwib3JnQ29kZSI6IjYyMTkzMjY3MTMwMjQ1MTIiLCJpc3MiOiJYSU5CQU5HIiwic2VjVmVyIjoiMSIsInR5cGUiOiJTRVJWSUNFIiwiZXhwIjoxNTE2NTM3MDIyLCJpYXQiOjE0ODQ5MTQ2MjIsImp0aSI6Ijg0ZTdjYzJhMjU5MjQ5Y2ViY2M2YmE5YTU1NjU4ODJlIn0.w5ABNoDIpb4uyib8XDCJ29DzxkH0yZfUNQOCG0Srm0M";

 /*       JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
        JwtAuthInfo jwtAuthInfo = jwtAuthenticator.authenticate(token);
        System.out.println(jwtAuthInfo);*/

        Map<String, Object> claims = JwtUtils.getClaims(jwt,
            "E7CB23BE-9F85-4CD1-89E7-7737901CE848");
        JwtAuthInfo jwtAuthInfo1 = new JwtAuthInfo(claims);
        System.out.println(jwtAuthInfo1);
    }
}
