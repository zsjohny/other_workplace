package com.finace.miscroservice.commons.base;


import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.IdUtil;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 基类action
 */
@Controller
@Scope("prototype")
public abstract class BaseController extends AbstractController {

    private Log logger = Log.getInstance(BaseController.class);

    protected HttpServletRequest request;
    protected ThreadLocal<HttpServletResponse> response=new ThreadLocal<>();

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response.set(response);
    }


    protected Integer paramInt(HttpServletRequest request, String str) {
        return NumberUtil.getInt(request.getParameter(str));
    }

    protected Long paramLong(HttpServletRequest request, String str) {
        return NumberUtil.getLong(request.getParameter(str));
    }

    protected String paramString(HttpServletRequest request, String str) {
        return StringUtils.isNull(request.getParameter(str));
    }

    public Map<String, Object> getRequestFormMap(HttpServletRequest request) throws UnsupportedEncodingException {
        String str = getRequestParams(request);
        Map<String, Object> params = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    params.put(paramName, paramValue);
                }
            }
        }
        if (StringUtils.isNotBlank(str) && params.size() == 0) {
            String str1 = java.net.URLDecoder.decode(str, "UTF-8");
            String[] strs = str.split("name=");
            String[] strs1 = str1.split("&");
            for (int i = 1; i < strs.length; i++) {
                String temp = strs[i].substring(0, strs[i].indexOf("------"));
                int index = temp.indexOf("\"", 1);
                index = index + 1;
                String key = temp.substring(0, index);
                String value = temp.substring(index, temp.length());
                params.put(key, value);
            }
            for (int i = 0; i < strs1.length; i++) {
                String[] temp = strs1[i].split("=");
                params.put(temp[0], temp[1]);
            }
        }
        return params;
    }

    /**
     * 使用 request.getInputStream()读取回调数据流
     *
     * @param request
     * @return
     */
    public String getRequestParams(HttpServletRequest request) {
        String params = "";
        try {
            request.setCharacterEncoding("UTF-8");
            InputStream in = request.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            params = sb.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return params;
    }

    /**
     * 获取参数map (把HttpServletRequest里的ParameterMap统一转换成HashMap)
     *
     * @param request
     * @return
     */
    public Map<String, String> getParamsMap(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, Object> reqParamMap = new HashMap<String, Object>();
        reqParamMap.putAll(request.getParameterMap());
        for (String key : reqParamMap.keySet()) {
            paramMap.put(key, request.getParameter(key));
        }
        return paramMap;
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public String getUserId() {
        String userId = String.valueOf(IdUtil.getId());
        if ("null".equals(userId)) {
            userId = request.getParameter("userId");
        }
        return userId;
    }


    /**
     * 输出html文件流
     * @param html
     * @throws IOException
     */
    public void outHtml(String html) throws IOException{
        HttpServletResponse httpServletResponse = response.get();
        if(httpServletResponse==null){
            return;
        }
        httpServletResponse.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html");
        httpServletResponse.setContentType("text/html;charset=utf-8");
        html = "<script type=\"text/javascript\" src=\"https://code.jquery.com/jquery-3.3.1.js\"></script>"+html;
//        html = "<script type=\"text/javascript\" src=\"https://www.etongjin.net/activity/jquery.min.js\"></script>"+html;
        logger.info(html);
        httpServletResponse.getWriter().write(html);

        response.remove();
//        response.getOutputStream().write(html.getBytes());
//        printWriter.write(html);
//        printWriter.flush();
//        printWriter.close();

    }






}
