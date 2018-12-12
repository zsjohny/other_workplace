package com.finace.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.dao.CreditDao;
import com.finace.miscroservice.activity.dao.CreditLogDao;
import com.finace.miscroservice.activity.dao.ShopDao;
import com.finace.miscroservice.activity.po.*;
import com.finace.miscroservice.activity.service.ImagesTypeService;
import com.finace.miscroservice.activity.service.ShopService;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.enums.ShopCommoditiesEnums;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.PageUtils;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    Log logger  =  Log.getInstance(ShopServiceImpl.class);

    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ImagesTypeService imagesTypeService;
    @Autowired
    private CreditDao creditDao;
    @Autowired
    private CreditLogDao creditLogDao;
    @Autowired
    private UserRedPacketsService userRedPacketsService;


    @Override
    public Response showCommodities(Integer page) {
        logger.info("展示商品页面");
        PageHelper.startPage(page,10);
        List<CommoditiesPO> commoditiesPOList = shopDao.showCommodities();
        PageUtils<CommoditiesPO> pageUtils = new PageUtils(commoditiesPOList);
        return Response.success(pageUtils);
    }

    @Override
    public Response showShopHome(String userId) {
        List<ImagesTypePO> imagesTypePOList =  imagesTypeService.getImagesType(7,1);
        CreditPO creditPO = creditDao.getCreditByUserId(userId);
//        Integer count = creditLogDao.getCreditLogSizeByUserId(userId);
        Integer count = shopDao.CountShopLogByUserId(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgs",imagesTypePOList.size()>0?imagesTypePOList.get(0).getImgurl():null);
        jsonObject.put("peas",creditPO==null?0:creditPO.getValue());
        jsonObject.put("count", count);
        return Response.success(jsonObject);
    }

    @Override
    public Response buyCommodity(Integer id, String userId) {
        CommoditiesPO commoditiesPO = shopDao.findCommodityByid(id);
        CreditPO creditPO = creditDao.getCreditByUserId(userId);

        if (commoditiesPO!=null&&commoditiesPO.getCount()>0&&commoditiesPO.getStatus()==1){
            if (creditPO==null||commoditiesPO.getGoldPeas()>creditPO.getValue()){
                logger.warn("金豆余额不足");
                return Response.errorMsg("金豆余额不足");
            }
            //减库存
            shopDao.subCommodityCount(id);
            //减金豆
            creditDao.updateCreditAddByUserId(userId,String.valueOf(-commoditiesPO.getGoldPeas()));
            String context = null;
            Integer status = 0;
            if (commoditiesPO.getClassify()== ShopCommoditiesEnums.DEFAULT.getCode()){
                context=String.format(ShopCommoditiesEnums.DEFAULT.getValue(),String.valueOf(commoditiesPO.getGoldPeas()));
                status=1;
            }else {
                context=String.format(ShopCommoditiesEnums.OTHERS.getValue(),String.valueOf(commoditiesPO.getGoldPeas()));
            }
            try {
                //发放红包
                userRedPacketsService.grantFlq(Integer.valueOf(userId),0,String.valueOf(commoditiesPO.getNid()),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //添加商品购买记录
            shopDao.addShopLog(id,userId, UUIdUtil.generateOrderNo(), DateUtils.getNowDateStr(),commoditiesPO.getGoldPeas(),context,status);
            return Response.success();
        }else {
            logger.warn("库存不足");
            return Response.errorMsg("库存不足");
        }
    }

    @Override
    public Response commodityRecord(Integer page, String userId) {

        PageHelper.startPage(page,10);
        List<CommoditiesLogPO> commoditiesLogPOList = shopDao.commodityRecord(userId);
        PageUtils<CommoditiesLogPO> pageUtils = new PageUtils<>(commoditiesLogPOList);
        return Response.success(pageUtils);
    }

    @Override
    public Response showCommodityDetailed(String userId, Integer id) {
        CommoditiesPO commoditiesPO = shopDao.showCommodityDetailed(id);
        return Response.success(commoditiesPO);
    }

    @Override
    public Response buyRecord(Integer page, String userId) {
        PageHelper.startPage(page,10);
        List<CommodityBuyRecordsPO> recordsPO = shopDao.buyRecord(userId);
        PageUtils<CommodityBuyRecordsPO> poPageUtils = new PageUtils<>(recordsPO);
        return Response.success(poPageUtils);
    }

    public static void main(String[] args) {
        Integer a = -1;
        String b = String.valueOf(a);
        System.out.println(String.valueOf(a));
        System.out.println(Integer.parseInt(b));
    }
}
