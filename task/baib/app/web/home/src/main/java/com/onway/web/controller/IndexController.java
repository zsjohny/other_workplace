/**
 * onway.com Inc.
 * Copyright (c) 2013-2013 All Rights Reserved.
 */
package com.onway.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onway.web.controller.result.JsonResult;

/**
 * 
 * @author guangdong.li
 * @version $Id: IndexController.java, v 0.1 2013-9-22 ÏÂÎç5:30:36  Exp $
 */
@Controller
public class IndexController {

    @RequestMapping("/test.do")
    @ResponseBody
    public Object testDo(HttpServletRequest request, ModelMap modelMap) throws Exception {
        return new JsonResult(true);
    }
}
