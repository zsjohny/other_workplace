package com.finace.miscroservice.activity.po;

import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import com.finace.miscroservice.commons.utils.tools.TimeUtil;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MyFriendsPO implements Serializable {
    private String phone; //手机号
    private String time;//注册时间
    private String type;//已出借 未出借

    public MyFriendsPO(String phone, String time, String type) {
        this.phone = phone;
        this.time = time;
        this.type = type;
    }


    public String getPhone() {
        return TextUtil.hidePhoneNo(phone);
    }

    public void setPhone(String phone) {
        this.phone = TextUtil.hidePhoneNo(phone);
    }

    public static void main(String[] args) {

    }
}
