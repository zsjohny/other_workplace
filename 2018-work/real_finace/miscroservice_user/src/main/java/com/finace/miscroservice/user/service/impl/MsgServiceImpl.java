package com.finace.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.enums.RedisKeyEnum;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.dao.MsgDao;
import com.finace.miscroservice.user.entity.response.MsgResponse;
import com.finace.miscroservice.user.service.MsgService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.finace.miscroservice.user.entity.response.MsgResponse.DEFAULT_PAGE_SIZE;

@Service
public class MsgServiceImpl implements MsgService {
    @Autowired
    private MsgDao msgDao;
    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Override
    public Response findHomeMsg(Integer userId) {
        Map<Integer, MsgResponse> map2 = msgDao.findHomeMsg(userId);
        String str1 = userStrHashRedisTemplate.get(RedisKeyEnum.NOTICE_KEY.toKey());
        String str2 = userStrHashRedisTemplate.get(RedisKeyEnum.OFFICIAL_KEY.toKey());
        JSONObject notice = JSONObject.parseObject(str1);
        JSONObject official = JSONObject.parseObject(str2);
        if (str1 != null) {
            MsgResponse notice2 = new MsgResponse();
            notice2.setAddtime(Long.valueOf(notice.get("addtime").toString()));
            notice2.setTopic(notice.get("topic").toString());
            notice2.setMsgCode(Integer.parseInt(notice.get("msgCode").toString()));
            map2.put(Integer.parseInt(notice.get("msgCode").toString()), notice2);
        }
        if (str2 != null) {
            MsgResponse official2 = new MsgResponse();
            official2.setAddtime(Long.valueOf(official.get("addtime").toString()));
            official2.setTopic(official.get("topic").toString());
            official2.setMsgCode(Integer.parseInt(official.get("msgCode").toString()));
            map2.put(Integer.parseInt(official.get("msgCode").toString()), official2);
        }
        return Response.success(map2);
    }

    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
        Date d = new Date();
        System.out.println(d.getTime()
        );
    }

    @Override
    public Response findMsgDetailedList(Integer userId, Integer page, Integer msgCode) {
        PageHelper.startPage(page, DEFAULT_PAGE_SIZE);
        List<MsgResponse> map = msgDao.findMsgDetailedList(userId, msgCode);
        return Response.success(map);
    }

    @Override
    public List<MsgResponse> getAppIndexMsg() {

        return msgDao.getMsgByAppIndex();
    }




}



















