package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionType;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.entity.response.StoreQuestionTypeResponse;
import com.e_commerce.miscroservice.operate.entity.response.YjjQuestionAnswerResponse;

import java.util.List;

/**
 * Create by hyf on 2018/11/23
 */
public interface StoreQuestionAnswerManageDao {
    /**
     * 查询 问题类型
     * @param storeQuestionAnswerRequest
     * @return
     */
    List<StoreQuestionTypeResponse> findAllStoreQuestionAnswerTpye(StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest);
    /**
     * 查询问题类型总数量
     * @return
     */
    Integer findQuestionTypeSize();

    /**
     * 新建问题类型
     * @return
     */
    Integer addQuestionType(String name, Integer sort);



    /**
     * 根据排序查询问题类型
     * @param sort
     * @return
     */
    YjjQuestionType findQuestionTypeOneBySort(Integer sort);

    /**
     * 编辑问题类型
     * @param id
     * @param name
     * @param sort
     * @return
     */
    Integer upQuestionType(Long id, String name, Integer sort);
    /**
     * 转移问题类型
     * @param name
     * @param id
     * @return
     */
    Integer upQuestionTypeName(Long id, String name);

    /**
     * 问题 查询
     * @param storeQuestionAnswerRequest
     * @return
     */
    List<YjjQuestionAnswerResponse> findAllStoreQuestionAnswer(StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest);

    /**
     * 添加问题
     * @param request
     * @return
     */
    Integer addStoreQuestion(StoreQuestionInsertRequest request);

    /**
     * 根据序号查询问题
     * @param sort
     * @return
     */
    YjjQuestionAnswer findQuestionOneBySort(Integer sort);

    /**
     * 修改问题
     * @param request
     * @return
     */
    Integer updateStoreQuestion(StoreQuestionInsertRequest request);


    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    YjjQuestionAnswer findOneStoreQuestionAnswer(Long id);

    /**
     * 获取问题类型下拉选
     * @return
     */
    List<YjjQuestionType> findSelectionShopQuestionAnswerTpye();
    /**
     * 批量显示隐藏
     * @param strings 需要批量显示或者隐藏的 id 集合
     * @param showNot
     * @return
     */
    void batchDisplayOrHiding( List<String>  strings, Integer showNot);

    /**
     * 批量 删除
     * @param list 需要批量显示或者隐藏的 id 集合
     * @param delNot 1 删除 0 正常
     * @return
     */
    void batchDelNot(List<String> list, Integer delNot);
    /**
     * 删除 问题类型
     * @param id
     * @return
     */
    void typeDel(Long id);

    /**
     * 根据问题id查询
     * @param exchangeId
     * @return
     */
    YjjQuestionType findQuestionTypeById(Long exchangeId);

    /**
     * 根据问题名称查询
     * @param name
     * @return
     */
    YjjQuestionType findQuestionTypeOneByName(String name);

    /**
     * 转移问题类型
     * @param id
     * @param exchangeId
     */
    void exchangeQuestionType(Long id, Long exchangeId);

    /**
     * 更新关联问题 数量
     * @param exchangeId
     * @param code
     * @param questionSize
     */
    void updateQuestionSize(Long exchangeId, Integer code, Integer questionSize);

    /**
     * 批量关联问题修改
     * @param list
     * @param code
     */
    void updateQuestionSizeList(List<String> list, Integer code);

    /**
     * 根据问题id 集合查询问题
     * @param list
     * @return
     */
    List<YjjQuestionAnswer> findQuestionAnswerByIdList(List<String> list);

    /**
     * 根据问题id集合获取 类型id集合
     * @param list
     * @return
     */
    List<String> findQuestionTypeIdByIdList(List<String> list);
}
