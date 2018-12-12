package com.finace.miscroservice.commons.plug.mybatis.util;

import com.finace.miscroservice.commons.annotation.Column;
import com.finace.miscroservice.commons.annotation.Table;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.plug.mybatis.mapper.MybatisBaseMapper;
import org.mybatis.spring.SqlSessionTemplate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.finace.miscroservice.commons.plug.mybatis.config.MybatisConfig.APPLICATION_CONTEXT;
import static com.finace.miscroservice.commons.plug.mybatis.config.MybatisConfig.IS_CAMEL;

/**
 * mybatis的操作工具
 */
public class MybatisOperaterUtil {
    private MybatisOperaterUtil() {

    }

    public static MybatisOperaterUtil getInstance() {
        return MybatisOperaterUtilHolder.INSTANCE;
    }

    private static SqlSessionTemplate sqlSessionTemplate;

    private static class MybatisOperaterUtilHolder {

        static {
            APPLICATION_CONTEXT.getBean(MybatisBaseMapper.class);
            sqlSessionTemplate = APPLICATION_CONTEXT.getBean(SqlSessionTemplate.class);
        }

        private final static MybatisOperaterUtil INSTANCE = new MybatisOperaterUtil();

    }


    private Log log = Log.getInstance(MybatisOperaterUtil.class);

    private final String INSERT_SQL = "INSERT INTO `%s` (%s) VALUES (%s) ";
    private final String UPDATE_SQL = "UPDATE  `%s` SET %s ";
    private final String SELECT_SQL = "SELECT  %s FROM  `%s`";
    private final String SELECT_ONE_SUFFIX = " LIMIT 1";
    private final String STR_TYPE = "java.lang.String";


    public <T> int update(T t, MybatisSqlWhereBuildUtil buildUtil) {
        int result = 0;
        String sql = createSql(t, UPDATE_SQL);
        if (sql.isEmpty()) {
            return result;
        }

        return sqlSessionTemplate.update((MybatisBaseMapper.class.getCanonicalName() + ".update").intern()
                , buildUtil == null ? sql : sql + buildUtil.build());
    }


    public <T> int save(T t) {
        int result = 0;
        String sql = createSql(t, INSERT_SQL);
        if (sql.isEmpty()) {
            return result;
        }
        return sqlSessionTemplate.insert((MybatisBaseMapper.class.getCanonicalName() + ".save").intern()
                , sql);

    }

    public <T> List<T> finAll(T t, MybatisSqlWhereBuildUtil buildUtil) {
        String sql = createSql(t, SELECT_SQL);
        if (sql.isEmpty()) {
            return null;
        }

        if (buildUtil == null) {
            buildUtil = MybatisSqlWhereBuildUtil.getInstance();
        }

        List<Map> resultList = sqlSessionTemplate.selectList((MybatisBaseMapper.class.getCanonicalName() + ".selectAll").intern(),
                sql + buildUtil.build());


        List<T> lists = new ArrayList<>();

        if (resultList != null && !resultList.isEmpty()) {

            for (Map map : resultList) {
                lists.add(convertMap2Obj(t, map));
            }

        }


        return lists;
    }


    public <T> T findOne(T t, MybatisSqlWhereBuildUtil buildUtil) {
        String sql = createSql(t, SELECT_SQL);
        if (sql.isEmpty()) {
            return null;
        }

        if (buildUtil == null) {
            buildUtil = MybatisSqlWhereBuildUtil.getInstance();
        }
        buildUtil.link(SELECT_ONE_SUFFIX);

        Map resultMap = sqlSessionTemplate.selectOne((MybatisBaseMapper.class.getCanonicalName() + ".selectOne").intern(),
                sql + buildUtil.build());


        if (resultMap != null && !resultMap.isEmpty()) {
            t = convertMap2Obj(t, resultMap);
        } else {
            t = null;
        }
        return t;
    }


    /**
     * 创建sql
     *
     * @param t       实体类
     * @param sqlType sql的类型
     * @return
     */

    private <T> String createSql(T t, String sqlType) {
        String sql = "";
        try {
            Class<?> superClass = t.getClass();
            Field[] fields = superClass.getFields();

            StringBuilder columnBuild = new StringBuilder();
            StringBuilder valueBuild = new StringBuilder();

            if (fields != null && fields.length != 0) {
                linkValidField(fields, superClass, sqlType, columnBuild, valueBuild, t);
            }
            fields = superClass.getDeclaredFields();
            if (fields != null && fields.length != 0) {
                linkValidField(fields, superClass, sqlType, columnBuild, valueBuild, t);
            }

            if (sqlType.equals(SELECT_SQL) && columnBuild.length() == 0) {

                //重新赋值全部查询
                fields = superClass.getFields();

                if (fields != null && fields.length != 0) {
                    linkAllField(fields, columnBuild, sqlType);
                }
                fields = superClass.getDeclaredFields();
                if (fields != null && fields.length != 0) {
                    linkAllField(fields, columnBuild, sqlType);
                }


            }


            String tableName;
            Table annotation = superClass.getAnnotation(Table.class);
            if (annotation != null) {
                tableName = annotation.value();
            } else {
                tableName = superClass.getSimpleName();

            }


            if (sqlType.equals(INSERT_SQL)) {

                if (columnBuild.length() == 0 || valueBuild.length() == 0) {
                    return sql;
                }

                sql = String.format(sqlType, tableName, columnBuild.toString(), valueBuild.toString());

            } else if (sqlType.equals(UPDATE_SQL)) {

                if (columnBuild.length() == 0) {
                    return sql;
                }

                sql = String.format(sqlType, tableName, columnBuild.toString());

            } else if (sqlType.equals(SELECT_SQL)) {

                sql = String.format(sqlType, columnBuild.toString(), tableName);


            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("sql 保存失败", e);
        }

        return sql;


    }

    /**
     * /**
     * 连接所有的字段
     *
     * @param fields      字段类型
     * @param sqlType     sql的类型
     * @param columnBuild 列表的build
     */
    private void linkAllField(Field[] fields, StringBuilder columnBuild, String sqlType) {

        for (Field field : fields) {
            String columnName = field2ColumnName(field);

            if (sqlType.equals(SELECT_SQL)) {
                if (columnBuild.length() > 0) {
                    columnBuild.append(",");

                }
                linkColunmNameAndFieldName(columnBuild, field.getName(), columnName);
            }

        }

    }

    /**
     * 连接列名和字段名
     *
     * @param columnBuild 列表的build
     * @param fieldName   字段名
     * @param columnName  列表名
     */
    private void linkColunmNameAndFieldName(StringBuilder columnBuild, String fieldName, String columnName) {

        columnBuild.append("`");
        columnBuild.append(columnName);
        columnBuild.append("` AS ");
        columnBuild.append(fieldName);

    }

    /**
     * 字段转列名
     *
     * @param field 字段的名称
     * @return
     */
    private String field2ColumnName(Field field) {
        String columnName;

        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null&&!annotation.value().isEmpty()) {
            columnName = annotation.value();
        } else {
            columnName = field.getName();
            if (IS_CAMEL) {
                columnName = camel2Underline(columnName);
            }
        }
        return columnName;

    }


    /**
     * 连接有效的字段
     *
     * @param fields      字段类型
     * @param superClass  父级的类
     * @param sqlType     sql的类型
     * @param columnBuild 列表的build
     * @param valueBuild  有效值得build
     * @param t           实体类
     */
    private <T> void linkValidField(Field[] fields, Class<?> superClass, String sqlType, StringBuilder columnBuild,
                                    StringBuilder valueBuild, T t)
            throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor descriptor;
        for (Field field : fields) {
            try {
                descriptor = new PropertyDescriptor(field.getName(), superClass);
            } catch (IntrospectionException e) {
                continue;

            }


            Object invoke = descriptor.getReadMethod().invoke(t);
            if (invoke != null) {
                if (columnBuild.length() > 0) {
                    columnBuild.append(",");

                }
                String columnName = field2ColumnName(field);

                if (sqlType.equals(INSERT_SQL)) {


                    if (valueBuild.length() > 0) {
                        valueBuild.append(",");

                    }
                    columnBuild.append("`");
                    columnBuild.append(columnName);
                    columnBuild.append("`");

                    if (field.getType().getName().equals(STR_TYPE)) {
                        contactStr(valueBuild, invoke);
                    } else {

                        valueBuild.append(invoke);
                    }

                } else if (sqlType.equals(UPDATE_SQL)) {
                    columnBuild.append("`");
                    columnBuild.append(columnName);
                    columnBuild.append("`=");

                    if (field.getType().getName().equals(STR_TYPE)) {
                        contactStr(columnBuild, invoke);
                    } else {
                        columnBuild.append(invoke);

                    }

                } else if (sqlType.equals(SELECT_SQL)) {
                    linkColunmNameAndFieldName(columnBuild, field.getName(), columnName);

                }
            }
        }


    }

    /**
     * 连接字符串
     *
     * @param builder 连接的build
     * @param invoke  连接的值
     */
    private void contactStr(StringBuilder builder, Object invoke) {
        builder.append("'");
        builder.append(invoke);
        builder.append("'");

    }

    /**
     * map转实体类
     *
     * @param t   实体类
     * @param map map类
     * @return
     */
    private  <T> T convertMap2Obj(T t, Map map) {
        T obj = null;
        try {
            Boolean unAssignFlag = Boolean.TRUE;
            Class<?> superClass = t.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(superClass);
            obj = (T) superClass.newInstance();

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                Object value = map.get(propertyName);
                if (value != null) {
                    if (unAssignFlag) {
                        unAssignFlag = Boolean.FALSE;
                    }
                    descriptor.getWriteMethod().invoke(obj, value);
                }
            }

            if (unAssignFlag) {
                obj = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        return obj;
    }

    private final char UNDERLINE = '_';

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    private String camel2Underline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
