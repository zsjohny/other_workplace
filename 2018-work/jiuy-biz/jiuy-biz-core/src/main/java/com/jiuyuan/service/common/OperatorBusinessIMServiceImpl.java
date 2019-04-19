package com.jiuyuan.service.common;

import com.admin.common.constant.factory.PageFactory;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.google.common.base.Verify;
import com.jiuyuan.dao.mapper.operator.OperatorBusinessIMMapper;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.BusinessInformation;
import com.store.service.StoreUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Create by hyf on 2018/8/20
 */
@Service
public class OperatorBusinessIMServiceImpl implements OperatorBusinessIMService {
    private Logger logger = LoggerFactory.getLogger(OperatorBusinessIMServiceImpl.class);
    @Autowired
    private OperatorBusinessIMMapper operatorBusinessIMMapper;
    @Autowired
    private IStoreBusinessNewService storeUserService;
    @Override
    public Page<BusinessInformation> findInformationAll(String name,String phone,String startTime,String endTime) {
//        if (pageNum==null||pageSize==null||pageNum<0||pageSize<0){
//            logger.warn("分页信息错误");
//            return JsonResponse.getInstance().setError("分页信息错误");
//        }
        Page<BusinessInformation> page = new PageFactory<BusinessInformation>().defaultPage();
//                new Page<BusinessInformation>(pageNum,pageSize);
        Wrapper<BusinessInformation> wrapper = new EntityWrapper<BusinessInformation>();
        if (StringUtils.isNotEmpty(name)){
            wrapper.eq("name",name);

        }
        if (StringUtils.isNotEmpty(phone)){
            wrapper.eq("phone",phone);
        }
        if (StringUtils.isNotEmpty(startTime)){
            wrapper.ge("create_time",startTime);

        }
        if (StringUtils.isNotEmpty(endTime)){
            wrapper.le("create_time",endTime);
        }

        List<BusinessInformation> list = operatorBusinessIMMapper.selectPage(page,wrapper);
        page.setRecords(list);
        return page;
    }

    @Override
    public JsonResponse findInformationByUserId(Long id) {
        if (id==null){
            logger.warn("商铺id为空");
            return JsonResponse.getInstance().setError("参数不能为空");
        }
        BusinessInformation businessInformation = new BusinessInformation();
        businessInformation.setUserId(id);
        BusinessInformation response = operatorBusinessIMMapper.selectOne(businessInformation);
        return JsonResponse.getInstance().setSuccessful().setData(response);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResponse updateInformationById(BusinessInformation businessInformation) {
        if (null==businessInformation.getId()||null==businessInformation.getUserId()||StringUtils.isAnyEmpty(businessInformation.getName(),businessInformation.getAddress(),businessInformation.getPhone())){
            logger.warn("参数不能为空");
            return JsonResponse.getInstance().setError("参数不能为空");
        }
        BusinessInformation businessInformation1 = new BusinessInformation();
        businessInformation1.setUserId(businessInformation.getUserId());
        businessInformation1.setDelState(0);
        BusinessInformation businessInformations = operatorBusinessIMMapper.selectOne(businessInformation1);
        if (null==businessInformations){
            logger.warn("商家信息不存在");
            return JsonResponse.getInstance().setError("不存在的商家信息，请添加商家信息");
        }

        operatorBusinessIMMapper.updateById(businessInformation);
        //修改认证信息
        StoreBusiness business = storeUserService.getStoreBusinessById(businessInformation.getId());
        if (business!=null){
            StoreBusiness storeBusiness = new StoreBusiness();
            storeBusiness.setId(businessInformation.getUserId());
            storeBusiness.setBusinessName(businessInformation.getName());
            storeUserService.updateById(storeBusiness);
        }

        return JsonResponse.getInstance().setSuccessful();
    }
}
