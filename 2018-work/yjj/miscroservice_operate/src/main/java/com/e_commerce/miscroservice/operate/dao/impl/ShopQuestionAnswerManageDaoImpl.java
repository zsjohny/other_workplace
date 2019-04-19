package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionType;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.ShopQuestionAnswerManageDao;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionAnswerResponse;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionTypeResponse;
import com.e_commerce.miscroservice.operate.mapper.ShopQuestionAnswerManageMapper;
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
public class ShopQuestionAnswerManageDaoImpl implements ShopQuestionAnswerManageDao {

    @Resource
    private ShopQuestionAnswerManageMapper shopQuestionAnswerManageMapper;
    /**
     * 查询 问题类型
     * @param shopQuestionAnswerTypeRequest
     * @return
     */
    @Override
    public List<ShopQuestionTypeResponse> findAllShopQuestionAnswerTpye(ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest) {
        Integer page = shopQuestionAnswerTypeRequest.getPageNumber();
        Integer size = shopQuestionAnswerTypeRequest.getPageSize();
        PageHelper.startPage(page,size);
        List<ShopQuestionTypeResponse> list = shopQuestionAnswerManageMapper.findAllShopQuestionAnswerTpye(shopQuestionAnswerTypeRequest);
        return list;
    }


    @Override
    public Integer findQuestionTypeSize() {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionType.class);
        Long size = MybatisOperaterUtil.getInstance().count(mybatisSqlWhereBuild);
        return size.intValue();
    }

    @Override
    public Integer addQuestionType(String name, Integer sort) {
        ShopQuestionType shopQuestionType = new ShopQuestionType();
        shopQuestionType.setTypeValue(name);
        shopQuestionType.setSort(sort);
        Integer insert = MybatisOperaterUtil.getInstance().save(shopQuestionType);
        return insert;
    }
    /**
     * 根据序号查询问题类型
     * @param sort
     * @return
     */
    @Override
    public ShopQuestionType findQuestionTypeOneBySort(Integer sort) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionType.class);
        mybatisSqlWhereBuild.eq(ShopQuestionType::getSort,sort);
        mybatisSqlWhereBuild.eq(ShopQuestionType::getDelStatus,StateEnum.NORMAL);
        ShopQuestionType size = MybatisOperaterUtil.getInstance().findOne(new ShopQuestionType(),mybatisSqlWhereBuild);
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
        ShopQuestionType size = new ShopQuestionType();
        size.setSort(sort);
        size.setTypeValue(name);
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionType.class);
        mybatisSqlWhereBuild.eq(ShopQuestionType::getId,id);
        Integer up = MybatisOperaterUtil.getInstance().update(size,mybatisSqlWhereBuild);
        return up;
    }

    @Override
    public Integer upQuestionTypeName(Long id, String name) {
        ShopQuestionType size = new ShopQuestionType();
        size.setTypeValue(name);
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(ShopQuestionType.class);
        mybatisSqlWhereBuild.eq(ShopQuestionType::getId,id);
        Integer up = MybatisOperaterUtil.getInstance().update(size,mybatisSqlWhereBuild);
        return up;
    }
    /**
     * 问题 查询
     * @param shopQuestionAnswerTypeRequest
     * @return
     */
    @Override
    public List<ShopQuestionAnswerResponse> findAllShopQuestionAnswer(ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest) {
        PageHelper.startPage(shopQuestionAnswerTypeRequest.getPageNumber(),shopQuestionAnswerTypeRequest.getPageSize());
        List<ShopQuestionAnswerResponse>  list =  shopQuestionAnswerManageMapper.findAllShopQuestionAnswer(shopQuestionAnswerTypeRequest);
        return list;
    }

    @Override
    public Integer addShopQuestion(ShopQuestionInsertRequest request) {
        ShopQuestionAnswer yjjQuestionAnswer = new ShopQuestionAnswer();
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
    public ShopQuestionAnswer findQuestionOneBySort(Integer sort) {

        ShopQuestionAnswer shopQuestionAnswer = MybatisOperaterUtil.getInstance().findOne(new ShopQuestionAnswer(),new MybatisSqlWhereBuild(ShopQuestionAnswer.class).eq(ShopQuestionAnswer::getSort,sort).eq(ShopQuestionAnswer::getDelStatus,StateEnum.NORMAL));
        return shopQuestionAnswer;
    }
    /**
     * 修改问题
     * @param request
     * @return
     */
    @Override
    public Integer updateShopQuestion(ShopQuestionInsertRequest request) {
        ShopQuestionAnswer shopQuestionAnswer = new ShopQuestionAnswer();
        shopQuestionAnswer.setQuestion(request.getQuestion());
        shopQuestionAnswer.setAnswer(request.getAnswer());
        shopQuestionAnswer.setQuestionTypeId(request.getQuestionTypeId());
        shopQuestionAnswer.setSort(request.getSort());
        Integer size = MybatisOperaterUtil.getInstance().update(shopQuestionAnswer,new MybatisSqlWhereBuild(ShopQuestionAnswer.class).eq(ShopQuestionAnswer::getId,request.getId()));
        return size;
    }

    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    @Override
    public ShopQuestionAnswer findOneShopQuestionAnswer(Long id) {
        ShopQuestionAnswer response = MybatisOperaterUtil.getInstance().findOne(new ShopQuestionAnswer(),new MybatisSqlWhereBuild(ShopQuestionAnswer.class).eq(ShopQuestionAnswer::getId,id));

        return response;
    }

    /**
     * 获取问题类型下拉选
     * @return
     */
    @Override
    public List<ShopQuestionType> findSelectionShopQuestionAnswerTpye() {
        List<ShopQuestionType> response = MybatisOperaterUtil.getInstance().finAll(new ShopQuestionType(),new MybatisSqlWhereBuild(ShopQuestionType.class).eq(ShopQuestionType::getDelStatus, StateEnum.NORMAL));

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
        shopQuestionAnswerManageMapper.batchDisplayOrHiding(strings,showNot);
    }

    /**
     * 批量 删除
     * @param list 需要批量显示或者隐藏的 id 集合
     * @param delNot 1 删除 0 正常
     * @return
     */
    @Override
    public void batchDelNot(List<String> list, Integer delNot) {
        shopQuestionAnswerManageMapper.batchDelNot(list,delNot);

    }

    @Override
    public void typeDel(Long id) {
        ShopQuestionType shopQuestionType = new ShopQuestionType();
        shopQuestionType.setDelStatus(StateEnum.DELETE);
        MybatisOperaterUtil.getInstance().update(shopQuestionType,new MybatisSqlWhereBuild(ShopQuestionType.class).eq(ShopQuestionType::getId,id));
    }

    /**
     * 根据id查找类型
     *
     * @param id
     * @return
     */
    @Override
    public ShopQuestionType findQuestionTypeById(Long id) {
        ShopQuestionType shopQuestionType =  MybatisOperaterUtil.getInstance().findOne(new ShopQuestionType(),new MybatisSqlWhereBuild(ShopQuestionType.class).eq(ShopQuestionType::getId,id).eq(ShopQuestionType::getDelStatus,StateEnum.NORMAL));

        return shopQuestionType;
    }

    /**
     * 问题名称查询
     * @param name
     * @return
     */
    @Override
    public ShopQuestionType findQuestionTypeOneByName(String name) {
        ShopQuestionType shopQuestionType =  MybatisOperaterUtil.getInstance().findOne(new ShopQuestionType(),new MybatisSqlWhereBuild(ShopQuestionType.class).eq(ShopQuestionType::getTypeValue,name).eq(ShopQuestionType::getDelStatus,StateEnum.NORMAL));

        return shopQuestionType;
    }

    @Override
    public void exchangeQuestionType(Long id, Long exchangeId) {
        shopQuestionAnswerManageMapper.exchangeQuestionType(id,exchangeId);
    }
    /**
     * 关联问题数量
     * @param questionTypeId
     */
    @Override
    public void updateQuestionSize(Long questionTypeId, Integer code,Integer size) {
        shopQuestionAnswerManageMapper.updateQuestionSize(questionTypeId,code,size);
    }
    /**
     * 批量 关联问题数量修改
     * @param list
     * @param code
     */
    @Override
    public void updateQuestionSizeList(List<String> list, Integer code) {
        shopQuestionAnswerManageMapper.updateQuestionSizeList(list,code);
    }

    /**
     * 根据问题id 集合查询问题
     *
     * @param list
     * @return
     */
    @Override
    public List<ShopQuestionAnswer> findQuestionAnswerByIdList(List<String> list) {
        return shopQuestionAnswerManageMapper.findQuestionAnswerByIdList(list);
    }

}
