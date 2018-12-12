package com.finace.miscroservice.task_scheduling.test.reload;

import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

    public MyClassLoader() {
        super(null);
    }

    /**
     * eg com.example.example
     */
    public <T> T register(Class<T> classNames) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(classNames.getSimpleName() + ".class");
        byte[] cache = new byte[inputStream.available()];
        inputStream.read(cache);
        return (T) defineClass(classNames.getCanonicalName(), cache, 0, cache.length);
    }


    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class cl = findLoadedClass(name);

        if (cl == null) {
            cl = getSystemClassLoader().loadClass(name);
        }

        if (resolve)
            resolveClass(cl);

        return cl;
    }


}
