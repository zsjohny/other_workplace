package com.finace.miscroservice.user.entity.response;

import com.finace.miscroservice.commons.enums.TxCodeEnum;
import lombok.Data;

import static com.finace.miscroservice.commons.enums.TxCodeEnum.DIRECT_RECHARGE_PAGE;

@Data
public class OpenAccountLogResponse {
    private String txCode;
    private String createTime;
    private String availBal; //可用余额
    private String txMoney; //操作金额
    private Integer isSuccess;

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        Integer code = 0;
        switch (txCode){
            case "directRechargePage":
                code=1;
                break;
            case "withdraw":
                code=4;
                break;
            case "tender":
                code=2;
                break;
            case "unfreeze":
                code=3;
                break;
            case "loan_hb":
                code=5;
                break;
            default:
                code=0;
                break;
        }
        this.txCode = String.valueOf(code);
    }

}
