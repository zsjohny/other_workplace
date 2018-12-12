package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.entity.response.MsgResponse;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MsgMapper {
    @MapKey("msgCode")
    Map<Integer, MsgResponse> findHomeMsg(@Param("userId") Integer userId);

    List<MsgResponse> findMsgDetailedList(@Param("userId") Integer userId, @Param("msgCode") Integer msgCode);

    /**
     * 根据类型获取消息通知
     * @return
     */
    List<MsgResponse> getMsgByAppIndex();
}
