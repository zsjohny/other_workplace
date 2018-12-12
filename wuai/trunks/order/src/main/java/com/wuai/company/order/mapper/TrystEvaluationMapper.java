package com.wuai.company.order.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wuai.company.order.entity.TrystEvaluation;
/**
 * Created by zTerry on 2018/3/14.
 * 用户评价
 */
@Mapper
public interface TrystEvaluationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrystEvaluation record);

    int insertSelective(TrystEvaluation record);

    TrystEvaluation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrystEvaluation record);

    int updateByPrimaryKey(TrystEvaluation record);
}