package com.finace.miscroservice.module;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Main {

    private static final String URL_SUFFIX = "/activity/load";

    private static final String JAR_SIGN = "jar";
    private static final String FILE_SIGN = "/";
    private static final String SUCCESS_SIGN = "200";

    /**
     * 执行java -jar ./xx.jar 后面接任何参数表示正式环境
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {

        String classPathName = System.getProperty("java.class.path");

        if (classPathName == null || classPathName.isEmpty() || !classPathName.endsWith(JAR_SIGN)) {
            return;
        }

        String[] urlArr = {"http://112.17.92.53:8081/activity"};
        ;
        if (args != null && args.length != 0) {
            urlArr = new String[]{"http://101.37.151.54:8083", "http://120.27.222.192:8083"};

        }


        for (String url : urlArr) {


            HttpURLConnection conn = (HttpURLConnection) new URL(url + URL_SUFFIX).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setDoOutput(Boolean.TRUE);
            conn.setDoInput(Boolean.TRUE);
            conn.connect();
            DataOutputStream stream = new DataOutputStream(conn.getOutputStream());

            String name = classPathName.split(".jar")[0];
            if (name.contains(FILE_SIGN)) {
                String[] namesArr = name.split(FILE_SIGN);
                name = namesArr[namesArr.length - 1];
            }
            stream.write(("name=" + name).getBytes());

            String result = new BufferedReader(new InputStreamReader(conn.getInputStream())).lines().collect(Collectors.joining());

            if (result.contains(SUCCESS_SIGN)) {
                System.out.println("exec success");
            } else {
                System.out.println("exec fail");
            }

            stream.close();
            conn.disconnect();

        }
    }
}
