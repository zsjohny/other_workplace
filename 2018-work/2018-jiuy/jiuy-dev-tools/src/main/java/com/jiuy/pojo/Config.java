package com.jiuy.pojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.jiuy.util.Biz;

import com.google.gson.Gson;

/**
 * xml解析出来的配置文件对象
 *
 * @author Aison
 * @version V1.0
 * @date 2017年12月28日 上午9:52:04
 */
public class Config {

    /**
     * 驱动
     */
    private String classDriver;
    /**
     * 数据库ip
     */
    private String host;
    /**
     * 数据库端口
     */
    private String prot;
    /**
     * 数据库用户名
     */
    private String userName;
    /**
     * 数据库密码
     */
    private String pwd;
    /**
     * 数据库
     */
    private String dataBase;
    /**
     * 表
     */
    private String[] tables;
    /**
     * projo的名称
     */
    private String[] projoNames;
    /**
     * 数据库连接
     */
    private String url;
    /**
     * pojo的路径
     */
    private String pojoPath;
    /**
     * mapper xml的路径
     */
    private String xmlPath;
    /**
     * xml所在的包
     */
    private String xmlPackage;
    /**
     * mapper接口的路径
     */
    private String mapperPath;
    /**
     * pojo的包名
     */
    private String pojoPackage;
    /**
     * projo的父类..完整的包名和类名
     */
    private String projoParent;
    /**
     * 严格模式下注释写错了会报错
     */
    private String strict;
    /**
     * mapperpackage
     */
    private String mapperPackage;
    /**
     * baseMapper的包名和类名
     */
    private String baseMapperPackage;
    /**
     * 主键的注释完整地址
     */
    private String primaryKeyAnnotation;
    /**
     * FieldNamePackage 的包名
     */
    private String fieldNameAnnotation;
    /**
     * modelNamePackage 的包名
     */
    private String modelNameAnnotation;
    /**
     * version字段
     */
    private String versionColumn;
    /**
     * 模式
     */
    private String schema;
    /**
     * sql与javamapping文件
     */
    private String mappingFile;
    /**
     * 是创建query 对象
     */
    private String createQuery;
    /**
     * Sql 数据类型和java类型的映射关系
     */
    private static Map<String, SqlType2JavaType> mapping = new HashMap<String, SqlType2JavaType>();
    /**
     * 需要转义的数据库关键字
     *
     * @SuppressWarnings("unused")
     */
    private static Map<String, String> escapeKeywords = new HashMap<String, String>();


    public Config() {

    }

    /**
     * @param
     * @return void    返回类型
     * @throws
     * @Title: instance
     * @Description: 工厂方法
     * @author Aison
     */
    public static Config instance() {
        return new Config();
    }

    /**
     * 默认的映射关系
     **/
     static {
     mapping.put("CHAR",SqlType2JavaType.instance("CHAR", null,"String",true));
     mapping.put("VARCHAR",SqlType2JavaType.instance("VARCHAR", null,"String",true));
     mapping.put("LONGVARCHAR",SqlType2JavaType.instance("LONGVARCHAR", null,"String",true));
     mapping.put("NUMERIC",SqlType2JavaType.instance("NUMERIC", "java.math.BigDecimal","BigDecimal",false));
     mapping.put("DECIMAL",SqlType2JavaType.instance("DECIMAL", "java.math.BigDecimal","BigDecimal",false));
     mapping.put("BIT",SqlType2JavaType.instance("BIT", null,"Boolean",true));
     mapping.put("BOOLEAN",SqlType2JavaType.instance("BOOLEAN", null,"Boolean",true));
     mapping.put("TINYINT",SqlType2JavaType.instance("TINYINT", null,"Integer",true));
     mapping.put("SMALLINT",SqlType2JavaType.instance("SMALLINT", null,"Short",true));
     mapping.put("INTEGER",SqlType2JavaType.instance("INTEGER", null,"Integer",true));
     mapping.put("INT",SqlType2JavaType.instance("INTEGER", null,"Integer",true));
     mapping.put("BIGINT",SqlType2JavaType.instance("BIGINT", null,"Long",true));
     mapping.put("BIGINTUNSIGNED",SqlType2JavaType.instance("BIGINT", null,"Long",true));
     mapping.put("REAL",SqlType2JavaType.instance("REAL", null,"Float",true));
     mapping.put("FLOAT",SqlType2JavaType.instance("FLOAT", null,"Float",true));
     mapping.put("DOUBLE",SqlType2JavaType.instance("DOUBLE", null,"Double",true));
     mapping.put("BINARY",SqlType2JavaType.instance("BINARY", null,"byte[]",true));
     mapping.put("VARBINARY",SqlType2JavaType.instance("VARBINARY", null,"byte[]",true));
     mapping.put("LONGVARBINARY",SqlType2JavaType.instance("LONGVARBINARY", null,"byte[]",true));
     mapping.put("DATE",SqlType2JavaType.instance("DATE", "java.util.Date","Date",false));
     mapping.put("TIME",SqlType2JavaType.instance("TIME", "java.util.Date","Date",false));
     mapping.put("TIMESTAMP",SqlType2JavaType.instance("TIMESTAMP", "java.util.Date","Date",false));
     mapping.put("CLOB",SqlType2JavaType.instance("CLOB", null,"String",true));
     mapping.put("BLOB",SqlType2JavaType.instance("BLOB", null,"byte[]",true));
     mapping.put("ARRAY",SqlType2JavaType.instance("ARRAY", "java.sql.Array","Array",false));
     mapping.put("REF",SqlType2JavaType.instance("REF", "java.sql.Ref","Ref",false));
     mapping.put("DATALINK",SqlType2JavaType.instance("DATALINK", "java.net.URL","URL",false));
     mapping.put("MEDIUMINT UNSIGNED", SqlType2JavaType.instance("INTEGER", null,"Integer",true));
     mapping.put("MEDIUMINTUNSIGNED", SqlType2JavaType.instance("INTEGER", null,"Integer",true));
     mapping.put("DATETIME", SqlType2JavaType.instance("TIMESTAMP", "java.util.Date","Date",true));


     }


     public static Boolean filled = false;


    /**
     * 初始化映射
     * @param
     * @date:   2018/5/24 13:25
     * @author Aison
     */
    private void initMapping() {
        if (filled) {
            return;
        } else {
            synchronized (filled) {
                if (filled) {
                    return;
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(new File(mappingFile));
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                        String line = bufferedReader.readLine();
                        while (line != null) {
                            if (Biz.valiArg(line)) {
                                Gson gson = new Gson();
                                SqlType2JavaType sqlType2JavaType = gson.fromJson(line, SqlType2JavaType.class);
                                mapping.put(sqlType2JavaType.getTempKey().replaceAll("\\s*|\t|\r|\n", ""), sqlType2JavaType);
                            }
                            line = bufferedReader.readLine();
                        }
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    filled = true;
                }
            }
        }

    }


    /**
     * 获取javaType通过jdbcType
     * @param sqlType 数据库数据类型
     * @date:   2018/5/24 13:25
     * @author Aison
     */
    public SqlType2JavaType getJavaType(String sqlType) {
        initMapping();
        sqlType = sqlType.replaceAll("\\s*|\t|\r|\n", "");
        return Config.mapping.get(sqlType);
    }


    public String getClassDriver() {
        return classDriver;
    }

    public void setClassDriver(String classDriver) {
        this.classDriver = classDriver;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProt() {
        return prot;
    }

    public void setProt(String prot) {
        this.prot = prot;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String[] getTables() {
        return tables;
    }

    public void setTables(String[] tables) {
        this.tables = tables;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPojoPath() {
        return pojoPath;
    }

    public void setPojoPath(String pojoPath) {
        this.pojoPath = pojoPath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getPojoPackage() {
        return pojoPackage;
    }

    public void setPojoPackage(String pojoPackage) {
        this.pojoPackage = pojoPackage;
    }

    public String getPrimaryKeyAnnotation() {
        return primaryKeyAnnotation;
    }

    public void setPrimaryKeyAnnotation(String primaryKeyAnnotation) {
        this.primaryKeyAnnotation = primaryKeyAnnotation;
    }

    public String getFieldNameAnnotation() {
        return fieldNameAnnotation;
    }

    public void setFieldNameAnnotation(String fieldNameAnnotation) {
        this.fieldNameAnnotation = fieldNameAnnotation;
    }

    public String getModelNameAnnotation() {
        return modelNameAnnotation;
    }

    public void setModelNameAnnotation(String modelNameAnnotation) {
        this.modelNameAnnotation = modelNameAnnotation;
    }

    public String getProjoParent() {
        return projoParent;
    }

    public void setProjoParent(String projoParent) {
        this.projoParent = projoParent;
    }

    public String getStrict() {
        return strict;
    }

    public void setStrict(String strict) {
        this.strict = strict;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public String getBaseMapperPackage() {
        return baseMapperPackage;
    }

    public void setBaseMapperPackage(String baseMapperPackage) {
        this.baseMapperPackage = baseMapperPackage;
    }

    public String[] getProjoNames() {
        return projoNames;
    }

    public void setProjoNames(String[] projoNames) {
        this.projoNames = projoNames;
    }

    public String getXmlPackage() {
        return xmlPackage;
    }

    public void setXmlPackage(String xmlPackage) {
        this.xmlPackage = xmlPackage;
    }

    public String getVersionColumn() {
        return versionColumn;
    }

    public void setVersionColumn(String versionColumn) {
        this.versionColumn = versionColumn;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCreateQuery() {
        return createQuery;
    }

    public void setCreateQuery(String createQuery) {
        this.createQuery = createQuery;
    }

    public String getMappingFile() {
        return mappingFile;
    }

    public void setMappingFile(String mappingFile) {
        this.mappingFile = mappingFile;
    }


}
