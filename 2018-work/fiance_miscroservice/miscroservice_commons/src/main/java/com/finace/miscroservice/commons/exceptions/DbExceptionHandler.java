package com.finace.miscroservice.commons.exceptions;

import com.finace.miscroservice.commons.config.DbConfig;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

import static com.finace.miscroservice.commons.auth.DynamicDataSourceInterceptor.DYNAMIC_DATASOURCE_CACHE;

/**
 * db的异常出俩
 */
@ControllerAdvice
public class DbExceptionHandler {

    private Log log = Log.getInstance(DbExceptionHandler.class);

    @ExceptionHandler(SQLException.class)
    public void dbHandler(HttpServletRequest request, SQLException e) {
        log.error("IP={} 执行方法={} sql 异常", Iptools.gainRealIp(request), request.getRequestURI(), e);
        DYNAMIC_DATASOURCE_CACHE.put(DbConfig.READ_DATA_SOURCE, Boolean.FALSE);

    }

}
