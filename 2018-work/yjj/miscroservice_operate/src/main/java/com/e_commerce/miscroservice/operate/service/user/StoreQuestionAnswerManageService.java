package com.e_commerce.miscroservice.operate.service.user;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionInsertRequest;

/**
 * Create by hyf on 2018/11/23
 */
public interface StoreQuestionAnswerManageService {
    /**
     * 查询 问题类型
     * @param storeQuestionAnswerRequest
     * @return
     */
    Response findAllStoreQuestionAnswerTpye(StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest);

    /**
     * 查询问题类型总数量
     * @return
     */
    Response findQuestionTypeSize();

    /**
     * 新建问题类型
     * @return
     */
    Response addQuestionType(String name, Integer sort);

    /**
     * 编辑问题类型
     * @param name
     * @param sort
     * @param id
     * @return
     */
    Response upQuestionType(String name, Integer sort, Long id);

    /**
     * 转移问题类型
     * @param exchangeId
     * @param id
     * @return
     */
    Response exchangeQuestionType(Long exchangeId, Long id);


    /**
     * 查询问题
     * @return
     */
    Response findAllStoreQuestionAnswer(StoreQuestionAnswerTypeRequest obj);

    /**
     * 新建问题
     * @param obj
     * @return
     */
    Response addStoreQuestion(StoreQuestionInsertRequest obj);

    /**
     * 修改问题
     * @param obj
     * @return
     */
    Response updateStoreQuestion(StoreQuestionInsertRequest obj);


    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    Response findOneStoreQuestionAnswer(Long id);

    /**
     * 获取问题类型下拉选
     * @return
     */
    Response findTypeSelection();

    /**
     * 批量显示隐藏
     * @param ids 需要批量显示或者隐藏的 id 集合
     * @param showNot
     * @return
     */
    Response batchDisplayOrHiding(String ids, Integer showNot);
    /**
     * 批量 删除
     * @param ids 需要批量显示或者隐藏的 id 集合
     * @param delNot 1 删除 0 正常
     * @return
     */
    Response batchDelNot(String ids, Integer delNot);
    /**
     * 删除 问题类型
     * @param id
     * @return
     */
    Response typeDel(Long id);
}
