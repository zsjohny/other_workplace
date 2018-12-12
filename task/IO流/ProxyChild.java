package org.finace.utils.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Ness on 2016/12/22.
 */
public class ProxyChild implements ProxySub {
    @Override
    public void test() {
        System.out.println("do something");
    }

    private static class ProxyHandle implements InvocationHandler {

        private Object obj;

        protected ProxyHandle(Object obj) {
            this.obj = obj;
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("beforeHandle");

            method.invoke(obj, args);
            System.out.println("endHandle");

            return null;
        }
    }

    public static void main(String[] args) {
        ProxyChild child = new ProxyChild();
        ProxyHandle proxyHandle = new ProxyHandle(child);
        Class<? extends ProxyChild> aClass = child.getClass();
        ProxySub o = (ProxySub) Proxy.newProxyInstance(aClass.getClassLoader(), aClass.getInterfaces(), proxyHandle);
        o.test();

    }

}
