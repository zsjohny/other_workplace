package com.jiuy.base.Interceptor;

import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理类..可以记录异常日志
 * @author Aison
 * @date 2018/5/19 21:25
 * @Copyright: 玖远网络
 */
@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handle(Exception e) {
       return  Biz.exceptionHandler(e);
    }


    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Object handleMy(BizException e) {
        return  Biz.exceptionHandler(e);
    }
}
