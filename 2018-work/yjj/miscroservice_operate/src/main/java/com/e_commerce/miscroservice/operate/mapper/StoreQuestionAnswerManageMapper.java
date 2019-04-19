package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionAnswer;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.response.YjjQuestionAnswerResponse;
import com.e_commerce.miscroservice.operate.entity.response.StoreQuestionTypeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/11/23
 */
@Mapper
public interface StoreQuestionAnswerManageMapper {
    /**
     * 查询 问题类型
     * @param storeQuestionAnswerRequest
     * @return
     */
    List<StoreQuestionTypeResponse> findAllStoreQuestionAnswerTpye(@Param("storeQuestionAnswerRequest") StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest);

    /**
     * 问题 查询
     * @param storeQuestionAnswerRequest
     * @return
     */
    List<YjjQuestionAnswerResponse> findAllStoreQuestionAnswer(@Param("storeQuestionAnswerRequest") StoreQuestionAnswerTypeRequest storeQuestionAnswerRequest);


    /**
     * 批量显示隐藏
     * @param list 需要批量显示或者隐藏的 id 集合
     * @param showNot
     * @return
     */
    void batchDisplayOrHiding(@Param("list")  List<String>  list, @Param("showNot") Integer showNot);
    /**
     * 批量 删除
     * @param list 需要批量显示或者隐藏的 id 集合
     * @param delNot 1 删除 0 正常
     * @return
     */
    void  batchDelNot(@Param("list") List<String> list, @Param("delNot") Integer delNot);
    /**
     * 转移问题类型
     * @param id
     * @param exchangeId
     */
    void exchangeQuestionType(@Param("id") Long id, @Param("exchangeId") Long exchangeId);
    /**
     * 更新关联问题 数量
     *
     * @param id
     * @param code
     * @param questionSize
     */
    void updateQuestionSize(@Param("id") Long id, @Param("code") Integer code, @Param("questionSize") Integer questionSize);
    /**
     * 批量关联问题修改
     *
     * @param list
     * @param code
     */
    void updateQuestionSizeList(@Param("list") List<String> list, @Param("code") Integer code);
    /**
     * 根据问题id 集合查询问题
     * @param list
     * @return
     */
    List<YjjQuestionAnswer> findQuestionAnswerByIdList(@Param("list") List<String> list);
    /**
     * 根据问题id集合获取 类型id集合
     * @param list
     * @return
     */
    List<String> findQuestionTypeIdByIdList(@Param("list") List<String> list);
}
