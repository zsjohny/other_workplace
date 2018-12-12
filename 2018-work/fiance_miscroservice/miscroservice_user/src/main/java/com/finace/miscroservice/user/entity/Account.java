package com.finace.miscroservice.user.entity;

/**
 * Created by hyf on 2018/3/7.
 */
public class Account extends BaseEntity {

    private int id;

    /**
     * account.user_id
     * 用户名称
     */
    private int userId;

    /**
     * account.hongbao
     *
     */
    private double hongbao;

    /**
     * account.total
     * 资金总额
     */
    private double total;

    /**
     * account.use_money
     *
     */
    private double useMoney;

    /**
     * account.no_use_money
     *
     */
    private double noUseMoney;

    /**
     * account.collection
     *
     */
    private double collection;


    public Account() {
    }

    public Account(int id, int userId, double hongbao, double total, double useMoney, double noUseMoney, double collection) {
        this.id = id;
        this.userId = userId;
        this.hongbao = hongbao;
        this.total = total;
        this.useMoney = useMoney;
        this.noUseMoney = noUseMoney;
        this.collection = collection;
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

    public double getHongbao() {
        return hongbao;
    }

    public void setHongbao(double hongbao) {
        this.hongbao = hongbao;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public double getCollection() {
        return collection;
    }

    public void setCollection(double collection) {
        this.collection = collection;
    }

}
