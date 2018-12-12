package com.tunnel.util;

import com.tunnel.domain.TunnelImportPort;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ness on 2016/11/8.
 */
public class test {
    public static void main(String[] args) throws IntrospectionException, IOException {

        TunnelImportPort tunnelImportPort = new TunnelImportPort();

        BeanInfo beanInfo = Introspector.getBeanInfo(tunnelImportPort.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        ConcurrentHashMap<String, Map<String, Integer>> sortMap = new ConcurrentHashMap<>();
        Map<String, Integer> map = null;


        File[] files = new File(System.getProperty("user.dir") + File.separator + "src\\main\\java\\com\\tunnel\\domain").listFiles();

        for (File file : files) {


            List<String> strings = Files.readAllLines(Paths.get(file.getPath()));

            map = new HashMap<>();


            int len = strings.size();

            int k = 0;
            for (int i = 0; i < len; i++) {
                if (i == len - 2) {
                    break;
                }
                if (strings.get(i + 1).trim().contains("page")) {
                    continue;
                }
                if (strings.get(i).trim().startsWith("//") && (strings.get(i + 1).trim().startsWith("Long") ||
                        strings.get(i + 1).trim().startsWith("Double") ||
                        strings.get(i + 1).trim().startsWith("Integer") || strings.get(i + 1).trim().startsWith("Boolean") ||
                        strings.get(i + 1).trim().startsWith("String") || strings.get(i + 1).trim().startsWith("LocalDateTime")
                ) && strings.get(i + 2).trim().length() == 0) {
                    map.put(strings.get(i + 1).trim().split(" ")[1], k++);
                }


            }
            if (!map.isEmpty()) {
                sortMap.put(file.getName(), map);

            }
        }


        Map<String, Integer> propertySortMap = sortMap.get(tunnelImportPort.getClass().getSimpleName() + ".groovy");


        PropertyDescriptor[] newPropertys = new PropertyDescriptor[propertyDescriptors.length];

        for (int i = 0; i < propertyDescriptors.length; i++) {


            for (PropertyDescriptor p : propertyDescriptors) {

                if (propertySortMap.get(p.getName()) == null) {
                    continue;
                }
                if (propertySortMap.get(p.getName()) == i) {
                    System.out.println(p.getName());
                    newPropertys[i] = p;
                }
            }


        }


        for (PropertyDescriptor s : newPropertys) {
            if (s == null) {
                continue;
            }
//            System.out.println(s.getName());
        }


    }
}
