package com.jiuy.operator.controller.newController.user;

import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.model.user.SupplierCustomerRb;
import com.jiuy.rb.model.user.SupplierCustomerRbQuery;
import com.jiuy.rb.service.user.IUserService;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 用户相关的入口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/29 14:32
 * @Copyright 玖远网络
 */
@Controller
@ResponseBody
@RequestMapping("/admin")
public class UserController {

    @Resource(name = "userService")
    private IUserService userService;

    /**
     * 客户信息
     *
     * @author Aison
     * @date 2018/6/29 14:38
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/customerDetail")
    public ResponseResult customerDetail(SupplierCustomerRbQuery query) {

        return ResponseResult.instance().success(userService.getSupplierCustomer(query));
    }

    /**
     * 获取某个供应商的用户分组
     * @param supplierId supplierId
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/6/29 15:12
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/customerGroup")
    public ResponseResult customerGroup(Long supplierId,Long orderNo) {

        return ResponseResult.instance().success(userService.groupList(supplierId,orderNo));
    }

    /**
     * 更新某个供应上的某个客户的分组
     *
     * @param query query
     * @author Aison
     * @date 2018/6/29 15:37
     * @return com.jiuy.base.util.ResponseResult
     */
    @RequestMapping("/updateStoreGroup")
    public ResponseResult updateStoreGroup(SupplierCustomerRbQuery query) {



        userService.updateStoreGroup(query,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }

}
