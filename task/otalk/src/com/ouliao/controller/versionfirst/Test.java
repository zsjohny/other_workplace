package com.ouliao.controller.versionfirst;

import com.xiaoluo.util.DesIosAndAndroid;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by nessary on 16-5-4.
 */
public class Test extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("df d" + message);
    }

    public static void main(String[] args) {
        System.out.println(DesIosAndAndroid.encryptDES("879227577","15924179757"));
    }
}
