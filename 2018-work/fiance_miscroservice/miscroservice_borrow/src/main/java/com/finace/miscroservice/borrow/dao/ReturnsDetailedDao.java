package com.finace.miscroservice.borrow.dao;

import com.finace.miscroservice.borrow.entity.response.HMoneyResponse;
import com.finace.miscroservice.borrow.entity.response.ReturnsDetailedResponse;
import com.finace.miscroservice.commons.entity.User;

public interface ReturnsDetailedDao {

    /**
     * 查询所有 回款信息
     * @param userId
     * @return
     */
    ReturnsDetailedResponse findAmtBack(Integer userId);

    /**
     * 查询所有待回款信息
     * @param userId
     * @return
     */
    ReturnsDetailedResponse findWaitBack(Integer userId);

    /**
     * 根据用户Id查询 用户
     * @param userId
     * @return
     */
    User findUserByUserId(Integer userId);

    /**
     * 查询汇付 用户的 投资部分信息--本 息
     * @param userId
     * @return
     */
    HMoneyResponse findHuifu(Integer userId);

    //累计投资总额
    Double findAmtInvestmentByUserId(Integer userId);
}
