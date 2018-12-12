package com.goldplusgold.td.sltp.core.operate.exceptions;


import com.goldplusgold.td.sltp.core.viewmodel.UserSltpOperVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.goldplusgold.td.sltp.core.operate.enums.ResponseTypeEnum.FAIL_PARAM;

/**
 * 全局的异常处理
 * Created by Ness on 2017/5/12.
 */
@ControllerAdvice
@ResponseBody
public class GlobalUserSltpExceptionController {
    Logger logger = LoggerFactory.getLogger(GlobalUserSltpExceptionController.class);

    @ExceptionHandler(Exception.class)
    public UserSltpOperVM recordGobalExcep(Exception e) {
        UserSltpOperVM vm = new UserSltpOperVM();
        logger.warn("全局异常", e);
        vm.setCode(FAIL_PARAM.toCode());
        return vm;
    }
}
