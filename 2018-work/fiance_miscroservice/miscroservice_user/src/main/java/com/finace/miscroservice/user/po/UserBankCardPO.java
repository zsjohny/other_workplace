package com.finace.miscroservice.user.po;

/**
 * 用户银行卡信息
 */
public class UserBankCardPO {

    private int id;
    private int userId;
    private String name;
    private String bankCard;
    private String pid;
    private String bankName;
    private String status;// enable,disable
    private String inscd;//银行机构号
    private String protocolno;//协议号
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProtocolno() {
        return protocolno;
    }

    public void setProtocolno(String protocolno) {
        this.protocolno = protocolno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInscd() {
        return inscd;
    }

    public void setInscd(String inscd) {
        this.inscd = inscd;
    }
}
