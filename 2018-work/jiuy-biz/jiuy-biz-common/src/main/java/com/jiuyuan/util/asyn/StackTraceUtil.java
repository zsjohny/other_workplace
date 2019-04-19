/**
 * 
 */
package com.jiuyuan.util.asyn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author LWS
 */
public class StackTraceUtil {

    public static StackTraceElement[] printStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static boolean containParentCallStack(Class annotaionClazz) {
        if(null == annotaionClazz){
            return false;
        }
        else{
            boolean bContains = false;
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for(StackTraceElement ste : stackTraceElements){
                try {
                    if(null != ste && null != AnnotationUtils.findAnnotation(Class.forName(ste.getClassName()), annotaionClazz)){
                        bContains = true;
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    // no class is found 
                    // do nothing here
                }
            }
            return bContains;
        }
    }
    
    public static boolean containParentCallStack(String parentName){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if(null != stackTraceElements && stackTraceElements.length != 0){
            boolean bContains = false;
            for(StackTraceElement ste : stackTraceElements){
                if(StringUtils.containsIgnoreCase(ste.getClassName(), parentName) || StringUtils.containsIgnoreCase(ste.getMethodName(), parentName)){
                    bContains = true;
                    break;
                }
            }
            return bContains;
        }else{
            return false;
        }
    }
}
