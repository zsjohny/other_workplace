package com.finace.miscroservice.official_website.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.official_website.dao.CommonDao;
import com.finace.miscroservice.official_website.dao.impl.CommonDaoImpl;
import com.finace.miscroservice.official_website.entity.response.ImageType;
import com.finace.miscroservice.official_website.entity.response.MsgTypeResponse;
import com.finace.miscroservice.official_website.enums.ImagesTypeEnums;
import com.finace.miscroservice.official_website.enums.MsgTypeEnums;
import com.finace.miscroservice.official_website.service.CommonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {
    private Log logger = Log.getInstance(CommonServiceImpl.class);
    @Autowired
    private CommonDao commonDao;
    private final static int DEFAULT_PAGE_SIZE = 10;
    @Override
    public Response runReport(Integer page) {
        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<ImageType> list = commonDao.imageType(ImagesTypeEnums.PMS_IMAGES.getCode());
        PageInfo<ImageType> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo);
    }

    @Override
    public Response activeCenter(Integer page) {
        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<MsgTypeResponse> list = commonDao.newsCenter(MsgTypeEnums.OFFICIAL_NOTICE.getCode());
        PageInfo<MsgTypeResponse> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo);
    }

    @Override
    public Response newsCenter(Integer page) {
        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<MsgTypeResponse> list = commonDao.newsCenter(MsgTypeEnums.NEWS_CENTER.getCode());
        PageInfo<MsgTypeResponse> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo);
    }

}
