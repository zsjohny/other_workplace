package com.onway.baib.common.util;

import com.onway.platform.common.exception.BaseRuntimeException;

public class VersionUtil {

    /**
     * 
     * 是否小于指定版本 （向后兼容）
     * @param currentVersion
     * @param oldVersion
     * @param platform
     * @return
     */
    public static boolean isSupport(String currentVersion, String oldVersion) {
        return compareVersion(currentVersion, oldVersion) < 0;
    }

    /**
     * 比较版本高低，version1 &lt; version2 返回-1 ，等于返回0，大于返回1
     * 
     * @param version1
     * @param version2
     * @return
     * @throws Exception
     */
    private static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            throw new BaseRuntimeException("版本号格式错误");
        }
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值  
        int diff = 0;
        while (idx < minLength
               && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度  
               && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符  
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；  
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

}
