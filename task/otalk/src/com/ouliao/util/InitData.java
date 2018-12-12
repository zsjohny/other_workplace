/**
 *
 */
package com.ouliao.util;

import com.ouliao.domain.versionfirst.User;
import com.xiaoluo.util.DesUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import redis.clients.jedis.Jedis;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xiaoluo
 * @version $Id: InitData.java, 2016年4月5日 上午10:16:28
 */

public class InitData implements ApplicationListener<ContextRefreshedEvent> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        final Jedis jedisDelete = new Jedis("localhost", 10088);

        //jedisDelete.flushAll();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Jedis jedis = new Jedis("localhost", 10088);
                List<User> list = entityManager.createQuery("from User  where userContract='true' and isDeleted='0'")
                        .getResultList();

                if (list != null & list.size() > 0) {

                    try {

                        for (User user : list) {

                            if (user == null) {
                                continue;
                            }
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(user);
                            oos.close();
                            baos.close();
                            jedis.set(String.valueOf("user:" + user.getUserId()).getBytes(), baos.toByteArray());
                            jedis.sadd("user:list", String.valueOf(user.getUserId()));

                        }

                        list = entityManager.createQuery(
                                "from User  where userContract='true' and isDeleted='0' order by userCallScore  desc",
                                User.class).getResultList();

                        if (list != null & list.size() > 0) {

                            for (User user : list) {

                                if (user == null) {
                                    continue;
                                }

                                jedis.set(String.valueOf("user:userCallScore:" + user.getUserId()), String
                                        .valueOf(user.getUserCallScore() == null ? "0" : user.getUserCallScore()));
                            }
                        }
                        list = entityManager.createQuery(
                                "from User  where userContract='true' and isDeleted='0' order by userCallTotal desc",
                                User.class).getResultList();

                        if (list != null & list.size() > 0) {

                            for (User user : list) {

                                if (user == null) {
                                    continue;
                                }

                                jedis.set(String.valueOf("user:userCallTotal:" + user.getUserId()), String
                                        .valueOf(user.getUserCallTotal() == null ? "0" : user.getUserCallTotal()));
                            }
                        }
                        jedis.disconnect();
                    } catch (Exception e) {
                        System.out.println(e.getMessage() + ".....");
                    }

                }

            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1000 * 60 * 60 * 24);
        jedisDelete.disconnect();
    }

    public static void main(String[] args) {
        // 255e47ed-cf61-4b9e-aab5-1eb6cb4676e4....传值...aeGG6A9nwZif0Q+WW4SnSGZGD26+CVbqwGb+Siz3Xk4kE+4J72eOHA==...
        System.out
                .println(DesUtil.decrypt("aeGG6A9nwZif0Q+WW4SnSGZGD26+CVbqwGb+Siz3Xk4kE+4J72eOHA==", "7807240830641"));
    }

}
