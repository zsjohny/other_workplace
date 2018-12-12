package com.tunnel.domain

import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 辅助通道调查
 * Created by Ness on 2016/10/11.
 */
@Entity
@Table(name = "assist_tunnel")
class AssistTunnel implements BaseDomain {
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

    //地形地貌
    String topographicFeatures

    //植被条件
    String vegetationCover

    //地质概况
    String roadQuality

    //不良地质概况
    String badGeologicalSurvey

    //水文条件概况
    String hydrologyCondition

    //建议洞门形式
    Integer tunnelHeadStyle

    //建议刷坡坡率
    String cuttingSlope

    //既有或规划建(构)筑物
    String plannedConstruction

    //施工场地
    String constructionPlant

    //施工便道
    String constructionRoad

    //环保水保
    String waterEnviron

    //其它需注意问题
    String otherNotice

    //辅助通道类型  0-斜井,1-竖井,2-横洞,3-平行导坑
    Integer accessTunnelType

    //与主洞平面关系
    String mainTunnelRelationWithPlane

    // 辅助坑道/风井与主洞平面关系示意图
    String acccessTunnelAndAirshaftRelationWithMainTunnel

    //生成日期
    LocalDateTime createime


}
