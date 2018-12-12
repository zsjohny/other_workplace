package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.po.AccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

    /**
     * 根据用户id获取账户信息
     * @param userId
     * @return
     */
    AccountPO getAccountByUserId(@Param("userId") String userId);

    /**
     *
     * @param accountPO
     * @return
     */
    int updateAccount(AccountPO accountPO);



}
