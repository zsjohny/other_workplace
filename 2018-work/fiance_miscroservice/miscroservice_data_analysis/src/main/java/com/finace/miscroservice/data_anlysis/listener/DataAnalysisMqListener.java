package com.finace.miscroservice.data_anlysis.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.entity.DataAnalysis;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DataAnalysisMqListener extends MqListenerConvert {


    private Log logger = Log.getInstance(DataAnalysisMqListener.class);


    @Override
    public void transferTo(String transferData) {
        try {
            logger.info("开始处理监听数据分析={}", transferData);


            DataAnalysis analysis = JSONObject.parseObject(transferData, DataAnalysis.class);


            if (StringUtils.isEmpty(analysis.getUid())) {

            }


        } catch (Exception e) {
            logger.error("处理监听数据分析={}异常", transferData, e);

        }

    }

}
