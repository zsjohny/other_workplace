package com.store.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.BizUtil;
import com.store.service.StoreUserService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.BusinessInformation;
import com.store.dao.mapper.BusinessInformationMapper;
import com.store.service.IBusinessInformationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * <p>
 * 商家信息表 服务实现类
 * </p>
 *
 * @author Hyf
 * @since 2018-08-15
 */
@Service
public class BusinessInformationService extends ServiceImpl<BusinessInformationMapper, BusinessInformation> implements IBusinessInformationService {
    Logger logger = LoggerFactory.getLogger(BusinessInformationService.class);
    @Autowired
    private BusinessInformationMapper businessInformationMapper;

    @Autowired
    private StoreUserService storeUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse addInformation(BusinessInformation businessInformation) {
        logger.info("商家={}，添加商家信息",businessInformation);
        if (StringUtils.isAnyEmpty(businessInformation.getName(),businessInformation.getAddress(),businessInformation.getPhone())){
            logger.warn("参数不能为空");
            return JsonResponse.getInstance().setError("参数不能为空");
        }
        BusinessInformation businessInformation2 = new BusinessInformation();
        businessInformation2.setUserId(businessInformation.getUserId());
        BusinessInformation response = businessInformationMapper.selectOne(businessInformation2);
        if (response!=null){
            logger.warn("信息已添加");
            return JsonResponse.getInstance().setError("请勿重复添加信息");
        }
        businessInformationMapper.insert(businessInformation);
//        businessInformationMapper.addInformation(businessInformation);
        return JsonResponse.getInstance().setSuccessful();
    }

    /**
     * 优化时间:2019年1月2日12:04:36
     * @Author:胡坤
     * @param id id
     * @return
     */
    @Override
    public BusinessInformation findInformationByUserId(Long id) {
        logger.info("根据商家UserId={}查询商家信息",id);
//        BusinessInformation businessInformation = new BusinessInformation();
//        businessInformation.setUserId(id);
//        BusinessInformation response = businessInformationMapper.selectOne(businessInformation);
        BusinessInformation response = businessInformationMapper.selectByUserId(id);
        if (BizUtil.isNotEmpty (response)) {
            response.setUserId (null);
        }
        return response;
    }




    @Override
    public JsonResponse updateInformationById(BusinessInformation businessInformation, Long id) {
        logger.info("商家={}，根据商家信息id={}，更新商家信息={}",id,businessInformation.getId(),businessInformation);
        if (StringUtils.isAnyEmpty(businessInformation.getName(),businessInformation.getAddress(),businessInformation.getPhone())){
            logger.warn("参数不能为空");
            return JsonResponse.getInstance().setError("参数不能为空");
        }
        businessInformation.setUserId(id);
        StoreBusiness storeBusiness = storeUserService.getStoreBusinessById(id);
        //认证通过
        if (storeBusiness!=null&&storeBusiness.getDataAuditStatus()!=null&&storeBusiness.getDataAuditStatus()==1){
            logger.info("用户已通过认证 name={}重置businessName={}",businessInformation.getName(),storeBusiness.getBusinessName());
            businessInformation.setName(storeBusiness.getBusinessName());
        }
        BusinessInformation businessInformation1= null;
        if (businessInformation.getId () != null) {
            businessInformation1 = businessInformationMapper.selectById(businessInformation.getId());
        }

        if (null==businessInformation1){
            logger.info("商家信息不存在 执行添加 businessInformation={} ",businessInformation);

            int i = businessInformationMapper.insert(businessInformation);
           if (i>0){
               return JsonResponse.successful();
           }
           return JsonResponse.fail("添加失败");
        }
        if (businessInformation.getName().equals(businessInformation1.getName())){
            int i = businessInformationMapper.updateById(businessInformation);
            if (i>0){
                return JsonResponse.successful();
            }
            return JsonResponse.fail("更新失败");
        }else {
            return JsonResponse.fail("店铺名称不同步");
        }
    }

    @Override
    public JsonResponse findInformationAll() {
        Page<BusinessInformation> page = new Page<>(1,10);
        Wrapper<BusinessInformation> wrapper = new EntityWrapper<BusinessInformation>();
        List<BusinessInformation> list = businessInformationMapper.selectPage(page,wrapper);
        return JsonResponse.getInstance().setSuccessful ().setData(list);
    }

    @Override
    public StoreBusiness selectStoreBusiness(Long storeId) {
        return businessInformationMapper.selectStoreBusiness(storeId);
    }


}
