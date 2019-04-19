package com.jiuyuan.util.instantiate;

/**
 * A factory to create instances of classes.
 * 
 */
public interface InstanceFactory {

    /**
     * @param hint A legal value that can be used to infect the instantiation process.
     * @return An instance of the given type.
     */
    <T> T instantiate(Class<T> type, String hint);
}
