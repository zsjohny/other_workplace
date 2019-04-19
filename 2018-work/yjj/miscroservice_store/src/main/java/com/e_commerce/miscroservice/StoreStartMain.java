package com.e_commerce.miscroservice;

import com.e_commerce.miscroservice.commons.annotation.colligate.init.Start;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.store.rpc.StoreBusinessAccountRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Start
public class StoreStartMain {

//    @Autowired
//    private StoreBusinessAccountRpcService storeBusinessAccountRpcService;

    public static void main(String[] args) {

        ApplicationContextUtil.run(StoreStartMain.class, Boolean.TRUE, args);
    }
//    @PostConstruct
//    public void te(){
//        ApplicationContextUtil.addTask(
//                new ApplicationContextUtil.Task() {
//                    @Override
//                    public void task() {
//                        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
//                        yjjStoreBusinessAccountLog.setUserId(3L);
//                        storeBusinessAccountRpcService.updateStoreBusinessAccount(yjjStoreBusinessAccountLog);
////                        storeBusinessAccountRpcService.selectStoreBusinessAccount(3L);
//                    }
//                }
//        );

//    }
}
