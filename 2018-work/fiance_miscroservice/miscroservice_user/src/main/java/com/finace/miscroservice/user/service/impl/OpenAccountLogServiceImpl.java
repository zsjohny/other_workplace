package com.finace.miscroservice.user.service.impl;

import com.finace.miscroservice.commons.enums.TxCodeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.dao.OpenAccountLogDao;
import com.finace.miscroservice.user.entity.response.OpenAccountLogResponse;
import com.finace.miscroservice.user.service.OpenAccountLogService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAccountLogServiceImpl implements OpenAccountLogService {
        Log  logger = Log.getInstance(OpenAccountLogServiceImpl.class);

        @Autowired
        private OpenAccountLogDao openAccountLogDao;
    @Override
    public Response findMoneyFlowingWaterByUserId(String userId,Integer page,Integer code) {
        logger.info("userId ={},page = {}请求 查询资金流水",userId,page);
        String txCode = null;
        switch (code){
            case 0:
                txCode=null;
                break;
            case 1:
                txCode= TxCodeEnum.DIRECT_RECHARGE_PAGE.getCode();
                break;
            case 2:
                txCode=TxCodeEnum.TENDER.getCode();
                break;
            case 3:
                txCode=TxCodeEnum.UNFREEZE.getCode();
                break;
            case 4:
                txCode=TxCodeEnum.WITHDRAW.getCode();
                break;
            default:
                txCode=null;
                break;
        }
        PageHelper.startPage(page,10);
        List<OpenAccountLogResponse> accountLog = openAccountLogDao.findMoneyFlowingWaterByUserId(userId,txCode);
        return Response.success(accountLog);
    }
}
