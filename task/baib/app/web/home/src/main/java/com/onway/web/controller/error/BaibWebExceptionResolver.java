package com.onway.web.controller.error;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ModelAndView;

import com.onway.web.frameset.smvc.exception.handler.ExceptionHandler;
import com.onway.web.frameset.smvc.util.WebHelper;

/**
 * 网站默认异常处理器
 * 
 * @author li.hong
 * @version $Id: baibWebExceptionResolver.java, v 0.1 2016年9月2日 下午5:42:38 li.hong Exp $
 */
public class BaibWebExceptionResolver implements ExceptionHandler, Ordered {

    /** logger */
    protected static final Logger logger = Logger.getLogger(BaibWebExceptionResolver.class);

    /** 默认异常页*/
    private String                errorPage;

    /** web辅助类 */
    private WebHelper             webHelper;

    /**
     * 
     * @see com.yylc.web.frameset.smvc.exception.handler.ExceptionHandler#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.Exception)
     */
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
                        Exception ex) {
        // TODO 待优化
        mv.setViewName(errorPage);
        // mv.addObject(ERROR_MSG, ((baibWebCoreException) ex).getMessage());
    }

    /** 
     * @throws IOException 
     * @see com.yylc.web.frameset.smvc.exception.handler.ExceptionHandler#processJson(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.servlet.ModelAndView, java.lang.Exception)
     */
    @Override
    public void processJson(HttpServletRequest request, HttpServletResponse response,
                            ModelAndView mv, Exception ex) {
        // TODO 待优化
        /* webHelper.writeJson(response, WebBaseResult.retFailMessage("系统异常,请稍后再试哦!"),
             DEFAULT_ENCODING);*/

        mv = null;
    }

    /** 
     * @see com.yylc.web.frameset.smvc.exception.handler.ExceptionHandler#support(javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.Exception)
     */
    @Override
    public boolean support(HttpServletRequest request, Object handler, Exception ex) {
        if (ex != null /*&& (ex instanceof FrontWebCoreException)*/) {
            return true;
        }
        return false;
    }

    /** 
     * @see com.yylc.web.frameset.smvc.exception.handler.ExceptionHandler#logError(java.lang.Exception)
     */
    @Override
    public void logError(Exception ex) {
    }

    /** 
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder() {
        return 0;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public void setWebHelper(WebHelper webHelper) {
        this.webHelper = webHelper;
    }
}
