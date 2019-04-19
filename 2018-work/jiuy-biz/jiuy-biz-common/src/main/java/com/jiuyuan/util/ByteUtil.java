package com.jiuyuan.util;

public class ByteUtil {
    public final static long ONE_KB_SIZE = 1024;

    public final static long ONE_MB_SIZE = 1024 * ONE_KB_SIZE;

    public final static long ONE_GB_SIZE = 1024 * ONE_MB_SIZE;

    public static String formatByteSize(long size) {
        StringBuilder strSize = new StringBuilder();
        if (size >= ONE_MB_SIZE) {
            if (size % ONE_MB_SIZE == 0) {
                strSize.append(size / ONE_MB_SIZE);
            } else {
                strSize.append(String.format("%.2f", (double) size / ONE_MB_SIZE));
            }
            strSize.append("MB");
        } else if (size >= ONE_KB_SIZE) {
            if (size % ONE_KB_SIZE == 0) {
                strSize.append(size / ONE_KB_SIZE);
            } else {
                strSize.append(String.format("%.2f", (double) size / ONE_KB_SIZE));
            }
            strSize.append("KB");
        } else {
            strSize.append(size);
            strSize.append("B");
        }
        return strSize.toString();
    }
}
