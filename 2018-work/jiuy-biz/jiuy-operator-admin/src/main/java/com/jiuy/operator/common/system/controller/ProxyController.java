package com.jiuy.operator.common.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.util.http.HttpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @version V1.0
 * @Package com.jiuy.operator.common.system.controller
 * @Description:  代理请求
 * @author: Aison
 * @date: 2018/4/26 10:26
 * @Copyright: 玖远网络
 */
@Controller
public class ProxyController {

    @ResponseBody
    @RequestMapping("proxy")
    public JSONObject proxy(String url, HttpServletRequest request) {
       Map<String,Object> param = HttpRequest.getRequestMap(request);
       param.remove("url");
       String json = HttpRequest.sendPost(url,param);
       JSONObject object = JSONObject.parseObject(json);
       return  object;
    }
}
