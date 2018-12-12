package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.dao.ImagesTypeDao;
import com.finace.miscroservice.activity.po.ImagesTypePO;
import com.finace.miscroservice.activity.service.ImagesTypeService;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagesTypeServiceImpl implements ImagesTypeService {

    @Autowired
    private ImagesTypeDao imagesTypeDao;


    @Override
    public List<ImagesTypePO> getImagesType(Integer type, Integer page) {
        //1--app首页  2--活动中心  3--PC首页 4--运营报告
        List<ImagesTypePO> list = imagesTypeDao.getImagesType(type);
        if( "1".equals(type) || "3".equals(type)){
             for ( ImagesTypePO imagesTypePO : list ){
                 //imagesTypePO.getStatus()   0--隐藏 1--显示
                 if( !( !DateUtils.compareDate(imagesTypePO.getStime(), DateUtils.getNowDateStr())
                      && DateUtils.compareDate(imagesTypePO.getEtime(), DateUtils.getNowDateStr())
                      && "1".equals(imagesTypePO.getStatus()) ) ){
                     list.remove(imagesTypePO);
                 }
                 //imagesTypePO.setSurl(imagesTypePO.getJumurl());
             }
        }

        return list;
    }





}
