package com.finace.miscroservice.distribute_task.zookeeper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.distribute_task.timerTask.TimeTask;
import com.finace.miscroservice.distribute_task.timerTask.TimeTaskBus;
import com.finace.miscroservice.distribute_task.util.LogUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

import static com.finace.miscroservice.distribute_task.zookeeper.ZKManager.IP_LINK;
import static com.finace.miscroservice.distribute_task.zookeeper.ZKManager.localIp;


/**
 * zk数据的管理类
 */
public class ZKDataManager {
    private LogUtil log;

    private final Map<String, Long> heartAddressCache = new ConcurrentHashMap<>();
    private final long DEFAULT_INVALID_TIME = 1000 * 40L;
    private final int FILE_PORT = 2080;

    private final String END_OFF = "end";

    public static final String FILENAME_END = ".cvs";

    public static final String SAVE_FILE_PREFIX = System.getProperty("java.io.tmpdir") + File.separator + "zkManager";

    private TimeTaskBus timeTaskBus;

    public ZKDataManager(TimeTaskBus timeTaskBus, LogUtil logUtil) {

        this.log = logUtil;
        try {
            log = logUtil.getClass().newInstance();
            log.setInstance(ZKDataManager.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取当前存活的服务器
     *
     * @return
     */
    public List<String> get() {
        return new ArrayList<>(heartAddressCache.keySet());
    }

    /**
     * 检测失效
     */
    public void checkValid() {

        //检测失效的集合
        long currentTime = System.currentTimeMillis();
        Map<String, String> invalidClient = new HashMap<>();
        for (Map.Entry<String, Long> entry : heartAddressCache.entrySet()) {
            if (currentTime - entry.getValue() > DEFAULT_INVALID_TIME) {
                log.info("节点={} 已经失效 ", entry.getKey());
                heartAddressCache.remove(entry.getKey());
                invalidClient.put((entry.getKey() + FILENAME_END).intern(), (entry.getKey() + FILENAME_END).intern());
            } else {
            }
        }
        if (invalidClient.size() > 0) {
            log.info("开始分配失效的节点任务");
            reBuildTask(invalidClient);

        }

        //检测服务器失效
        if (currentTime - SERVER_CREATE_TIME > DEFAULT_INVALID_TIME) {
            if (serverSocket != null && !serverSocket.isClosed()) {
                log.info("文件服务端已经达到失效时间 关闭文件服务器");
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    log.error("关闭文件服务器出错", e);
                }
            }
        }

    }


    {
        File file = new File(SAVE_FILE_PREFIX);
        if (file.exists()) {
            deleteDirect(file);
        } else {
            file.mkdir();
        }

    }

    /**
     * 递归删除空目录
     *
     * @param file 文件夹
     */
    private void deleteDirect(File file) {
        File[] childFile = file.listFiles();

        //分级删除
        for (File tmpFile : childFile) {

            if (tmpFile.isDirectory()) {
                deleteDirect(tmpFile);
            } else {
                tmpFile.delete();
            }
        }
    }


    /**
     * 存入Ip
     *
     * @param saveIp 待存入的Ip
     */
    public void put(String saveIp) {
        heartAddressCache.put(saveIp, System.currentTimeMillis());
    }

    /**
     * 转移数据
     *
     * @param ip   转移的Ip
     * @param data 转移的数据
     */
    public void transferData(String ip, String data) {

        if (ip == null || ip.isEmpty() || data == null || data.isEmpty()) {
            log.warn("所传参数 ip={} data={}  不正确", ip, data);
            return;
        }

        File file = new File((SAVE_FILE_PREFIX + File.separator + ip + FILENAME_END).intern());

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)))) {

            bufferedWriter.write(data);
            bufferedWriter.newLine();
            log.info("文件名={}{}写入数据={} 成功", ip, FILENAME_END, data);
        } catch (Exception e) {
            log.error("写入文件={} 出错", file.getName(), e);
        }


    }

    private volatile ServerSocket serverSocket;

    private long SERVER_CREATE_TIME;


    /**
     * 转移文件
     *
     * @param address           待连接的服务端地址
     * @param fileName          待连接的文件名称
     * @param zkDataOperateEnum zk数据操作类型
     */
    public void transferFile(String address, String fileName, ZKDataOperateEnum zkDataOperateEnum) {
        if (address == null || address.isEmpty() || zkDataOperateEnum == null) {
            log.warn("所传的转移文件服务端地址={} 操作类型={} 为空", address, zkDataOperateEnum);
            return;
        }

        switch (zkDataOperateEnum) {
            case CREATE_SERVER:
                if (address.split(IP_LINK)[0].equals(localIp.split(IP_LINK)[0])) {
                    //建立服务端等待被传递文件
                    startServer(fileName);


                }
                break;
            case ACCEPT_DATA:
                if (!address.split(IP_LINK)[0].equals(localIp.split(IP_LINK)[0])) {
                    startClient(address);
                }
                break;
            default:
                log.error("暂不支持此类型");
        }

    }


    /**
     * 启动客户端
     *
     * @param serverAddress 服务端地址
     */
    public void startClient(String serverAddress) {

        if (serverAddress == null || serverAddress.isEmpty()) {
            log.warn("所传服务端地址为空");
            return;
        }

        try {

            Socket socket = new Socket(serverAddress, FILE_PORT);


            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {


                String line = "";
                String saveFileName = "";
                StringBuilder builder = new StringBuilder();
                File file;

                String readText;

                while ((line = reader.readLine()) != null) {
                    readText = line.trim();
                    if (readText.isEmpty()) {
                        continue;
                    }
                    if (readText.endsWith(FILENAME_END)) {
                        saveFileName = readText;
                    } else if (readText.equals(END_OFF)) {
                        file = new File(SAVE_FILE_PREFIX + File.separator + saveFileName);
                        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)))) {
                            bufferedWriter.write(builder.toString());
                            log.info("文件={}写入成功", saveFileName);
                            builder.delete(0, builder.length());
                        }
                    } else {
                        builder.append(readText);
                        builder.append("\n");
                    }
                }


            }

            //关闭资源
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            log.error("客户端获取服务端={} 文件数据出错", serverAddress, e);
        }

    }

    /**
     * 启动文件服务器
     *
     * @param fileName 转移的文件名称(可选)
     */
    public void startServer(String fileName) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            log.warn("已经存在文件服务器 无须创建");
            return;
        }
        //转义空的文件名称
        fileName = fileName == null ? "" : fileName.trim();
        String fiName = fileName;

        Thread thread = new Thread(() -> {
            try {

                serverSocket = new ServerSocket();

                serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), FILE_PORT));
                SERVER_CREATE_TIME = System.currentTimeMillis();
                log.info("文件服务器建立完成");
                Socket accept = serverSocket.accept();


                File file = new File(SAVE_FILE_PREFIX);

                if (!file.exists() || file.isFile()) {
                    log.warn("文件夹={}不存在", file.getName());
                    return;
                }


                for (File fi : file.listFiles()) {

                    if (fi.isDirectory() || !fi.getName().endsWith(FILENAME_END) || (!(fiName.isEmpty() || fiName.equals(fi.getName())))) {
                        continue;
                    }

                    //先写文件名
                    accept.getOutputStream().write((fi.getName() + "\n").getBytes());

                    //在写文件
                    Files.copy(fi.toPath(), accept.getOutputStream());

                    //最后写结束
                    accept.getOutputStream().write(("\n" + END_OFF + "\n").intern().getBytes());

                    log.info("文件={}传输完成", fi.getName());

                }
                accept.shutdownOutput();
                log.info("所有文件传输完毕");

                //关闭资源
                accept.close();

                LockSupport.parkNanos(1000 * 1000 * 20);

                serverSocket.close();

            } catch (IOException e) {
                log.error("文件服务器操作失败", e);
            }

        });
        thread.setName("fileServerName");
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setDaemon(Boolean.TRUE);
        thread.start();

    }

    /**
     * 重新构建工作
     *
     * @param invalidClient 心跳的服务集合
     */
    private void reBuildTask(Map<String, String> invalidClient) {

        try {
            File file = new File(SAVE_FILE_PREFIX);

            if (!file.exists() || file.isFile()) {
                log.warn("文件夹={}不存在", file.getName());
                return;
            }


            //获取存活的服务有序集合
            Set<String> aliveLists = new TreeSet<>(heartAddressCache.keySet());

            //添加自己
            aliveLists.add(localIp);
            //获取自己
            int index = 0;
            for (String address : aliveLists) {
                index++;
                if (localIp.equals(address)) {
                    break;
                }
            }


            List<String> lines;

            StringBuilder builder = new StringBuilder();

            for (File fi : file.listFiles()) {
                if (fi.isDirectory() || !fi.getName().endsWith(FILENAME_END) || invalidClient.get(fi.getName()) == null) {
                    continue;
                }


                lines = Files.readAllLines(fi.toPath(), Charset.forName("utf-8"));

                int average = lines.size() / aliveLists.size();
                //获取自己的内容
                int start = average * (index - 1);

                int end = index == aliveLists.size() ? lines.size() : average * index;

                for (int i = start; i < end; i++) {
                    builder.append(lines.get(i));
                    builder.append("\n");

                    timeTaskBus.execute(jsonArray2TimeTaskArray(JSONObject.parseArray(lines.get(i))));

                }

                try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((SAVE_FILE_PREFIX + File.separator + localIp + FILENAME_END).intern(), true)))) {
                    bufferedWriter.write(builder.toString());
                }

                log.info("文件={}的任务恢复完成", fi.getName());
                builder.delete(0, builder.length());

            }
        } catch (
                Exception e)

        {
            log.error("创建工作出错", e);
        }
    }


    /**
     * jsonArray转TimeTask数组
     *
     * @param jsonArray
     * @return
     */
    private TimeTask[] jsonArray2TimeTaskArray(JSONArray jsonArray) {
        int len = jsonArray == null ? 0 : jsonArray.size();
        TimeTask[] timeTasks = new TimeTask[len];
        for (int i = 0; i < len; i++) {
            timeTasks[i] = jsonArray.getObject(i, TimeTask.class);
        }
        return timeTasks;
    }


}
