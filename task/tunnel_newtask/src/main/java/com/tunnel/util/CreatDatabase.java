package com.tunnel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * Created by Ness on 2016/10/11.
 */
public class CreatDatabase implements ServletContextListener {

    public static Boolean register = false;

    private String DEAFULT_DATABASES = "tunnel";

    private static Logger logger = LoggerFactory.getLogger(CreatDatabase.class);


    public void contextInitialized(ServletContextEvent sce) {


        if (CompareDate.compare()) {

            System.exit(-1);
        } else {

            CreatDatabase.register = true;
            try {

                Properties properties = new Properties();
                properties.load(CreatDatabase.class.getClassLoader().getResourceAsStream("jdbc.properties"));


                Class.forName("com.mysql.jdbc.Driver");

                Connection connection = DriverManager.getConnection(properties.getProperty("jdbcInitUrl"), properties.getProperty("user"), properties.getProperty("password"));

                PreparedStatement preparedStatement = connection.prepareStatement("CREATE  database if NOT  EXISTS " + DEAFULT_DATABASES + " CHARACTER  SET  utf8");

                preparedStatement.execute();

                logger.info("创建数据库成功");


            } catch (Exception e) {
                logger.warn("创建数据库出错", e);
            }
        }


    }


    public CreatDatabase() {
        super();
    }

    public static String getSessionId(HttpServletRequest request) {


        Cookie[] cookies = request.getCookies();

        String sessionId = "";

        if (cookies == null) {
            return sessionId;
        }

        if (cookies.length == 1) {
            sessionId = cookies[0].getValue();
            logger.info("获取的sessionId为{}", sessionId);
            return sessionId;

        }


        for (Cookie cookie : cookies) {

            if (cookie == null) {
                continue;
            }

            if (cookie.getName().contains("JSESSIONID")) {

                sessionId = cookie.getValue();
                break;


            }
        }


        logger.info("获取的sessionId为{}", sessionId);
        return sessionId;

    }

    public static void main(String[] args) {
        try {
            String path = System.getProperty("catalina.home");
            System.out.println(path);
            Process exec = Runtime.getRuntime().exec("cmd /c "+path+"/bin/shutdown.bat");

            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getErrorStream(),"gbk"));
            String str = "";
            while (str!=null){
                str = reader.readLine();
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
