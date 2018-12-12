package com.tunnel.domain

import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 隧道进出口调查
 * Created by Ness on 2016/10/11.
 */
@Entity
@Table(name = "tunnel_import_port")
class TunnelImportPort implements BaseDomain {

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

    //进口or出口
    Boolean importOrExport

    //地形地貌
    String topographicFeatures

    //植被条件
    String vegetationCover

    //洞口地质概况
    String tunnelHeadQuality

    //建议刷坡坡率
    String cuttingSlope

    //地表水影响
    String surfaceWaterInfluence

    //建议洞门型式
    Integer portalType

    //既有或规划建(构)筑物
    String plannedConstruction

    //洞口衔接条件
    String tunnelHeadCondition

    //施工场地
    String constructionPlant

    //施工便道
    String constructionRoad

    //环保水保
    String waterEnviron

    //其它需注意问题
    String otherNotice

    //生成日期
    LocalDateTime createime

    @Override
    public String toString() {
        return "TunnelImportPort{" +
                "id=" + id +
                ", createime=" + createime +
                ", page=" + page +
                ", tunnelNumber='" + tunnelNumber + '\'' +
                ", importOrExport=" + importOrExport +
                ", topographicFeatures='" + topographicFeatures + '\'' +
                ", vegetationCover='" + vegetationCover + '\'' +
                ", tunnelHeadQuality='" + tunnelHeadQuality + '\'' +
                ", cuttingSlope=" + cuttingSlope +
                ", surfaceWaterInfluence='" + surfaceWaterInfluence + '\'' +
                ", portalType=" + portalType +
                ", plannedConstruction='" + plannedConstruction + '\'' +
                ", tunnelHeadCondition='" + tunnelHeadCondition + '\'' +
                ", constructionPlant='" + constructionPlant + '\'' +
                ", constructionRoad='" + constructionRoad + '\'' +
                ", waterEnviron='" + waterEnviron + '\'' +
                ", otherNotice='" + otherNotice + '\'' +
                '}';
    }
}
