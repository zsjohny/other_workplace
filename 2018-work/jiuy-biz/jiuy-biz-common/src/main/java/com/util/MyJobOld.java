package com.util;

/**
 * 执行任务
 * @author Aison
 * @version V1.0
 * @date 2018/5/30 17:14
 * @Copyright 玖远网络
 */
public interface MyJobOld<S,T> {

    /**
     * 提供异界接口可以修改list中的每个元素的类型和属性
     * @param source
     * @param target
     * @author Aison
     * @date 2018/6/5 17:54
     */
    void execute(S source, T target);

}