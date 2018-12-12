package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.po.AccountCashPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 */
@Mapper
public interface AccountCashMapper {


    /**
     *
     * @param id
     * @return
     */
   AccountCashPO getAccountCashById(@Param("id") int id);

    /**
     *
     * @param accountCashPO
     */
   int  addAccountCash(AccountCashPO accountCashPO);

    /**
     *
     * @param accountCashPO
     */
   int updateAccountCash(AccountCashPO accountCashPO);

}
