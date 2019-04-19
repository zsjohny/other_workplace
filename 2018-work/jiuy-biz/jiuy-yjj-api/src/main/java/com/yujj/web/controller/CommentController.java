package com.yujj.web.controller;

import java.util.Scanner;

import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyuan.util.oauth.common.Display;
import com.jiuyuan.util.oauth.common.credential.IClientCredentials;
import com.jiuyuan.util.oauth.sns.common.response.ISnsResponse;
import com.jiuyuan.util.oauth.sns.common.response.SnsResponseType;
import com.jiuyuan.util.oauth.sns.common.user.ISnsEndUser;
import com.jiuyuan.util.oauth.sns.common.user.SnsEndUser;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.util.oauth.v2.credential.IRawDataV2TokenCredentials;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.util.http.HttpUtil;
import com.yujj.util.oauth.common.credential.ClientCredentials;

@Controller
@RequestMapping("/comment")
public class CommentController {
    public static void main(String[] args) {
        DefaultHttpClient httpClient = HttpUtil.createHttpClient(null, 10, 10, 10, 0, false, false);
        HttpClientService httpClientService = new HttpClientService();
        httpClientService.setHttpClient(httpClient);
        IClientCredentials clientCredentials =
            new ClientCredentials("wxdb9d5649b7a5f2c7", "2c19e030390877fb11d09b89ff546ca0",
                "https://www.yujiejie.com/ext/oauth/callback.do");
        WeiXinV2API api =
            new WeiXinV2API(httpClientService, "https://open.weixin.qq.com/connect/oauth2/authorize",
                "snsapi_userinfo", clientCredentials);
        String authUrl = api.getAuthorizeUrlFit(null, "", Display.PLUGIN);
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String code = scanner.nextLine();
            IRawDataV2TokenCredentials tokenCredentials = api.getTokenCredentials(code, null, null);
            ISnsResponse<ISnsEndUser> weiboResponse = api.getEndUser(tokenCredentials, null);
            ISnsEndUser snsEndUser = new SnsEndUser();
            if (weiboResponse.getResponseType() == SnsResponseType.SUCCESS) {
                snsEndUser = weiboResponse.getData();
            }
        }
        scanner.close();
    }
}
