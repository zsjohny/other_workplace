package com.yujj.web.controller.advice;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.util.http.MimeTypes;
import com.jiuyuan.web.help.JsonpResponse;
import com.jiuyuan.util.http.HttpUtil;

@ControllerAdvice
public class CommonControllerAdvice {

    private static Logger logger = LoggerFactory.getLogger(CommonControllerAdvice.class);

    @Autowired
    private ObjectMapper objectMapper;

    @ModelAttribute("params")
    public Map<String, Object> getParams(@RequestParam Map<String, Object> params) {
        return params;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("arr_params")
    public Map<String, String[]> getParams(HttpServletRequest request) {
        return request.getParameterMap();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error("Error in controller. uri: {}, referer: {}, ua: {}, remoteIp: {}",
            HttpUtil.getRequestUrl(request), HttpUtil.getReferer(request), HttpUtil.getUserAgent(request),
            HttpUtil.getClientIp(request), e);

        if (HttpUtil.isJsonRequest(request)) {
            JsonpResponse jsonpResponse = new JsonpResponse(request.getParameter("jsonp_callback"));
            jsonpResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);

            try {
                String text = objectMapper.writeValueAsString(jsonpResponse);
                HttpUtil.sendResponse(response, MimeTypes.APPLICATION_JSON, "UTF-8", text);
            } catch (Exception ex) {
                // ignore
            }
            return null;
        }
		
        return Constants.ERROR_MAINTENANCE;
    }
}
