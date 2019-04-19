package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.activity.CityPartner;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.CityPartnerDao;
import com.e_commerce.miscroservice.operate.service.activity.CityPartnerService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/22 17:15
 * @Copyright 玖远网络
 */
@Service
public class CityPartnerServiceImpl implements CityPartnerService {
    @Resource
    private CityPartnerDao cityPartnerDao;
    /**
     * 城市合伙人列表
     * @param name
     * @param phone
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Response findAllCityPartner(String name, String phone, Integer pageNumber, Integer pageSize) {
        List<CityPartner> list = cityPartnerDao.findAllCityPartner(name,phone,pageNumber,pageSize);
        int total = cityPartnerDao.findTotal(name,phone);
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("list",list);
        return Response.success(map);
    }
}
