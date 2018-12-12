package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.commons.enums.MsgCodeEnum;
import com.finace.miscroservice.user.dao.MsgDao;
import com.finace.miscroservice.user.entity.response.MsgResponse;
import com.finace.miscroservice.user.mapper.MsgMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class MsgDaoImpl implements MsgDao{
    @Resource
    private MsgMapper msgMapper;
    /**
     * 消息首页
     * @param userId
     * @return
     */
    @Override
    public Map<Integer,MsgResponse> findHomeMsg(Integer userId) {
        return msgMapper.findHomeMsg(userId);
    }

    @Override
    public  List<MsgResponse>  findMsgDetailedList(Integer userId,  Integer msgCode) {
        if (msgCode== MsgCodeEnum.NOTICE_CENTER.getCode()||msgCode== MsgCodeEnum.OFFICIAL_NOTICE.getCode()){
            userId=null;
        }
        return msgMapper.findMsgDetailedList(userId,msgCode);
    }


    /**
     * 根据类型获取消息通知
     * @return
     */
    public List<MsgResponse> getMsgByAppIndex(){
        return msgMapper.getMsgByAppIndex();
    }
}
