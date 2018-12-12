package com.finace.miscroservice.borrow.mapper;

import com.finace.miscroservice.borrow.entity.response.HMoneyResponse;
import com.finace.miscroservice.borrow.entity.response.ReturnsDetailedResponse;
import com.finace.miscroservice.commons.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReturnsDetailedMapper {
    ReturnsDetailedResponse findAmtBack(@Param("userId") Integer userId);

    ReturnsDetailedResponse findWaitBack(@Param("userId") Integer userId);

    User findUserByUserId(@Param("userId") Integer userId);

    HMoneyResponse findHuifu(@Param("userId") Integer userId);

    Double findAmtInvestmentByUserId(@Param("userId") Integer userId);
}
