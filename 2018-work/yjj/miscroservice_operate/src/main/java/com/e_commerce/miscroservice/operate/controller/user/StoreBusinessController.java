package com.e_commerce.miscroservice.operate.controller.user;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.service.user.StoreBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/14 13:48
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/store/account")
public class StoreBusinessController {

    @Autowired
    private StoreBusinessService storeAccountRecharge;

    /**
     * 充值
     * @param userId 会员id
     * @param money
     * @return
     */
    @RequestMapping("recharge")
    public Response storeAccountRecharge(Long userId,Double money){
        return storeAccountRecharge.storeAccountRecharge(userId,money);
    }
}
