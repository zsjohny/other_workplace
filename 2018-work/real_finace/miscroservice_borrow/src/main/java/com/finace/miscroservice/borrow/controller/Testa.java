package com.finace.miscroservice.borrow.controller;

import com.finace.miscroservice.borrow.service.FuiouH5PayService;
import com.finace.miscroservice.borrow.service.impl.SaleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Testa {

    @Autowired
    private SaleServiceImpl saleService;

    @GetMapping("test2")
    public void test() {
        FuiouH5PayService.PayCallBackResult payCallBackResult = new FuiouH5PayService.PayCallBackResult();
        payCallBackResult.setResponseCode("0000");
        payCallBackResult.setMchntCd("0003310F0596898");
        payCallBackResult.setMchntOrderId("90448180613155606");
        payCallBackResult.setOrderId("001585937044");
        payCallBackResult.setBankCard("6227000268610059772");
        payCallBackResult.setAmt("3000000");
        saleService.onFuiouPaySuccess(payCallBackResult);

    }
}
