package com.jiuyuan.util.current;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 多线程执行任务
 */
public class ExecutorService {
    private Logger log = LoggerFactory.getLogger(ExecutorService.class);
    private ThreadPoolExecutor poolExecutor;
    private int LIVE_TILE = 200;
    private String THREAD_NAME = "finace";
    private final int MAX_QUEUE_COUNT = 1000;


    /**
     * 初始化pool线程
     *
     * @param threadCount 线程数量
     * @param threadName  线程的名称
     */
    public ExecutorService(int threadCount, String threadName) {
        init(threadCount, threadName);
    }


    /**
     * 初始化pool线程
     *
     * @param threadCount 线程数量
     */
    public ExecutorService(int threadCount) {
        init(threadCount, "");
    }

    /**
     * 初始化
     *
     * @param threadCount
     * @param threadName
     */
    private void init(int threadCount, String threadName) {

        if (StringUtils.isNotEmpty(threadName)) {
            THREAD_NAME = threadName;
        }
        int cpuCount = Runtime.getRuntime().availableProcessors();
        int maxCount = cpuCount >> 1;
        if (threadCount > maxCount) {
            threadCount = maxCount;
        } else if (threadCount <= 0) {
            threadCount = 1;

        }
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(MAX_QUEUE_COUNT);

        poolExecutor = new ThreadPoolExecutor(threadCount, threadCount, LIVE_TILE, TimeUnit.MILLISECONDS, queue, new ExecutorThreadFactory(), new DealWithTask());

    }


    /**
     * 添加执行任务
     *
     * @param task
     */
    public void addTask(ExecutorTask task) {
        poolExecutor.execute(task);


    }


    private class DealWithTask implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                r.run();

            }


        }
    }


    private class ExecutorThreadFactory implements ThreadFactory {

        private final ThreadGroup group;

        private final AtomicLong threadCount = new AtomicLong(0);
        private final StringBuilder builder = new StringBuilder(THREAD_NAME);

        {
            SecurityManager manager = System.getSecurityManager();
            group = (manager == null ? Thread.currentThread().getThreadGroup() : manager.getThreadGroup());
        }


        @Override
        public Thread newThread(Runnable r) {
            builder.substring(0, THREAD_NAME.length());
            Thread thread = new Thread(group, r, builder.append(threadCount.incrementAndGet()).toString());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            thread.setPriority(Thread.NORM_PRIORITY);

            return thread;
        }
    }


    public void shutdown() {
        if (!poolExecutor.isShutdown()) {
            poolExecutor.shutdown();
            log.info("关闭线程组success");
        }
    }


}
