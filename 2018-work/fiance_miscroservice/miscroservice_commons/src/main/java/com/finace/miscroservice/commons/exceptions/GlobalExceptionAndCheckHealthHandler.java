package com.finace.miscroservice.commons.exceptions;

import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常和健康检查处理
 */
@InnerRestController
@ControllerAdvice
public class GlobalExceptionAndCheckHealthHandler {

    private Log logger = Log.getInstance(GlobalExceptionAndCheckHealthHandler.class);

    @ExceptionHandler(Exception.class)
    public Response recordGlobal(HttpServletRequest request, Exception e) {
        logger.error("IP={} 方法={} 全局异常", Iptools.gainRealIp(request), request.getRequestURI(), e);
        return Response.crash();
    }

    @GetMapping("healths")
    public String healths() {
        return "OK";
    }
}
