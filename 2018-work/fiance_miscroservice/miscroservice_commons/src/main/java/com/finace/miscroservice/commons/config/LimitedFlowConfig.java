package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限制流量的配置中心
 */
@Configuration
@ConditionalOnExpression("${limter.flow.enabled}")
public class LimitedFlowConfig extends RedisTemplateConfig {


    private Log logger = Log.getInstance(LimitedFlowConfig.class);



    /**
     * 创建脚本的redisTemplate
     *
     * @return
     */
    @Bean("scriptTemplate")
    public RedisTemplate createRedisTemplate() {
        RedisTemplate templateCache = createTemplateCache(RedisTemplateConfig.OperateEnum.SCRIPT);
        return templateCache;
    }

    /**
     * 初始化执行脚本
     *
     * @return
     */
    @Bean
    public DefaultRedisScript createRedisScript() {
        DefaultRedisScript script = new DefaultRedisScript();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/limitedFlowScript.lua")));
        script.setResultType(Integer.class);
        return script;
    }


    @Autowired
    @Qualifier("scriptTemplate")
    private RedisTemplate scriptTemplate;

    @Autowired
    private DefaultRedisScript defaultRedisScript;

    /**
     * 脚本所需数据集合的缓存
     */
    private Map<String, List> paramsCache = new ConcurrentHashMap<>();

    /**
     * 初始化脚本所需参数
     *
     * @param identifiedKey 限制的标识key
     * @param limiterCount  限制的大小
     */
    public void init(String identifiedKey, Integer limiterCount) {
        List params = new ArrayList<>();
        //设定参数
        params.add(identifiedKey);
        params.add(String.valueOf(limiterCount));
        try {
            params.add(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
            logger.error("获取电脑计算机名称出错", e);
        }

        paramsCache.put(identifiedKey, params);

        //执行
        Object result = scriptTemplate.execute(defaultRedisScript, params, Operate.INIT.toVal());

        logger.info("script load result ==> {}", result);

    }

    /**
     * 获取令牌
     *
     * @param identifiedKey 获取的限制的标识的key
     * @return true 获取成功 false获取失败
     */
    public Boolean acquire(String identifiedKey) {
        Boolean acquireResultFlag = Boolean.FALSE;
        List params = paramsCache.get(identifiedKey);
        if (params == null || params.isEmpty()) {

            logger.warn("请先初始化脚本执行参数 ");
            return acquireResultFlag;
        }

        params.set(2, String.valueOf(System.currentTimeMillis()));
        Object result = scriptTemplate.execute(defaultRedisScript, params, Operate.ACQUIRE.toVal());


        if (result != null && result.equals(Operate.SUCCESS.toVal())) {
            acquireResultFlag = Boolean.TRUE;
        } else {
            logger.info("获取令牌={} 已经超出限定大小={} 被舍弃,当前的数量是={} ", identifiedKey, params.get(1), result);
        }

        return acquireResultFlag;


    }

    /**
     * 重置限流的大小
     *
     * @param identifiedKey 获取的限制的标识的key
     * @param limiterCount  限流的大小
     */
    public void resetLimiterCount(String identifiedKey, int limiterCount) {

        if (StringUtils.isEmpty(identifiedKey) || limiterCount <= 0) {
            logger.warn("重新设置限流的参数有误 key={} count={}", identifiedKey, limiterCount);
            return;
        }

        if (paramsCache.isEmpty()) {
            logger.warn("没有需要设置的限流");
            return;
        }

        Set<Map.Entry<String, List>> entries = paramsCache.entrySet();


        List newList = null;
        String newKey = "";
        for (Map.Entry<String, List> entry : entries) {
            if (entry.getKey().equals(identifiedKey)) {
                entry.getValue().set(1, String.valueOf(limiterCount));
                newList = entry.getValue();
                newKey = entry.getKey();
                break;
            }
        }

        if (newList == null) {
            logger.warn("集合中检测不到key={}的限流值", identifiedKey);
            return;
        }

        paramsCache.put(newKey, newList);
        logger.info("限流key={} 大小重置成功", identifiedKey);

    }


    /**
     * 操作枚举
     */

    private enum Operate {
        //初始化脚本     获取令牌           返回操作成功标识
        INIT("init"), ACQUIRE("acquire"), SUCCESS("success");
        private String val;

        Operate(String val) {
            this.val = val;
        }

        public String toVal() {
            return val;
        }
    }


}
