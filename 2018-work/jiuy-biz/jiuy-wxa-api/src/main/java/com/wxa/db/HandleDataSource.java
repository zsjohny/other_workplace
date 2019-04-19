package com.wxa.db;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandleDataSource {
    public static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void putAll(Map<Object, Object> allDataSource) {
        allDataSourceCache.putAll(allDataSource);
    }

    private static Map<Object, Object> allDataSourceCache = new ConcurrentHashMap<>();

    public static DataSource getDataSourceByKey(Object key) {

        if (key == null) {
            key = "write";

        }
        Object datasource = allDataSourceCache.get(key);

        return (DataSource) datasource;
    }


    public static void putDataSource(String datasource) {
        holder.set(datasource);
    }

    /**
     * 获取当前线程的数据源
     *
     * @return
     */
    public static String getDataSource() {
        String datasource = holder.get();

        return datasource;
    }

    /**
     * 获取当前线程的数据源
     *
     * @return
     */
    public static void clear() {
        holder.remove();
    }


}