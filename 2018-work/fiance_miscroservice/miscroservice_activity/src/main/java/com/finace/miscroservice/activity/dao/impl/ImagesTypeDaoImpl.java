package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.ImagesTypeDao;
import com.finace.miscroservice.activity.mapper.ImagesTypeMapper;
import com.finace.miscroservice.activity.po.ImagesTypePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImagesTypeDaoImpl implements ImagesTypeDao {

    @Autowired
    private ImagesTypeMapper imagesTypeMapper;


    @Override
    public List<ImagesTypePO> getImagesType(Integer type) {
        return imagesTypeMapper.getImagesType(type);
    }
}
