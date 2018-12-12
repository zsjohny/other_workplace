package com.finace.miscroservice.commons.exceptions;

import com.finace.miscroservice.commons.annotation.InnerRestController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;

/**
 * 错误页面异常处理
 */
@InnerRestController
@ConditionalOnExpression("${spring.security.enabled}")
public class ErrorExceptionHandler implements ErrorController {
    @Override
    public String getErrorPath() {
        return "";
    }


}
