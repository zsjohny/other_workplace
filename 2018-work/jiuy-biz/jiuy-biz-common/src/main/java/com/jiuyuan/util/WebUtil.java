package com.jiuyuan.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;

public class WebUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 或许项目根路径
     * 格式：http://dev.yujiejie.com:8080/item/
     * @param request
     * @return
     */
    public static String getWebBaseUrl(HttpServletRequest request) {
    	return getWebBaseUrl(request,request.getScheme());
    }
    /**
     * 或许项目根路径
     * 格式：http://dev.yujiejie.com:8080/item/
     * @param request
     * @param scheme  http or https
     * 
     * @return
     */
    
    public static String getWebBaseUrl(HttpServletRequest request,String scheme) {
    	StringBuffer webBaseUrl = new StringBuffer();
       	webBaseUrl.append(scheme);
      	webBaseUrl.append("://");
      	webBaseUrl.append(request.getServerName());
      	int port = request.getServerPort();
      	if(port != 80 && port != 0 ){
      		webBaseUrl.append(":");
          	webBaseUrl.append(port);
      	}
    	String path = request.getContextPath();
    	if(StringUtils.isNotEmpty(path)){
    		webBaseUrl.append("/");
    		webBaseUrl.append(path);
    	}
    	//webBaseUrl.append("/");
    	return webBaseUrl.toString();
    }
    
    /**
     * 或许项目根路径
     * 格式：http://dev.yujiejie.com:8080/item/
     * @param request
     * @return
     */
    public static String getWebAllUrl(HttpServletRequest request) {
    	String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();
    	//System.out.println("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);
    	return url;
    }
    /**
     * basePath=================
baseurl=================/m/ext/oauth/wapCheckLogin.json
RealPath=================D:\JYworkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp7\wtpwebapps\yujj-web\
webBasePath=================http://dev.yujiejie.com:8080/
获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url=http://dev.yujiejie.com/m/ext/oauth/wapCheckLogin.json?null
协议名：//域名=http://dev.yujiejie.com
获取项目名=
获取参数=null
获取全路径=http://dev.yujiejie.com:8080/m/ext/oauth/wapCheckLogin.json
     * @param request
     * @return
     */
    public static void test(HttpServletRequest request) {
    	String basePath = request.getContextPath();
    	System.out.println("basePath================="+basePath);
    	String baseurl = request.getRequestURI();
    	System.out.println("baseurl================="+baseurl);
    	String RealPath =  request.getSession().getServletContext().getRealPath("/");
    	System.out.println("RealPath================="+RealPath);
    	String ContextLoaderRealPath =   ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
    	String path = request.getContextPath();
    	String webBasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    	System.out.println("webBasePath================="+webBasePath);
    	String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();
    	System.out.println("获取全路径（协议类型：//域名/项目名/命名空间/action名称?其他参数）url="+url);
    	String url2=request.getScheme()+"://"+ request.getServerName();//+request.getRequestURI();
    	System.out.println("协议名：//域名="+url2);
    	System.out.println("获取项目名="+request.getContextPath());
    	System.out.println("获取参数="+request.getQueryString());
    	System.out.println("获取全路径="+request.getRequestURL());
    }
}
