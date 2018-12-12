
import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 其它风险源调查
 * Created by Ness on 2016/10/16.
 */
@Entity
@Table(name = "other_risk_examine")
class OtherRiskExamine implements BaseDomain {
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

    //位置
    Boolean location

    //风险描述
    String riskDesc

    //生成日期
    LocalDateTime createime


}
