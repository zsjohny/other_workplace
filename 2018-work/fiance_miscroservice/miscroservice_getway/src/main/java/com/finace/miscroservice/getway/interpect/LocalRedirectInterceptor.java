package com.finace.miscroservice.getway.interpect;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.finace.miscroservice.commons.utils.UidUtils;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 本地测试的重定向向类
 * 用于测试环境的服务所有必须支持get方式
 */
@Configuration
@RefreshScope
public class LocalRedirectInterceptor {

    private Log log = Log.getInstance(LocalRedirectInterceptor.class);

    /**
     * 配置的云服务器的拦截路径
     * 格式 设备标识 i/a 也可以是具体的uid-拦截的模块( 支持多模块)  多个用,分开 例如
     * <p>
     * eaxxx-user|order,ebxxx-order|user
     * </p>
     */
    @Value("${localRedirect}")
    private String localRedirect;

    /**
     * 具体的连接本地的Ip和端口 如果是多个模块则是 多个端口(对应上面模块的顺序) 例如
     * http://localhost-8081|8082,http://localhost-8084|8083
     */
    @Value("${redirectUrl}")
    private String redirectUrl;


    /**
     * 是否打开重定向 true打开 false关闭
     */
    @Value("${open.direct}")
    private Boolean openRedirectFlag;

    private String SPLIT_SIGN = ",";


    /**
     * 判断设定参数符合
     *
     * @return
     */
    private Boolean checkInValidParam() {

        Boolean isForbidden = Boolean.TRUE;
        if (localRedirect.split(SPLIT_SIGN).length == redirectUrl.split(SPLIT_SIGN).length) {
            isForbidden = Boolean.FALSE;
        }
        return isForbidden;
    }


    @PostConstruct
    public void init() {
        //检测参数是否符合
        if (checkInValidParam()) {
            log.warn("拦截参数设定不符合规则");
        }
    }

    /**
     * 本机测试重定向
     *
     * @param context 请求上下文
     */
    public Boolean redirect(RequestContext context) {
        Boolean forwardFlag = Boolean.FALSE;

        if (!openRedirectFlag) {
            return forwardFlag;
        }


        try {
            if (StringUtils.isEmpty(localRedirect)) {
                return forwardFlag;
            }


            String[] localCaches = localRedirect.split(SPLIT_SIGN);
            int len = localCaches.length;
            for (int i = 0; i < len; i++) {

                if (StringUtils.isEmpty(localCaches[i])) {
                    continue;
                }
                //判断是否有分段 则进行分段处理
                String[] split = localCaches[i].split("-");
                String needForwardDevice = split[0];
                String needForwardMode = split[1];

                HttpServletRequest request = context.getRequest();

                String uid = request.getHeader(JwtToken.UID);

                //获取设备
                String device = UidUtils.decryptUid(uid);


                if (StringUtils.isEmpty(device)) {
                    log.info("Ip没有uid", Iptools.gainRealIp(request));
                    return forwardFlag;
                }
                //如果只是一个设备标识则只截取一个进行判断
                if (needForwardDevice.length() == 1) {
                    device = device.substring(0, 1);
                } else {
                    device = uid;
                }

                String[] modeArr = request.getRequestURI().split("/");

                //获取模块名称
                String mode = modeArr.length > 1 ? modeArr[1] : modeArr[0];

                if (device.equals(needForwardDevice) && needForwardMode.contains(mode)) {

                    HttpServletResponse response = context.getResponse();
                    StringBuilder builder = new StringBuilder("?");

                    for (Enumeration<String> enumeration = request.getParameterNames(); enumeration.hasMoreElements(); ) {
                        String key = enumeration.nextElement();
                        builder.append(key);
                        builder.append("=");
                        builder.append(request.getParameter(key));
                        builder.append("&");
                    }

                    String params = "";
                    if (builder.length() > 1) {
                        params = builder.substring(0, builder.length() - 1);
                    }


                    String[] forwardModes = needForwardMode.split("\\|");
                    int modeLen = forwardModes.length;

                    for (int j = 0; j < modeLen; j++) {
                        if (mode.equals(forwardModes[j])) {
                            String[] _redirectArr = redirectUrl.split(SPLIT_SIGN)[i].split("-");
                            String url = _redirectArr[0] + ":" + _redirectArr[1].split("\\|")[j] + request.getRequestURI().substring(1 + mode.length()) + params;
                            log.info("IP={} 请求={} 被重定向到请求={}", Iptools.gainRealIp(request), request.getRequestURI(), url);

                            response.sendRedirect(url);
                            context.setSendZuulResponse(false);
                            forwardFlag = Boolean.TRUE;
                            break;

                        }
                    }


                }

            }
        } catch (Exception e) {
            log.error("本地重定向拦截出错", e);
        }
        return forwardFlag;
    }


}
