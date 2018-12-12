package com.tunnel.domain

import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 *
 * 大变形风险源调查
 * Created by Ness on 2016/10/16.
 */
@Entity
@Table(name = "shape_risk_examine")
class ShapeRiskExamine implements BaseDomain {
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

    //起始里程
    Double startCourse

    //终止里程
    Double endCourse

    //地层岩性
    String generalSituation

    //地质构造
    String geologicalStructure

    //地下水
    String undergroundWater

    //不良地质
    String badGeological

    //特殊岩土
    String specialRock

    //隧道埋深
    String tunnelLine

    //周边环境
    String arroundEnviroment

    //生成日期
    LocalDateTime createime


}
