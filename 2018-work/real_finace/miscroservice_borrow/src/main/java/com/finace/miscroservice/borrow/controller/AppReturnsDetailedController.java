package com.finace.miscroservice.borrow.controller;

import com.finace.miscroservice.borrow.service.ReturnsDetailedService;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收益明细Controller
 */
@RestController
public class AppReturnsDetailedController extends BaseController {
    private Log logger = Log.getInstance(AppReturnsDetailedController.class);
    @Autowired
    private ReturnsDetailedService returnsDetailedService;


    /**
     * 我的收益
     * @return
     */
    @RequestMapping("returns/detailed/auth")
    public Response showReturnsDetailed(){
        String userId = getUserId();//用户id
//        String userId = "60089";//用户id
        if (userId==null){
            logger.warn("用户id为空");
            return Response.errorMsg("用户id为空");
        }
        return returnsDetailedService.findReturnsDetailed(Integer.parseInt(userId));
    }
}
