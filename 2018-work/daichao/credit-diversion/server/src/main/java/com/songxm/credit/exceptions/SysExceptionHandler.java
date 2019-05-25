package com.songxm.credit.exceptions;

import com.songxm.credit.comon.credit.diversion.dto.ComResultRspDTO;
import lombok.extern.slf4j.Slf4j;
import moxie.cloud.service.server.ServerException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
@Order(-2732)
public class SysExceptionHandler {

	/**
     * 参数异常
	 * @param e
	 * @return
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    private ComResultRspDTO illegalParamsExceptionHandler(MethodArgumentNotValidException e) {
        log.error("invalid request!", e);
        return  new ComResultRspDTO(400,"无效的参数请求");
    }

    @ExceptionHandler(ServerException.class)
    @ResponseBody
    private ComResultRspDTO serverException(MethodArgumentNotValidException e) {
        log.error("invalid request!", e);
        return  new ComResultRspDTO(500,e.getMessage());
    }
    /**
     * 其他未处理异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ComResultRspDTO runtimeExceptionHandler(Exception e) {
        log.error(" huge error：{}", e);
        return new ComResultRspDTO(500,"系统异常");
    }


}