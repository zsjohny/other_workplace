package com.goldplusgold.mq.pubsub.simple_impl;

import com.goldplusgold.mq.pubsub.MQPublisher;
import com.goldplusgold.mq.pubsub.MQSubscriber;
import com.goldplusgold.mq.pubsub.MsgChannelBus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 消息订阅服务简单实现, 供应用内使用
 * 最多支持10个消息频道, 超出后注册新频道发布者/订阅者, 返回失败
 * 单个频道最多缓存10000条消息, 超出后在该频道发布消息返回失败
 * 单个频道最多支持100个订阅者, 超出后在该频道订阅消息返回失败
 * Created by Administrator on 2017/5/10.
 */
@Repository("msgChannelBusSimpleImpl")
public class MsgChannelBusSimpleImpl implements MsgChannelBus {
    private final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final int CHANNELS_MAX_SIZE = 10;
    private final int CHANNEL_QUEUE_MAX_SIZE = 10000;
    private final int PER_CHANNEL_SUBSCRIBERS_MAX_SIZE = 100;

    private final Map<String, BlockingQueue<Object>> channels = new HashMap<>();
    private final Map<String, MQPublisher> publishers = new HashMap<>();
    private final Map<String, List<MQSubscriber>> subscribers = new HashMap<>();

    private final ExecutorService listenerLoops = Executors.newCachedThreadPool();
    private final ExecutorService eventExecutors = new ThreadPoolExecutor(
            PROCESSORS, PROCESSORS,0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(PROCESSORS<<1), new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public boolean init(String[] args) {
        throw new RuntimeException("Nonsupport operation!");
    }

    @Override
    public MQPublisher registerPublisher(String channel) {
        if (channel == null) return null;
        MQPublisher publisher = publishers.get(channel);
        if (publisher!=null) return publisher;
        if (!createChannel(channel)) return null;
        return publishers.get(channel);
    }

    @Override
    public boolean registerSubscriber(MQSubscriber subscriber, String channel) {
        if (subscriber == null || channel == null) return false;
        List<MQSubscriber> subscriberList = subscribers.get(channel);
        if (subscriberList!=null){
            if (subscriberList.size()>PER_CHANNEL_SUBSCRIBERS_MAX_SIZE) return false;
            subscriberList.add(subscriber);
            return true;
        }else {
            if(!createChannel(channel)) return false;
            subscribers.get(channel).add(subscriber);
            return true;
        }
    }

    private synchronized boolean createChannel(String channel){
        if (channels.get(channel)!=null) return true;
        if (channels.size()>CHANNELS_MAX_SIZE) return false;
        BlockingQueue<Object> ch = new LinkedBlockingQueue<>(CHANNEL_QUEUE_MAX_SIZE);
        channels.put(channel, ch);
        publishers.put(channel, new MQPublishSimpleImpl(ch));
        subscribers.put(channel, new LinkedList<>());
        listenerLoops.execute(new ListenTask(channel));
        return true;
    }

    private class ListenTask implements Runnable{
        private String channel;

        private ListenTask(String channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            while (true){
                BlockingQueue<Object> ch = channels.get(channel);
                if (ch==null){
                    publishers.remove(channel);
                    subscribers.remove(channel);
                    break;
                }
                try {
                    Object msg = ch.take();
                    List<MQSubscriber> subscriberList = subscribers.get(channel);
                    for (MQSubscriber subscriber : subscriberList){
                        eventExecutors.execute(new EventTask(subscriber,msg));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    //continue
                }
            }
        }
    }

    private class EventTask implements Runnable{
        private MQSubscriber subscriber;
        private Object msg;

        private EventTask(MQSubscriber subscriber, Object msg) {
            this.subscriber = subscriber;
            this.msg = msg;
        }

        @Override
        public void run() {
            subscriber.onMsg(msg);
        }
    }
}
