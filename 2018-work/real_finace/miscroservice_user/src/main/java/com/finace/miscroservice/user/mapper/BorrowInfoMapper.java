package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.po.BorrowerInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BorrowInfoMapper {


    /**
     * 根据用户id获取借款人信息
     * @param userId
     * @return
     */
    BorrowerInfoPO getBorrowerInfoByUserId(@Param("userId") String userId);

    /**
     * 新增借款人信息
     * @param borrowerInfoPO
     * @return
     */
    int addBorrowerInfo(BorrowerInfoPO borrowerInfoPO);



















}
