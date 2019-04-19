package com.jiuy.user.servie;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.user.model.OperatorUser;
import com.jiuy.user.model.StoreUser;
import com.jiuy.user.model.SupplierUser;
import com.jiuy.user.model.User;

/**
 * 用户操作的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/8 21:54
 * @Copyright 玖远网络
 */
public interface IUserService {

    /**
     * 添加用户的接口
     *
     * @param user 用户邓庄
     * @param operatorUser 运营平台用户
     * @param supplierUser  供应商用户
     * @param storeUser 门店用户
     * @param  userSession 操作员
     * @author Aison
     * @date 2018/6/8 21:56
     */
    MyLog<Long> addUser(User user, OperatorUser operatorUser, SupplierUser supplierUser, StoreUser  storeUser, UserSession userSession);

}
