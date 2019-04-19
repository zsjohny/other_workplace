package com.jiuy.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.exception.BizException;
import com.jiuy.exception.GlobalsEnums;
import com.jiuy.pojo.Config;
import com.jiuy.pojo.SqlParams;
import com.jiuy.pojo.SqlTable;
import com.jiuy.pojo.SqlType2JavaType;
import com.jiuy.util.Biz;


/**
 * 处理数据库的各种操作，返回供其他类操作的原始数据
 *
 * @author Aison
 * @version V1.0
 * @date 2017年12月28日 上午9:46:06
 */
public class JDBC {

    /**
     * isInit : 静态变量是否加载了驱动
     */
    private static Boolean isInit = false;

    /**
     * cn : 数据库连接
     */
    private Connection connection;

    /**
     * config : 配置信息
     */
    private Config config;

    /**
     * dbmd : 数据库的元信息
     */
    private DatabaseMetaData dbmd;


    /**
     * <p>Title: </p>
     * <p>Description: 构造函数config</p>
     *
     * @param config
     */
    public JDBC(Config config) {
        this.config = config;
        try {
            this.connection = DriverManager.getConnection(config.getUrl(), config.getUserName(), config.getPwd());
            this.dbmd = connection.getMetaData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(GlobalsEnums.CONNECTION_GET_ERROR, Biz.getFullException(e));
        }
    }


    /**
     * @param @param config
     * @return void    返回类型
     * @throws
     * @Title: loadDriverClass
     * @Description: 加载数据库驱动
     * @author Aison
     */
    private static void loadDriverClass(Config config) {
        try {
            synchronized (isInit) {
                if (isInit) {
                    return;
                }
                Class.forName(config.getClassDriver());
                isInit = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(GlobalsEnums.DRIVER_LOAD_ERROR, Biz.getFullException(e));
        }
    }

    /**
     * 获取一个jdbc实例
     *
     * @return JDBC    返回类型
     * @author Aison
     */
    public static JDBC instance(Config config) {
        if (!isInit) {
            loadDriverClass(config);
        }
        return new JDBC(config);
    }


    /**
     * 更新一个sql
     *
     * @param sql
     * @param param
     * @return Integer    返回类型
     * @author Aison
     */
    public Integer execute(String sql, String param) {
        PreparedStatement prs = null;
        ResultSet rs = null;
        try {
            prs = this.connection.prepareStatement(sql);
            return prs.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (prs != null) {
                    prs.close();
                }
                connection.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    /**
     * 获取某个表的所有的字段的注释
     *
     * @param tableName
     * @return Map<String       ,       String>    返回类型
     * @author Aison
     */
    private Map<String, String> getColumnComments(String tableName) {

        Map<String, String> columnComments = new HashMap<String, String>();
        ResultSet rs = null;
        try {
            rs = dbmd.getColumns(null, "%", tableName, "%");
            while (rs.next()) {
                String remarks = rs.getString("REMARKS") == null ? "" : rs.getString("REMARKS").replace("\r\n", " ");
                columnComments.put(rs.getString("COLUMN_NAME"), remarks);
            }
        } catch (Exception e) {
            throw new BizException(GlobalsEnums.COMMENTS_LOAD_ERROR, Biz.getFullException(e));
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    throw new BizException(GlobalsEnums.COMMENTS_LOAD_ERROR, Biz.getFullException(e2));
                }
            }
        }
        return columnComments;
    }


    /**
     * 获取主键
     *
     * @param schemaName
     * @param tableName
     * @return String    返回类型
     * @author Aison
     */
    private String getAllPrimaryKeys(String schemaName, String tableName) {

        String pkName = null;
        ResultSet rs = null;
        try {
            rs = dbmd.getPrimaryKeys(null, schemaName, tableName);
            while (rs.next()) {
                pkName = rs.getString("COLUMN_NAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                    throw new BizException(GlobalsEnums.PK_GET_ERROR, Biz.getFullException(e2));
                }
            }
        }
        return pkName;
    }


    /**
     * 构建SqlTable
     *
     * @param tableName
     * @param tableReamrk
     * @param tableType
     * @return SqlTable    返回类型
     * @author Aison
     */
    private SqlTable createTable(String tableName, String tableReamrk, String tableType, Config config) {

        SqlTable sqlTable = null;

        try {

            sqlTable = new SqlTable();
            sqlTable.switchTableType(tableType);
            sqlTable.setTableName(tableName);
            sqlTable.setTableComment(tableReamrk);
            String[] pojoNames = config.getProjoNames();
            String pojoName = "";
            if(pojoNames!=null) {
                pojoName = pojoNames[0];
            } else {
                pojoName = Biz.firstUpcase(Biz.underlineToCamel(tableName));
            }
            sqlTable.setPojoName(pojoName);
            sqlTable.setConfig(config);
            // 列的注释
            Map<String, String> commentsMap = getColumnComments(tableName);
            // 查询全集
            ResultSetMetaData rsData = connection.prepareStatement("select *  from  " + tableName + " where 1=-1").getMetaData();
            // 主键
            String primaryKey = getAllPrimaryKeys(null, tableName);
            // 列的集合
            List<SqlParams> sqlParamses = new ArrayList<SqlParams>();

            for (int i = 1; i <= rsData.getColumnCount(); i++) {

                SqlParams sqlParams = new SqlParams();
                // db的数据类型
                String sqlType = rsData.getColumnTypeName(i);
                // 通过db的数据类型获取 db与java的对应关系的对象
                SqlType2JavaType typeMapping = config.getJavaType(sqlType);
                // db列名
                String columnName = rsData.getColumnName(i);

                sqlParams.setIsPk(columnName.equals(primaryKey));
                sqlParams.setColumnName(columnName);
                sqlParams.setComments(commentsMap.get(columnName));
                System.out.println("columnName: " + columnName + " sqlType:" + sqlType);
                sqlParams.setFullJavaType(typeMapping.getFullJavaType());
                sqlParams.setProperty(Biz.underlineToCamel(columnName));
                sqlParams.setShortJavaType(typeMapping.getShortJavaType());
                sqlParams.setSqlType(typeMapping.getSqlType());
                sqlParams.setNotNeedImport(typeMapping.getIsNomarl());
                // 如果是主键 则给table赋值..
                if (sqlParams.getIsPk()) {
                    sqlTable.setPkSqlParams(sqlParams);
                }
                sqlParamses.add(sqlParams);
            }
            sqlTable.setSqlPars(sqlParamses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlTable;
    }


    /**
     * 获取sqlTbales的实例
     *
     * @param tableName
     * @return List<SqlTable>    返回类型
     * @author Aison
     */
    public List<SqlTable> getSqlTbales(String tableName, Config config) {
        try {
            List<SqlTable> sts = new ArrayList<SqlTable>();
            tableName = tableName == null ? "%" : tableName;
            ResultSet rs = dbmd.getTables(null, null, tableName, new String[]{"TABLE", "VIEW"});
            String remark;
            while (rs.next()) {
                remark = rs.getString("REMARKS");
                if (config.getUrl().indexOf("mysql") > 0) {
                    String sql = "SELECT TABLE_COMMENT FROM information_schema.tables where TABLE_NAME = '" + tableName + "'";
                    sql = Biz.valiArg(config.getSchema()) ? sql + " AND TABLE_SCHEMA = '" + config.getSchema() + "'" : sql;
                    ResultSet reamarkrs = connection.prepareStatement(sql).executeQuery();
                    while (reamarkrs.next()) {
                        remark = reamarkrs.getString(1);
                    }
                    reamarkrs.close();
                }
                sts.add(createTable(rs.getString("TABLE_NAME"), remark, rs.getString("TABLE_TYPE"), config));
            }
            return sts;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }


    /************************************************geter and seter****************************************************************/

    public Config getConfig() {
        return config;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public DatabaseMetaData getDbmd() {
        return dbmd;
    }

    public void setDbmd(DatabaseMetaData dbmd) {
        this.dbmd = dbmd;
    }


}
