package com.e_commerce.miscroservice.operate.service.user;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/14 13:48
 * @Copyright 玖远网络
 */
public interface StoreBusinessService {


    /**
     * 充值
     * @param userId
     * @param money
     * @return
     */
    Response storeAccountRecharge(Long userId, Double money);
}
