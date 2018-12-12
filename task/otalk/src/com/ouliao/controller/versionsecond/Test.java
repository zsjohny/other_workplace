package com.ouliao.controller.versionsecond;

import org.apache.commons.mail.EmailException;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * Created by nessary on 16-5-13.
 */
public class Test {
    public static void main(String[] args) throws IOException, EmailException {
        Jedis jedis = new Jedis("localhost", 10088);

        jedis.publish("test", "hello");



        /*Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();

        while (networkInterfaceEnumeration.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
            Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                System.out.println(addressEnumeration.nextElement());
            }

        }*/

    }
}


