package com.finace.miscroservice.commons.base;

import com.finace.miscroservice.commons.log.Log;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 控制层基类
 */
public abstract class AbstractController {

    // 日志
    private Log logger = Log.getInstance(AbstractController.class);

    /**
     * 直接返回一个字符串
     *
     * @param response
     * @param text     需要返回的字符串
     */
    protected void textResponse(HttpServletResponse response, String text) {
        PrintWriter out = null;
        response.setContentType("text/html;charset=UTF-8");
        try {
            out = response.getWriter();
            out.write(text);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


}
