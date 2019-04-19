package com.jiuy.pojo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据库每张表对应一个SqlTbale
 *
 * @author Aison
 * @version V1.0
 * @date 2017年12月28日 上午10:29:07
 */
public class SqlTable {


    /**
     * tableName : 表名称
     */
    private String tableName;
    /**
     * sqlPars : 列属性
     */
    private List<SqlParams> sqlPars;

    /**
     * pkSqlParams : 主键的字段类型
     */
    private SqlParams pkSqlParams;

    /**
     * voName : java的是pojo名称
     */
    private String pojoName;
    /**
     * tableComment : 表备注
     */
    private String tableComment;
    /**
     * tableType : TableType 枚举
     */
    private TableType tableType;
    /**
     * config : 对应这条数据的config
     */
    private Config config;

    /**
     * versonColumn : 数据版本的的字段
     */
    private SqlParams versonColumn;


    /**
     * 获取所有需要引入的java的类型
     *
     * @return Set<String>    返回类型
     * @author Aison
     */
    public Set<String> getAllImportJavaType() {
        Set<String> needImportTypeSet = new HashSet<String>();
        for (SqlParams sqlParam : sqlPars) {
            // 需要显示导入的
            if (!sqlParam.getNotNeedImport()) {
                needImportTypeSet.add(sqlParam.getFullJavaType());
            }
        }
        return needImportTypeSet;
    }

    /**
     * 传递表类型判断表类型
     *
     * @param tableType
     * @author Aison
     */
    public void switchTableType(String tableType) {
        if ("TABLE".equals(tableType)) {
            this.tableType = TableType.TABLE;
        } else if ("VIEW".equals(tableType)) {
            this.tableType = TableType.VIEW;
        } else if ("NICKNAME".equals(tableType)) {
            this.tableType = TableType.NICKNAME;
        }
    }

    /**
     * @return String    返回类型
     * @author Aison
     */
    public String getMapperNameWithPackage() {
        return config.getMapperPackage() + "." + pojoName + "Mapper";
    }

    /**
     * 获取xml的绝对路径
     *
     * @return String    返回类型
     * @author Aison
     */
    public String getXmlAbsolutePath() {
        return this.config.getXmlPath() + "" + config.getXmlPackage().replaceAll("\\.", "\\\\") + "\\" + pojoName + "Mapper.xml";
    }

    /**
     * 获取pojo的绝对路径
     *
     * @return String    返回类型
     * @author Aison
     */
    public String getPojoAbsolutePath() {
        return this.config.getPojoPath() + "" + config.getPojoPackage().replaceAll("\\.", "\\\\") + "\\" + pojoName + ".java";
    }

    /**
     * 获取pojoQuery的绝对路径
     *
     * @return String    返回类型
     * @author Aison
     */
    public String getPojoQueryAbsolutePath() {
        return this.config.getPojoPath() + "" + config.getPojoPackage().replaceAll("\\.", "\\\\") + "\\" + pojoName + "Query.java";
    }

    /**
     * 获取mapper.java的绝对路径
     *
     * @return String    返回类型
     * @author Aison
     */
    public String getMapperAbsolutePath() {
        return this.config.getMapperPath() + "" + config.getMapperPackage().replaceAll("\\.", "\\\\") + "\\" + pojoName + "Mapper.java";
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SqlParams> getSqlPars() {
        return sqlPars;
    }

    public void setSqlPars(List<SqlParams> sqlPars) {
        this.sqlPars = sqlPars;
    }

    public SqlParams getPkSqlParams() {
        return pkSqlParams;
    }

    public void setPkSqlParams(SqlParams pkSqlParams) {
        this.pkSqlParams = pkSqlParams;
    }

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public SqlParams getVersonColumn() {
        return versonColumn;
    }

    public void setVersonColumn(SqlParams versonColumn) {
        this.versonColumn = versonColumn;
    }


    /**
     * 表的类型枚举
     *
     * @author Aison
     * @version V1.0
     * @date 2017年12月28日 上午10:53:02
     */
    public enum TableType {

        TABLE(1, "表"),
        VIEW(2, "视图"),
        NICKNAME(3, "nickName");

        private Integer code;

        private String name;

        TableType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
