//package com.store.mybatisplus;
//
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;
//
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//import java.net.URI;
//
///**
// * 考虑tomcat启动加载缓慢 利用jetty 内置启动
// * Created by Ness on 2017/3/5.
// */
//public class Services {
//    private int port = 8080;
//    private String contextPath = "";
//    private final String PATH_PREFIX = "/";
//    private final int EXIT = 113;
//    private final String DEFAULT_DIRECTORY_SPLIT = "/target/classes/";
//    private final String DEFAULT_WEB_XML = "/src/main/webapp/WEB-INF/web.xml";
//    private final String SUCCESS_MSG = ">>>>>>>>>>>>  service start up OK  !!";
//    private final String RESTART_MSG = "===============>>>  now restart service  <<<=============== ";
//    private final String STOP_MSG = "service stop  SUCCESS  !!  <<<<<<<<<<<<<<<";
//
//
//    private final int WAITTING_TIME = 100;
//    private final int RESTART_TIME = 1000;
//
//    private String ACCESS_URL = "";
//    private final String DEFAULT_HTTP_PREFIX = "http://localhost:";
//
//
//    /**
//     * 启动
//     */
//    public void start() {
//
//        String parentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
//
//        if (!parentPath.endsWith(DEFAULT_DIRECTORY_SPLIT)) {
//            throw new RuntimeException(System.err.append("current directory incorrect !! \n").toString());
//        }
//
//        String path = parentPath.split(DEFAULT_DIRECTORY_SPLIT)[0];
//
//        File file = new File(path + DEFAULT_WEB_XML);
//        if (!file.exists()) {
//            throw new RuntimeException(System.err.append("directory  file  incorrect !! \n ").toString());
//        }
//
//        Server server = new Server(port);
//        WebAppContext context = new WebAppContext();
//
//        new Thread(() -> {
//            while (true) {
//
//                if (server.isStarted()) {
//
//                    //默认打开浏览器
//                    if (!ACCESS_URL.isEmpty()) {
//
//                        try {
//                            Desktop.getDesktop().browse(URI.create(ACCESS_URL));
//                        } catch (IOException e) {
//
//                            try {
//                                server.stop();
//                            } catch (Exception e1) {
//
//                            }
//                            try {
//                                Thread.sleep(WAITTING_TIME);
//                            } catch (InterruptedException ex) {
//                            }
//
//                            System.exit(-1);
//
//                            throw new RuntimeException(String.format("input  url %s incorrect \n", ACCESS_URL));
//
//                        }
//
//                    }
//
//
//                    try {
//                        Thread.sleep(WAITTING_TIME);
//                    } catch (InterruptedException e) {
//
//                    }
//
//                    //开始运行
//                    System.out.println("*****************debug模式运行过程中,控制台输入【回车键】可快速重启服务 输入【q+回车键】可快速停止服务*****************");
//                    Integer read = null;
//                    try {
//                        read = System.in.read();
//                    } catch (IOException e) {
//                        throw new RuntimeException(System.err.append("you input  incorrect \n").append(e.getMessage()).toString());
//
//                    }
//
//                    //监听用户事件
//                    if (read != null) {
//
//
//                        try {
//                            server.stop();
//                        } catch (Exception e) {
//                            throw new RuntimeException(System.err.append("service stop error\n").append(e.getMessage()).toString());
//                        }
//                        System.err.println(STOP_MSG);
//
//                        try {
//                            Thread.sleep(WAITTING_TIME);
//                        } catch (InterruptedException e) {
//
//                        }
//
//                        if (read == EXIT) {
//                            System.exit(-1);
//                            return;
//                        }
//
//                        System.out.println("*************************************************************************");
//                        System.out.println("*************************************************************************");
//                        try {
//                            Thread.sleep(WAITTING_TIME);
//                        } catch (InterruptedException e) {
//
//                        }
//                        System.err.println(RESTART_MSG);
//
//
//                        try {
//                            Thread.sleep(WAITTING_TIME);
//                        } catch (InterruptedException e) {
//
//                        }
//
//                        start();
//
//
//                    }
//                    break;
//                }
//
//
//            }
//        }).start();
//        /**
//         * 上下文
//         */
//
//        context.setContextPath(PATH_PREFIX + contextPath);
//
//
//        /**
//         * 描述的文件
//         */
//        context.setDescriptor(file.getAbsolutePath());
//
//        /**
//         * 扫描的路径
//         */
//        context.setResourceBase(parentPath);
//
//
//        server.setHandler(context);
//
//
//        try {
//            server.start();
//            System.err.println(SUCCESS_MSG);
//
//            /**
//             * 让jetty内置等待线程启动
//             */
//            server.join();
//
//
//        } catch (Exception e) {
//            try {
//                server.stop();
//            } catch (Exception e1) {
//                throw new RuntimeException(System.err.append("service stop error\n").append(e1.getMessage()).toString());
//            }
//        }
//    }
//
//    public Services() {
//
//    }
//
//    /**
//     * 设置默认访问路径
//     *
//     * @param url 默认访问路径
//     * @return
//     */
//
//    public Services setDefaultUrl(String url) {
//        if (url == null) {
//            return this;
//        }
//        StringBuilder builder = new StringBuilder(DEFAULT_HTTP_PREFIX);
//        builder.append(port);
//        builder.append(contextPath);
//        builder.append(PATH_PREFIX);
//        builder.append(url);
//        this.ACCESS_URL = builder.toString();
//        return this;
//    }
//
//    public Services(int port) {
//        this.port = port;
//    }
//
//    public Services(String contextPath) {
//        this.contextPath = contextPath;
//    }
//
//
//    public Services(int port, String contextPath) {
//        this.port = port;
//        this.contextPath = contextPath;
//    }
//
//    public static void main(String[] args) {
//        new Services().start();
//
//    }
//
//}
