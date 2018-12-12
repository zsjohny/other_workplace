package com.reliable.util;

import java.util.Properties;

public class CaluRatio
{
    public static Double calScore(String socre, Properties properties)
    {
        return Double.valueOf(1.0D / Double.parseDouble(properties.getProperty("appTotal")) * Double.parseDouble(socre.trim()) + Double.parseDouble(properties.getProperty("appRatio")));
    }

    public static void main(String[] args)
    {
        System.out.println(QueryOutside.outsideFinde("15924179757", "2015-07-21", "Android"));
    }
}