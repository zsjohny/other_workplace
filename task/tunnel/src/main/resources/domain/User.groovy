
import com.tunnel.util.BaseDomain

import javax.persistence.*
import java.time.LocalDateTime

/**
 * Created by Ness on 2016/10/8.
 */
@Entity
@Table(name = "user")
public class User implements BaseDomain {
    Integer id;
    //用户名
    String name;
    //真实姓名
    String realName

    //密码
    String password;
    //密钥
    String passKey
    //权限水准
    Integer authorLevel
    //创建时间
    LocalDateTime createTime;

    //状态 true 能使用 false 禁用
    Boolean status

    //登录时间
    Date loadTime

    //开始页数
    Integer page

    //原始密码
    String originalPass


    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    @Transient
    Integer getPage() {
        return page
    }

    //原始密码
    @Transient
    String getOriginalPass() {
        return originalPass
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", realName='" + realName + '\'' +
                ", password='" + password + '\'' +
                ", passKey='" + passKey + '\'' +
                ", authorLevel=" + authorLevel +
                ", createTime=" + createTime +
                ", status=" + status +
                ", loadTime=" + loadTime +
                ", page=" + page +
                ", originalPass='" + originalPass + '\'' +
                '}';
    }

    public enum UserType {
        superUser(0, "超级用户"),
        seniorUser(1, "高级用户"),
        commontUser(2, "普通用户");

        private int key;
        private String value;

        public UserType(int key, String value) {
            this.key = key
            this.value = value
        }

        public int getValue() {
            return key
        }


    }


}
