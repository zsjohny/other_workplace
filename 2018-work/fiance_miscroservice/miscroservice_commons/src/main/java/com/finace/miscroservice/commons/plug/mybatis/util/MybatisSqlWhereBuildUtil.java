package com.finace.miscroservice.commons.plug.mybatis.util;

import java.util.regex.Pattern;

/**
 * mybatis的更新构建的util
 */
public class MybatisSqlWhereBuildUtil {

    private MybatisSqlWhereBuildUtil() {

    }

    private final String EQ = "=";
    private final String NEQ = "!=";
    private final String GT = ">";
    private final String GTE = ">=";
    private final String LT = "<";
    private final String LTE = "<=";
    private final String LIKE = "LIKE";
    private final String LIKE_SIGN = "%";
    private final String LIMIT = " LIMIT %d,%d";
    private final String WHERE = " WHERE ";
    private final String EXTRA_WHERE = " WHERE  LIMIT";
    private final String STR_TYPE = "java.lang.String";


    private ThreadLocal<StringBuilder> sqlBuild = InheritableThreadLocal.withInitial(() -> new StringBuilder(WHERE));

    public static MybatisSqlWhereBuildUtil
    getInstance() {
        return MybatisSqlWhereBuildUtilHolder.INSTANCE;
    }

    private static class MybatisSqlWhereBuildUtilHolder {

        private final static MybatisSqlWhereBuildUtil INSTANCE = new MybatisSqlWhereBuildUtil();
    }


    /**
     * 等于
     *
     * @param column 列表名
     * @param val    值
     * @return
     */
    public MybatisSqlWhereBuildUtil eq(String column, Object val) {
        link(column, EQ, val);
        return getInstance();
    }


    /**
     * 不等于
     *
     * @param column 列表名
     * @param val    值
     * @return
     */
    public MybatisSqlWhereBuildUtil neq(String column, Object val) {
        link(column, NEQ, val);
        return getInstance();
    }


    /**
     * 大于
     *
     * @param column 列表名
     * @param val    值
     * @return
     */
    public MybatisSqlWhereBuildUtil gt(String column, Object val) {
        link(column, GT, val);
        return getInstance();
    }


    /**
     * 大于等于
     *
     * @param column 列表名
     * @param val    值
     * @return
     */
    public MybatisSqlWhereBuildUtil gte(String column, Object val) {
        link(column, GTE, val);
        return getInstance();
    }


    /**
     * 小于
     *
     * @param column 列表名
     * @param val    值
     * @return
     */
    public MybatisSqlWhereBuildUtil lt(String column, Object val) {
        link(column, LT, val);
        return getInstance();
    }

    /**
     * 小于等于
     *
     * @param column 列表名
     * @param val    值
     * @return
     */
    public MybatisSqlWhereBuildUtil lte(String column, Object val) {
        link(column, LTE, val);
        return getInstance();
    }

    /**
     * 模糊查询
     *
     * @param column 模糊的列明
     * @param val    模糊的值 格式有%val,val%,val(不写默认全模糊查询)
     * @return
     */
    public MybatisSqlWhereBuildUtil like(String column, String val) {
        StringBuilder valBuild = new StringBuilder();
        if (val.contains(LIKE_SIGN)) {
            valBuild.append(val);
        } else {
            valBuild.append(LIKE_SIGN);
            valBuild.append(val);
            valBuild.append(LIKE_SIGN);
        }
        link(column, LIKE, valBuild.toString());
        return getInstance();
    }


    /**
     * 链接
     *
     * @param column 列表的名称
     * @param arith  运算
     * @param val    值
     */
    private void link(String column, String arith, Object val) {
        if (sqlBuild.get().length() != WHERE.length()) {
            and();
        }
        sqlBuild.get().append("`");
        sqlBuild.get().append(column);
        sqlBuild.get().append("` ");
        sqlBuild.get().append(arith);
        sqlBuild.get().append(" ");
        if (val.getClass().getName().equals(STR_TYPE)) {
            sqlBuild.get().append("'");
            sqlBuild.get().append(val);
            sqlBuild.get().append("'");

        } else {
            sqlBuild.get().append(val);
        }

    }


    private void and() {
        sqlBuild.get().append(" AND ");
    }

    /**
     * 页数
     *
     * @param start 开始页数 默认第一页开始
     * @param count 每页返回的数目
     * @return
     */
    public MybatisSqlWhereBuildUtil page(Integer start, Integer count) {
        sqlBuild.get().append(String.format(LIMIT, ((start <= 1 ? 1 : start) - 1) * count, count));
        return getInstance();
    }

    public void link(String sql) {
        sqlBuild.get().append(" ");
        sqlBuild.get().append(sql);
        sqlBuild.get().append(" ");
    }

    /***防止查询单个增加page条件**/
    private final Pattern pattern = Pattern.compile("LIMIT[ ,\\d]+LIMIT[ ,\\d]+$");

    /**
     * 构建sql
     *
     * @return
     */
    public String build() {
        String build = sqlBuild.get().toString();
        if (build.startsWith(EXTRA_WHERE)) {
            build = build.replaceAll(WHERE, "");
        }
        if (pattern.matcher(build).find()) {
            //去除多余limit
            build = build.replaceAll("LIMIT \\d+,\\d+", "");
        }
        sqlBuild.remove();
        return build;
    }


}
