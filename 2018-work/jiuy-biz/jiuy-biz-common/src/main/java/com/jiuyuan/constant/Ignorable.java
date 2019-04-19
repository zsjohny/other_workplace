package com.jiuyuan.constant;

/**
 * <pre>
 * 主要解决mapper里面all的情况下不进行status...字段的过滤
 * </pre>
 * 
 */
public interface Ignorable {

    boolean isIgnorable();

}
