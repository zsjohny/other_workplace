package com.goldplusgold.td.sltp.core.parammodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 止盈止损的前端的PM
 * Created by Ness on 2017/5/23.
 */
public class UserSltpPM implements Serializable {


    private static final long serialVersionUID = 2947286299573185692L;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 伪id
     */
    private String uuid;

    /**
     * 止损价格
     */
    private Double slPrice;

    /**
     * 止盈价格
     */
    private Double tpPrice;

    /**
     * 手数
     */
    private Integer lots;

    /**
     * 合约
     */
    private String contract;


    /**
     * 空头/多头  0 空头 1 多头
     */
    private Integer bearBull;

    /**
     * 止盈止损类型  0是止损  1是止盈
     */
    private Integer sltpType;


    /**
     * 是否委托成功 0 成功 1是失败 2失效
     */
    private Integer commissionResult;


    /**
     * 委托价格
     */
    private Double commissionPrice;

    /**
     * 浮动价格
     */
    private Double floatPrice;

    /**
     * 委托触发时间
     */
    private Long commissionStartDate;


    /**
     * 触发结束时间
     */
    private Long commissionEndDate;

    /**
     * 触发委托失效类型  目前只有默认
     */
    private Integer commissionExpireType;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getSlPrice() {
        return slPrice;
    }

    public void setSlPrice(Double slPrice) {
        this.slPrice = slPrice;
    }

    public Double getTpPrice() {
        return tpPrice;
    }

    public void setTpPrice(Double tpPrice) {
        this.tpPrice = tpPrice;
    }

    public Integer getLots() {
        return lots;
    }

    public void setLots(Integer lots) {
        this.lots = lots;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public Integer getBearBull() {
        return bearBull;
    }

    public void setBearBull(Integer bearBull) {
        this.bearBull = bearBull;
    }

    public Integer getSltpType() {
        return sltpType;
    }

    public void setSltpType(Integer sltpType) {
        this.sltpType = sltpType;
    }

    public Integer getCommissionResult() {
        return commissionResult;
    }

    public void setCommissionResult(Integer commissionResult) {
        this.commissionResult = commissionResult;
    }

    public Double getCommissionPrice() {
        return commissionPrice;
    }

    public void setCommissionPrice(Double commissionPrice) {
        this.commissionPrice = commissionPrice;
    }

    public Double getFloatPrice() {
        return floatPrice;
    }

    public void setFloatPrice(Double floatPrice) {
        this.floatPrice = floatPrice;
    }

    public Long getCommissionStartDate() {
        return commissionStartDate;
    }

    public void setCommissionStartDate(Long commissionStartDate) {
        this.commissionStartDate = commissionStartDate;
    }

    public Long getCommissionEndDate() {
        return commissionEndDate;
    }

    public void setCommissionEndDate(Long commissionEndDate) {
        this.commissionEndDate = commissionEndDate;
    }

    public Integer getCommissionExpireType() {
        return commissionExpireType;
    }

    public void setCommissionExpireType(Integer commissionExpireType) {
        this.commissionExpireType = commissionExpireType;
    }

    /**
     * 转成userSltp实体类
     *
     * @return
     */
    @JsonIgnore
    public UserSltpRecord pm2UserSltp() {
        UserSltpRecord userSltpRecord = new UserSltpRecord();

        if (StringUtils.isNotEmpty(userId)) {
            userSltpRecord.setUserId(userId);
        }

        if (StringUtils.isNotEmpty(uuid)) {
            userSltpRecord.setUuid(uuid);
        }

        if (slPrice != null) {
            userSltpRecord.setSlPrice(slPrice);
        }

        if (tpPrice != null) {
            userSltpRecord.setTpPrice(tpPrice);
        }

        if (lots != null) {
            userSltpRecord.setLots(lots);
        }

        if (StringUtils.isNotEmpty(contract)) {
            userSltpRecord.setContract(contract);
        }


        if (bearBull != null) {
            userSltpRecord.setBearBull(bearBull);
        }
        if (sltpType != null) {
            userSltpRecord.setSltpType(sltpType);
        }

        if (commissionResult != null) {
            userSltpRecord.setCommissionResult(commissionResult);
        }

        if (commissionPrice != null) {
            userSltpRecord.setCommissionPrice(commissionPrice);
        }
        if (floatPrice != null) {
            userSltpRecord.setFloatPrice(floatPrice);
        }

        if (commissionStartDate != null) {
            userSltpRecord.setCommissionStartDate(commissionStartDate);
        }
        if (commissionEndDate != null) {
            userSltpRecord.setCommissionEndDate(commissionEndDate);
        }

        if (commissionExpireType != null) {
            userSltpRecord.setCommissionExpireType(commissionExpireType);
        }


        return userSltpRecord;


    }


}
