package com.finace.miscroservice.commons.utils;

import java.util.ResourceBundle;

public class ResourceBundleTest {
    public static void main(String[] args) {
//        ResourceBundle resourceBundle = ResourceBundle.getBundle("pathFilter");
        //具体包下面的
        ResourceBundle resourceBundle = ResourceBundle.getBundle("script.pathFilter");
//        resourceBundle=new PropertyResourceBundle(new InputStreamReader(""))
        System.out.println(resourceBundle.getString("health"));

    }
}
