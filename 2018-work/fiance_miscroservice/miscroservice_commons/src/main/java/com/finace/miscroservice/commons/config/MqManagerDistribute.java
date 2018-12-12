package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.finace.miscroservice.commons.enums.MqNameEnum.*;

/**
 * Mq的管理分配
 */
@Configuration
public class MqManagerDistribute implements BeanFactoryAware {

    private Log logger = Log.getInstance(MqManagerDistribute.class);

    @Autowired
    @Lazy
    private MqManagerConfig mqManagerConfig;


    @Bean
    @ConfigurationProperties("mq")
    public MqManagerConfig initMqConfig() {
        return new MqManagerConfig();
    }

    private MqConfig mqConfig;

    /**
     * 所有channel的缓存类
     */
    private final Map<String, Boolean> channeOrBeanListsCache = new HashMap<>();


    @PostConstruct
    public void init() {
        //检查参数
        if (mqManagerConfig.isEmpty()) {
            return;
        }

        logger.info("mq config init...");


        //获取channels通道集合
        List<String> channels = mqManagerConfig.getChannels();


        //mq消息管理类
        MqManager manager;

        if (channels == null || channels.isEmpty()) {
            logger.info("check channels is empty not load");
        } else {
            logger.info("channels start parse ");
            //channel格式 消息名称:消息类型(fanout:direct):类的名称
            String[] _params;


            for (String channel : channels) {
                if (!checkNameRegularByChannel(channel)) {
                    logger.info("channel={} illegal is give up", channel);
                    continue;
                }


                //初始化mqManger
                manager = initMqManager();


                _params = channel.split(":");


                //检测并存入缓存
                if (channeOrBeanListsCache.putIfAbsent(_params[0], Boolean.TRUE) != null) {
                    logger.info("channel={} have been add ", _params[0]);
                    continue;
                }

                //注册信息
                mqConfig.setQueueName(_params[0] + QUEUE_NAME_SUFFIX.toName());
                mqConfig.setRoutingKeyName(_params[0] + ROUTING_KEY_NAME_SUFFIX.toName());
                mqConfig.setExchangeName(_params[0] + EXCHANGER_NAME_SUFFIX.toName());
                MqListenerConvert mqListenerConvert;
                try {
                    mqListenerConvert = beanFactory.getBean(_params[2], MqListenerConvert.class);

                    //检测bean是否被使用过
                    if (channeOrBeanListsCache.putIfAbsent(_params[2], Boolean.TRUE) != null) {
                        logger.info("bean={} have been used ", _params[2]);
                        continue;
                    }


                } catch (BeansException e) {
                    logger.error("parse channel={} ,init bean ={} not found", _params[0], _params[2]);
                    continue;
                }


                mqConfig.setMqListenerConvert(mqListenerConvert);


                //重新初始化mqConfig
                manager.setMqConfig(mqConfig);

                //创建相应队列
                switch (_params[1]) {
                    case "fanout":
                        //注册 fanout通道
                        //判断队列是否是允许随机名称
                        if (_params.length == 4) {
                            Boolean _randomFlag = Boolean.valueOf(_params[3]);
                            if (_randomFlag) {
                                //队列名称不唯一 保证全部接受到
                                mqConfig.setQueueName(mqConfig.getQueueName() + UUIdUtil.generateName());
                                logger.info("channel queueName create by random success", _params[0]);
                            }
                        }


                        manager.registerFanoutListener();

                        break;
                    default:
                        //注册 direct通道
                        manager.registerDirectListener();

                }

                logger.info("channel={} has load success", _params[0]);


            }

        }


        /**
         * 判断是否实例化发送类
         */
        if (mqManagerConfig.getOpenSend() != null && mqManagerConfig.getOpenSend()) {

            //初始化mqManger
            manager = initMqManager();

            //注册发送类
            manager.registerRabbitMqTemplate();
        }


    }


    /***
     * 拷贝相同的部分
     * @param mqManagerConfig mq管理配置中心
     * @return
     */
    private MqConfig copySameParams(MqManagerConfig mqManagerConfig) {
        MqConfig mqConfig = new MqConfig();
        mqConfig.setHost(mqManagerConfig.getHost());
        mqConfig.setPort(mqManagerConfig.getPort());
        mqConfig.setUserName(mqManagerConfig.getUserName());
        mqConfig.setUserPass(mqManagerConfig.getUserPass());
        mqConfig.setFactory((DefaultListableBeanFactory) beanFactory);
        return mqConfig;
    }


    /**
     * 检查channel命名是否合规
     *
     * @return
     */
    private boolean checkNameRegularByChannel(String channel) {

        boolean regularFlag = false;
        if (StringUtils.isEmpty(channel)) {

            return regularFlag;
        }

        if (channel.matches("\\w+:(fanout|direct):\\w+(:(true|false))?")) {
            regularFlag = true;
        }

        return regularFlag;


    }

    /**
     * 初始化 mqManager
     *
     * @return
     */
    private MqManager initMqManager() {

        mqConfig = copySameParams(mqManagerConfig);

        //mq消息管理类
        MqManager manager = new MqManager();

        manager.setMqConfig(mqConfig);

        //注册connectionFactory
        manager.registerConnectionFactory();

        return manager;

    }

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


}
