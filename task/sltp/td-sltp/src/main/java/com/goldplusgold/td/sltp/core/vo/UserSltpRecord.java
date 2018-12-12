package com.goldplusgold.td.sltp.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户的止盈止损记录
 * Created by Ness on 2017/5/10.
 */
public class UserSltpRecord implements Serializable, Comparable<UserSltpRecord> {

    /**
     * 当天的24小时失效时间
     */
    public static final int NOWDAY_EXPIRE_TIME = 1000 * 60 * 60 * 24;

    private static final long serialVersionUID = 7596344752839384918L;

    /**
     * id
     */
    @JsonIgnore
    private Integer id;
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


    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @JsonIgnore
    private Timestamp updateTime;


    /**
     * 是否删除 true是 false不是
     */
    @JsonIgnore
    private Boolean deleted;


    /**
     * 主动触发 true是
     */
    @JsonIgnore
    private Boolean isAutoTriggle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSltpRecord that = (UserSltpRecord) o;

        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Double getCommissionPrice() {
        return commissionPrice;
    }

    public void setCommissionPrice(Double commissionPrice) {
        this.commissionPrice = commissionPrice;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public  Integer getCommissionResult() {
        return commissionResult;
    }

    public void setCommissionResult(Integer commissionResult) {
        this.commissionResult = commissionResult;
    }


    public Integer getCommissionExpireType() {
        return commissionExpireType;
    }

    public void setCommissionExpireType(Integer commissionExpireType) {
        this.commissionExpireType = commissionExpireType;
    }

    public Boolean getAutoTriggle() {
        return isAutoTriggle;
    }

    public void setAutoTriggle(Boolean autoTriggle) {
        isAutoTriggle = autoTriggle;
    }

    public Double getFloatPrice() {
        return floatPrice;
    }

    public void setFloatPrice(Double floatPrice) {
        this.floatPrice = floatPrice;
    }

    public static final String _BEAR = "0";
    public static final String _BULL = "1";

    /**
     * 获取相反的空头或者多头的标识
     *
     * @return
     */
    public String getOppositeBearBull() {
        if (this.bearBull != null && this.bearBull.equals(SltpType.BEAR.toType())) {
            return _BULL;
        }

        return _BEAR;
    }

    /**
     * 默认按创建时间排序
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(UserSltpRecord other) {
        return this.createTime.compareTo(other.getCreateTime());
    }


    /**
     * 止盈止损内部类型
     */

    public enum SltpType {

        SL(0), TP(1), MAX_SLTP_SIZE(1), BEAR(0), BULL(1), MAX_BEARBULL_SIZE(1), COMMISS_SUCCESS(0), COMMISS_ERROR(1), COMMISS_INVAILD(2), MAX_COMMISS_MARK(1), NOWDAY_EXPIRE(0);
        private int type;


        SltpType(int type) {
            this.type = type;
        }

        public int toType() {
            return type;
        }
    }


    /**
     * 检查插入参数
     *
     * @return
     */
    @JsonIgnore
    public boolean checkSaveArgument() {

        //行情委托提交
        if (isAutoTriggle != null && isAutoTriggle) {
            if (userId == null) return false;
            if (StringUtils.isEmpty(uuid)) return false;
        } else {
            if (userId != null) return false;
            if (floatPrice == null || floatPrice < 0) return false;
            if (commissionResult == null || commissionResult > SltpType.MAX_COMMISS_MARK.toType()) return false;
            if (commissionPrice == null || commissionPrice < 0) return false;
            if (commissionStartDate != null) return false;
            if (commissionEndDate != null) return false;
            if (deleted != null) return false;
            if (createTime != null) return false;
            if (updateTime != null) return false;
            if (id != null) return false;
            if (sltpType == null || sltpType > SltpType.MAX_SLTP_SIZE.toType()) return false;

        }


        if (lots == null || lots < 0) return false;
        if (StringUtils.isEmpty(contract)) return false;
        if ((slPrice == null || slPrice < 0) && tpPrice == null) return false;
        if ((tpPrice == null || tpPrice < 0) && slPrice == null) return false;
        if (bearBull == null || bearBull > SltpType.MAX_BEARBULL_SIZE.toType()) return false;

        return true;

    }


    /**
     * 检查新增参数
     *
     * @return
     */
    @JsonIgnore
    public boolean checkUpdateArgument() {
        //防止用户传入
        if (userId != null) return false;
        if (StringUtils.isNotEmpty(contract)) return false;
        if (commissionStartDate != null) return false;
        if (commissionEndDate != null) return false;
        if (sltpType != null) return false;
        if (bearBull != null) return false;
        if ((lots == null || lots < 0) && commissionResult == null) return false;
        if (deleted != null) return false;
        if (createTime != null) return false;
        if (updateTime != null) return false;
        if (id != null) return false;


        if (commissionResult == null) {
            //满足更新条件---需要更新的字段
            if ((slPrice == null && tpPrice == null) || (slPrice != null && tpPrice != null)) return false;
            if (commissionPrice == null || commissionPrice < 0) return false;
            if (floatPrice == null || floatPrice < 0) return false;
            if (StringUtils.isEmpty(uuid)) return false;

        } else {
            //这里设置失效--交易回调
            if (slPrice != null || tpPrice != null) return false;
            if (commissionPrice != null) return false;
            if (floatPrice != null) return false;
            if (StringUtils.isEmpty(uuid)) return false;
        }

        return true;

    }


    private Logger logger = LoggerFactory.getLogger(UserSltpRecord.class);


    /**
     * 转移交易接口
     *
     * @return
     */
    @JsonIgnore
    public UserSltpRecord trasferTrade() {

        if (!isAccordTrasferTrade()) {
            logger.warn("转移交接接口 参数不符合规范");
            return null;
        }

//        UserSltpRecord userSltpRecord = new UserSltpRecord();
//        if (slPrice != null) {
//            userSltpRecord.setSlPrice(slPrice);
//        } else if (tpPrice != null) {
//            userSltpRecord.setTpPrice(tpPrice);
//        } else {
//            logger.warn("发送转移交易接口 止盈止损不正确");
//            return null;
//        }
//
//        userSltpRecord.setUserId(userId);
//        userSltpRecord.setContract(contract);
//        userSltpRecord.setUuid(uuid);
//        userSltpRecord.setLots(lots);
//        userSltpRecord.setBearBull(bearBull);
//        userSltpRecord.setCommissionPrice(commissionPrice);
        return this;
    }

    /**
     * 判断是否符合交易接口的参数
     *
     * @return
     */
    @JsonIgnore
    public  boolean isAccordTrasferTrade() {

        if ((slPrice == null || slPrice < 0) && tpPrice == null) {
            logger.warn("发送转移交易接口 slPrice为空");
            return false;
        }

        if ((tpPrice == null || tpPrice < 0) && slPrice == null) {
            logger.warn("发送转移交易接口 tpPrice为空");
            return false;
        }


        if (StringUtils.isEmpty(userId)) {
            logger.warn("发送转移交易接口 userId为空");
            return false;
        }
        if (StringUtils.isEmpty(contract)) {
            logger.warn("发送转移交易接口 contract为空");
            return false;
        }
        if (StringUtils.isEmpty(uuid)) {
            logger.warn("发送转移交易接口 uuid为空");
            return false;
        }
        if (lots == null || lots < 0) {
            logger.warn("发送转移交易接口 lots不正确");
            return false;
        }
        if (bearBull == null || bearBull < 0) {
            logger.warn("发送转移交易接口 bearBull不正确");
            return false;
        }

        if (bearBull == null || bearBull > SltpType.MAX_BEARBULL_SIZE.toType()) {
            logger.warn("发送转移交易接口 bearBull不正确");
            return false;
        }
        if (commissionPrice == null || commissionPrice < 0) {
            logger.warn("发送转移交易接口 commissionPrice不正确");
            return false;
        }
        return true;
    }


    /**
     * 转移数据到新的实体类 并且判断价格是否发生变化
     *
     * @param userSltpRecord 需要转移的实体类
     * @return
     */
    @JsonIgnore
    public boolean judgeEqualPriceAndtransfer(UserSltpRecord userSltpRecord) {

        Boolean flag = Boolean.FALSE;

        if (userSltpRecord == null) {
            return flag;
        }

        if (userSltpRecord.getSlPrice() != null && userSltpRecord.getSlPrice() >= 0 && this.slPrice != null) {
            if (this.slPrice.equals(userSltpRecord.getSlPrice())) {
                flag = Boolean.TRUE;
            } else {
                this.slPrice = userSltpRecord.getSlPrice();
            }
        }

        if (userSltpRecord.getTpPrice() != null && userSltpRecord.getTpPrice() >= 0 && tpPrice != null) {
            if (this.slPrice.equals(userSltpRecord.getSlPrice())) {
                flag = Boolean.TRUE;
            } else {
                this.tpPrice = userSltpRecord.getTpPrice();
            }

        }

        if (userSltpRecord.getFloatPrice() != null && userSltpRecord.getFloatPrice() >= 0) {
            this.floatPrice = userSltpRecord.getFloatPrice();
        }


        if (userSltpRecord.getCommissionPrice() != null && userSltpRecord.getCommissionPrice() >= 0) {
            this.commissionPrice = userSltpRecord.getCommissionPrice();
        }


        return flag;

    }


    @Override
    public String toString() {
        return "UserSltpRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", uuid='" + uuid + '\'' +
                ", slPrice=" + slPrice +
                ", tpPrice=" + tpPrice +
                ", lots=" + lots +
                ", contract='" + contract + '\'' +
                ", bearBull=" + bearBull +
                ", sltpType=" + sltpType +
                ", commissionResult=" + commissionResult +
                ", commissionPrice=" + commissionPrice +
                ", commissionStartDate=" + commissionStartDate +
                ", commissionEndDate=" + commissionEndDate +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                '}';
    }
}
