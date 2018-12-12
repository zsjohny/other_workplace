package com.finace.miscroservice.commons.handler;

import com.finace.miscroservice.commons.annotation.Column;
import com.finace.miscroservice.commons.annotation.Id;
import com.finace.miscroservice.commons.annotation.Table;
import com.finace.miscroservice.commons.annotation.Transient;
import com.finace.miscroservice.commons.log.Log;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.SQLExec;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 数据的自动生成的迭代
 */
public class DbHandler {
    private Log log = Log.getInstance(DbHandler.class);

    private final int ERROR_HIGH_GRADE = 4;
    private final int ERROR_JUNIOR_GRADE = 1;


    private Boolean isCamel = Boolean.TRUE;
    private Boolean showSql = Boolean.TRUE;

    private final char UNDERLINE = '_';
    private Boolean alwaysInit = Boolean.TRUE;

    private final String tmpFilePath = System.getProperty("java.io.tmpdir");

    private final long DEFAULT_INVALID_TIME = 1000 * 60 * 60 * 24;

    private static Boolean LOADING = Boolean.TRUE;

    private Boolean delAndNewTables = Boolean.FALSE;


    private DataSource dataSource;
    private Connection conn;

    private final String DATASOURCE_ERROR_MSG = "dataSource was not empty";
    private final String CONNECT_ERROR_MSG = "connect was not empty";
    private final String PACKAGEBASE_ERROR_MSG = "packageBase was not empty";
    private final String ID_ERROR_MSG = "@Id annotation was not empty";
    private final String DB_NAME = "db.sql";
    private final String DEFAULT_FILE_PREFIX = "file:";
    private static final int NOT_DEFAULT_LENGTH = -1;
    private static final int NOT_ASSIGN_LENGTH = -2;
    private static final int NOT_PRECISION_LENGTH = -3;


    /**
     * 包的路径 多个包用,分隔开
     */
    private String packageBase;

    /**
     * 初始化数据
     *
     * @throws Exception
     */
    public void init() throws Exception {
        if (LOADING) {
            synchronized (this) {
                if (!alwaysInit) {
                    log.info("本次不初始化实体类和表格映射");
                    return;
                }
                createSql();
                executeSql();
                LOADING = false;
            }
        }


    }

    /**
     * 生成新的sql语句
     *
     * @param file sql存储文件
     * @param list 扫描类的list
     * @throws Exception
     */
    private void autoNewSql(File file, List<Class<?>> list) throws Exception {
        if (list == null || list.isEmpty() || file == null) {
            return;
        }

        StringBuffer sql = new StringBuffer();

        list.forEach((Class<?> x) -> {
            Table table = x.getAnnotation(Table.class);
            if (table != null) {

                Connection connect;
                PreparedStatement statement;

                Boolean isCreateSqlFlag = Boolean.TRUE;
                try {
                    connect = getConnect(dataSource);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (sql.length() > 0) {
                    sql.append("\n");
                }
                String tableCheckSql = "show tables like '%s'";
                if (delAndNewTables) {
                    tableCheckSql = "drop table `%s`";
                }

                try {
                    statement = connect.prepareStatement(String.format(tableCheckSql, table.value()));
                    Boolean resultCheck;
                    if (delAndNewTables) {
                        resultCheck = statement.execute();
                    } else {
                        resultCheck = statement.executeQuery().next();
                    }
                    if (resultCheck) {
                        if (delAndNewTables) {
                            //delAndNewTables为false
                            isCreateSqlFlag = Boolean.TRUE;
                        } else {
                            //delAndNewTables为false
                            isCreateSqlFlag = Boolean.FALSE;
                        }
                    }
                } catch (SQLException e) {
                    if (delAndNewTables) {
                        //delAndNewTables 为true
                        isCreateSqlFlag = Boolean.TRUE;
                    } else {
                        throw new RuntimeException(e);
                    }
                }


                if (isCreateSqlFlag) {
                    sql.append("create table  ");


                    sql.append("`");
                    sql.append(table.value());
                    sql.append("` ( ");

                }

                Field[] fields = x.getDeclaredFields();
                String id = "";

                StringBuilder sqlBuild;
                StringBuilder commSql;
                StringBuilder updateSql = new StringBuilder();
                for (Field filed : fields) {


                    if (filed.getAnnotation(Transient.class) != null) {
                        continue;
                    }
                    String especialColumnName;

                    if (isCamel) {
                        especialColumnName = camel2Underline(filed.getName());
                    } else {
                        especialColumnName = filed.getName();
                    }

                    commSql = new StringBuilder();
                    String type = filed.getType().getName();
                    if (filed.getAnnotation(Id.class) != null) {

                        id = especialColumnName;

                        if (isCreateSqlFlag) {
                            sql.append(TableAttribute.getIdCommit(id, type));
                        }
                        continue;
                    }
                    Column column = filed.getAnnotation(Column.class);
                    if (column != null&&!column.value().isEmpty()) {
                        especialColumnName = column.value();
                    }
                    if (isCreateSqlFlag) {
                        sql.append("`");
                        sql.append(especialColumnName);
                        sql.append("` ");
                    }
                    commSql.append(TableAttribute.getColumn(type));
                    commSql.append("(");

                    if (column != null) {
                        if (TableAttribute.getLength(type) == NOT_DEFAULT_LENGTH) {
                            commSql.delete(commSql.length() - 1, commSql.length());

                            //添加默认值
                            commSql.append(!column.defaultVal().isEmpty() ? TableAttribute.getDefaultValue(column.defaultVal()) :
                                    column.isNUll() ? TableAttribute.getDefaultNull() : TableAttribute.getNotNull());


                        } else if (TableAttribute.getLength(type) == NOT_ASSIGN_LENGTH) {
                            commSql.delete(commSql.length() - 1, commSql.length());

                            DateGeneStrategy dateGeneStrategy = column.dateGeneStrategy();
                            if (dateGeneStrategy != DateGeneStrategy.DEFAULT) {
                                //判断是否有默认时间
                                commSql.append(dateGeneStrategy.getDefaultValue());
                            } else {
                                throw new RuntimeException(System.err.append(String.format("className %s 的属性 %s 为TimeStamp 必须有默认值", x.getName(), filed.getName())).toString());
                            }

                        } else {
                            //精度计算的时候 (M,N) M需要大于N这边默认如果不大于则加上N的精度
                            commSql.append(column.length() == 0 ? TableAttribute.getLength(type) :
                                    column.length() > column.precision() ?
                                            column.length() : column.length() + column.precision());
                            commSql.append(TableAttribute.getPrecision(type) == NOT_PRECISION_LENGTH ? "" :
                                    "," + column.precision());
                            commSql.append(") ");

                            //添加默认值
                            commSql.append(!column.defaultVal().isEmpty() ? TableAttribute.getDefaultValue(column.defaultVal()) :
                                    column.isNUll() ? TableAttribute.getDefaultNull() : TableAttribute.getNotNull());

                        }


                        //添加注释
                        commSql.append(TableAttribute.getCommit(column.commit()));


                    } else {

                        if (TableAttribute.getLength(type) == NOT_DEFAULT_LENGTH) {
                            commSql.delete(commSql.length() - 1, commSql.length());

                            commSql.append(TableAttribute.getDefaultNull());


                        } else if (TableAttribute.getLength(type) == NOT_ASSIGN_LENGTH) {
                            commSql.delete(commSql.length() - 1, commSql.length());

                            DateGeneStrategy dateGeneStrategy = column.dateGeneStrategy();
                            if (dateGeneStrategy != DateGeneStrategy.DEFAULT) {
                                //判断是否有默认时间
                                commSql.append(dateGeneStrategy.getDefaultValue());
                            } else {
                                throw new RuntimeException(System.out.append(String.format("className %s 的属性 %s 为TimeStamp 必须有默认值 %n", x.getName(), filed.getName())).toString());
                            }

                        } else {
                            commSql.append(TableAttribute.getLength(type));
                            int precision = TableAttribute.getPrecision(type);
                            commSql.append(precision == NOT_PRECISION_LENGTH ? "" : "," + precision);
                            commSql.append(") ");
                            commSql.append(TableAttribute.getDefaultNull());
                        }


                        commSql.append(TableAttribute.getDefaultCommit());

                    }

                    if (isCreateSqlFlag) {

                        //分别赋予sql
                        sql.append(commSql.toString());

                    }

                    try {


                        sqlBuild = new StringBuilder("desc ");
                        sqlBuild.append(table.value());
                        sqlBuild.append("  ");
                        sqlBuild.append(especialColumnName);
                        statement = connect.prepareStatement(sqlBuild.toString());
                        ResultSet resultSet = statement.executeQuery();

                        if (!resultSet.next()) {

                            updateSql.append("alter table  ");
                            updateSql.append(table.value());
                            updateSql.append(" add ");
                            updateSql.append(especialColumnName);
                            updateSql.append(" ");
                            updateSql.append(commSql.delete(commSql.length() - 1, commSql.length()));
                            updateSql.append("; \n");


                        }

                    } catch (SQLException e) {

                        if (!e.getMessage().endsWith("doesn't exist")) {
                            throw new RuntimeException(e);
                        }
                    }


                }

                if (id.isEmpty()) {
                    throw new RuntimeException(System.out.append(x.getName()).append(" ").append(ID_ERROR_MSG).append(" \n").toString());
                }


                if (isCreateSqlFlag) {
                    sql.append(TableAttribute.getPrimaryId(id));
                }
                sql.append("\n");
                sql.append(updateSql);


            }
        });

        try {
            closeConnect(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        if (sql.length() != 0) {

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true));
            osw.write(sql.toString());
            osw.close();

        }


    }


    /**
     * 生成sql
     */
    private void createSql() throws Exception {
        if (dataSource == null) {
            throw new RuntimeException(System.out.append(DATASOURCE_ERROR_MSG).append(" \n").toString());
        }
        Connection connect = getConnect(dataSource);

        if (connect == null) {
            throw new RuntimeException(System.out.append(CONNECT_ERROR_MSG).append(" \n").toString());
        }
        if (packageBase == null || packageBase.isEmpty()) {
            throw new RuntimeException(System.out.append(PACKAGEBASE_ERROR_MSG).append(" \n").toString());
        }
        log.info("开始扫描{}包下面的表格实体类", packageBase);
        List<Class<?>> classNameList = new ArrayList<>();
        //判断是否是多个包名
        String[] packageToScannerArr;
        if (packageBase.contains(",")) {
            packageToScannerArr = packageBase.split(",");
        } else {
            packageToScannerArr = new String[]{
                    packageBase
            };
        }
        for (String packName : packageToScannerArr) {
            if (packName.contains(".")) {
                packName = packName.replaceAll("\\.", "/");
            }
            URL resource = Thread.currentThread().getContextClassLoader().getResource(packName);
            if (resource == null) {
                throw new RuntimeException(String.format("packageName %s was incorrect package %n", packName));
            }
            String path = resource.getFile();

            if (path == null || path.isEmpty()) {
                throw new RuntimeException(String.format("packageName %s was not exist %n", packName));
            }

            if (path.contains("%20")) {
                path = path.replaceAll("%20", " ");
            }
            if (path.contains("file:")) {
                path = path.substring(5);
            }
            //jar包
            if (path.contains("!")) {
                path = path.split("!")[0];

                //获取jar内的包名
                JarInputStream inputStream = new JarInputStream(new FileInputStream(path));
                JarEntry nextJarEntry = inputStream.getNextJarEntry();
                String className = "";
                while (nextJarEntry != null) {
                    className = nextJarEntry.getName();
                    if (className.contains(packName) && className.endsWith(".class")) {
                        String[] lastClassNameArr = className.split("/");
                        className = (packName + "." + lastClassNameArr[lastClassNameArr.length - 1].split("\\.")[0]).replaceAll("/", ".");
                        classNameList.add(Class.forName(className));
                    }
                    nextJarEntry = inputStream.getNextJarEntry();
                }

                inputStream.close();
            } else {
                //获取不是jar内的包名
                File files = new File(path);
                if (files == null || !files.isDirectory()) {
                    throw new RuntimeException(String.format("packageName %s was not directory %n", packName));
                }
                File[] fileArr = files.listFiles();
                //开头的包名
                String startClassName = packName.split("/")[0];

                URLClassLoader urlClassLoader;
                String[] classNameArr;
                StringBuilder builder;

                for (File childFile : fileArr) {
                    classNameArr = childFile.getAbsolutePath().split(startClassName);
                    urlClassLoader = new URLClassLoader(new URL[]{
                            new URL(DEFAULT_FILE_PREFIX + classNameArr[0])
                    });
                    builder = new StringBuilder();
                    builder.append(startClassName);
                    builder.append(classNameArr[1].replaceAll("\\\\", "."));

                    //去除.class
                    classNameList.add(urlClassLoader.loadClass(builder.substring(0, builder.length() - 6)));

                }

            }

        }


        //生产sql文件
        File file = new File(tmpFilePath, DB_NAME);

        if (file.exists() || file.length() == 0 || System.currentTimeMillis() - file.lastModified() > DEFAULT_INVALID_TIME) {
            file.delete();
        } else {
            file.createNewFile();
        }

        autoNewSql(file, classNameList);

        log.info("结束扫描{}包下面的表格实体类", packageBase);
    }


    /**
     * 执行sql
     */
    private void executeSql() throws NoSuchFieldException, SQLException, IllegalAccessException {

        log.info("开始进行数据库导入操作");

        DefaultLogger logger = new DefaultLogger();
        //设定输入流
        logger.setErrorPrintStream(System.err);
        logger.setOutputPrintStream(System.out);
        //设定错误级别
        logger.setMessageOutputLevel(showSql ? ERROR_HIGH_GRADE : ERROR_JUNIOR_GRADE);

        Project project = new Project();
        //绑定日志
        project.addBuildListener(logger);

        //创建目标文件
        Target target = new Target();
        //创建工作目标
        SQLExec task = new SQLExec();

        //利用反射创建connection
        Field conn = task.getClass().getDeclaredField("conn");
        conn.setAccessible(Boolean.TRUE);
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(Boolean.FALSE);
        conn.set(task, connection);
        //创建执行sql的文件
        task.setSrc(new File(tmpFilePath + File.separator + DB_NAME));
        task.setProject(project);
        task.setTaskName(this.getClass().getSimpleName());

        //目标添加工作
        target.addTask(task);
        target.setProject(project);
        target.execute();

        log.info("进行数据库导入操做success....");


    }


    public enum DateGeneStrategy {
        DEFAULT(""), CREATE(" NULL DEFAULT CURRENT_TIMESTAMP  "), UPDATE(" NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        private String value;

        DateGeneStrategy(String value) {
            this.value = value;
        }

        public String getDefaultValue() {
            return value;
        }

    }

    private enum TableAttribute {
        DATE("java.util.Date", "Date", NOT_DEFAULT_LENGTH, NOT_PRECISION_LENGTH),
        TIMESTAMP("java.sql.Timestamp", "TIMESTAMP", NOT_ASSIGN_LENGTH, NOT_PRECISION_LENGTH),
        LONG("java.lang.Long", "bigint", 11, NOT_PRECISION_LENGTH),
        DOUBLE("java.lang.Double", "double", 11, 0),
        INTEGER("java.lang.Integer", "int", 11, NOT_PRECISION_LENGTH),
        STRING("java.lang.String", "varchar", 2 << 8, NOT_PRECISION_LENGTH),
        BOOLEAN("java.lang.Boolean", "bit", 1, NOT_PRECISION_LENGTH);
        private String key;
        private String columnName;
        private int value;
        private int precision;

        TableAttribute(String key, String columnName, int value, int precision) {
            this.key = key;
            this.columnName = columnName;
            this.value = value;
            this.precision = precision;
        }

        private int getLength() {
            return value;
        }


        private static int getLength(String key) {

            if (key == null || key.isEmpty()) {
                return 0;
            }
            TableAttribute[] values = TableAttribute.values();

            for (TableAttribute defaultAttr : values) {

                if (defaultAttr.key.equals(key)) {

                    return defaultAttr.value;
                }
            }

            return 0;
        }


        private static int getPrecision(String key) {

            if (key == null || key.isEmpty()) {
                return NOT_PRECISION_LENGTH;
            }
            TableAttribute[] values = TableAttribute.values();

            for (TableAttribute defaultAttr : values) {

                if (defaultAttr.key.equals(key)) {

                    return defaultAttr.precision;
                }
            }

            return NOT_PRECISION_LENGTH;
        }

        private static String getNotNull() {
            return "NOT NULL ";
        }

        private static String getDefaultNull() {
            return " DEFAULT NULL ";
        }

        private static String getDefaultCommit() {
            return ",";
        }


        private static String getPrimaryId(String key) {
            StringBuilder builder = new StringBuilder("PRIMARY KEY (`");
            builder.append(key);
            builder.append("`) ");
            builder.append(" ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            return builder.toString();
        }


        private static String getCommit(String value) {
            StringBuilder builder = new StringBuilder(" COMMENT '");
            builder.append(value);
            builder.append("' ,");
            return builder.toString();
        }

        private static String getIdCommit(String value, String type) {
            StringBuilder builder = new StringBuilder("`");
            builder.append(value);
            builder.append("` ");
            builder.append(getColumn(type));
            builder.append(" (");
            builder.append(getLength(type));
            builder.append(") ");
            builder.append("NOT NULL  AUTO_INCREMENT , ");
            return builder.toString();
        }


        private static String getColumn(String key) {

            if (key == null || key.isEmpty()) {
                return "";
            }
            TableAttribute[] values = TableAttribute.values();

            for (TableAttribute defaultAttr : values) {
                if (defaultAttr.key.equals(key)) {
                    return defaultAttr.columnName;
                }
            }

            return "";
        }


        private static String getDefaultValue(String key) {

            if (key == null || key.isEmpty()) {
                return "";
            }

            return "DEFAULT '" + key + "'";
        }


    }


    /**
     * 获取链接
     *
     * @param source
     * @return
     * @throws Exception
     */
    private Connection getConnect(DataSource source) throws Exception {
        if (source == null) {
            return null;
        }
        if (conn == null) {
            conn = source.getConnection();
        }
        return conn;

    }

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

    /**
     * 关闭连接
     *
     * @param connection
     * @throws SQLException
     */
    private void closeConnect(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }


    public void setShowSql(Boolean showSql) {
        this.showSql = showSql;
    }


    public void setAlwaysInit(Boolean alwaysInit) {
        this.alwaysInit = alwaysInit;
    }

    public void setAlwaysDrop(Boolean delAndNewTables) {
        this.delAndNewTables = delAndNewTables;
    }

    public void setIsCamel(Boolean isCamel) {
        this.isCamel = isCamel;
    }
}
