package com.finace.miscroservice.commons.utils;

import com.finace.miscroservice.commons.log.Log;
import lombok.Synchronized;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 程序启动控制类
 */
public class ApplicationContextUtil {

    private static Log logger = Log.getInstance(ApplicationContextUtil.class);

    public static final String EXCLUDE_START_PARAMS = "--spring.config.location=classpath:/bootstrap-exclude.yml";


    private static final Boolean LOG_ENABLED_FLAG = Boolean.FALSE;


    private static final String CONFIG_ENVIRON_PROPERTIES = "properties/config-environ-%s.properties";


    private static final BlockingQueue<Task> taskCache = new ArrayBlockingQueue<>(20);

    private static ConfigurableApplicationContext context;


    private final static String LOG_PATH_KEY = "log.path";

    public final static String TEST_ENVIRON = "test";

    public final static String DEV_ENVIRON = "dev";

    public final static String APP_ENVIRON = "app.environ";


    /**
     * 启动
     *
     * @param source 启动的类
     * @param args   启动指定参数
     */
    @Synchronized
    public static void run(Object source, String... args) {


        try {
            String fileEnvironSuffix = DEV_ENVIRON;

            if (System.getProperty(LOG_PATH_KEY) == null) {
                //设定日志的地址
                System.setProperty(LOG_PATH_KEY, Paths.get("..").toRealPath().normalize() + File.separator + "logs" + File.separator);

                fileEnvironSuffix = TEST_ENVIRON;
            }

            System.setProperty(APP_ENVIRON, fileEnvironSuffix);

            //加载环境配置
            Properties properties = new Properties();
            properties.load(new ClassPathResource(String.format(CONFIG_ENVIRON_PROPERTIES, fileEnvironSuffix))
                    .getInputStream());

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key == null || value == null) {
                    continue;
                }
                //判定是否是通过参数设定的属性
                if (System.getProperty(key.toString()) != null) {
                    continue;
                }
                System.setProperty(key.toString(), value.toString());

            }

        } catch (IOException e) {
            logger.error("加载环境配置出错", e);

        }


        if (LOG_ENABLED_FLAG) {
            try {
                logger.info("开始自动生成api文档");
                Scanner.getDoc();

            } catch (IOException e) {
                logger.error("自动生成api文档失败", e);
            }
        }
        logger.info("开始启动服务......");
        context = SpringApplication.run(source, args);
        Environment environment = context.getEnvironment();
        String port = environment.getProperty("server.port");
        logger.info("======================================================================================");
        logger.info("                                                                                      ");
        logger.info(String.format("................................服务启动成功,端口为%s................................", port));
        logger.info(String.format("................................日志保存路径为: %s................................", System.getProperty(LOG_PATH_KEY)));
        logger.info("                                                                                      ");
        logger.info("======================================================================================");


        //执行启动后的任务
        if (!taskCache.isEmpty()) {
            for (Iterator<Task> iterator = taskCache.iterator(); iterator.hasNext(); ) {
                iterator.next().task();
            }
        }

        if (LOG_ENABLED_FLAG) {

            try {
                logger.info("api文档生成成功 访问地址:http://" + InetAddress.getLocalHost().getHostName() + ":" + port);
            } catch (UnknownHostException e) {
                logger.error("获取本地地址错误", e);
            }

        }

    }

    /**
     * 根据 实体类获取spring注册类容
     *
     * @param bean 需要加载的类
     * @return
     */
    public static <T> T getBean(Class<T> bean) {

        if (context == null) {
            return null;
        }
        return context.getBean(bean);
    }


    /**
     * 根据 类名获取spring注册类容
     *
     * @param beanName 需要加载的类的名称
     * @return
     */
    public static Object getBean(String beanName) {

        if (context == null) {
            return null;
        }
        return context.getBean(beanName);
    }


    /**
     * 拷贝数据参数
     *
     * @param src    原目标地址
     * @param params 需要拷贝的参数
     * @return
     */
    public static String[] copy(String[] src, String params) {
        String[] dest;
        String appendParam = params;
        if (src == null) {
            dest = new String[]{appendParam};
        } else {
            dest = new String[src.length + 1];
            System.arraycopy(src, 0, dest, 0, src.length);
            dest[src.length] = appendParam;
        }

        return dest;
    }

    private ApplicationContextUtil() {
    }


    public interface Task {

        void task();

    }

    /**
     * 添加启动完成后的任务
     *
     * @param task 启动任务
     */
    public static void addTask(Task task) {
        taskCache.add(task);
    }


}
