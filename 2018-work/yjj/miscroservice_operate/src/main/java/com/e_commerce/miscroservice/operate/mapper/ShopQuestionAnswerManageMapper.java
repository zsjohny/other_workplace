package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionAnswerResponse;
import com.e_commerce.miscroservice.operate.entity.response.ShopQuestionTypeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/11/23
 */
@Mapper
public interface ShopQuestionAnswerManageMapper {
    /**
     * 查询 问题类型
     * @param shopQuestionAnswerTypeRequest
     * @return
     */
    List<ShopQuestionTypeResponse> findAllShopQuestionAnswerTpye(@Param("shopQuestionAnswerTypeRequest") ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest);

    /**
     * 问题 查询
     * @param shopQuestionAnswerTypeRequest
     * @return
     */
    List<ShopQuestionAnswerResponse> findAllShopQuestionAnswer(@Param("shopQuestionAnswerTypeRequest") ShopQuestionAnswerTypeRequest shopQuestionAnswerTypeRequest);

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
    void batchDelNot(@Param("list") List<String> list, @Param("delNot") Integer delNot);

    /**
     * 问题类型转换
     * @param id
     * @param exchangeId
     */
    void exchangeQuestionType(@Param("id") Long id, @Param("exchangeId") Long exchangeId);

    /**
     * 关联问题数量
     * @param questionTypeId
     * @param code
     */
    void updateQuestionSize(@Param("questionTypeId") Long questionTypeId, @Param("code") Integer code, @Param("size") Integer size);
    /**
     * 批量 关联问题数量修改
     * @param list
     * @param code
     */
    void updateQuestionSizeList(@Param("list") List<String> list, @Param("code") Integer code);
    /**
     * 根据问题id 集合查询问题
     *
     * @param list
     * @return
     */
    List<ShopQuestionAnswer> findQuestionAnswerByIdList(@Param("list") List<String> list);
}
