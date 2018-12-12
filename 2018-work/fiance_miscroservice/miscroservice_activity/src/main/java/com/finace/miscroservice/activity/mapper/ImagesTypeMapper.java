package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.ImagesTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImagesTypeMapper {


    /**
     * 根据类型获取图片配置信息  1--app首页  2--活动中心  3--PC首页 4--运营报告
     * @param type
     * @return
     */
   List<ImagesTypePO> getImagesType(@Param("type") Integer type);





}
