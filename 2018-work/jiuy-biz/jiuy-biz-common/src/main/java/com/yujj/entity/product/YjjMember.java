package com.yujj.entity.product;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员表
 * Create by hyf on 2018/8/13
 */
@TableName("yjj_member")
public class YjjMember implements Serializable {

    private static final long serialVersionUID = 3007971592392626432L;
    /**
     * id
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 平台类型
     */
    @TableField("platform_type")
    private Integer platformType;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 会员等级 默认0：普通用户，一级会员：1 等
     */
    @TableField("member_level")
    private Integer memberLevel;
    /**
     * 到期时间
     */
    @TableField("end_time")
    private Long endTime;
    /**
     * 历史充值总额
     */
    @TableField("money_total")
    private BigDecimal moneyTotal;
    /**
     * 默认0 正常  1 删除 2冻结
     */
    @TableField("del_state")
    private Integer delState;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private String updateTime;
    /**
     * 会员类型 1:基础版,2:会员版,3:至尊版
     */
    @TableField("type")
    private Integer type;
    /**
     * 有效时间队列和会员等级,时间降序排序 1515640271000:3,1519630271000:2
     */
    @TableField("valid_time_queue")
    private String validTimeQueue;
    /**
     * 市
     */
    private String  city;

    /**
     * 省
     */
    private String  province;

    /**
     * 区
     */
    private String  district;
    /**
     * 开通渠道
     */
    private String canal;
    /**
     * 姓名
     */
    private String name;
    public YjjMember() {
    }

    public YjjMember(Long id, Integer platformType, Long userId, Integer memberLevel, Long endTime, BigDecimal moneyTotal, Integer delState, String createTime, String updateTime, Integer type, String validTimeQueue, String city, String province, String district, String canal, String name) {
        this.id = id;
        this.platformType = platformType;
        this.userId = userId;
        this.memberLevel = memberLevel;
        this.endTime = endTime;
        this.moneyTotal = moneyTotal;
        this.delState = delState;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.type = type;
        this.validTimeQueue = validTimeQueue;
        this.city = city;
        this.province = province;
        this.district = district;
        this.canal = canal;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getValidTimeQueue() {
        return validTimeQueue;
    }

    public void setValidTimeQueue(String validTimeQueue) {
        this.validTimeQueue = validTimeQueue;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(BigDecimal moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    public Integer getDelState() {
        return delState;
    }

    public void setDelState(Integer delState) {
        this.delState = delState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "YjjMember{" +
                "id=" + id +
                ", platformType=" + platformType +
                ", userId=" + userId +
                ", memberLevel=" + memberLevel +
                ", endTime=" + endTime +
                ", moneyTotal=" + moneyTotal +
                ", delState=" + delState +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", type=" + type +
                ", validTimeQueue='" + validTimeQueue + '\'' +
                '}';
    }
}
