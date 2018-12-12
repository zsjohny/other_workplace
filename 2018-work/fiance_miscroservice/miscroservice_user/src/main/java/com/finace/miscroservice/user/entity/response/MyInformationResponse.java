package com.finace.miscroservice.user.entity.response;

import com.finace.miscroservice.commons.utils.tools.TextUtil;
import com.finace.miscroservice.user.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

@ToString
public class MyInformationResponse extends BaseEntity {
    private String phone;//手机号
    private String realName;//真实姓名
    private String idCard;//身份证
    private String bankName;//开户行
    private String bankCard;//银行卡号

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = TextUtil.hidePhoneNo(phone);
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName =TextUtil.hideRealnameChar(realName);
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = TextUtil.hideCardNo(idCard);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = TextUtil.hideBankCard(bankCard);
    }
}
