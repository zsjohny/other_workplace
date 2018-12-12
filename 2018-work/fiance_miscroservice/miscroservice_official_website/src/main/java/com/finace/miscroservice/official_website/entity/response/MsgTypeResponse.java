package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.official_website.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.text.SimpleDateFormat;

@Data
@ToString
public class MsgTypeResponse extends BaseEntity {
    private String topic ;//标题
    private String msg;//内容
    private String addTime;//添加时间
    private String img;//图片地址

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.addTime = simpleDateFormat.format(Long.parseLong(addTime));
    }

    public static void main(String[] args) {
//        1521686332241
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(System.currentTimeMillis());
        System.out.println(simpleDateFormat.format(Long.parseLong("1521686332241")));
    }

}
