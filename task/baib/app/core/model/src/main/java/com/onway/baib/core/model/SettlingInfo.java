package com.onway.baib.core.model;

import java.util.List;

/**
 * 待到账返回结果集
 * 
 * @author jiaming.zhu
 * @version $Id: SettlingInfo.java, v 0.1 2017年2月8日 上午10:20:02 ZJM Exp $
 */
public class SettlingInfo {
    //待到账总额
    private String               allAmount;
    //待到账列表
    private List<SettlingResult> settlingResultList;

    public String getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(String allAmount) {
        this.allAmount = allAmount;
    }

    public List<SettlingResult> getSettlingResultList() {
        return settlingResultList;
    }

    public void setSettlingResultList(List<SettlingResult> settlingResultList) {
        this.settlingResultList = settlingResultList;
    }

}
