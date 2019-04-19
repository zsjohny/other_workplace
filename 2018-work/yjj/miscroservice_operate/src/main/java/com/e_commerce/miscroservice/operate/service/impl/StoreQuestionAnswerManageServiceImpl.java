package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionType;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.StoreQuestionAnswerManageDao;
import com.e_commerce.miscroservice.operate.enmus.QuestionSizeUpDownEnums;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.entity.response.StoreQuestionTypeResponse;
import com.e_commerce.miscroservice.operate.entity.response.YjjQuestionAnswerResponse;
import com.e_commerce.miscroservice.operate.service.user.StoreQuestionAnswerManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 16:52
 * @Copyright 玖远网络
 */
@Service
public class StoreQuestionAnswerManageServiceImpl implements StoreQuestionAnswerManageService {
    private Log logger = Log.getInstance(StoreQuestionAnswerManageServiceImpl.class);
    @Resource
    private StoreQuestionAnswerManageDao storeQuestionAnswerManageDao;
    /**
     * 查询 问题类型
     * @param storeQuestionAnswerRequest
     * @return
     */
    @Override
    public Response findAllStoreQuestionAnswerTpye(StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest) {
        List<StoreQuestionTypeResponse> list = storeQuestionAnswerManageDao.findAllStoreQuestionAnswerTpye(storeQuestionAnswerRequest);
        SimplePage<StoreQuestionTypeResponse> pageInfo= new SimplePage<>(list);
        return Response.success(pageInfo);
    }

    /**
     * 查询问题类型总数量
     * @return
     */
    @Override
    public Response findQuestionTypeSize() {
        Integer size = storeQuestionAnswerManageDao.findQuestionTypeSize();
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
        YjjQuestionType yjjQuestionType = storeQuestionAnswerManageDao.findQuestionTypeOneBySort(sort);
        if (yjjQuestionType!=null){
            logger.warn("该序号已存在sort={}",sort);
            return Response.errorMsg("该序号已存在");
        }
        YjjQuestionType questionTypeOneByName = storeQuestionAnswerManageDao.findQuestionTypeOneByName(name);
        if (questionTypeOneByName!=null){
            logger.warn("该名称已存在name={}",name);
            return Response.errorMsg("该名称已存在");
        }
        Integer inset = storeQuestionAnswerManageDao.addQuestionType(name,sort);
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
        YjjQuestionType yjjQuestionType = storeQuestionAnswerManageDao.findQuestionTypeOneBySort(sort);
        if (yjjQuestionType!=null&&!yjjQuestionType.getId().equals(id)){
            logger.warn("该序号已存在sort={}",sort);
            return Response.errorMsg("该序号已存在");
        }
        YjjQuestionType questionTypeOneByName = storeQuestionAnswerManageDao.findQuestionTypeOneByName(name);
        if (questionTypeOneByName!=null&&!questionTypeOneByName.getId().equals(id)){
            logger.warn("该名称已存在name={}",name);
            return Response.errorMsg("该名称已存在");
        }
        Integer up = storeQuestionAnswerManageDao.upQuestionType(id,name,sort);
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
        YjjQuestionType yjjQuestionType =  storeQuestionAnswerManageDao.findQuestionTypeById(exchangeId);
        YjjQuestionType ex =  storeQuestionAnswerManageDao.findQuestionTypeById(id);
        if (yjjQuestionType == null){
            logger.warn("转移的问题类型不存在");
            return  Response.errorMsg("转移的问题类型不存在");
        }
        storeQuestionAnswerManageDao.updateQuestionSize(exchangeId, QuestionSizeUpDownEnums.UP_SIZE.getCode(),ex.getQuestionSize());
        storeQuestionAnswerManageDao.updateQuestionSize(id,QuestionSizeUpDownEnums.DOWN_SIZE.getCode(),ex.getQuestionSize());

        storeQuestionAnswerManageDao.exchangeQuestionType(id,exchangeId);

        return Response.success();
    }

    /**
     * 查询问题
     * @return
     */
    @Override
    public Response findAllStoreQuestionAnswer(StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest) {
        logger.info("问题查询={}",storeQuestionAnswerRequest);
       List<YjjQuestionAnswerResponse> yjjQuestionAnswerResponse =  storeQuestionAnswerManageDao.findAllStoreQuestionAnswer(storeQuestionAnswerRequest);
        SimplePage<YjjQuestionAnswerResponse> simplePage = new SimplePage<>(yjjQuestionAnswerResponse);
       return Response.success(simplePage);
    }
    /**
     * 新建问题
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response addStoreQuestion(StoreQuestionInsertRequest request) {
        Map map = AnnotationUtils.validate(request);
        if (map.get("result")==Boolean.FALSE){
            return Response.error(map.get("message"));
        }
        YjjQuestionAnswer yjjQuestionAnswer = storeQuestionAnswerManageDao.findQuestionOneBySort(request.getSort());
        if (yjjQuestionAnswer!=null&&!yjjQuestionAnswer.getId().equals(request.getId())){
            logger.warn("该序号已存在yjjQuestionAnswer.getId={},request.getId={}",yjjQuestionAnswer.getId(),request.getId());
            return Response.errorMsg("该序号已存在");
        }
        Integer indert = storeQuestionAnswerManageDao.addStoreQuestion(request);
        if (indert==0){
            logger.warn("新建问题失败");
            return Response.errorMsg("新建问题失败");
        }
        //关联问题数量更新 增加
        storeQuestionAnswerManageDao.updateQuestionSize(request.getQuestionTypeId(), QuestionSizeUpDownEnums.UP_SIZE.getCode(),QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());

        return Response.success();
    }
    /**
     * 修改问题
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response updateStoreQuestion(StoreQuestionInsertRequest request) {
        Map map = AnnotationUtils.validate(request);
        if (map.get("result")==Boolean.FALSE){
            return Response.error(map.get("message"));
        }
        YjjQuestionAnswer yjjQuestionAnswer = storeQuestionAnswerManageDao.findQuestionOneBySort(request.getSort());
        if (yjjQuestionAnswer!=null&&!yjjQuestionAnswer.getId().equals(request.getId())){
            logger.warn("该序号已存在");
            return Response.errorMsg("该序号已存在");
        }
        YjjQuestionType yjjQuestionType= storeQuestionAnswerManageDao.findQuestionTypeById(yjjQuestionAnswer.getQuestionTypeId());

        Integer indert = storeQuestionAnswerManageDao.updateStoreQuestion(request);
        if (indert==0){
            logger.warn("修改问题失败");
            return Response.errorMsg("修改问题失败");
        }

        if (yjjQuestionType!=null&&!yjjQuestionType.getId().equals(request.getQuestionTypeId())){
            logger.info("类型修改yjjQuestionType.getId()={},request.getQuestionTypeId()={}",yjjQuestionType.getId(),request.getQuestionTypeId());
            //关联问题数量更新 增加
            storeQuestionAnswerManageDao.updateQuestionSize(request.getQuestionTypeId(), QuestionSizeUpDownEnums.UP_SIZE.getCode(),QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());
            //关联问题数量更新 减少
            storeQuestionAnswerManageDao.updateQuestionSize(yjjQuestionType.getId(), QuestionSizeUpDownEnums.DOWN_SIZE.getCode(),QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());

        }

        return Response.success();
    }


    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    @Override
    public Response findOneStoreQuestionAnswer(Long id) {
        logger.info("根据问题id={}查询单个问题",id);
        YjjQuestionAnswer yjjQuestionAnswer = storeQuestionAnswerManageDao.findOneStoreQuestionAnswer(id);
        return Response.success(yjjQuestionAnswer);
    }

    /**
     * 获取问题类型下拉选
     * @return
     */
    @Override
    public Response findTypeSelection() {
        logger.info("获取问题下拉选");
        List<YjjQuestionType> yjjQuestionTypes = storeQuestionAnswerManageDao.findSelectionShopQuestionAnswerTpye();
        return Response.success(yjjQuestionTypes);
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
        storeQuestionAnswerManageDao.batchDisplayOrHiding(list,showNot);
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
        List<YjjQuestionAnswer> yjjQuestionAnswerList = storeQuestionAnswerManageDao.findQuestionAnswerByIdList(list);
        for (YjjQuestionAnswer yjjQuestionAnswer : yjjQuestionAnswerList){
            if (!yjjQuestionAnswer.getDelStatus().equals(delNot)){
                logger.info("关联问题操作yjjQuestionAnswer.getQuestionTypeId()={}",yjjQuestionAnswer.getQuestionTypeId());
                storeQuestionAnswerManageDao.updateQuestionSize(yjjQuestionAnswer.getQuestionTypeId(), delNot,QuestionSizeUpDownEnums.DEFAULT_OPERATE_SIZE.getCode());
            }
        }
        storeQuestionAnswerManageDao.batchDelNot(list,delNot);



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
        storeQuestionAnswerManageDao.typeDel(id);
        return Response.success();
    }
}
