package com.finace.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.dao.UserChannelGeneralizeDao;
import com.finace.miscroservice.activity.po.entity.UserChannelGeneralize;
import com.finace.miscroservice.activity.service.UserChannelGeneralizeService;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.IDFA_OR_IMEI;

@Service
public class UserChannelGeneralizeServiceImpl implements UserChannelGeneralizeService {
    Log logger = Log.getInstance(UserChannelGeneralizeServiceImpl.class);
    @Autowired
    private UserChannelGeneralizeDao userChannelGeneralizeDao;

    @Override
    public Map<String,String> insertUserChannelGeneralize(String appid, String idfa, String keywords, String channel, String timestamp,  String callback, String phone) {
        Map<String,String> map = new HashMap<>();
        if (StringUtils.isEmpty(idfa)||StringUtils.isEmpty(keywords)||StringUtils.isEmpty(channel)||StringUtils.isEmpty(timestamp)
                ||StringUtils.isEmpty(callback)||StringUtils.isEmpty(phone)||StringUtils.isEmpty(appid)){
            logger.info("请求参数不全");
            map.put("code","-3");
            map.put("message","请求参数不全");
            return map;
        }
        UserChannelGeneralize userChannelGeneralize1 = userChannelGeneralizeDao.findChannelGeneralize(idfa,phone);
        if (userChannelGeneralize1!=null&&userChannelGeneralize1.getActivate()==1){
            map.put("code","-1");
            map.put("message","用户已激活");
            return map;
        }
        if (userChannelGeneralize1!=null&&userChannelGeneralize1.getIdfa().equals(idfa)){
            map.put("code","-4");
            map.put("message","用户已参加活动请及时激活");
            return map;
        }
        UserChannelGeneralize userChannelGeneralize = new UserChannelGeneralize();
        try {
            logger.info("秒赚渠道点击");
//            UserChannelGeneralize userChannelGeneralize = new UserChannelGeneralize();
            userChannelGeneralize.setAppid(appid);
            userChannelGeneralize.setActivate(0);
            userChannelGeneralize.setIdfa(idfa);
            userChannelGeneralize.setKeywords(keywords);
            userChannelGeneralize.setChannel(channel);
            userChannelGeneralize.setTimestamp(timestamp);
            userChannelGeneralize.setCallback(callback);
            userChannelGeneralize.setPhone(phone);

            userChannelGeneralizeDao.insertUserChannelGeneralize(userChannelGeneralize);
            map.put("code","0");
            map.put("message","ok");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            logger.warn(e.getMessage());
            map.put("code","-2");
            map.put("message","error");
            return map;
        }

    }

    @Override
    public Map<String, String> findUserChannelGeneralize(String idfa, String phone) {
        String[] idfas = idfa.split(",");
        Map<String ,String >  map = new HashMap<>();
        if (idfas.length>0){
            for (int i =0;i<idfas.length;i++){
                UserChannelGeneralize userChannelGeneralize = userChannelGeneralizeDao.findChannelGeneralize(idfas[i],phone);
                Integer code = 0; //未激活
                if (userChannelGeneralize!=null&&userChannelGeneralize.getActivate()==1){
                    code = 1;  //已激活
                }
                map.put(idfas[i],String.valueOf(code));
            }
        }

        return map;
    }

    @Override
    public void test(Integer pageNum) {


        PageHelper.startPage(pageNum,10);
        List<String> idfas = userChannelGeneralizeDao.findChannelGeneralizes();
        for (String idfa : idfas){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("os","ios");
            jsonObject.put("idfa",idfa);
            logger.info("=====================");
            logger.info("模拟推送");
            logger.info("=====================");
            ApplicationContextUtil.getBean(MqTemplate.class).sendMsg(IDFA_OR_IMEI.toName(),jsonObject.toJSONString());
        }
    }
}
