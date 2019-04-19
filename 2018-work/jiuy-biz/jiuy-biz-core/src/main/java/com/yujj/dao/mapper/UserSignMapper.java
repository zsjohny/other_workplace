package com.yujj.dao.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.UserSign;

@DBMaster
public interface UserSignMapper {

    int insertUserSign(UserSign userSign);

    UserSign getUserSign(@Param("userId") long userId, @Param("dayTime") int dayTime);

    @MapKey("weekDay")
    Map<Integer, UserSign> getUserSignOfWeek(@Param("userId") long userId, @Param("mondayTime") int mondayTime);

    int getTotalSignCount(@Param("userId") long userId);

    int getTotalSignCoins(@Param("userId") long userId);

}
