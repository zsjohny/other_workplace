package com.goldplusgold.td.sltp.monitor.operate.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Ness on 2017/5/12.
 */
public class ManagerChannelBus {


    public Map<String, BlockingQueue> _channels = new HashMap<>();


    public Map<String, LinkedList<Subscriber>> _subscribers = new HashMap<>();


    public ExecutorService _executors = Executors.newFixedThreadPool(10);
    public ExecutorService _eventExecutor = Executors.newFixedThreadPool(5);

    public Publisher regPublisher(String channel) {

        BlockingQueue _channel = _channels.get(channel);
        if (_channel == null) {
            _channel = new LinkedBlockingDeque(10);
            _channels.putIfAbsent(channel, _channel);
            BlockingQueue _consumer = _channel;

            _executors.execute(() -> {
                while (true) {

                    LinkedList<Subscriber> subscribers = _subscribers.get(channel);
                    if (subscribers == null) {
                        continue;
                    }

                    Object msg = null;
                    try {
                        msg = _consumer.take();
                    } catch (InterruptedException e) {

                    }


                    if (msg == null) {
                        continue;
                    }

                    for (Iterator<Subscriber> iterator = subscribers.iterator(); iterator.hasNext(); ) {

                        Subscriber next = iterator.next();
                        Object finalMsg = msg;
                        _eventExecutor.execute(()->next.subscribe((String) finalMsg));

                    }

                }
            });
        }


        return new InnerPublisher(_channel);


    }


    private boolean checkExist(String channel) {
        return _channels.containsKey(channel);
    }


    public void regSubscriber(String channel, Subscriber... subscriberArr) {
        if (!checkExist(channel)) {
            throw new RuntimeException("please register publisher");
        }

        LinkedList<Subscriber> subscribers = _subscribers.get(channel);


        if (subscribers == null) {
            subscribers = new LinkedList<>();
        }
        for (Subscriber sub : subscriberArr) {
            subscribers.add(sub);
        }
        _subscribers.put(channel, subscribers);


    }


    private class InnerPublisher implements Publisher {
        private BlockingQueue _channel;

        public InnerPublisher(BlockingQueue _channel) {
            this._channel = _channel;
        }

        @Override
        public void publish(String msg) {
            try {
                _channel.offer(msg,1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {

            }
        }
    }


    public static void main(String[] args) {
        ManagerChannelBus channelBus = new ManagerChannelBus();

        new Thread(() -> {
            Publisher publisher = channelBus.regPublisher("111");
            for (int i = 0; i < 1000; i++) {

                publisher.publish(Thread.currentThread().getName() + "____________" + i);
            }

        }).start();
//        new Thread(() -> {
//            Publisher publisher1 = channelBus.regPublisher("111");
//            for (int i = 0; i < 1000; i++) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//
//                }
//                publisher1.publish(Thread.currentThread().getName() +"~~~~~~~~~~~~~"+ i);
//            }
//
//        }).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }
        channelBus.regSubscriber("111", (x) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                    System.out.println("1111111111"+x);
                }
                , (x) -> {

                    System.out.println("2222222222"+x);
                }
        );


    }


}
