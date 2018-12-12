package com.finace.miscroservice.user.po;

/**
 * 资金日志实体类
 */
public class AccountLogPO {

    private int id;
    private int userId;
    private String type;
    private double total;
    private double money;
    private double useMoney;
    private double noUseMoney;
    private double collection;
    private int toUser;
    private String remark;
    private String addtime;
    private String addip;
    private String toUserName;
    private String typeName;
    private String seqNo;  //交易流水号
    private double sunMoney;//累计利息(推荐人用)


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return userId;
    }

    public void setUser_id(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


    public double getCollection() {
        return collection;
    }

    public void setCollection(double collection) {
        this.collection = collection;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public double getSunMoney() {
        return sunMoney;
    }

    public void setSunMoney(double sunMoney) {
        this.sunMoney = sunMoney;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(double useMoney) {
        this.useMoney = useMoney;
    }

    public double getNoUseMoney() {
        return noUseMoney;
    }

    public void setNoUseMoney(double noUseMoney) {
        this.noUseMoney = noUseMoney;
    }

    public int getToUser() {
        return toUser;
    }

    public void setToUser(int toUser) {
        this.toUser = toUser;
    }
}
