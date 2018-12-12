package com.onway.web.controller.template;

import org.apache.log4j.Logger;
import com.onway.platform.common.exception.BaseRuntimeException;
import com.onway.platform.common.service.template.impl.ServiceTemplateImpl;
import com.onway.web.controller.result.JsonResult;

/**
 * 服务模板的实现
 * 
 * @author guangdong.li
 * @version $Id: ServiceTemplateImpl.java, v 0.1 15 Feb 2016 11:44:58 guangdong.li Exp $
 */
public class ControllerTemplateImpl implements ControllerTemplate {

    /** logger */
    private static final Logger logger = Logger.getLogger(ServiceTemplateImpl.class);

    @Override
    public void execute(JsonResult jsonResult, ControllerCallBack callBack) {

        try {

            callBack.check();

            callBack.executeService();

            /*if (jsonResult.isBizSucc()) {
                jsonResult.setErrMsg(ErrorCode.SUCCESS.getDesc());
            }*/
        } catch (BaseRuntimeException ce) {
            logger.warn("业务处理失败", ce);
            jsonResult.markResult(false, ce.getCode(), ce.getMessage());
        } catch (Throwable e) {
            logger.error("业务处理系统异常", e);
            /*jsonResult.markResult(false, ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getDesc());*/
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("业务处理系统操作结果：" + jsonResult);
            }
        }
    }
}
