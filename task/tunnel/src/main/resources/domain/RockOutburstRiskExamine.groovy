
import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 岩爆风险源调查
 * Created by Ness on 2016/10/16.
 */
@Entity
@Table(name = "rock_outburst_risk_examine")
class RockOutburstRiskExamine implements BaseDomain {
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

    // 岩石单轴饱和抗压强度（Rc）
    Double uniaxialSaturatedCompressiveStrengthOfRock

    //地质构造
    String geologicalStructure

    //隧道埋深
    Double tunnelLine

    //最大主应力σ1
    Double maxStrain

    //洞轴线与最大主应力夹角
    String tunnelAxesAndMaxStrain

    //生成日期
    LocalDateTime createime


}
