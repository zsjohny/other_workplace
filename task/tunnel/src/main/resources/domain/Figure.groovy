
import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 附图
 * Created by Ness on 2016/10/16.
 */
@Entity
@Table(name = "figure")
class Figure implements BaseDomain {
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

    //拍摄位置
    Boolean cameraSite

    //里程桩号
    Double mileagePile

    //拍摄方向
    String shootingDirection

    //拍摄时间
    String shootingTime

    //照片图像
    String photosPicture

    //生成日期
    LocalDateTime createime


}
