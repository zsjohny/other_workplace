/**
 *
 */
package com.tunnel.util;


import com.tunnel.controller.AccessController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

/**
 * @author xiaoluo
 * @version $Id: Md5.java, 2016年3月3日 上午10:19:36
 */

public class Md5 {
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        Files.readAllBytes(Paths.get(new File(AccessController.class.getResource("/").getPath(), "example.xls").getPath()));

//        Path path = Paths.get(new File(AccessController.class.getClassLoader().getResource("example.xls").getFile()).getPath());
//        System.out.println(path);
    }
}
