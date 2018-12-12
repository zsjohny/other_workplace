package com.finace.miscroservice.distribute_task.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.distribute_task.timerTask.TimeTask;
import com.finace.miscroservice.distribute_task.timerTask.TimeTaskBus;
import com.finace.miscroservice.distribute_task.util.IpUtil;
import com.finace.miscroservice.distribute_task.util.LogUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

import static com.finace.miscroservice.distribute_task.zookeeper.ZKDataManager.FILENAME_END;
import static com.finace.miscroservice.distribute_task.zookeeper.ZKDataManager.SAVE_FILE_PREFIX;


/**
 * zookeeper的管理类
 */
public class ZKManager {

    private ZooKeeper zooKeeper;

    private LogUtil log;


    private TimeTaskBus timeTaskBus;

    private ZKDataManager zkDataManager;

    private final int TIMEOUT = 30000;
    private final int MAX_CONNECT_COUNT = 3;
    private String serverList;

    private final String BUILD_HEARTBEAT_PATH = "/vote.necessary.heartbeat.timerScheduling";
    private final String BUILD_TRANSFER_DATA_PATH = "/vote.necessary.transferData.timerScheduling";
    private final String BUILD_VOTE_MASTER_PATH = "/vote.necessary.voteMaster.timerScheduling";

    public static String localIp;
    //毫秒
    private final long DELAY_TIME = 1000 * 60 * 3L;
    //毫秒
    private final long INTERVAL_TIME = 1000 * 10L;
    //豪微秒
    private final long WAIT_RESTART_TIME = 1000 * 1000 * 20L;
    public static final String IP_LINK = "_";
    public final String MASTER_LINK = "|";
    private final String DATA_LINK = "~";

    private final int DEFAULT_SPILT_LEN = 20;


    private TimerTask timerTask;

    {
        String local;
        try {
            local = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        localIp = local + IP_LINK + pid;

        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (zooKeeper == null) {
                    log.info("检测没有zk 定时任务取消");
                    timerTask.cancel();
                    return;
                }
                try {
                    zooKeeper.setData(BUILD_HEARTBEAT_PATH, localIp.getBytes("utf-8"), -1);
                    LockSupport.parkNanos(1000 * 200);
                    zkDataManager.checkValid();
                } catch (Exception e) {
                    log.error("客户端={} 发送心跳消息失败", localIp, e);
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, DELAY_TIME, INTERVAL_TIME);

    }

    public ZKManager(String serverList, TimeTaskBus timeTaskBus, LogUtil logUtil) {
        this.serverList = serverList;
        this.timeTaskBus = timeTaskBus;
        try {
            log = logUtil.getClass().newInstance();
            log.setInstance(ZKManager.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        zkDataManager = new ZKDataManager(timeTaskBus, logUtil);
        connect();
    }


    public ZKManager(String serverList, TimeTaskBus timeTaskBus, LogUtil logUtil, IpUtil ipUtil) {
        localIp = ipUtil.getInternetIp() + IP_LINK + localIp.split(IP_LINK)[1];
        this.serverList = serverList;
        this.timeTaskBus = timeTaskBus;
        try {
            log = logUtil.getClass().newInstance();
            log.setInstance(ZKManager.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        zkDataManager = new ZKDataManager(timeTaskBus, logUtil);
        connect();
    }


    /**
     * 监听心跳变化
     */
    private void monitorHeartBeat() {
        if (zooKeeper == null) {
            return;
        }
        try {

            byte[] heartBeat = zooKeeper.getData(BUILD_HEARTBEAT_PATH, event ->
                    {
                        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                            monitorHeartBeat();
                        } else {
                            log.warn("路径={} 监听出现异常 当前路径状态={}", BUILD_HEARTBEAT_PATH, event.getState());
                        }
                    }

                    , null);

            if (heartBeat == null) {

                return;
            }

            String saveIp = new String(heartBeat);
            if (saveIp.isEmpty()) {
                return;
            }
            if (!localIp.equals(saveIp)) {
                log.info("接受心跳地址={}", saveIp);
                zkDataManager.put(saveIp);
            }


        } catch (Exception e) {
            log.error("监听心跳出错", e);
        }
    }


    /**
     * 监听选举变化
     */
    private void monitorVoteMaster() {
        if (zooKeeper == null) {
            return;
        }
        try {

            byte[] address = zooKeeper.getData(BUILD_VOTE_MASTER_PATH, event ->
                    {
                        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                            monitorVoteMaster();
                        } else {
                            log.warn("路径={} 监听出现异常 当前路径状态={}", BUILD_VOTE_MASTER_PATH, event.getState());
                        }
                    }

                    , null);

            if (address == null) {

                return;
            }

            String addressStr = new String(address);
            if (addressStr.isEmpty()) {
                return;
            }

            //格式  地址+"|"+操作类型+"|"+文件名称(可选)
            String[] split = addressStr.split("\\" + MASTER_LINK);
            log.info("接受选举地址={} 操作类型={}", split[0], split[1]);
            zkDataManager.transferFile(split[0], split.length > 2 ? split[2] : "", ZKDataOperateEnum.getOperateByName(split[1]));

        } catch (Exception e) {
            log.error("监听选举出错", e);
        }
    }

    /**
     * 监听传送数据
     */
    private void monitorTransferData() {
        if (zooKeeper == null) {
            return;
        }
        try {

            byte[] data = zooKeeper.getData(BUILD_TRANSFER_DATA_PATH, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    monitorTransferData();
                } else {
                    log.warn("路径={} 监听出现异常 当前路径状态={}", BUILD_TRANSFER_DATA_PATH, event.getState());
                }
            }, null);

            if (data == null) {

                return;
            }
            String acceptData = new String(data, "utf-8");

            if (acceptData.isEmpty()) {
                return;
            }

            if (!acceptData.contains(DATA_LINK)) {
                log.warn("接受传送数据={}格式不正确", acceptData);
                return;
            }
            //形式 IP+":"+data
            String[] split = acceptData.split(DATA_LINK);
            log.info("接受Ip={}的数据={}", split[0], split[1]);
            zkDataManager.transferData(split[0], split[1]);

        } catch (Exception e) {
            log.error("监听传送数据出错", e);
        }
    }

    /**
     * 初始化有一些参数
     */
    private void init() {
        if (zooKeeper == null) {
            return;
        }

        try {
            //初始构建心跳路径
            Stat exists = zooKeeper.exists(BUILD_HEARTBEAT_PATH, null);

            if (exists == null) {
                zooKeeper.create(BUILD_HEARTBEAT_PATH, localIp.getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("初始化心跳构建路径={} 成功", BUILD_HEARTBEAT_PATH);
            } else {
                log.info("路径={} 已经存在无需再次构建", BUILD_HEARTBEAT_PATH);
            }


            //初始传送数据路径
            exists = zooKeeper.exists(BUILD_TRANSFER_DATA_PATH, null);

            if (exists == null) {
                zooKeeper.create(BUILD_TRANSFER_DATA_PATH, "".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("初始化构建传送路径={} 成功", BUILD_TRANSFER_DATA_PATH);
            } else {
                log.info("路径={} 已经存在无需再次构建", BUILD_TRANSFER_DATA_PATH);
            }

            //初始选举路径
            exists = zooKeeper.exists(BUILD_VOTE_MASTER_PATH, null);

            if (exists == null) {
                zooKeeper.create(BUILD_VOTE_MASTER_PATH, "".getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("初始化构建传送路径={} 成功", BUILD_VOTE_MASTER_PATH);
            } else {
                log.info("路径={} 已经存在无需再次构建", BUILD_VOTE_MASTER_PATH);
            }


            new Thread(() -> {
                LockSupport.parkNanos(WAIT_RESTART_TIME);

                //监听
                monitorHeartBeat();

                monitorTransferData();

                monitorVoteMaster();
                //恢复数据
                restartData();
            }).start();

        } catch (Exception e) {
            log.error("初始化构建参数错误", e);
        }

    }


    /**
     * 重连
     */
    private void reConnect() {

        if (zooKeeper != null) {
            try {
                zooKeeper.close();

                if (connect()) {
                    log.info("zk重连成功");
                }
            } catch (InterruptedException e) {
                log.error("zk关闭出错", e);
            }
        }


    }


    /**
     * 连接
     */
    public Boolean connect() {

        Boolean connectFlag = Boolean.FALSE;

        try {
            AtomicInteger connectCount = new AtomicInteger();
            zooKeeper = new ZooKeeper(serverList, TIMEOUT, event -> {
                if (event == null) {
                    log.warn("zk连接事件为空");
                    return;
                }

                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    log.info("zk连接成功");

                } else if (event.getState() == Watcher.Event.KeeperState.Expired) {

                    if (connectCount.getAndDecrement() >= MAX_CONNECT_COUNT) {
                        log.warn("zk达到最大连接次数={} 自动放弃", MAX_CONNECT_COUNT);
                        System.exit(-1);
                        return;
                    }
                    reConnect();

                }
            });
            connectFlag = Boolean.TRUE;
            registerDestroy();
            init();
        } catch (IOException e) {
            log.error("建立连接出错", e);
            zooKeeper = null;
        }


        return connectFlag;
    }


    /**
     * 传递数据
     *
     * @param data 需要传递的数据
     */
    public void transfer(String data) {
        if (data == null || data.isEmpty()) {
            log.warn("传递所传数据为空");
            return;
        }

        if (zooKeeper == null) {
            return;
        }

        try {
            zooKeeper.setData(BUILD_TRANSFER_DATA_PATH, (localIp + DATA_LINK + data).getBytes("utf-8"), -1);
        } catch (Exception e) {
            log.error("传递数据={} 出现错误", data, e);
        }

    }

    /**
     * 选取主服务端
     *
     * @param serviceAddress    服务端地址
     * @param zkDataOperateEnum zk数据操作类型
     */
    private void voteMaster(String serviceAddress, ZKDataOperateEnum zkDataOperateEnum) {

        try {
            if (zooKeeper == null) {
                return;
            }
            zooKeeper.setData(BUILD_VOTE_MASTER_PATH, (serviceAddress + MASTER_LINK + zkDataOperateEnum.getOperate()).intern().getBytes("utf-8"), -1);
        } catch (Exception e) {
            log.error("选举服务端数据={} 出现错误", serviceAddress, e);
        }
    }


    /**
     * 注册销毁
     */
    private void registerDestroy() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                    log.info("zk关闭成功");
                } catch (InterruptedException e) {
                    log.error("zk关闭失败", e);
                }


            }
        }));

    }

    /**
     * 重置本地定时服务
     *
     * @param timeTasks
     */
    public void resetAllTask(List<TimeTask> timeTasks) {

        try {
            if (timeTasks == null || timeTasks.isEmpty() || timeTaskBus == null) {
                log.warn("重启任务所传参数为空");
                return;
            }
            //本地添加定时任务
            timeTaskBus.init(timeTasks);


            //写入本地数据库
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((SAVE_FILE_PREFIX + File.separator + (localIp + FILENAME_END).intern()).intern(), true)))) {

                List<TimeTask> tmpList = new ArrayList<>(DEFAULT_SPILT_LEN);

                for (TimeTask timeTask : timeTasks) {
                    if (tmpList.size() == DEFAULT_SPILT_LEN) {
                        //分段写入
                        bufferedWriter.write(JSONObject.toJSONString(tmpList));
                        bufferedWriter.write("\n");
                        tmpList.clear();
                    } else {
                        tmpList.add(timeTask);
                    }

                }

                //最后写入剩下部分
                bufferedWriter.write(JSONObject.toJSONString(tmpList));

            }

            //启动文件服务
            zkDataManager.startServer((localIp + FILENAME_END).intern());

            //同步数据到其他服务
            log.info("发送同步初始化数据");
            voteMaster(localIp.split(IP_LINK)[0], ZKDataOperateEnum.ACCEPT_DATA);
        } catch (Exception e) {
            log.error("启动所有定时任务出错", e);
        }

    }


    private void restartData() {
        LockSupport.parkNanos(WAIT_RESTART_TIME);
        List<String> aliveClient = zkDataManager.get();
        if (aliveClient.isEmpty()) {
            log.warn("当前无存活服务");
            return;
        }


        String ip = localIp.split(IP_LINK)[0];
        aliveClient = aliveClient.stream().filter(client -> !client.split(IP_LINK)[0].equals(ip)).collect(Collectors.toList());
        //选举服务端Ip
        String serverAddress = aliveClient.size() > 0 ? aliveClient.get((int) (Math.random() * aliveClient.size())) : "";

        if (serverAddress == null || serverAddress.isEmpty()) {
            log.info("当前没有可用服务端");
            return;
        } else {
            //发送选举服务端的通知
            log.info("发送选举服务端={}通知", serverAddress);
        }


        voteMaster(serverAddress, ZKDataOperateEnum.CREATE_SERVER);

        //这里再次排除自己
        if (!serverAddress.equals(localIp)) {
            LockSupport.parkNanos(WAIT_RESTART_TIME);
            //接受数据
            zkDataManager.startClient(serverAddress);

        }


    }


}



