package com.goldplusgold.mq.msgs;

/**
 * 用户主动平仓事件
 * Created by Administrator on 2017/5/16.
 */
public class UserOffset {
    /** 合约名称 */
    private String contractName;
    /** 空仓/多仓 0-空, 1-多 */
    private Integer bearBull;
    /** 用户ID */
    private String userID;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getBearBull() {
        return bearBull;
    }

    public void setBearBull(Integer bearBull) {
        this.bearBull = bearBull;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "UserOffset{" +
                "contractName='" + contractName + '\'' +
                ", bearBull=" + bearBull +
                ", userID='" + userID + '\'' +
                '}';
    }
}
