package com.finace.miscroservice.commons.plug.mybatis.handler;

import com.finace.miscroservice.commons.log.Log;

/**
 * mybatis的注解拦截处理器
 */
public class MybatisAnnotationHandler {

    private Log log = Log.getInstance(MybatisAnnotationHandler.class);

    public String update(String sql) {
        log.info("sql={} start update", sql);
        return sql;

    }


    public String selectOne(String sql) {
        log.info("sql={} start selectOne", sql);
        return sql;

    }


    public String selectAll(String sql) {
        log.info("sql={} start selectAll", sql);
        return sql;

    }


    public String save(String sql) {
        log.info("sql={} start save", sql);
        return sql;

    }


}
