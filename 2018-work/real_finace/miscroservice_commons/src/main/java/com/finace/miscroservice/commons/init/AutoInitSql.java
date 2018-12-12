package com.finace.miscroservice.commons.init;

import com.finace.miscroservice.commons.log.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 自动初始化sql
 */

public class AutoInitSql {

    /**
     * 初始化的文件名称
     */
    private final String INIT_FILE_NAME = "/script/init.sql";

    private Log log = Log.getInstance(AutoInitSql.class);

    private String className;

    private String url;

    private String username;

    private String password;


    public AutoInitSql(String className, String url, String username, String password) {
        this.className = className;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 初始化任务
     */
    public void init() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(INIT_FILE_NAME), "utf-8"))) {

            String line = "";
            StringBuilder sqlBuild = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                //过滤空格和注释
                if (line.isEmpty() || line.charAt(1) == '-') {
                    continue;
                }
                sqlBuild.append(line);
                sqlBuild.append("\n");

            }

            if (sqlBuild.length() == 0) {
                log.warn("sql脚本={}文件参数为空 不执行任务", INIT_FILE_NAME);

            }

            Class.forName(className);

            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuild.toString());

            try {
                preparedStatement.execute();
            } catch (SQLException e) {
                //ignore
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();

                }
            }

            log.info("sql脚本={}执行成功", INIT_FILE_NAME);

        } catch (Exception e) {
            log.error("sql脚本={}执行出错", INIT_FILE_NAME, e);
        }


    }


}
