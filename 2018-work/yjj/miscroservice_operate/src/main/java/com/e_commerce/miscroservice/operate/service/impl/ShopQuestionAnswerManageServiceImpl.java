package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionType;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.ShopQuestionAnswerManageDao;
import com.e_commerce.miscroservice.operate.enmus.QuestionSizeUpDownEnums;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionAnswerResponse;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionTypeResponse;
import com.e_commerce.miscroservice.operate.service.user.ShopQuestionAnswerManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 16:52
 * @Copyright 玖远网络
 */
@Service
public class ShopQuestionAnswerManageServiceImpl implements ShopQuestionAnswerManageService {
    private Log logger = Log.getInstance(ShopQuestionAnswerManageServiceImpl.class);
    @Resource
    private ShopQuestionAnswerManageDao shopQuestionAnswerManageDao;
    /**
     * 查询 问题类型
     * @param shopQuestionAnswerTypeRequest
     * @return
     */
    @Override
    public Response findAllShopQuestionAnswerType(ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest) {
        List<ShopQuestionTypeResponse> list = shopQuestionAnswerManageDao.findAllShopQuestionAnswerTpye(shopQuestionAnswerTypeRequest);
        SimplePage<ShopQuestionTypeResponse> pageInfo= new SimplePage<>(list);
        return Response.success(pageInfo);
    }

    /**
     * 查询问题类型总数量
     * @return
     */
    @Override
    public Response findQuestionTypeSize() {
        Integer size = shopQuestionAnswerManageDao.findQuestionTypeSize();
        return Response.success(size);
    }
    /**
     * 新建问题类型
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response addQuestionType(String name, Integer sort) {
        if (StringUtils.isEmpty(name)||sort==null){
            logger.warn("参数为空name={},sort={}",name,sort);
            return Response.errorMsg("参数为空");
        }
        ShopQuestionType shopQuestionType = shopQuestionAnswerManageDao.findQuestionTypeOneBySort(sort);
        if (shopQuestionType!=null){
            logger.warn("该序号已存在sort={}",sort);
            return Response.errorMsg("该序号已存在");
        }
        ShopQuestionType questionTypeOneByName = shopQuestionAnswerManageDao.findQuestionTypeOneByName(name);
        if (questionTypeOneByName!=null){
            logger.warn("该名称已存在name={}",name);
            return Response.errorMsg("该名称已存在");
        }
        Integer inset = shopQuestionAnswerManageDao.addQuestionType(name,sort);
        if (inset==0){
            logger.warn("问题添加失败");
            return Response.errorMsg("问题添加失败");
        }
        return Response.success();
    }

    /**
     * 编辑问题类型
     * @param name
     * @param sort
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response upQuestionType(String name, Integer sort, Long id) {
        if (StringUtils.isEmpty(name)||sort==null||id==null){
            logger.warn("参数为空name={},sort={},id={}",name,sort,id);
            return Response.errorMsg("参数为空");
        }
        ShopQuestionType shopQuestionType = shopQuestionAnswerManageDao.findQuestionTypeOneBySort(sort);
        if (shopQuestionType!=null&&!shopQuestionType.getId().equals(id)){
            logger.warn("该序号已存在sort={}",sort);
            return Response.errorMsg("该序号已存在");
        }
        ShopQuestionType questionTypeOneByName = shopQuestionAnswerManageDao.findQuestionTypeOneByName(name);
        if (questionTypeOneByName!=null&&!questionTypeOneByName.getId().equals(id)){
            logger.warn("该名称已存在name={}",name);
            return Response.errorMsg("该名称已存在");
        }
        Integer up = shopQuestionAnswerManageDao.upQuestionType(id,name,sort);
        if (up==0){
            logger.warn("问题编辑失败");
            return Response.errorMsg("问题编辑失败");
        }
        return Response.success();
    }

    /**
     * 转移问题类型
     * @param exchangeId
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response exchangeQuestionType(Long exchangeId, Long id) {
        logger.info("转移问题类型=exchangeId={}，id={}",exchangeId,id);
        if (exchangeId==null||id==null){
            logger.warn("参数为空exchangeId={},id={}",exchangeId,id);
            return Response.errorMsg("参数为空");
        }
        ShopQuestionType shopQuestionType =  shopQuestionAnswerManageDao.findQuestionTypeById(exchangeId);
        ShopQuestionType ex =  shopQuestionAnswerManageDao.findQuestionTypeById(id);
        if (shopQuestionType == null){
            logger.warn("转移的问题类型不存在");
            return  Response.errorMsg("转移的问题类型不存在");
        }
        shopQuestionAnswerManageDao.updateQuestionSize(exchangeId,QuestionSizeUpDownEnums.UP_SIZE.getCode(),ex.getQuestionSize());
        shopQuestionAnswerManageDao.updateQuestionSize(id,QuestionSizeUpDownEnums.DOWN_SIZE.getCode(),ex.getQuestionSize());
        shopQuestionAnswerManageDao.exchangeQuestionType(id,exchangeId);

        return Response.success();
    }

    /**
     * 查询问题
     * @return
     */
    @Override
    public Response findAllShopQuestionAnswer(ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest) {
        logger.info("问题查询={}",shopQuestionAnswerTypeRequest);
       List<ShopQuestionAnswerResponse> shopQuestionAnswerResponses =  shopQuestionAnswerManageDao.findAllShopQuestionAnswer(shopQuestionAnswerTypeRequest);
        SimplePage<ShopQuestionAnswerResponse> simplePage = new SimplePage<>(shopQuestionAnswerResponses);
       return Response.success(simplePage);
    }
    /**
     * 新建问题
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response addShopQuestion(ShopQuestionInsertRequest request) {
        Map map = AnnotationUtils.validate(request);
        if (map.get("result")==Boolean.FALSE){
            return Response.error(map.get("message"));
        }
        ShopQuestionAnswer shopQuestionAnswer = shopQuestionAnswerManageDao.findQuestionOneBySort(request.getSort());
        if (shopQuestionAnswer!=null&&!shopQuestionAnswer.getId().equals(request.getId())){
            logger.warn("该序号已存在shopQuestionAnswer.getId={},request.getId={}",shopQuestionAnswer.getId(),request.getId());
            return Response.errorMsg("该序号已存在");
        }
        Integer indert = shopQuestionAnswerManageDao.addShopQuestion(request);
        if (indert==0){
            logger.warn("新建问题失败");
            return Response.errorMsg("新建问题失败");
        }
        //关联问题数量更新 增加
        shopQuestionAnswerManageDao.updateQuestionSize(request.getQuestionTypeId(), QuestionSizeUpDownEnums.UP_SIZE.getCode(),QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());
        return Response.success();
    }
    /**
     * 修改问题
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response updateShopQuestion(ShopQuestionInsertRequest request) {
        Map map = AnnotationUtils.validate(request);
        if (map.get("result")==Boolean.FALSE){
            return Response.error(map.get("message"));
        }
        ShopQuestionAnswer shopQuestionAnswer = shopQuestionAnswerManageDao.findQuestionOneBySort(request.getSort());
        if (shopQuestionAnswer!=null&&!shopQuestionAnswer.getId().equals(request.getId())){
            logger.warn("该序号已存在shopQuestionAnswer.getId={},request.getId={}",shopQuestionAnswer.getId(),request.getId());
            return Response.errorMsg("该序号已存在");
        }
        ShopQuestionType shopQuestionType = shopQuestionAnswerManageDao.findQuestionTypeById(shopQuestionAnswer.getQuestionTypeId());
        Integer indert = shopQuestionAnswerManageDao.updateShopQuestion(request);
        if (indert==0){
            logger.warn("修改问题失败");
            return Response.errorMsg("修改问题失败");
        }
        if (shopQuestionType!=null&&!shopQuestionType.getId().equals(request.getQuestionTypeId())){
            logger.info("类型修改shopQuestionType.getId()={},request.getQuestionTypeId()={}",shopQuestionType.getId(),request.getQuestionTypeId());
            //关联问题数量更新 增加
            shopQuestionAnswerManageDao.updateQuestionSize(request.getQuestionTypeId(), QuestionSizeUpDownEnums.UP_SIZE.getCode(),QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());
            //关联问题数量更新 减少
            shopQuestionAnswerManageDao.updateQuestionSize(shopQuestionType.getId(), QuestionSizeUpDownEnums.DOWN_SIZE.getCode(),QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());

        }

        return Response.success();
    }

    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    @Override
    public Response findOneShopQuestionAnswer(Long id) {
        logger.info("根据问题id={}查询单个问题",id);
        ShopQuestionAnswer shopQuestionAnswer = shopQuestionAnswerManageDao.findOneShopQuestionAnswer(id);
        return Response.success(shopQuestionAnswer);
    }

    /**
     * 获取问题类型下拉选
     * @return
     */
    @Override
    public Response findTypeSelection() {
        logger.info("获取问题下拉选");
        List<ShopQuestionType> shopQuestionType = shopQuestionAnswerManageDao.findSelectionShopQuestionAnswerTpye();
        return Response.success(shopQuestionType);
    }

    /**
     * 批量显示隐藏
     * @param ids 需要批量显示或者隐藏的 id 集合
     * @param showNot
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response batchDisplayOrHiding(String ids, Integer showNot) {
        logger.info("批量显示隐藏ids={},showNot={}",ids,showNot);
        if (StringUtils.isEmpty(ids)||showNot==null){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        String[] strings = ids.split(",");
        List<String> list = Arrays.asList(strings);
        shopQuestionAnswerManageDao.batchDisplayOrHiding(list,showNot);
        return Response.success();
    }
    /**
     * 批量 删除
     * @param ids 需要批量显示或者隐藏的 id 集合
     * @param delNot 1 删除 0 正常
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response batchDelNot(String ids, Integer delNot) {
        logger.info("批量删除ids={},delNot={}",ids,delNot);
        if (StringUtils.isEmpty(ids)||delNot==null){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        String[] strings = ids.split(",");
        List<String> list = Arrays.asList(strings);

        List<ShopQuestionAnswer> shopQuestionAnswerList = shopQuestionAnswerManageDao.findQuestionAnswerByIdList(list);
        for (ShopQuestionAnswer shopQuestionAnswer : shopQuestionAnswerList){
            if (!shopQuestionAnswer.getDelStatus().equals(delNot)){
                logger.info("关联问题操作shopQuestionAnswer.getQuestionTypeId()={}",shopQuestionAnswer.getQuestionTypeId());
                shopQuestionAnswerManageDao.updateQuestionSize(shopQuestionAnswer.getQuestionTypeId(),delNot,QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());
            }
        }
        shopQuestionAnswerManageDao.batchDelNot(list,delNot);

        return Response.success();
    }

    /**
     * 删除 问题类型
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response typeDel(Long id) {
        logger.info("删除 问题类型id={}",id);
        if (id==null){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        shopQuestionAnswerManageDao.typeDel(id);
        return Response.success();
    }


}
