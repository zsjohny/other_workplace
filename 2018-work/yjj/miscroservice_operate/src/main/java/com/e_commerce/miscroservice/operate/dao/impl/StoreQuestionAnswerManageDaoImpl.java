package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionType;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.entity.response.YjjQuestionAnswerResponse;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.StoreQuestionAnswerManageDao;
import com.e_commerce.miscroservice.operate.entity.response.StoreQuestionTypeResponse;
import com.e_commerce.miscroservice.operate.mapper.StoreQuestionAnswerManageMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 16:52
 * @Copyright 玖远网络
 */
@Repository
public class StoreQuestionAnswerManageDaoImpl implements StoreQuestionAnswerManageDao {

    @Resource
    private StoreQuestionAnswerManageMapper storeQuestionAnswerManageMapper;
    /**
     * 查询 问题类型
     * @param storeQuestionAnswerRequest
     * @return
     */
    @Override
    public List<StoreQuestionTypeResponse> findAllStoreQuestionAnswerTpye(StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest) {
        Integer page = storeQuestionAnswerRequest.getPageNumber();
        Integer size = storeQuestionAnswerRequest.getPageSize();
        PageHelper.startPage(page,size);
        List<StoreQuestionTypeResponse> list = storeQuestionAnswerManageMapper.findAllStoreQuestionAnswerTpye(storeQuestionAnswerRequest);
        return list;
    }


    @Override
    public Integer findQuestionTypeSize() {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionType.class);
        Long size = MybatisOperaterUtil.getInstance().count(mybatisSqlWhereBuild);
        return size.intValue();
    }

    @Override
    public Integer addQuestionType(String name, Integer sort) {
        YjjQuestionType yjjQuestionType = new YjjQuestionType();
        yjjQuestionType.setTypeValue(name);
        yjjQuestionType.setSort(sort);
        Integer insert = MybatisOperaterUtil.getInstance().save(yjjQuestionType);
        return insert;
    }
    /**
     * 根据序号查询问题类型
     * @param sort
     * @return
     */
    @Override
    public YjjQuestionType findQuestionTypeOneBySort(Integer sort) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionType.class);
        mybatisSqlWhereBuild.eq(YjjQuestionType::getSort,sort);
        mybatisSqlWhereBuild.eq(YjjQuestionType::getDelStatus,StateEnum.NORMAL);
        YjjQuestionType size = MybatisOperaterUtil.getInstance().findOne(new YjjQuestionType(),mybatisSqlWhereBuild);
        return size;
    }
    /**
     * 编辑问题类型
     * @param id
     * @param name
     * @param sort
     * @return
     */
    @Override
    public Integer upQuestionType(Long id, String name, Integer sort) {
        YjjQuestionType size = new YjjQuestionType();
        size.setSort(sort);
        size.setTypeValue(name);
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionType.class);
        mybatisSqlWhereBuild.eq(YjjQuestionType::getId,id);
        Integer up = MybatisOperaterUtil.getInstance().update(size,mybatisSqlWhereBuild);
        return up;
    }

    @Override
    public Integer upQuestionTypeName(Long id, String name) {
        YjjQuestionType size = new YjjQuestionType();
        size.setTypeValue(name);
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjQuestionType.class);
        mybatisSqlWhereBuild.eq(YjjQuestionType::getId,id);
        Integer up = MybatisOperaterUtil.getInstance().update(size,mybatisSqlWhereBuild);
        return up;
    }
    /**
     * 问题 查询
     * @param storeQuestionAnswerRequest
     * @return
     */
    @Override
    public List<YjjQuestionAnswerResponse> findAllStoreQuestionAnswer(StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest) {
        PageHelper.startPage(storeQuestionAnswerRequest.getPageNumber(),storeQuestionAnswerRequest.getPageSize());
        List<YjjQuestionAnswerResponse>  li =  storeQuestionAnswerManageMapper.findAllStoreQuestionAnswer(storeQuestionAnswerRequest);
        return li;
    }

    @Override
    public Integer addStoreQuestion(StoreQuestionInsertRequest request) {
        YjjQuestionAnswer yjjQuestionAnswer = new YjjQuestionAnswer();
        yjjQuestionAnswer.setQuestion(request.getQuestion());
        yjjQuestionAnswer.setAnswer(request.getAnswer());
        yjjQuestionAnswer.setQuestionTypeId(request.getQuestionTypeId());
        yjjQuestionAnswer.setSort(request.getSort());
        Integer size = MybatisOperaterUtil.getInstance().save(yjjQuestionAnswer);
        return size;
    }

    /**
     * 根据序号查询问题
     * @param sort
     * @return
     */
    @Override
    public YjjQuestionAnswer findQuestionOneBySort(Integer sort) {

        YjjQuestionAnswer yjjQuestionAnswer = MybatisOperaterUtil.getInstance().findOne(new YjjQuestionAnswer(),new MybatisSqlWhereBuild(YjjQuestionAnswer.class).eq(YjjQuestionAnswer::getSort,sort).eq(YjjQuestionAnswer::getDelStatus
                ,StateEnum.NORMAL));
        return yjjQuestionAnswer;
    }
    /**
     * 修改问题
     * @param request
     * @return
     */
    @Override
    public Integer updateStoreQuestion(StoreQuestionInsertRequest request) {
        YjjQuestionAnswer yjjQuestionAnswer = new YjjQuestionAnswer();
        yjjQuestionAnswer.setQuestion(request.getQuestion());
        yjjQuestionAnswer.setAnswer(request.getAnswer());
        yjjQuestionAnswer.setQuestionTypeId(request.getQuestionTypeId());
        yjjQuestionAnswer.setSort(request.getSort());
        Integer size = MybatisOperaterUtil.getInstance().update(yjjQuestionAnswer,new MybatisSqlWhereBuild(YjjQuestionAnswer.class).eq(YjjQuestionAnswer::getId,request.getId()));
        return size;
    }


    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    @Override
    public YjjQuestionAnswer findOneStoreQuestionAnswer(Long id) {
        YjjQuestionAnswer response = MybatisOperaterUtil.getInstance().findOne(new YjjQuestionAnswer(),new MybatisSqlWhereBuild(YjjQuestionAnswer.class).eq(YjjQuestionAnswer::getId,id));

        return response;
    }

    /**
     * 获取问题类型下拉选
     * @return
     */
    @Override
    public List<YjjQuestionType> findSelectionShopQuestionAnswerTpye() {
        List<YjjQuestionType> response = MybatisOperaterUtil.getInstance().finAll(new YjjQuestionType(),new MybatisSqlWhereBuild(YjjQuestionType.class).eq(YjjQuestionType::getDelStatus, StateEnum.NORMAL));

        return response;
    }

    /**
     * 批量显示隐藏
     * @param strings 需要批量显示或者隐藏的 id 集合
     * @param showNot
     * @return
     */
    @Override
    public void batchDisplayOrHiding( List<String> strings, Integer showNot) {
        storeQuestionAnswerManageMapper.batchDisplayOrHiding(strings,showNot);
    }

    /**
     * 批量 删除
     * @param list 需要批量显示或者隐藏的 id 集合
     * @param delNot 1 删除 0 正常
     * @return
     */
    @Override
    public void batchDelNot(List<String> list, Integer delNot) {
        storeQuestionAnswerManageMapper.batchDelNot(list,delNot);
    }

    @Override
    public void typeDel(Long id) {
        YjjQuestionType shopQuestionType = new YjjQuestionType();
        shopQuestionType.setDelStatus(StateEnum.DELETE);
        MybatisOperaterUtil.getInstance().update(shopQuestionType,new MybatisSqlWhereBuild(YjjQuestionType.class).eq(YjjQuestionType::getId,id));
    }

    @Override
    public YjjQuestionType findQuestionTypeById(Long id) {
        YjjQuestionType yjjQuestionType =  MybatisOperaterUtil.getInstance().findOne(new YjjQuestionType(),new MybatisSqlWhereBuild(YjjQuestionType.class).eq(YjjQuestionType::getId,id).eq(YjjQuestionType::getDelStatus,StateEnum.NORMAL));

        return yjjQuestionType;
    }

    /**
     * 根据问题名称查询
     * @param name
     * @return
     */
    @Override
    public YjjQuestionType findQuestionTypeOneByName(String name) {
        YjjQuestionType yjjQuestionType =  MybatisOperaterUtil.getInstance().findOne(new YjjQuestionType(),new MybatisSqlWhereBuild(YjjQuestionType.class).eq(YjjQuestionType::getTypeValue,name).eq(YjjQuestionType::getDelStatus,StateEnum.NORMAL));

        return yjjQuestionType;
    }

    /**
     * 转移问题类型
     * @param id
     * @param exchangeId
     */
    @Override
    public void exchangeQuestionType(Long id, Long exchangeId) {
        storeQuestionAnswerManageMapper.exchangeQuestionType(id,exchangeId);
    }

    /**
     * 更新关联问题 数量
     *
     * @param id
     * @param code
     * @param questionSize
     */
    @Override
    public void updateQuestionSize(Long id, Integer code, Integer questionSize) {
        storeQuestionAnswerManageMapper.updateQuestionSize(id,code,questionSize);
    }

    /**
     * 批量关联问题修改
     *
     * @param list
     * @param code
     */
    @Override
    public void updateQuestionSizeList(List<String> list, Integer code) {
        storeQuestionAnswerManageMapper.updateQuestionSizeList(list,code);
    }
    /**
     * 根据问题id 集合查询问题
     * @param list
     * @return
     */
    @Override
    public List<YjjQuestionAnswer> findQuestionAnswerByIdList(List<String> list) {
        return storeQuestionAnswerManageMapper.findQuestionAnswerByIdList(list);
    }

    /**
     * 根据问题id集合获取 类型id集合
     * @param list
     * @return
     */
    @Override
    public List<String> findQuestionTypeIdByIdList(List<String> list) {
        return storeQuestionAnswerManageMapper.findQuestionTypeIdByIdList(list);
    }
}
