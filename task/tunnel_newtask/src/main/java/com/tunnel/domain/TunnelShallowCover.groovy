package com.tunnel.domain

import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 隧道浅埋段调查
 * Created by Ness on 2016/10/11.
 */
@Entity
@Table(name = "tunnel_shallow_cover")
class TunnelShallowCover implements BaseDomain {

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

    //起始里程
    Double startCourse

    //终止里程
    Double endCourse

    //隧道埋深(m)
    String tunnelLine

    //地形地貌
    String topographicFeatures

    //植被条件
    String vegetationCover

    //浅埋段不良地质
    String shallowBad

    //地表水影响
    String surfaceWaterInfluence

    //既有或规划建(构)筑物
    String plannedConstruction

    //施工场地
    String constructionPlant

    //施工便道
    String constructionRoad

    //环保水保
    String waterEnviron

    //建议施工方式
    String constractWay

    //其它需注意问题
    String otherNotice

    //生成日期
    LocalDateTime createime


}
