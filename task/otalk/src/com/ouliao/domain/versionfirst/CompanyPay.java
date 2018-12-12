package com.ouliao.domain.versionfirst;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nessary on 16-5-30.
 */
@Entity
@Table(name = "companypay")
public class CompanyPay {

    private Long companyPayId;

    private String isDeleted;


    private String date;

    private Double companyConsume;

    private Double companyCost;

    private String isRegister;
    private Date createTime;
    private Date modifyTime;

    @GeneratedValue
    @Id
    public Long getCompanyPayId() {
        return companyPayId;
    }

    public Double getCompanyCost() {
        return companyCost;
    }

    public void setCompanyCost(Double companyCost) {
        this.companyCost = companyCost;
    }

    @Column(length = 64)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getCompanyConsume() {
        return companyConsume;
    }

    public void setCompanyConsume(Double companyConsume) {
        this.companyConsume = companyConsume;
    }

    public void setCompanyPayId(Long companyPayId) {
        this.companyPayId = companyPayId;
    }

    @Column(length = 8)
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
