
import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * 项目信息
 * Created by Ness on 2016/10/11.
 */
@Entity
@Table(name = "project")
class Project implements BaseDomain {

    //Id
    Integer id

    @Id
    @GeneratedValue
    Integer getId() {
        return id
    }
    //开始页数
    Integer page

    @Transient
    Integer getPage() {
        return page
    }

    //项目编号
    String projectNumber

    //项目名称
    String projectName

    //调查人员
    String examinePersion

    //联系方式
    String contactInfo

    //调查日期时间
    String examineDate

    //生成日期
    LocalDateTime createime

    @Override
    public String toString() {

        return "Project{" +
                "id=" + id +
                ", page=" + page +
                ", projectNumber='" + projectNumber + '\'' +
                ", projectName='" + projectName + '\'' +
                ", examinePersion='" + examinePersion + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", examineDate='" + examineDate + '\'' +
                ", createime=" + createime +
                '}';
    }
}
