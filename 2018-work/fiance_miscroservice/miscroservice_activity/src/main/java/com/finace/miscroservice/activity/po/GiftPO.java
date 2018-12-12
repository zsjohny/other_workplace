package com.finace.miscroservice.activity.po;

import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class GiftPO implements Serializable{

    private Integer id;
    private Integer code;
    private String isSend;
    private String userId;
    private String jiangPinName;
    private String addTime;
    private String remark;
    private String underUser;
    private String phone;
    private String money;
    private List<String> send;


    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        if ("0".equals(isSend)){
            isSend="未发送";
        }else {
            isSend="已发送";
        }
        this.isSend = isSend;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = TextUtil.hideUsernameChar(phone);
    }
}
