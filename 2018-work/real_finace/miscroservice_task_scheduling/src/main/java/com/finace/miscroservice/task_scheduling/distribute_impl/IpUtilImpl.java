package com.finace.miscroservice.task_scheduling.distribute_impl;


import com.finace.miscroservice.distribute_task.util.IpUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP的获取工具类
 */
public class IpUtilImpl implements IpUtil {
    private final String CONTAIN_INFO = "IP地址";

    private final Pattern compile = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");

    @Override
    public String getInternetIp() {
        String internetIp = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://www.ipip.net").openStream()))) {

            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains(CONTAIN_INFO)) {
                    Matcher matcher = compile.matcher(line);
                    while (matcher.find()) {
                        internetIp = matcher.group();
                    }

                }
            }

        } catch (Exception e) {

        }
        return internetIp;

    }


}
