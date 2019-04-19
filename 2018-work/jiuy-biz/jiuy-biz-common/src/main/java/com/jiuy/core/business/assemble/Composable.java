package com.jiuy.core.business.assemble;

/**
 * 该接口表示实现该接口的对象都是内部可组合拼装的，组合条件为
 * 该对象的Id
 * 
 * @author LWS
 *
 */
public interface Composable<T extends Object> {

    public T getIdentity(); 
}
