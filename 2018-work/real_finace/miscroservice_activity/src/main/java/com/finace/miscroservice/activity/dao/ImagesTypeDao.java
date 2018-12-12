package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.ImagesTypePO;

import java.util.List;



public interface ImagesTypeDao {

    /**
     * 根据类型获取图片配置信息  1--app首页  2--活动中心  3--PC首页 4--运营报告
     * @param type
     * @return
     */
    public List<ImagesTypePO> getImagesType(Integer type);

}
