package com.tunnel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;

/**
 * Created by Ness on 2016/10/11.
 */
public class CompareDate {

    public static Boolean compare() {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("http://register.hengmo.net:10000/rg");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-type", "application/x-java-serialized-object");
            connection.setRequestMethod("POST");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connection.connect();
            String len = reader.readLine();
            while (len != null) {
                sb.append(len);
                len = reader.readLine();
            }

        } catch (Exception e1) {
        }

        String key = sb.toString();

        String[] dateArr = new String(Base64.getDecoder().decode(key)).split(":");
        LocalDate date = LocalDate.now();
        int year = Integer.valueOf(DesUtil.decrypt(dateArr[1], new String(Base64.getDecoder().decode(dateArr[0]))));
        int month = Integer.valueOf(DesUtil.decrypt(dateArr[2], new String(Base64.getDecoder().decode(dateArr[0]))));
        int day = Integer.valueOf(DesUtil.decrypt(dateArr[3], new String(Base64.getDecoder().decode(dateArr[0]))));

        LocalDate registerDate = LocalDate.of(year, month, day);


        return date.isAfter(registerDate);
    }

    public static void main(String[] args) {
        System.out.println(compare());
    }
}
