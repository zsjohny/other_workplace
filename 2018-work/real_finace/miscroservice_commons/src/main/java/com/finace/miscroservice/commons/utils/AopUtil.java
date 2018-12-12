package com.finace.miscroservice.commons.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * aop的工具类
 */
public class AopUtil {

    private AopUtil() {

    }

    /**
     * 根绝切点获取返回类型的默认值
     *
     * @param joinPoint 切点
     * @return
     */
    public static Object getReturnType(JoinPoint joinPoint) {
        Object result;
        Signature signature = joinPoint.getSignature();
        String returnName = ((MethodSignature) signature).getReturnType().getTypeName().toLowerCase();
        switch (returnName) {
            case "boolean":
                result = Boolean.FALSE;
                break;
            case "string":
                result = "";
                break;
            case "response":
                result = Response.fail();
                break;
            default:
                result = null;
        }
        return result;
    }
}
