
import com.tunnel.util.BaseDomain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import java.time.LocalDateTime

/**
 * 隧道信息调查和查询的关系
 * Created by Ness on 2016/10/18.
 */
@Entity
@Table(name = "tunnel_examine_relation_query")
class TunnelExamineRelationQuery implements BaseDomain {

    /**
     * 自增Id
     */
    Integer id

    @Id
    @GeneratedValue
    Integer getId() {
        return id
    }
    /**
     * 隧道编号
     */
    String tunnelNumber

    /**
     * 表的名称
     */
    String tableName

    /**
     * 创建时间
     */
    LocalDateTime createTime

}
