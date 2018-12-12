package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.enums.TaskBuildPathEnum;
import com.finace.miscroservice.commons.log.Log;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;


/**
 * 任务的构建路径配置
 */
@Configuration
public class TaskBuildPathConfig {


    private Log log = Log.getInstance(TaskBuildPathConfig.class);


    @Value("${zookeeper.serverList}")
    private String serverLists;

    private final String LINK = ":";
    private final int TIME_OUT = 30000;
    private final long DEFAULT_SAVE_TIME = 1000 * 60*30;


    /**
     * 检查是否能构建成功
     *
     * @param taskBuildPathEnum 需要进行构建的路径枚举
     * @return
     */
    public Boolean checkCanBuild(TaskBuildPathEnum taskBuildPathEnum) {

        Boolean buildSuccessFlag = Boolean.FALSE;

        if (taskBuildPathEnum == null) {
            log.warn("用户所传路径为空");
            return buildSuccessFlag;
        }
        String path = taskBuildPathEnum.getPath();


        ZooKeeper zooKeeper = null;
        try {
            //建立连接
            zooKeeper = new ZooKeeper(serverLists, TIME_OUT, event -> {
            });
            //判断是否存在
            Stat exists = zooKeeper.exists(path, false);
            String localNetName = InetAddress.getLocalHost().toString();

            if (exists != null) {
                //获取参数
                byte[] data = zooKeeper.getData(path, false, null);

                String result = new String(data);


                if (!result.contains(LINK)) {
                    log.warn("路径={} 所设置值={} 不符合规范", path, result);
                    return buildSuccessFlag;
                } else {
                    //格式 时间戳:当前本机网络的名称
                    //先判断是否是否没过期
                    if (System.currentTimeMillis() - Long.parseLong(result.split(LINK)[0]) < DEFAULT_SAVE_TIME) {
                        //判断是否是本机 如果不是 则终止
                        if (!result.split(LINK)[1].equals(localNetName)) {
                            log.warn("在时间={} 限定内已有其他机器构建路径={} 无须再次构建", DEFAULT_SAVE_TIME, path);
                            return buildSuccessFlag;
                        }
                    }

                    //删除之前构建的
                    zooKeeper.delete(path, -1);

                }


            }

            String res = zooKeeper.create(path, (System.currentTimeMillis() + LINK + localNetName).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            if (res != null) {
                buildSuccessFlag = Boolean.TRUE;
                log.info("路径={},已经构建成功", path);
            }

        } catch (Exception e) {
            log.error("构建路径={} 出错", path, e);
        } finally {
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    log.error("zk关闭资源失败", e);
                }

            }
        }

        return buildSuccessFlag;
    }


}
