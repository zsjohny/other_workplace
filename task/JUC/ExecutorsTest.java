package org.dream.quota.processor;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by nessary on 16-8-11.
 */
public class ExecutorsTest {

    private void calCable() {


        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("into......");
                Integer i = 0;

                i = i + 100;


                return i;
            }
        };

        FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);


        new Thread(futureTask).start();
        System.out.println("start......");

        try {
            Integer integer = futureTask.get();

            System.out.println(integer + "....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        System.out.println("end......");
    }


    private static void calThreadByExecutors() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<Integer> submit = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Integer integer = 0;


                integer += integer + 100;
                System.out.println("into...");
                return integer;
            }

        });

        System.out.println("star....");

        try {
            Integer integer = submit.get();
            System.out.println("caluResult1111..." + integer);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        System.out.println("end...");

    }


    private static void calExecutorsByFutureTask() {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("into.....");
                return 100 + 10;
            }
        });

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(futureTask);

        executorService.shutdownNow();

        System.out.println("start...");

        try {
            Integer integer = futureTask.get();
            System.out.println("caluResult1111111111111..." + integer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        System.out.println("end....");


    }


    private static void calReadExecutorByLock() {


        final User user = new User();

        user.setAge(23);
        user.setMoney(1000.0);

        final Lock lock = new ReentrantLock();

        Runnable runnable = new Runnable() {

            @Override
            public void run() {


                try {
                    lock.lock();
                    System.out.println("into  thread:" + Thread.currentThread().getId() + "....");

                    double total = user.getMoney();

                    TimeUnit.SECONDS.sleep(2);
                    double v = total - 100;

                    TimeUnit.SECONDS.sleep(2);

                    user.setMoney(v);
                    System.out.println("thread:" + Thread.currentThread().getId() + ",money:" + user.getMoney() + "remain..." + v);


                } catch (Exception e) {
                    System.out.println("erro..." + Thread.currentThread().getId() + e.getMessage());


                } finally {
                    try {
                        lock.unlock();

                    } catch (Exception e) {
                        System.out.println("e");
                    }
                }

            }
        };


        Thread thread1 = new Thread(runnable);

        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();

        thread2.interrupt();

    }


    private static void calReadExecutorsByReadWriter() {

        final User user = new User();

        user.setAge(23);
        user.setMoney(1000.0);

        final ReadWriteLock lock = new ReentrantReadWriteLock();


        Runnable runnableWrite = new Runnable() {

            @Override
            public void run() {


                try {
                    lock.writeLock().lock();
                    System.out.println("into  thread write:" + Thread.currentThread().getId() + "....");

                    double total = user.getMoney();
                    double v = total - 100;

//                    TimeUnit.SECONDS.sleep(2);

                    user.setMoney(v);
                    System.out.println("thread write: " + Thread.currentThread().getId() + ",money:" + user.getMoney() + "remain..." + v);


                } catch (Exception e) {
                    System.out.println("error write..." + Thread.currentThread().getId() + e.getMessage());


                } finally {
                    try {
                        lock.writeLock().unlock();

                    } catch (Exception e) {
                        System.out.println("e write");
                    }
                }

            }
        };

        Runnable runnableRead = new Runnable() {


            @Override
            public void run() {


                try {
                    lock.readLock().lock();
                    System.out.println("into   thread  read:" + Thread.currentThread().getId() + "....");

                    double total = user.getMoney();

                    double v = total - 100;

                    TimeUnit.SECONDS.sleep(2);

                    user.setMoney(v);
                    System.out.println("thread  read:" + Thread.currentThread().getId() + ",money:" + user.getMoney() + "remain..." + v);


                } catch (Exception e) {
                    System.out.println("error read..." + Thread.currentThread().getId() + e.getMessage());


                } finally {
                    try {
                        lock.readLock().unlock();


                    } catch (Exception e) {
                        System.out.println("e read");
                    }
                }

            }


        };

        Thread threadRead1 = new Thread(runnableRead);
        Thread threadRead2 = new Thread(runnableRead);

        Thread threadWrite1 = new Thread(runnableWrite);
        Thread threadWrite2 = new Thread(runnableWrite);

        threadRead1.start();
        threadRead2.start();

        threadWrite1.start();
        threadWrite2.start();


    }


    private static void testEqualThread() {


        Thread i1 = new Thread(new RunIt3());
        Thread i2 = new Thread(new RunIt3());
        i1.start();
        i2.start();
        i2.interrupt();


    }

    static class RunIt3 implements Runnable {

        private Lock lock = new ReentrantLock();

        public void run() {
            try {
                //---------------------------------a
                lock.tryLock();
//                lock.lockInterruptibly();


                System.out.println(Thread.currentThread().getName() + " running");
                TimeUnit.SECONDS.sleep(20);
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " finished");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrupted");

            }

        }
    }


    private static void distingRunAndStart() {

       /* Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("111");
            }
        };

        runnable.run();

        System.out.println("into...");


        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("2222");
            }
        };

        runnable2.run();*/


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("111");
            }
        };


        System.out.println("into...");


        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("2222");
            }
        };

        Thread thread = new Thread(runnable);

        Thread thread1 = new Thread(runnable2);

        thread.start();
        thread1.start();


    }

    public static void main(String[] args) {
        distingRunAndStart();


    }


    static class User {

        private String name;

        private Integer age;

        private Double money;

        public Double getMoney() {
            return money;
        }

        public void setMoney(Double money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
