package com.jiuy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.jiuy.exception.BizException;
import com.jiuy.exception.GlobalsEnums;
import com.jiuy.jdbc.JDBC;
import com.jiuy.pojo.Config;
import com.jiuy.pojo.SqlTable;
import com.jiuy.pojo.XmlUtil;

/**
 * 创建文件的主要入口程序
 *
 * @author Aison
 * @version V1.0
 * @date 2017年12月28日 上午9:49:17
 */
public class MainEnter {


    /**
     * 创建文件主要入口方法
     *
     * @param
     * @return void    返回类型
     * @throws
     * @author Aison
     */
    public StringBuffer doCreate(String configXmlPath) {

        String xmlStr = fileToString(configXmlPath);
        // 解析xml
        List<Config> configs = xml2Config(xmlStr);
        StringBuffer sbrs = new StringBuffer();
        for (Config config : configs) {
            for (String table : config.getTables()) {
                JDBC jdbc = JDBC.instance(config);
                List<SqlTable> sqlTbaleItem = jdbc.getSqlTbales(table, config);
                MakeFile mkFile = MakeFile.instance(config);
                sbrs.append(mkFile.createMapperXml(sqlTbaleItem, config));
                sbrs.append(mkFile.createPojo(sqlTbaleItem, config));
                sbrs.append(mkFile.createMapper(sqlTbaleItem, config));
            }
        }
        return sbrs;
    }

    /**
     * 返回xml中的内容
     *
     * @param xmlPath
     * @return String    返回类型
     * @author Aison
     */
    private String fileToString(String xmlPath) {
        FileInputStream fileInpuStream = null;
        InputStreamReader reader = null;
        BufferedReader bufferReader = null;

        try {
            File file = new File(xmlPath);
            fileInpuStream = new FileInputStream(file);
            reader = new InputStreamReader(fileInpuStream);
            bufferReader = new BufferedReader(reader);
            String line = bufferReader.readLine();
            StringBuffer xmlBuffer = new StringBuffer();
            while (line != null) {
                xmlBuffer.append(line).append("\r\n");
                line = bufferReader.readLine();
            }
            return xmlBuffer.toString();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (fileInpuStream != null) {
                    fileInpuStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 将xml字符串转成config对象
     *
     * @param xmlStr xml字符串
     * @return Config    返回类型
     * @author Aison
     */
    @SuppressWarnings("unchecked")
    private List<Config> xml2Config(String xmlStr) {

        Map<String, Object> map = XmlUtil.getMap(xmlStr);


        Object mapper = map.get("mapper");
        Object strict = map.get("strict");
        String author = (String) map.get("author");
//        if (!Biz.valiArg(author) || !"aichengsong".equals(author)) {
//            throw new BizException(GlobalsEnums.AUTHOR_ERROR);
//        }
        String strictStr = strict == null ? "0" : (String) strict;
        String mappingFile = (String) map.get("mappingFile");
        Map<String, Object> annotations = (Map<String, Object>) map.get("Annotations");
        String baseMapperClass = "";
        String primaryKey = "";
        String modelName = "";
        String fieldName = "";

        if (annotations != null) {
            baseMapperClass = (String) annotations.get("baseMapperClass");
            primaryKey = (String) annotations.get("primaryKeyClass");
            modelName = (String) annotations.get("modelNameClass");
            fieldName = (String) annotations.get("fieldNameClass");
        }

        boolean isMap = Map.class.isAssignableFrom(mapper.getClass());
        boolean isList = List.class.isAssignableFrom(mapper.getClass());
        // 如果是map则将这个对象转成list
        List<Object> mapperArray = null;
        if (isMap) {
            mapperArray = new ArrayList<Object>();
            mapperArray.add(mapper);
        } else if (isList) {
            mapperArray = (List<Object>) mapper;
        }

        Map<String, String> dbConfig = (Map<String, String>) map.get("dbconf");
        String url = dbConfig.get("url");
        String userName = dbConfig.get("userName");
        String pwd = dbConfig.get("pwd");
        String dirver = dbConfig.get("classDriver");
        String schema = dbConfig.get("schema");
        List<Config> configs = new ArrayList<Config>();

        for (Object item : mapperArray) {
            Config config = new Config();
            Map<String, Object> mapperMap = (Map<String, Object>) item;
            Map<String, String> pojo = (Map<String, String>) mapperMap.get("pojo");
            Map<String, String> mapperXml = (Map<String, String>) mapperMap.get("mapperXml");
            Map<String, String> mapperInterface = (Map<String, String>) mapperMap.get("mapperInterface");

            String projoPackage = pojo.get("package");
            String pojoName = pojo.get("name");
            String pojoAbsolutePath = pojo.get("absolutePath");
            String createQuery = pojo.get("createQuery");

            config.setPojoPackage(projoPackage);
            config.setPojoPath(pojoAbsolutePath);
            config.setCreateQuery(createQuery);
            if (Biz.valiArg(pojoName)) {
                config.setProjoNames(pojoName.split(","));
            }

            String mapperPackage = mapperInterface.get("package");
            String mapperPath = mapperInterface.get("absolutePath");
            config.setMapperPackage(mapperPackage);
            config.setMapperPath(mapperPath);

            String xmlPath = mapperXml.get("absolutePath");
            String xmlPakage = mapperXml.get("package");
            String versionColumn = mapperXml.get("versionColumn");
            config.setXmlPath(xmlPath);
            config.setXmlPackage(xmlPakage);
            config.setVersionColumn(versionColumn);

            String tables = (String) mapperMap.get("table");
            config.setTables(tables.split(","));

            config.setBaseMapperPackage(baseMapperClass);
            config.setFieldNameAnnotation(fieldName);
            config.setModelNameAnnotation(modelName);
            config.setPrimaryKeyAnnotation(primaryKey);
            config.setMappingFile(mappingFile);

            config.setUrl(url);
            config.setPwd(pwd);
            config.setUserName(userName);
            config.setStrict(strictStr);
            config.setClassDriver(dirver);
            config.setSchema(schema);
            configs.add(config);

        }

        return configs;
    }


}
