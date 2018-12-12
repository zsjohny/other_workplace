package com.finace.miscroservice.commons.base;

import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Rc4Utils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * sql的动态执行
 * (用于不停机修改项目)
 */
@InnerRestController
public class SqlExecController {

    private static final Log log = Log.getInstance(SqlExecController.class);
    @Autowired
    @Lazy
    private DataSource dataSource;

    private final String UPDATE_SQL_SYNTAX = "UPDATE";
    private final String SELECT_SQL_SYNTAX = "SELECT";
    private final String INSERT_SQL_SYNTAX = "INSERT";
    private final String FORBIDDEN_SQL_SYNTAX = "*";
    private final Long MILLSECOND_INTERVAL = 1000L * 30;

    /**
     * 执行动态sql的请求
     *
     * @param sql 动态sql
     * @return
     */

    @RequestMapping("exec")
    public Map<String, Object> execSql(String auth, Long time, String sql, String sign) {

        Map<String, Object> returnMap = new HashMap<>(2);

        Connection connection = null;
        int row = 0;

        try {

            if (Math.abs(time - System.currentTimeMillis()) > MILLSECOND_INTERVAL) {
                log.warn("用户所传参数auth={},sql={},time={}时间不正确", auth, sql, time);
                return returnMap;
            }

            if (!Rc4Utils.toString(auth, time + "").equals(sql)) {
                log.warn("用户所传参数auth={},sql={},time={} 认证不正确", auth, sql, time);
                return returnMap;
            }

            Map<String, Object> dataMap = new TreeMap<>();
            dataMap.put("sql", sql);
            dataMap.put("time", time);
            dataMap.put("auth", auth);
            StringBuilder paramBuild = new StringBuilder(dataMap.size());

            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                paramBuild.append(entry.getKey());
                paramBuild.append("=");
                paramBuild.append(entry.getValue());
                paramBuild.append("&");

            }

            if (!Md5(paramBuild.toString()).equals(sign)) {
                log.warn("用户所传参数auth={},sql={},time={}加密方式正确", auth, sql, time);
                return returnMap;
            }


            sql = checkValidSql(sql);

            if (sql.isEmpty()) {
                return returnMap;
            }

            connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);


            connection.setAutoCommit(Boolean.FALSE);

            //查询
            if (sql.toUpperCase().startsWith(SELECT_SQL_SYNTAX)) {

                String resultColumn = sql.split("select|SELECT")[1].split("from|FROM")[0];

                String[] resultsArr = resultColumn.split(",");

                Set<String> columnNames = new HashSet<>(resultsArr.length);

                String columnName;

                for (String str : resultsArr) {
                    if (str.contains(".")) {
                        columnName = str.split("\\.")[1].trim();
                    } else {
                        columnName = str.trim();

                    }


                    if (columnName.toUpperCase().contains("AS")) {
                        columnName = columnName.split("as|AS")[1].trim();
                    }
                    columnNames.add(columnName);

                }

                ResultSet rs = statement.executeQuery();

                List<Map<String, String>> resultList = new ArrayList<>();
                Map<String, String> resultMap;

                while (rs.next()) {
                    resultMap = new HashMap<>(resultList.size());
                    for (String column : columnNames) {
                        resultMap.put(column, rs.getString(column));
                    }
                    resultList.add(resultMap);
                }
                row = resultList.size();

                returnMap.put("data", resultList);

                //修改
            } else if (sql.toUpperCase().startsWith(UPDATE_SQL_SYNTAX)) {
                row = statement.executeUpdate();
                //新增
            } else if (sql.toUpperCase().startsWith(INSERT_SQL_SYNTAX)) {
                row = statement.execute() ? 1 : 0;
            } else {
                log.warn("不支持其他sql操作,sql={}", sql);
                return returnMap;
            }

            connection.commit();
            statement.close();
            log.info("sql={}执行完毕", sql);
        } catch (Exception e) {
            log.error("sql={}执行异常", sql, e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {

            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }

        returnMap.put("row", row);
        return returnMap;

    }


    /**
     * 检测sql是否有效
     *
     * @param sql sql语句
     * @return
     */
    private String checkValidSql(String sql) {
        String validSql = "";
        if (sql == null || sql.isEmpty() || sql.contains(FORBIDDEN_SQL_SYNTAX)) {
            log.warn("sql={}包含不允许语法", sql);
            return validSql;
        }

        try {
            validSql = new CCJSqlParserManager().parse(new StringReader(sql)).toString();
        } catch (JSQLParserException e) {
            log.warn("sql={}检测异常", sql);
        }
        return validSql;


    }

    public static String Md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {

        }
        return result.toUpperCase();
    }


}
