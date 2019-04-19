package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionType;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionAnswerResponse;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionTypeResponse;

import java.util.List;

/**
 * Create by hyf on 2018/11/23
 */
public interface ShopQuestionAnswerManageDao {
    /**
     * 查询 问题类型
     * @param shopQuestionAnswerTypeRequest
     * @return
     */
    List<ShopQuestionTypeResponse> findAllShopQuestionAnswerTpye(ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest);
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
    ShopQuestionType findQuestionTypeOneBySort(Integer sort);

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
     * @param shopQuestionAnswerTypeRequest
     * @return
     */
    List<ShopQuestionAnswerResponse> findAllShopQuestionAnswer(ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest);

    /**
     * 添加问题
     * @param request
     * @return
     */
    Integer addShopQuestion(ShopQuestionInsertRequest request);

    /**
     * 根据序号查询问题
     * @param sort
     * @return
     */
    ShopQuestionAnswer findQuestionOneBySort(Integer sort);

    /**
     * 修改问题
     * @param request
     * @return
     */
    Integer updateShopQuestion(ShopQuestionInsertRequest request);

    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    ShopQuestionAnswer findOneShopQuestionAnswer(Long id);

    /**
     * 获取问题类型下拉选
     * @return
     */
    List<ShopQuestionType> findSelectionShopQuestionAnswerTpye();
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
     * 根据id查找类型
     * @param exchangeId
     * @return
     */
    ShopQuestionType findQuestionTypeById(Long exchangeId);

    /**
     * 问题名称查询
     * @param name
     * @return
     */
    ShopQuestionType findQuestionTypeOneByName(String name);

    /**
     * 转换问题类型
     * @param id
     * @param exchangeId
     */
    void exchangeQuestionType(Long id, Long exchangeId);

    /**
     * 关联问题数量
     * @param questionTypeId
     */
    void updateQuestionSize(Long questionTypeId,Integer code,Integer size);

    /**
     * 批量 关联问题数量修改
     * @param list
     * @param code
     */
    void updateQuestionSizeList(List<String> list, Integer code);

    /**
     * 根据问题id 集合查询问题
     * @param list
     * @return
     */
    List<ShopQuestionAnswer> findQuestionAnswerByIdList(List<String> list);
}
