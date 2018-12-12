package com.finace.miscroservice.borrow.entity.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 收益明细 Response
 */
@ToString
@Getter
@Setter
public class ReturnsDetailedResponse implements Serializable{
    private Double amtPrincipal;//累计投资总额（本金）
    private Double amtReturnedMoney;//累计已回款总额（本息）
    private Double amtWaitBackMoney;//总待回款总额（本息）
    private Double waitBackPrincipal;//待回款本金
    private Double waitBackInterest;//待回款利息
    private Double backInterest;//利息收益
    private Double backInterestPro;//加利息收益
    private Double backRedPacket;//红包收益
    private Double others;//其他收益
    private Double amtBack;//累计已收益
    private Double interestPercent;//利息百分比
    private Double redPacketPercent;//红包百分比
    private Double interestProPercent;//加利息收益百分比
    private Double othersPercent;//其他收益百分比


    /**
    public void setAmtPrincipal(Double amtPrincipal) {
        this.amtPrincipal = amtPrincipal ==null?0.0:amtPrincipal;
    }

    public void setAmtReturnedMoney(Double amtReturnedMoney) {
        this.amtReturnedMoney = amtReturnedMoney ==null?0.0:amtReturnedMoney;
    }

    public void setAmtWaitBackMoney(Double amtWaitBackMoney) {
        this.amtWaitBackMoney = amtWaitBackMoney ==null?0.0:amtWaitBackMoney;
    }

    public void setWaitBackPrincipal(Double waitBackPrincipal) {
        this.waitBackPrincipal = waitBackPrincipal ==null?0.0:waitBackPrincipal;
    }

    public void setWaitBackInterest(Double waitBackInterest) {
        this.waitBackInterest = waitBackInterest ==null?0.0:waitBackInterest;
    }

    public void setBackInterest(Double backInterest) {
        this.backInterest = backInterest ==null?0.0:backInterest;
    }

    public void setBackInterestPro(Double backInterestPro) {
        this.backInterestPro = backInterestPro ==null?0.0:backInterestPro;
    }

    public void setBackRedPacket(Double backRedPacket) {
        this.backRedPacket = backRedPacket ==null?0.0:backRedPacket;
    }

    public void setOthers(Double others) {
        this.others = others ==null?0.0:others;
    }

    public void setAmtBack(Double amtBack) {
        this.amtBack = amtBack ==null?0.0:amtBack;
    }

    public void setInterestPercent(Double interestPercent) {
        this.interestPercent = interestPercent ==null?0.0:interestPercent;
    }

    public void setRedPacketPercent(Double redPacketPercent) {
        this.redPacketPercent = redPacketPercent ==null?0.0:redPacketPercent;
    }

    public void setInterestProPercent(Double interestProPercent) {
        this.interestProPercent = interestProPercent ==null?0.0:interestProPercent;
    }

    public void setOthersPercent(Double othersPercent) {
        this.othersPercent = othersPercent ==null?0.0:othersPercent;
    }
     **/

}
