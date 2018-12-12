package com.finace.miscroservice.getway.filter;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.*;
import com.finace.miscroservice.commons.utils.tools.DeviceUtil;
import com.finace.miscroservice.getway.interpect.AccessInterceptor;
import com.finace.miscroservice.getway.util.OptionsCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.finace.miscroservice.commons.utils.JwtToken.INVALID_LOAD_SIGN;

/**
 * 权限的过滤器
 */
public class UserAuthFilter extends UserFilter {
    private Log logger = Log.getInstance(UserAuthFilter.class);

    private static final String VERSION = "version";
    private static final String USER_AGANT = "user-agent";

    private AccessInterceptor accessInterceptor;

    public UserAuthFilter(AccessInterceptor accessInterceptor) {
        this.accessInterceptor = accessInterceptor;
    }

    private static InheritableThreadLocal<String> idCache = new InheritableThreadLocal<>();


    public static void setId(String id) {
        idCache.set(id);
    }


    public static String getId() {
        String id = idCache.get();
        if (id == null) {
            return "";
        } else {
            idCache.remove();
            return id;
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest req, ServletResponse res, Object mappedValue) {


        if (OptionsCheckUtil.isContain((HttpServletRequest) req)) {
            return true;
        }


        HttpServletRequest request = WebUtils.toHttp(req);


        //校验
        if (!accessInterceptor.isPass(WebUtils.toHttp(request))) {

            return false;
        }

        String token = request.getHeader(JwtToken.TOKEN);
        String uid = request.getHeader(JwtToken.UID);

        // 1--android 2--ios 4--pc
        Integer isDevice = DeviceUtil.isDevice(request.getHeader(USER_AGANT));
        String version = request.getHeader(VERSION);

        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
            logger.warn("Ip={} 方法={} 没有token进行访问, 设备={}, version={}", Iptools.gainRealIp(request), request.getRequestURI(), isDevice, version);
            req.setAttribute(ERROR, Response.fail());
            return false;
        }


        Integer id = JwtToken.parseToken(token, uid);

        if (id == null) {
            logger.warn("Ip={} 方法={} token={}解析id不正确, 设备={}, version={}", Iptools.gainRealIp(request), request.getRequestURI(), token, isDevice, version);
            req.setAttribute(ERROR, Response.fail());
            return false;
        }


        //判断token是否失效
        if (id.equals(INVALID_LOAD_SIGN)) {
            logger.warn("Ip={} 下访问方法={} token已经失效, 设备={}, version={}", Iptools.gainRealIp(request), request.getRequestURI(), isDevice, version);
            req.setAttribute(ERROR, Response.response(Constant.ECODE_506));
            return false;
        }


        // 判断是否是单点登陆或者修改密码登陆
        if (!TokenOperateUtil.verify(id, token)) {
            logger.warn("Ip={} 用户={} 下访问方法={} 进行了异地登陆, 设备={}, version={}", Iptools.gainRealIp(request), id, request.getRequestURI(), isDevice, version);
            req.setAttribute(ERROR, Response.response(Constant.ECODE_507));
            return false;
        }


        //设置Id
        setId(String.valueOf(id));


        return true;

    }


    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {
        HttpServletRequest req = WebUtils.toHttp(request);
        writeResponse(WebUtils.toHttp(response), req == null ? Response.fail() : req.getAttribute(ERROR));
        return false;
    }


    private final String ERROR = "error";


    private void writeResponse(HttpServletResponse response, Object tips) {

        if (response == null || tips == null) {
            return;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(JSONObject.toJSONString(tips));
            printWriter.flush();
        } catch (IOException e) {
            logger.error("获取写入数据流异常", e);
        }

    }
}
