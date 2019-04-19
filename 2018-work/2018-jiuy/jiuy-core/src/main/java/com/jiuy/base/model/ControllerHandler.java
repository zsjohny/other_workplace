package com.jiuy.base.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller的拦截回调
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/27 9:45
 * @Copyright 玖远网络
 */
public interface ControllerHandler {

    /**
     * controller 拦截器的回调
     * @param httpServletRequest httpServletRequest
     * @param httpServletResponse  httpServletResponse
     * @param o o
     * @author Aison
     * @date 2018/6/27 9:39
     */
    boolean controllerHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o);

}
