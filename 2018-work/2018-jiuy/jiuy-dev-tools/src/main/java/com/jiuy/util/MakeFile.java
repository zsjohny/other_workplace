package com.jiuy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.jiuy.exception.BizException;
import com.jiuy.exception.GlobalsEnums;
import com.jiuy.pojo.Config;
import com.jiuy.pojo.SqlParams;
import com.jiuy.pojo.SqlTable;


/**
 * 创建各个程序的文件
 *
 * @author Aison
 * @version V1.0
 * @date 2017年12月28日 下午2:51:13
 */
class MakeFile {

    private static String space4 = "	";
    private static String space8 = "		";
    private static String space12 = "			";

    /**
     * 工厂方法..
     * @param  config 配置信息
     * @return MakeFile    返回类型
     * @author Aison
     */
     static MakeFile instance(Config config) {
        checkExists(pathWithPackage(config.getPojoPath(), config.getPojoPackage()));
        checkExists(pathWithPackage(config.getMapperPath(), config.getMapperPackage()));
        checkExists(pathWithPackage(config.getXmlPath(), config.getXmlPackage()));
        checkExists(config.getPojoPath());
        checkExists(config.getMapperPath());
        checkExists(config.getXmlPath());
        return new MakeFile();
    }

    /**
     * 创建pojo
     * @param  tables 表的映射集合
     * @param  config 配置信息
     * @return void    返回类型
     * @author Aison
     */
     StringBuffer createPojo(List<SqlTable> tables, Config config) {
        // 检验一下文件是否存在了
        checkExists(config.getPojoPath());
        StringBuffer sb = new StringBuffer();
        for (SqlTable table : tables) {
            try {
                doMakePojo(table, config);
                if ("1".equals(config.getCreateQuery())) {
                    createQuery(table, config);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sb.append("表生成失败：" + table.getTableName() + Biz.getExceptionStr(e));
            }
        }
        return sb;
    }


    /**
     * 创建mapper
     *
     * @param tables 表的映射集合
     * @param config 配置信息
     * @return StringBuffer    返回类型
     * @author Aison
     */
     StringBuffer createMapper(List<SqlTable> tables, Config config) {

        checkExists(config.getMapperPath());
        StringBuffer createRs = new StringBuffer();
        for (SqlTable table : tables) {
            try {
                doCreateMapper(table, config);
            } catch (Exception e) {
                e.printStackTrace();
                // 将错误的信息返回回去..这里是允许部分成功的
                createRs.append("表生成mapper失败：" + table.getTableName());
            }
        }
        return createRs;
    }

    /**
     * 创建mapper xml
     *
     * @param tables
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
     StringBuffer createMapperXml(List<SqlTable> tables, Config config) {

        StringBuffer createRs = new StringBuffer();
        for (SqlTable table : tables) {
            try {
                doCreateMapperXml(table, config);
            } catch (Exception e) {
                e.printStackTrace();
                createRs.append("表生成xml失败：" + table.getTableName());
            }
        }
        return createRs;
    }



    private static String pathWithPackage(String path, String packages) {
        String pojoPack = packages;
        String realPath = path + pojoPack.replaceAll("\\.", "\\\\");
        return realPath;
    }

    /**
     * 判断文件是否存在 不存在则创建
     * @param path 文件路径
     * @return void    返回类型
     * @author Aison
     */
    private static File checkExists(String path) {
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        return targetFile;
    }

    /**
     * 创建pojo文件
     * @param  table 表的映射
     * @param  config 配置信息
     * @author Aison
     */
    private void doMakePojo(SqlTable table, Config config) throws Exception {

        String projoName = table.getPojoName();
        String parentName = config.getProjoParent();
        boolean isStrict = "1".equals(config.getStrict());

        File pojoFile = new File(table.getPojoAbsolutePath());

        StringBuffer costomGetter = new StringBuffer();
        if (pojoFile.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pojoFile)));
            String str = br.readLine();
            boolean constomGetFlag = false;
            // 缓存自定义的field
            while (str != null) {
                if (!constomGetFlag && str.indexOf("@Costom-getter") > 0) {
                    constomGetFlag = true;
                }
                if (constomGetFlag) {
                    costomGetter.append(str).append("\r\n");
                }
                str = br.readLine();
            }
            br.close();
        }

        StringBuffer projoBuffer = new StringBuffer();

        projoBuffer.append("package ").append(config.getPojoPackage()).append("; \r\n\r\n");
        projoBuffer.append("import ").append(config.getFieldNameAnnotation()).append("; \r\n");
        projoBuffer.append("import ").append(config.getModelNameAnnotation()).append("; \r\n");
        projoBuffer.append("import ").append(config.getPrimaryKeyAnnotation()).append("; \r\n");
        projoBuffer.append("import ").append("com.jiuy.base.model.Model").append("; \r\n");
        projoBuffer.append("import ").append("lombok.Data").append("; \r\n");
        boolean hasParent = parentName != null;

        // import
        Set<String> needImportJavaTypes = table.getAllImportJavaType();
        for (String importType : needImportJavaTypes) {
            projoBuffer.append("import ").append(importType).append(";\r\n");
        }
        // parentPojo
        if (hasParent) {
            projoBuffer.append("import ").append(config.getProjoParent()).append(";\r\n");
        }

        String tableComment = table.getTableComment();
        projoBuffer.append("\r\n/**").append("\r\n")
                .append(" * ").append(tableComment).append("\r\n")
                .append(" * <p> \r\n")
                .append(" * ").append("请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性").append("\r\n")
                .append(" * \r\n")
                .append(" * @author ").append(Biz.getSysUserName()).append("\r\n")
                .append(" * @version V1.0  ").append("\r\n")
                .append(" * @date ").append(Biz.dateFormat(new Date(), "yyyy年MM月dd日 a hh:mm:ss")).append("\r\n")
                .append(" */").append("\r\n");

        //ModelName
        String modelName = "";
        projoBuffer.append("@Data");
        if (Biz.valiArg(tableComment)) {
            int maohao = tableComment.indexOf(":");
            maohao = maohao == -1 ? tableComment.indexOf("：") : maohao;
            //如果是这种   课件信息:主要是记录课件的信息
            modelName = maohao > 0 ? tableComment.split(":")[0] : tableComment;
        } else if (isStrict) {
            throw new BizException(GlobalsEnums.TABLE_COMMENT_INVALID);
        }

        projoBuffer.append("\r\n@ModelName(name = \"" + modelName + "\", tableName = \""+table.getTableName()+"\")");

        projoBuffer.append("\r\npublic class ").append(projoName).append(" extends Model");
        if (hasParent) {
            String parentPojo = parentName.substring(parentName.lastIndexOf(".") + 1);
            projoBuffer.append(" extends ").append(parentPojo);
        }
        projoBuffer.append(" {  \r\n \r\n");

        // 写某个字段
        for (SqlParams sqlParams : table.getSqlPars()) {
            // 完整的注释
            String comment = sqlParams.getComments();
            // field的名称
            String fieldName = "";
            // 添加注释
            if (Biz.valiArg(comment)) {
                comment = sqlParams.getComments();
                int maohaox = comment.indexOf(":");
                if(maohaox == -1) {
                    fieldName = comment.indexOf("：") > 0 ? comment.split("：")[0] : comment;
                } else {
                    fieldName = comment.indexOf(":") > 0 ? comment.split(":")[0] : comment;
                }

                projoBuffer.append(space4).append("/**").append("\r\n")
                        .append(space4).append(" * ").append(comment).append("\r\n")
                        .append(space4).append(" */").append("  \r\n");
                projoBuffer.append(space4).append("@FieldName(name = \"" + fieldName.replaceAll("\\n","").replaceAll("\\n","") + "\")").append("  \r\n");
            } else if (isStrict) {
                // 没有注释返回错误
                throw new BizException(GlobalsEnums.COMMENT_INVALID);
            }
            if (sqlParams.getIsPk()) {
                projoBuffer.append(space4).append("@PrimaryKey").append("  \r\n");
            }
            if (("1".equals(config.getStrict())) && (Biz.valiArg(comment, fieldName))) {
                throw new BizException(GlobalsEnums.COMMENTS_LOAD_ERROR);
            }
            projoBuffer.append(space4).append("private ").append(sqlParams.getShortJavaType()).append(" ").append(sqlParams.getProperty()).append(";  \r\n \r\n");
        }

        if (costomGetter.length() > 0) {
            projoBuffer.append(costomGetter);
        } else {
            projoBuffer.append(space4)
                    .append("//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 \r\n ");
            projoBuffer.append(" \r\n }");
        }
        FileOutputStream fos = new FileOutputStream(pojoFile);
        fos.write(projoBuffer.toString().getBytes());
        fos.close();
    }

    /**
     * 创建查询实体类
     *
     * @param table 表的映射
     * @param config 配置信息
     * @author Aison
     */
    private void createQuery(SqlTable table, Config config) throws Exception {

        File pojoFile = new File(table.getPojoQueryAbsolutePath());
        StringBuffer queryBuffer = new StringBuffer();
        if (pojoFile.exists()) {
            return ;
        }

        String pojoName = table.getPojoName();

        queryBuffer.append("package ").append(config.getPojoPackage()).append("; \r\n\r\n");
        queryBuffer.append("import ").append("lombok.Data").append("; \r\n");
        queryBuffer.append("\r\n/**").append("\r\n")
                .append(" * ").append(pojoName).append("的拓展实体。").append("\r\n")
                .append(" * ").append("添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题").append("\r\n")
                .append(" * \r\n")
                .append(" * @author ").append(Biz.getSysUserName()).append("\r\n")
                .append(" * @version V1.0  ").append("\r\n")
                .append(" * @date ").append(Biz.dateFormat(new Date(), "yyyy年MM月dd日 a hh:mm:ss")).append("\r\n")
                .append(" * @Copyright 玖远网络 \r\n")
                .append("*/").append("\r\n");
        queryBuffer.append("@Data");
        queryBuffer.append("\r\npublic class ").append(pojoName).append("Query");
        queryBuffer.append(" extends ").append(table.getPojoName());
        queryBuffer.append(" {  \r\n \r\n");
        queryBuffer.append("} \r\n");

        FileOutputStream fos = new FileOutputStream(pojoFile);
        fos.write(queryBuffer.toString().getBytes());
        fos.close();
    }





    /**
     * 实际的创建mapper
     *
     * @param table
     * @param config
     * @author Aison
     */
    private void doCreateMapper(SqlTable table, Config config) throws Exception {

        File mapperFile = new File(table.getMapperAbsolutePath());
        StringBuffer mapperBuffer = new StringBuffer();
        StringBuffer oldBuffer = new StringBuffer();

        if (mapperFile.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mapperFile)));
            String str = br.readLine();
            boolean costom = false;
            while (str != null) {
                if (!costom && str.indexOf("@Costom") > 0) {
                    costom = true;
                }
                if (costom) {
                    oldBuffer.append(str).append("\r\n");
                }
                str = br.readLine();
            }
            br.close();
        }


        String mapperPackage = config.getMapperPackage();
        String projoPackage = config.getPojoPackage();
        String projoName = table.getPojoName();

        mapperBuffer.append("package ")
                .append(mapperPackage)
                .append("; \r\n \r\n")
                .append("import ")
                .append(projoPackage + "." + table.getPojoName())
                .append("; \r\n")
                .append("import ")
                .append(config.getBaseMapperPackage())
                .append(";")
                .append("\r\n\r\n");


        mapperBuffer
                .append("/** \r\n")
                .append(" * ").append(table.getTableComment()).append(" 的mapper 文件").append("\r\n \r\n")
                .append(" * @author ").append(Biz.getSysUserName()).append("\r\n")
                .append(" * @version V1.0 ").append("\r\n")
                .append(" * @date ").append(Biz.dateFormat(new Date(), "yyyy年MM月dd日 a hh:mm:ss")).append("\r\n")
                .append(" * @Copyright 玖远网络 \r\n")
                .append(" */\r\n");
        mapperBuffer.append("public interface ")
                .append(projoName + "Mapper extends BaseMapper<" + projoName + ">")
                .append("{  \r\n\r\n");
        if (oldBuffer.length() > 0) {
            mapperBuffer.append(oldBuffer);
        } else {
            mapperBuffer.append(space4).append("// @Costom ").append("\r\n");
            mapperBuffer.append(space4).append("// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面").append("\r\n\r\n");
            mapperBuffer.append("}");
        }
        FileOutputStream fos = new FileOutputStream(mapperFile);
        fos.write(mapperBuffer.toString().getBytes());
        fos.close();
    }




    /**
     * 实际的创建mapper xml
     *
     * @param table
     * @param config
     * @author Aison
     */
    private void doCreateMapperXml(SqlTable table, Config config) throws Exception {


        File mapperXmlFile = new File(table.getXmlAbsolutePath());
        // 如果文件存在则将以前自定义的sql等全部拿出来
        StringBuffer oldBuff = new StringBuffer();
        if (mapperXmlFile.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mapperXmlFile)));
            String str = br.readLine();
            boolean costom = false;
            while (str != null) {
                if (!costom && str.indexOf("@Costom") > 0) {
                    costom = true;
                }
                if (costom) {
                    oldBuff.append(str).append("\r\n");
                }
                str = br.readLine();
            }
            br.close();
        }

        StringBuffer xmlBufff = new StringBuffer();
        xmlBufff.append(createResultMap(table, config));

        xmlBufff.append(createBaseColumnSql(table, config));

        xmlBufff.append(createUpdateBaseSql(table, config));

        xmlBufff.append(createNomarl(table, config));

        xmlBufff = createHeadandFoot(xmlBufff, oldBuff, table.getMapperNameWithPackage());

        FileOutputStream fos = new FileOutputStream(mapperXmlFile);
        fos.write(xmlBufff.toString().getBytes());
        fos.close();
    }



    /**
     * 创建头和尾..和自定义的sql
     *
     * @param xmlBuffer
     * @param oldBuffer
     * @param nameSpace
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createHeadandFoot(StringBuffer xmlBuffer, StringBuffer oldBuffer, String nameSpace) {

        if (oldBuffer.length() == 0) {
            oldBuffer.append(space4)
                    .append("<!--  @Costom -->")
                    .append("\r\n")
                    .append(space4)
                    .append("<!-- 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql放在此行下面 -->")
                    .append("\r\n")
                    .append(space4)
                    .append("<sql id=\"costomWhereCase\">\r\n")
                    .append(space4)
                    .append("</sql>\r\n\r\n")
                    .append("</mapper>");
        } else {
            if (oldBuffer.indexOf("costomWhereCase") == -1) {
                int costom = oldBuffer.indexOf("<!-- 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql放在此行下面 -->")
                        + "<!-- 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql放在此行下面 -->".length();
                oldBuffer.insert(costom, "\r\n" + space4 + "<sql id=\"costomWhereCase\">\r\n" + space4 + "</sql>");
            }
        }
        xmlBuffer.append(oldBuffer);
        xmlBuffer.insert(0, "<mapper namespace=\"" + nameSpace + "\"> \r\n \r\n ");
        xmlBuffer.insert(0, "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" > \r\n");
        xmlBuffer.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \r\n");

        return xmlBuffer;
    }


    /**
     * 创建resultMap
     *
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createResultMap(SqlTable table, Config config) {
        String pojoFullPath = config.getPojoPackage() + "." + table.getPojoName();
        StringBuffer resultMap = new StringBuffer();
        // ---------------BaseResultMap
        resultMap.append(comment("主要的返回映射"))
                .append(space4)
                .append("<resultMap id=\"BaseResultMap\" type=\"")
                .append(pojoFullPath)
                .append("\"")
                .append(space4)
                .append(">")
                .append("\r\n");
        SqlParams pkParm = table.getPkSqlParams();
        // 把主键生成了
        if (pkParm != null) {
            resultMap
                    .append(space8)
                    .append("<id column=\"")
                    .append(pkParm.getColumnName())
                    .append("\" property=\"")
                    .append(pkParm.getProperty())
                    .append("\" jdbcType=\"")
                    .append(pkParm.getSqlType())
                    .append("\" />")
                    .append("\r\n");
        }
        List<SqlParams> sqlParams = table.getSqlPars();
        for (SqlParams sqlParam : sqlParams) {
            if (!sqlParam.getIsPk()) {
                resultMap.append(space8)
                        .append("<result column=\"")
                        .append(sqlParam.getColumnName())
                        .append("\"  property=\"")
                        .append(sqlParam.getProperty())
                        .append("\"  jdbcType=\"")
                        .append(sqlParam.getSqlType())
                        .append("\" />")
                        .append("\r\n");
            }
        }
        resultMap.append(space4).append("</resultMap>").append("\r\n\r\n");
        return resultMap;
    }

    /**
     * 创建baseSql
     *
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createBaseColumnSql(SqlTable table, Config config) {

        StringBuffer sqlBfffer = new StringBuffer();
        sqlBfffer.append(comment("所有的列名")).append(space4).append("<sql id=\"Base_Column_List\" >").append("\r\n").append(space8);
        StringBuffer allColumns = new StringBuffer("");
        List<SqlParams> sqlParms = table.getSqlPars();
        int i = 0;
        for (SqlParams sqlParam : sqlParms) {
            String column = sqlParam.getColumnName();
            allColumns.append(column).append(",");
            if (i % 8 == 0 && i != 0) {
                allColumns.append("\r\n").append(space8);
            }
            i++;
        }
        allColumns = allColumns.delete(allColumns.lastIndexOf(","), allColumns.length());
        sqlBfffer.append(allColumns).append("\r\n");
        sqlBfffer.append(space4).append("</sql>").append("\r\n\r\n");
        return sqlBfffer;
    }


    /**
     * 创建更新的baseSql
     *
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createUpdateBaseSql(SqlTable table, Config config) {

        StringBuffer updateBaseSql = createFieldIf(table, config, "", space8,true);
        updateBaseSql.insert(0, comment("精确更新的字段") + space4 + "<sql id=\"updateBase\">\r\n");
        updateBaseSql.append(space4).append("</sql>\r\n\r\n");
        return updateBaseSql;
    }


    /**
     * 列名
     *
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createIfColumnName(SqlTable table, Config config, String space) {

        StringBuffer columnIfBuff = new StringBuffer();

        List<SqlParams> sqlParams = table.getSqlPars();
        int i = 1;
        for (SqlParams sqlParam : sqlParams) {
            columnIfBuff.append(space)
                    .append("<if test=\"" + sqlParam.getProperty() + " !=null\">  \r\n")
                    .append(space + space4)
                    .append(sqlParam.getColumnName());
            if (i != sqlParams.size()) {
                columnIfBuff.append(",\r\n");
            } else {
                columnIfBuff.append("\r\n");
            }
            columnIfBuff.append(space).append("</if>\r\n");
        }

        return columnIfBuff;
    }

    /**
     * 创建body
     *
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createFieldIf(SqlTable table, Config config, String prefix, String space,boolean flag) {

        StringBuffer fieldBuffer = new StringBuffer();
        List<SqlParams> sqlParams = table.getSqlPars();
        for (SqlParams sqlParam : sqlParams) {
            if (!sqlParam.getColumnName().equals(config.getVersionColumn())) {
                fieldBuffer.append(createFieldIf(sqlParam, space, prefix,flag));
            } else {
                table.setVersonColumn(sqlParam);
            }
        }
        int i = fieldBuffer.lastIndexOf(",");
        fieldBuffer.delete(i, i + 1);
        return fieldBuffer;
    }

    /**
     * 给一个sqlParam返回一个if节点
     *
     * @param sqlParam
     * @param space
     * @param prefix
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createFieldIf(SqlParams sqlParam, String space, String prefix,boolean flag) {
        StringBuffer fieldBuffer = new StringBuffer();
        fieldBuffer.append(space)
                .append("<if test=\"" + sqlParam.getProperty() + "  !=null\">\r\n")
                .append(space + space4)
                .append(prefix);

        if(flag) {
            fieldBuffer.append(sqlParam.getColumnName()).append(" = ");
        }

        fieldBuffer
                .append("#{")
                .append(sqlParam.getProperty())
                .append(",")
                .append("jdbcType=")
                .append(sqlParam.getSqlType())
                .append("}, \r\n");
        fieldBuffer.append(space).append("</if>\r\n");
        return fieldBuffer;
    }

    /**
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createInsertBase(SqlTable table, Config config) {

        StringBuffer insertBase = new StringBuffer(space8 + "(\r\n");
        List<SqlParams> sqlParams = table.getSqlPars();
        int i = 0;
        for (SqlParams sqlParam : sqlParams) {
            if (i != sqlParams.size()) {
                insertBase.append(space12).append(sqlParam.getColumnName()).append(",\r\n");
            } else {
                insertBase.append(space12).append(sqlParam.getColumnName()).append("\r\n");
            }
            i++;
        }
        insertBase.append(space8).append(")\r\n");
        return insertBase;
    }

    /**
     * 【what to do】
     *
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createInsertVals(SqlTable table, Config config, String prefix, Boolean colum) {
        StringBuffer insertBase = new StringBuffer();
        List<SqlParams> sqlParams = table.getSqlPars();
        int i = 1;
        for (SqlParams sqlParam : sqlParams) {

            insertBase.append(space12);
            if (colum) {
                insertBase.append(sqlParam.getColumnName())
                        .append(" = ");
            }
            insertBase.append("#{")
                    .append(prefix)
                    .append(sqlParam.getProperty())
                    .append(" ,jdbcType=")
                    .append(sqlParam.getSqlType());
            if (i != sqlParams.size()) {
                insertBase.append("},\r\n");
            } else {
                insertBase.append("}\r\n");
            }
            i++;
        }
        return insertBase;
    }


    /**
     * 添加注释
     *
     * @param comment
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer comment(String comment) {
        StringBuffer commentBuffer = new StringBuffer();
        return commentBuffer.append(space4)
                .append("<!-- " + comment + "-->\r\n");
    }

    /**
     * 创建一些不能共用的
     *
     * @param table
     * @param config
     * @return StringBuffer    返回类型
     * @author Aison
     */
    private StringBuffer createNomarl(SqlTable table, Config config) {

        StringBuffer nomarlBuffer = new StringBuffer();
        String fullProjo = config.getPojoPackage() + "." + table.getPojoName();
        StringBuffer allColumns = createInsertBase(table, config);

        //不是视图
        if (table.getTableType() != SqlTable.TableType.VIEW) {
            // ---------------selectByPrimaryKey
            SqlParams pkParam = table.getPkSqlParams();
            String pkJavaType = !pkParam.getNotNeedImport() ? pkParam.getFullJavaType() : pkParam.getShortJavaType();
            nomarlBuffer.append(comment("通过主键查找"))
                    .append(space4)
                    .append("<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"")
                    .append(pkJavaType)
                    .append("\"")
                    .append(" >")
                    .append("\r\n")
                    .append(space8)
                    .append("select \r\n")
                    .append(space8)
                    .append("<include refid=\"Base_Column_List\" />")
                    .append("\r\n")
                    .append(space8)
                    .append("from ")
                    .append(table.getTableName())
                    .append("\r\n")
                    .append(space8)
                    .append("where  ")
                    .append(pkParam.getColumnName())
                    .append(" = #{")
                    .append("id")
                    .append(",jdbcType=")
                    .append(pkParam.getSqlType())
                    .append("} ")
                    .append("\r\n")
                    .append(space4)
                    .append("</select>")
                    .append("\r\n\r\n");
            // ---------------deleteByPrimaryKey
            nomarlBuffer.append(comment("通过主键删除"))
                    .append(space4)
                    .append("<delete id=\"deleteByPrimaryKey\" parameterType=\"")
                    .append(pkJavaType)
                    .append("\"")
                    .append(" >")
                    .append("\r\n")
                    .append(space8)
                    .append("delete from ")
                    .append(table.getTableName())
                    .append("\r\n")
                    .append(space8)
                    .append("where ")
                    .append(pkParam.getColumnName())
                    .append(" = #{").append("id")
                    .append(",jdbcType=")
                    .append(pkParam.getSqlType())
                    .append("} ")
                    .append("\r\n")
                    .append(space4)
                    .append("</delete>").append("\r\n\r\n");
            // ---------------insert
            int last = allColumns.lastIndexOf(",");
            String inserAllColumns = allColumns.replace(last,last+1,"").toString();
            StringBuffer valsBuffer = createInsertVals(table, config, "", false);
            nomarlBuffer.append(comment("普通插入全字段插t入"))
                    .append(space4)
                    .append("<insert id=\"insert\" parameterType=\"")
                    .append(fullProjo)
                    .append("\" >")
                    .append("\r\n")
                    .append(space8)
                    .append("insert into ")
                    .append(table.getTableName())
                    .append("\r\n")
                    .append(inserAllColumns)
                    .append(space8)
                    .append("values\r\n")
                    .append(space8)
                    .append("(\r\n")
                    .append(valsBuffer)
                    .append(space8)
                    .append(")\r\n")
                    .append(space4)
                    .append("</insert>\r\n\r\n");
            // ---------------insertBach
            StringBuffer valsBufferItem = createInsertVals(table, config, "item.", false);

            nomarlBuffer.append(comment("批量插入，全部字段插入"))
                    .append(space4)
                    .append("<insert id=\"insertBach\"   parameterType=\"List\" >")
                    .append("\r\n")
                    .append(space8)
                    .append("insert into ")
                    .append(table.getTableName())
                    .append("\r\n")
                    .append(inserAllColumns)
                    .append(space8)
                    .append("values \r\n")
                    .append(space8)
                    .append("<foreach collection=\"list\" item=\"item\" separator=\",\">\r\n")
                    .append(space8)
                    .append("(\r\n")
                    .append(valsBufferItem)
                    .append(space8)
                    .append(")\r\n")
                    .append(space8)
                    .append("</foreach>\r\n")
                    .append(space4)
                    .append("</insert>\r\n\r\n");
            // ---------------insertSelective
            StringBuffer columnBuffer = createIfColumnName(table, config, space12);
            StringBuffer fieldIf = createFieldIf(table, config, "", space12,false);
            nomarlBuffer.append(comment("精确插入，非null得字段才会插入"))
                    .append(space4)
                    .append("<insert id=\"insertSelective\" parameterType=\"")
                    .append(fullProjo)
                    .append("\"")
                    .append(" useGeneratedKeys=\"true\" keyProperty=\"" + pkParam.getProperty() + "\"")
                    .append(" >\r\n")
                    .append(space8)
                    .append("insert into ")
                    .append(table.getTableName())
                    .append("\r\n")
                    .append(space8)
                    .append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" > \r\n")
                    .append(columnBuffer)
                    .append(space8)
                    .append("</trim>")
                    .append("\r\n\r\n")
                    .append(space8)
                    .append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >")
                    .append("\r\n")
                    .append(fieldIf)
                    .append(space8)
                    .append("</trim>")
                    .append("\r\n")
                    .append(space4)
                    .append("</insert>")
                    .append(" \r\n\r\n");
            // ---------------updateByPrimaryKeySelective
            SqlParams versionColumn = table.getVersonColumn();
            nomarlBuffer.append(comment("精确更新"))
                    .append(space4)
                    .append("<update id=\"updateByPrimaryKeySelective\" parameterType=\"")
                    .append(fullProjo)
                    .append("\" >")
                    .append("\r\n")
                    .append(space8)
                    .append("update ")
                    .append(table.getTableName())
                    .append("\r\n")
                    .append(space8)
                    .append("<set>")
                    .append("\r\n");
            if (Biz.valiArg(versionColumn)) {
                nomarlBuffer.append(createFieldIf(table.getVersonColumn(), space12, "",true));
            }
            nomarlBuffer.append(space12)
                    .append("<include refid=\"updateBase\"/>\r\n")
                    .append(space8)
                    .append("</set>\r\n")
                    .append(space8)
                    .append("where ")
                    .append(pkParam.getColumnName())
                    .append(" = #{")
                    .append(pkParam.getProperty())
                    .append(",jdbcType=")
                    .append(pkParam.getSqlType())
                    .append("} \r\n")
                    .append(space4)
                    .append("</update>")
                    .append(" \r\n\r\n");
            // ---------------update version

            if (Biz.valiArg(versionColumn)) {
                nomarlBuffer.append(comment("乐观锁更新"))
                        .append(space4)
                        .append("<update id=\"updateOptimisticLock\" parameterType=\"")
                        .append(fullProjo)
                        .append("\" >")
                        .append("\r\n")
                        .append(space8)
                        .append("update")
                        .append(table.getTableName())
                        .append("\r\n")
                        .append(space8)
                        .append("<set>")
                        .append("\r\n")
                        .append(space12)
                        .append(versionColumn.getColumnName())
                        .append(" = ")
                        .append(versionColumn.getColumnName())
                        .append("+1,\r\n")
                        .append(space12)
                        .append("<include refid=\"updateBase\"/>\r\n")
                        .append(space8)
                        .append("</set>\r\n")
                        .append(space8)
                        .append("where ")
                        .append(pkParam.getColumnName())
                        .append(" = #{")
                        .append(pkParam.getProperty())
                        .append(",jdbcType=")
                        .append(pkParam.getSqlType())
                        .append("} ")
                        .append(" AND ")
                        .append(versionColumn.getColumnName())
                        .append(" = #{")
                        .append(versionColumn.getProperty())
                        .append(",jdbcType=")
                        .append(versionColumn.getSqlType())
                        .append("} \r\n")
                        .append(space4)
                        .append("</update>")
                        .append(" \r\n\r\n");
            }
            // ---------------updateByPrimaryKey
            StringBuffer upNoIf = createInsertVals(table, config, "", true);
            nomarlBuffer.append(comment("全字段更新 需要注意"))
                    .append(space4)
                    .append("<update id=\"updateByPrimaryKey\" parameterType=\"")
                    .append(fullProjo)
                    .append("\" >")
                    .append("\r\n")
                    .append(space8)
                    .append("update ")
                    .append(table.getTableName())
                    .append("\r\n")
                    .append(space8)
                    .append("set \r\n")
                    .append(upNoIf)
                    .append(space8)
                    .append("where ")
                    .append(pkParam.getColumnName())
                    .append(" = #{")
                    .append(pkParam.getProperty())
                    .append(",jdbcType=")
                    .append(pkParam.getSqlType())
                    .append("} \r\n")
                    .append(space4)
                    .append("</update>")
                    .append(" \r\n\r\n");
        }
        // ---------------whereCase
        StringBuffer ifStringBuffer = createFieldIf(table, config, "AND ", space8,true);
        String ibf =  ifStringBuffer.toString().replaceAll("},","}");
        nomarlBuffer.append(comment("所有的条件集合"))
                .append(space4)
                .append("<sql id=\"whereCase\"  >")
                .append("\r\n")
                .append(space8)
                .append("where  1=1 \r\n")
                .append(ibf)
                .append(space8)
                .append("<include refid=\"costomWhereCase\"/>\r\n")
                .append(space4)
                .append("</sql>")
                .append("\r\n\r\n");

        // ---------------select one
        nomarlBuffer.append(comment("查询一条，如果返回多条会报错"))
                .append(space4)
                .append("<select id=\"selectOne\" parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">")
                .append("\r\n")
                .append(space8)
                .append("SELECT")
                .append("\r\n")
                .append(space8)
                .append("<include refid=\"Base_Column_List\" /> ")
                .append("\r\n")
                .append(space8)
                .append("FROM ")
                .append(table.getTableName())
                .append(" \r\n")
                .append(space8)
                .append("<include refid=\"whereCase\" /> ")
                .append("\r\n")
                .append(space4)
                .append("</select>")
                .append("\r\n\r\n");

        // ---------------select list
        nomarlBuffer.append(comment("查询一个list"))
                .append(space4)
                .append("<select id=\"selectList\" parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">")
                .append("\r\n")
                .append(space8)
                .append("SELECT")
                .append("\r\n")
                .append(space8)
                .append("<include refid=\"Base_Column_List\" /> ")
                .append("\r\n")
                .append(space8)
                .append("FROM ")
                .append(table.getTableName())
                .append(" \r\n")
                .append(space8)
                .append("<include refid=\"whereCase\" /> ")
                .append("\r\n")
                .append(space4)
                .append("</select>")
                .append("\r\n\r\n");

        // selectCount
        nomarlBuffer.append(comment("查询一个list"))
                .append(space4)
                .append("<select id=\"selectCount\" parameterType=\"java.util.Map\" resultType=\"java.lang.Integer\">")
                .append("\r\n")
                .append(space8)
                .append("SELECT")
                .append("\r\n")
                .append(space8)
                .append("count(1) ")
                .append("\r\n")
                .append(space8)
                .append("FROM ")
                .append(table.getTableName())
                .append("\r\n")
                .append(space8)
                .append("<include refid=\"whereCase\" /> ")
                .append("\r\n")
                .append(space4)
                .append("</select>")
                .append("\r\n\r\n");

        return nomarlBuffer;
    }


//	public static void createXml(SqlTable st, Map<String, String> map) {
//		File f = new File(map.get("xmlPath") + st.getVoName() + "Mapper.xml");
//		try {
//		
//			// ---------------where case
//			ci = 0;
//			sb1.append("    <sql id=\"whereCase\"  >").append("\r\n");
//			sb1.append("         where  1=1  \r\n");
//			for (SqlParams sp : st.getSqlPars()) {
//				sb1.append("        <if test=\"").append(sp.getField()).append(" !=null\">  \r\n")
//						.append("         AND ").append(SqlParams.changeKeyWords(sp.getColumnName())).append("  = #{")
//						.append(sp.getField()).append(",jdbcType=").append(sp.getSqlType()).append("}");
//				sb1.append("\r\n").append("        </if>").append("\r\n");
//				;
//			}
//			sb1.append("        <include refid=\"costomWhereCase\"/>\r\n");
//			sb1.append("    </sql>").append(" \r\n\r\n");
//
//			// ---------------order by
//			// sb1.append(" <sql id=\"orderByCase\" >").append("\r\n");
//			// sb1.append(" <if test =\"orderBy !=null\">").append("\r\n");
//			// sb1.append(" ORDER BY ${orderBy} ").append("\r\n");
//			// sb1.append(" </if> ").append("\r\n");
//			// sb1.append(" <if test =\"beginIndex !=null and endIndex !=null
//			// \">").append("\r\n");
//			// sb1.append(" limit #{beginIndex},#{endIndex} ").append("\r\n");
//			// sb1.append(" </if> ").append("\r\n");
//			// sb1.append(" </sql>").append("\r\n\r\n");
//
//			// ---------------select one
//			sb1.append("    <select id=\"selectOne\" parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">")
//					.append("\r\n");
//			sb1.append("         SELECT").append("\r\n");
//			sb1.append("         <include refid=\"Base_Column_List\" /> ").append("\r\n");
//			sb1.append("         FROM ").append(st.getTableName()).append(" \r\n");
//			sb1.append("         <include refid=\"whereCase\" /> ").append("\r\n");
//			sb1.append("    </select>").append("\r\n\r\n");
//
//			// ---------------select list
//			sb1.append("    <select id=\"selectList\" parameterType=\"java.util.Map\" resultMap=\"BaseResultMap\">")
//					.append("\r\n");
//			sb1.append("         SELECT").append("\r\n");
//			sb1.append("         <include refid=\"Base_Column_List\" /> ").append("\r\n");
//			sb1.append("         FROM ").append(st.getTableName()).append(" \r\n");
//			sb1.append("         <include refid=\"whereCase\" /> ").append("\r\n");
//			// sb1.append(" <include refid=\"orderByCase\" /> ").append("\r\n");
//			sb1.append("    </select>").append("\r\n\r\n");
//
//			// ---------------select count
//			sb1.append(
//					"    <select id=\"selectCount\" parameterType=\"java.util.Map\" resultType=\"java.lang.Integer\">")
//					.append("\r\n");
//			sb1.append("         SELECT").append("\r\n");
//			sb1.append("         count(1) ").append("\r\n");
//			sb1.append("         FROM ").append(st.getTableName()).append(" \r\n");
//			sb1.append("         <include refid=\"whereCase\" /> ").append("\r\n");
//			sb1.append("    </select>").append("\r\n\r\n");
//
//			if (sb.length() == 0) {
//				sb1.append("<!--  @Costom -->").append("\r\n");
//				sb1.append("<!-- 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql放在此行下面 -->").append("\r\n");
//				sb1.append("\r\n");
//				sb1.append("    <sql id=\"costomWhereCase\">\r\n</sql>\r\n");
//				sb1.append("</mapper>");
//			} else {
//				if (sb.indexOf("costomWhereCase") == -1) {
//					int costom = sb.indexOf("<!-- 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql放在此行下面 -->")
//							+ "<!-- 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql放在此行下面 -->".length();
//					sb.insert(costom, "\r\n    <sql id=\"costomWhereCase\">\r\n    </sql>");
//				}
//			}
//			sb1.append(sb);
//			sb1.insert(0, "<mapper namespace=\"" + nameSpace + "\"> \r\n");
//			sb1.insert(0,
//					"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" > \r\n");
//			sb1.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \r\n");
//
//			FileOutputStream fos = new FileOutputStream(f);
//			fos.write(sb1.toString().getBytes());
//
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


}
