package com.jiuy.pojo;

/**
 * sql type与java type的对应关系
 * @author Aison
 * @version V1.0
 * @date 2017年12月28日 下午1:49:51
 */
public class SqlType2JavaType {

    /**
     * shortJavaType : 短的javaType
     */
    private String shortJavaType;

    /**
     * sqlType : 数据库的类型
     */
    private String sqlType;

    /**
     * fullJavaType : 完整的java类型带包名
     */
    private String fullJavaType;

    /**
     * isNomarl : 是否是不需要引入包名就可以使用的
     */
    private Boolean isNomarl;

    private String tempKey;

    public SqlType2JavaType() {

    }

    public SqlType2JavaType(String sqlType, String fullJavaType, String shortJavaType, Boolean isNomarl) {
        this.shortJavaType = shortJavaType;
        this.sqlType = sqlType;
        this.fullJavaType = fullJavaType;
        this.isNomarl = isNomarl;
    }

    /**
     * 工厂方法
     * @param  shortJavaType
     * @param   sqlType
     * @param   fullJavaType
     * @return SqlType2JavaType    返回类型
     * @author Aison
     */
    public static SqlType2JavaType instance(String sqlType, String fullJavaType, String shortJavaType, Boolean isNomarl) {

        return new SqlType2JavaType(sqlType, fullJavaType, shortJavaType, isNomarl);
    }

    public String getShortJavaType() {
        return shortJavaType;
    }

    public void setShortJavaType(String shortJavaType) {
        this.shortJavaType = shortJavaType;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getFullJavaType() {
        return fullJavaType;
    }

    public void setFullJavaType(String fullJavaType) {
        this.fullJavaType = fullJavaType;
    }

    public Boolean getIsNomarl() {
        return isNomarl;
    }

    public void setIsNomarl(Boolean isNomarl) {
        this.isNomarl = isNomarl;
    }

    public String getTempKey() {
        return tempKey;
    }

    public void setTempKey(String tempKey) {
        this.tempKey = tempKey;
    }


}
