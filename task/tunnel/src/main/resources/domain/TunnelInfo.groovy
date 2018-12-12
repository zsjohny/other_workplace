
import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 隧道基本信息
 * Created by Ness on 2016/10/11.
 */
@Entity
@Table(name = "tunnel_info")
class TunnelInfo implements BaseDomain {

    //Id
    Long id

    @Id
    @GeneratedValue
    Long getId() {
        return id
    }
    //生成日期
    LocalDateTime createime

    //开始页数
    Integer page

    @Transient
    Integer getPage() {
        return page
    }

    //隧道编号
    String tunnelNumber

    //隧道名称
    String tunnelName

    //所在项目编号

    String projectNumber

    //所在线位
    String wireLocation

    //设计阶段
    Integer designPeriod

    //结构型式
    Integer structuralStyle
    //隧道走向
    String tunnelDriection

    //起讫里程 - 右线 - 进口端
    Double startCourse_rightWire_importPort

    // 起讫里程 - 右线 - 出口端
    Double startCourse_rightWire_exportPort

    //  起讫里程 - 左线 - 进口端
    Double startCourse_leftWire_importPort

    //  起讫里程 - 左线 - 出口端
    Double startCourse_leftWire_exportPort

    // 路面标高 ( m )- 右线 - 进口端
    Double roadHigh_rightWire_importPort

    //  路面标高 ( m ) - 右线 - 出口端
    Double roadHigh_rightWire_exportPort

    //   路面标高 ( m ) - 左线 - 进口端
    Double roadHigh_leftWire_importPort

    //   路面标高 ( m )- 左线 - 出口端
    Double roadHigh_leftWire_exportPort

    // 线间距(m)-洞口-进口端
    Double interWire_tunnelHead_importPort

    // 线间距(m)-洞口-出口端
    Double interWire_tunnelHead_exportPort
    // 线间距(m)-洞身
    Double interWire_tunnelBody

    // 平面线形 - 右线
    Double parallelLine_rightWire

    //  平面线形 - 左线
    Double parallelLine_leftWire

    //  坡度坡长 - 右线
    Double slopeLine_rightWire

    //  坡度坡长 -  左线
    Double slopeLine_leftWire

    // 隧道埋深( m )- 右线
    Double tunnelLine_rightWire

    // 隧道埋深( m )- 左线
    Double tunnelLine_leftWire

    //施工组织建议
    String constructionTeamAdvice

    //气候条件
    String weatherCondition

    //地形地貌
    String topographicFeatures;

    //地层岩性概况
    String generalSituationFormation

    //不良地质概况
    String badGeologicalSurvey

    //水文地质概况
    String hydrogeologicalSurvey

    //地震烈度及动参数
    String dynamicParameters

    //浅埋偏压情况
    String shallowBuried

    //既有或规划建（构）筑物
    String plannedConstruction

    @Column(length = 12)
    Double getStartCourse_rightWire_importPort() {
        return startCourse_rightWire_importPort
    }

    @Column(length = 12)
    Double getStartCourse_rightWire_exportPort() {
        return startCourse_rightWire_exportPort
    }

    @Column(length = 12)
    Double getStartCourse_leftWire_importPort() {
        return startCourse_leftWire_importPort
    }

    @Column(length = 12)
    Double getStartCourse_leftWire_exportPort() {
        return startCourse_leftWire_exportPort
    }

    @Column(length = 12)
    Double getRoadHigh_rightWire_importPort() {
        return roadHigh_rightWire_importPort
    }

    @Column(length = 12)
    Double getRoadHigh_rightWire_exportPort() {
        return roadHigh_rightWire_exportPort
    }

    @Column(length = 12)
    Double getRoadHigh_leftWire_importPort() {
        return roadHigh_leftWire_importPort
    }

    @Column(length = 12)
    Double getRoadHigh_leftWire_exportPort() {
        return roadHigh_leftWire_exportPort
    }

    @Column(length = 12)
    Double getInterWire_tunnelHead_importPort() {
        return interWire_tunnelHead_importPort
    }

    @Column(length = 12)
    Double getInterWire_tunnelHead_exportPort() {
        return interWire_tunnelHead_exportPort
    }

    @Column(length = 12)
    Double getParallelLine_rightWire() {
        return parallelLine_rightWire
    }

    @Column(length = 12)
    Double getParallelLine_leftWire() {
        return parallelLine_leftWire
    }

    @Column(length = 12)
    Double getSlopeLine_rightWire() {
        return slopeLine_rightWire
    }

    @Column(length = 12)
    Double getSlopeLine_leftWire() {
        return slopeLine_leftWire
    }

    @Column(length = 12)
    Double getTunnelLine_rightWire() {
        return tunnelLine_rightWire
    }

    @Column(length = 12)
    Double getTunnelLine_leftWire() {
        return tunnelLine_leftWire
    }

    @Override
    public String toString() {
        return "TunnelInfo{" +
                "Id=" + Id +
                ", createime=" + createime +
                ", page=" + page +
                ", tunnelNumber='" + tunnelNumber + '\'' +
                ", tunnelName='" + tunnelName + '\'' +
                ", projectNumber='" + projectNumber + '\'' +
                ", wireLocation='" + wireLocation + '\'' +
                ", designPeriod=" + designPeriod +
                ", structuralStyle=" + structuralStyle +
                ", tunnelDriection='" + tunnelDriection + '\'' +
                ", startCourse_rightWire_importPort=" + startCourse_rightWire_importPort +
                ", startCourse_rightWire_exportPort=" + startCourse_rightWire_exportPort +
                ", startCourse_leftWire_importPort=" + startCourse_leftWire_importPort +
                ", startCourse_leftWire_exportPort=" + startCourse_leftWire_exportPort +
                ", roadHigh_rightWire_importPort=" + roadHigh_rightWire_importPort +
                ", roadHigh_rightWire_exportPort=" + roadHigh_rightWire_exportPort +
                ", roadHigh_leftWire_importPort=" + roadHigh_leftWire_importPort +
                ", roadHigh_leftWire_exportPort=" + roadHigh_leftWire_exportPort +
                ", interWire_tunnelHead_importPort=" + interWire_tunnelHead_importPort +
                ", interWire_tunnelHead_exportPort=" + interWire_tunnelHead_exportPort +
                ", parallelLine_rightWire=" + parallelLine_rightWire +
                ", parallelLine_leftWire=" + parallelLine_leftWire +
                ", slopeLine_rightWire=" + slopeLine_rightWire +
                ", slopeLine_leftWire=" + slopeLine_leftWire +
                ", tunnelLine_rightWire=" + tunnelLine_rightWire +
                ", tunnelLine_leftWire=" + tunnelLine_leftWire +
                ", constructionTeamAdvice='" + constructionTeamAdvice + '\'' +
                ", weatherCondition='" + weatherCondition + '\'' +
                ", topographicFeatures='" + topographicFeatures + '\'' +
                ", generalSituationFormation='" + generalSituationFormation + '\'' +
                ", badGeologicalSurvey='" + badGeologicalSurvey + '\'' +
                ", hydrogeologicalSurvey='" + hydrogeologicalSurvey + '\'' +
                ", dynamicParameters='" + dynamicParameters + '\'' +
                ", shallowBuried='" + shallowBuried + '\'' +
                ", plannedConstruction='" + plannedConstruction + '\'' +
                '}';
    }
}
