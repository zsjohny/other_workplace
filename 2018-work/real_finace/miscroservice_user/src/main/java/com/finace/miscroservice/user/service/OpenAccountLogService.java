package com.finace.miscroservice.user.service;

import com.finace.miscroservice.commons.utils.Response;

public interface OpenAccountLogService {
    /**
     * 资金流水
     * @param userId
     * @param page
     * @param code
     * @return
     */
    Response findMoneyFlowingWaterByUserId(String userId,Integer page,Integer code);
}
