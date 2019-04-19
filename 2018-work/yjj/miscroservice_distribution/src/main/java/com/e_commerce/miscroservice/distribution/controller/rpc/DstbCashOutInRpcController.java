package com.e_commerce.miscroservice.distribution.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.distribution.service.ShopMemberAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/30 20:55
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "dstb/rpc/cashOutIn" )
public class DstbCashOutInRpcController{

    @Autowired
    private ShopMemberAccountService shopMemberAccountService;

    /**
     * 提现审核
     *
     * @param cashOutId 流水id
     * @param isPass 1 通过,0 拒绝提现, 2再次提现
     * @param ipAddress ip
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/30 20:43
     */
    @RequestMapping( "cashOutAudit" )
    public Response cashOutAudit(Long cashOutId,
                                 @RequestParam(value = "isPass", defaultValue = "0") Integer isPass,
                                 String ipAddress) {
        try {
            shopMemberAccountService.cashOutAudit (cashOutId, isPass, ipAddress);
            return Response.success ();
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler (e);
        }
    }

}
