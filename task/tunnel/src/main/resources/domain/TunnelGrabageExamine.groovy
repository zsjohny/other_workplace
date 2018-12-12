
import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 隧道弃渣场调查
 * Created by Ness on 2016/10/16.
 */
@Entity
@Table(name = "tunnel_grabage_examine")
public class TunnelGrabageExamine implements BaseDomain {

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

    //隧道工区
    String tunnelArea

    //渣场位置
    String garbageLocation

    //弃渣运距
    Double garbaeDistance

    //占地类型及面积
    Double coverAreaTypeOrAcreage

    //环保水保建议
    String envirWaterAdvice

    //其它需注意问题
    String otherNotice

    //生成日期
    LocalDateTime createime


}
