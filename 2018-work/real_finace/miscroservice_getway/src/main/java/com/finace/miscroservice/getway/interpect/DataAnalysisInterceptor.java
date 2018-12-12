package com.finace.miscroservice.getway.interpect;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.annotation.DependsOnMq;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.current.ExecutorService;
import com.finace.miscroservice.commons.current.ExecutorTask;
import com.finace.miscroservice.commons.entity.DataAnalysis;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.DATA_ANALYSIS;

/**
 * 数据统计的拦截器
 */
@Component
@DependsOnMq
public class DataAnalysisInterceptor {

    private final String DATA_CALC_INTO_NAME = "dataAnalysis";
    private ExecutorService executor =
            new ExecutorService(Runtime.getRuntime().availableProcessors() >> 1, DATA_CALC_INTO_NAME);


    private Log log = Log.getInstance(DataAnalysis.class);

    @Autowired
    private MqTemplate mqTemplate;

    /**
     * 转发连接
     *
     * @param request 请求地址
     * @param uid     客户端的特定的uid
     * @param uid     用户的Id
     */
    public void forward(HttpServletRequest request, String uid, String userId) {

        executor.addTask(new ExecutorTask() {
            @Override
            public void doJob() {
                log.info("IP={} 下uid={} 转发数据访问", Iptools.gainRealIp(request), uid);
                DataAnalysis analysis = new DataAnalysis();
                analysis.setUserId(userId);
                analysis.setUid(uid);
                analysis.setDid(request.getParameter("did"));
                analysis.setChannel(request.getParameter("channel"));
                analysis.setUrl(request.getRequestURI());
                mqTemplate.sendMsg(DATA_ANALYSIS.toName(), JSONObject.toJSONString(analysis));
            }
        });


    }


}
