package com.goldplusgold.td.market.common;

import java.util.concurrent.locks.StampedLock;

/**
 * Created by Ness on 2017/4/23.
 */
public class ReadOrWriteDemo {

    //需要修改的变量
    private String name = null;


    //此时的static是根据业务场景来的 倘若这个类是公类那么就 是static
    private StampedLock readLock = new StampedLock();

    private StampedLock wirteLock = new StampedLock();


    public String read() {

        //获取乐观读锁
        long stamp = readLock.tryOptimisticRead();

        String temp = name;
        //验证是否此刻有其他写锁
        if (!readLock.validate(stamp)) {

            //如果有其他写锁  则进行强制性获取读锁
            stamp = readLock.readLock();

            //操纵
            name = temp;
            //释放读锁
            readLock.unlockRead(stamp);


        }


        return name;


    }


    public void write(String name) {

        //获取读锁  判断是否 此刻能获取到
        long stamp = wirteLock.readLock();

        try {


            while (this.name == null) {
                //获取悲观锁
                long conver = wirteLock.tryConvertToWriteLock(stamp);
                //如果获取不成功结果为0
                if (conver != 0L) {

                    this.name = name;
                    //将锁置换 用于释放
                    stamp = conver;
                    System.out.println("写入成功...");

                } else {

                    //因为是读锁 循环进行转换
                    wirteLock.unlockRead(stamp);

                    //获取强制性写锁
                    wirteLock.writeLock();

                }

            }


        } finally {

            //释放锁---不一定是读锁
            wirteLock.unlock(stamp);
        }


    }

    public static void main(String[] args) {
        ReadOrWriteDemo demo = new ReadOrWriteDemo();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> demo.write("tom")

            ).start();

        }
        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                System.out.println("read..." + demo.read());
            }).start();
        }
    }

}
