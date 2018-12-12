package com.finace.miscroservice.official_website.dao.impl;

import com.finace.miscroservice.official_website.dao.CommonDao;
import com.finace.miscroservice.official_website.entity.response.ImageType;
import com.finace.miscroservice.official_website.entity.response.MsgTypeResponse;
import com.finace.miscroservice.official_website.mapper.CommonMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CommonDaoImpl implements CommonDao {
    @Resource
    private CommonMapper commonMapper;


    @Override
    public List<ImageType> imageType(Integer code) {
        return  commonMapper.imageType(code);
    }

    @Override
    public List<MsgTypeResponse> newsCenter(Integer code) {
        return commonMapper.newsCenter(code);
    }
}
