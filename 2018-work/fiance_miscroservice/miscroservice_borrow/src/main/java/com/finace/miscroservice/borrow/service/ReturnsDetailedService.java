package com.finace.miscroservice.borrow.service;

import com.finace.miscroservice.commons.utils.Response;

public interface ReturnsDetailedService {
    /**
     * 我的收益
     * @return
     */
    Response findReturnsDetailed(Integer userId);
}
