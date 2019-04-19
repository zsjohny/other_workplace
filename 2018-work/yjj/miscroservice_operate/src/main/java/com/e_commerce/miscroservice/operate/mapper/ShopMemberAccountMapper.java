package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.distribution.OperUserDstbRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/20 16:03
 * @Copyright 玖远网络
 */
@Mapper
public interface ShopMemberAccountMapper{

    List<Long> findUserIds(OperUserDstbRequest query);

    Map<String, BigDecimal> satisticsByUserId(@Param ("userIds") List<Long> userIds);
}
