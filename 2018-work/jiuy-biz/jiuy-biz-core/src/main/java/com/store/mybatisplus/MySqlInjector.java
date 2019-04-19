package com.store.mybatisplus;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
//import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.entity.TableInfo;
//import com.baomidou.mybatisplus.mapper.AutoSqlInjector;
import com.baomidou.mybatisplus.mapper.AutoSqlInjector;
/**
 * 注入自定义SQL
 * @author zhaoxinglin
 *
 */
public class MySqlInjector extends AutoSqlInjector {
	  private static final Logger logger = LoggerFactory.getLogger(MySqlInjector.class);
    @Override
    public void inject(Configuration configuration, MapperBuilderAssistant builderAssistant, Class<?> mapperClass,
            Class<?> modelClass, TableInfo table) {
        /* 添加一个自定义方法 */
        deleteAllUser(mapperClass, modelClass, table);
    }

    public void deleteAllUser(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
//    	logger.info("注入自定义SQL，MySqlInjector");
    	
        /* 执行 SQL ，动态 SQL 参考类 SqlMethod */
        String sql = "delete from " + table.getTableName();

        /* mapper 接口方法名一致 */
        String method = "deleteAll";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
//        this.addMappedStatement(mapperClass, method, sqlSource, SqlCommandType.DELETE, Integer.class);
        this.addDeleteMappedStatement(mapperClass,method,sqlSource);
    }

}