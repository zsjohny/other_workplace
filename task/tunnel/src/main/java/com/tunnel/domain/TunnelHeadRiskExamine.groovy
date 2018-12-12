package com.tunnel.domain

import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 洞口失稳风险源调查
 * Created by Ness on 2016/10/16.
 */
@Entity
@Table(name = "tunnel_head_risk_examine")
class TunnelHeadRiskExamine implements BaseDomain {
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

    //进口or出口
    Boolean importOrExport

    //地形地貌
    String topographicFeatures

    //植被发育
    String vegetationDeveop

    //地表水系
    String shallowWater

    //地层岩性
    String generalSituation

    //地下水
    String undergroundWater

    //地质构造
    String geologicalStructure

    //不良地质
    String badGeological

    //顺层偏压
    String beddingBias

    //地形偏压
    String trrainBias

    //周边环境
    String arroundEnviroment

    //生成日期
    LocalDateTime createime


}
