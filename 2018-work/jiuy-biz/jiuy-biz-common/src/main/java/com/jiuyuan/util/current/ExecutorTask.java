package com.jiuyuan.util.current;

/**
 * 任务的名称
 */
public abstract class ExecutorTask implements Runnable {

    @Override
    public void run() {
        doJob();
    }

    public abstract void doJob();

}
