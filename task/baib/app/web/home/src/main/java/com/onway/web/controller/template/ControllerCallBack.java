package com.onway.web.controller.template;

/**
 * onway.com Inc.
 * Copyright (c) 2013-2013 All Rights Reserved.
 */

/**
 * <pre> 业务操作回调的接口 </pre>
 * 
 * @author guangdong.li
 * @version $Id: ServiceCallback.java, v 0.1 2013-11-4 下午3:31:46  Exp $
 */
public interface ControllerCallBack {

    public void check();

    /**
     * <pre> 执行业务逻辑
     * </pre>
     */
    public void executeService();
}
