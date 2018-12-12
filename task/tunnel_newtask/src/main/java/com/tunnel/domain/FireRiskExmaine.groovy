package com.tunnel.domain

import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 *  火灾风险源调查
 * Created by Ness on 2016/10/16.
 */
@Entity
@Table(name = "fire_risk_exmaine")
class FireRiskExmaine implements BaseDomain {

    //Id
    Long id

    @Id
    @GeneratedValue
    Long getId() {
        return id
    }

    //开始页数
    Integer page

    @Transient
    Integer getPage() {
        return page
    }

    //隧道编号
    String tunnelNumber

    //风险源是否存在
    Boolean riskSourceExists

    //风险发生概率 备注：1-几乎不可能，2-很少发生，3-偶然发生，4-可能发生，5-频繁发生
    Integer riskHappendProbalility

    //风险源描述
    String riskDecribe

    //辅助坑道
    String acccessTunnel

    // 交通量(Pcu/h)
    Double trafficRadio

    //货车比例
    String truckScaleRadio

    //运营通风方案
    String operationalVentilationScheme

    //防灾救援方案
    String fileRecue

    //监控方案
    String monitorPlan

    //照明方案
    String lightPlan

    //隧道长度
    String tunnelLength

    //生成日期
    LocalDateTime createime




}
