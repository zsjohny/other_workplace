package com.finace.miscroservice.user.entity.response;

import com.finace.miscroservice.commons.utils.tools.TextUtil;
import com.finace.miscroservice.user.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class MyRewardsRecordResponse extends BaseEntity {
    private String phone;
    private Double money;
    private String content;
    private String redPacket;

    public MyRewardsRecordResponse() {
    }

    public MyRewardsRecordResponse(String phone, Double money, String content) {
        this.phone = phone;
        this.money = money;
        this.content = content;
    }

    public MyRewardsRecordResponse(String phone, Double money, String content, String redPacket) {
        this.phone = phone;
        this.money = money;
        this.content = content;
        this.redPacket = redPacket;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        String hidePhone = TextUtil.hidePhoneNo(phone);
        this.phone = hidePhone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money==null?0d:money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        String text = "首投%s";
        String contents = String.format(text,content);
        this.content = content!=null?"未投资":contents;
    }

    public static void main(String[] args) {
        MyRewardsRecordResponse myInvitationResponse = new MyRewardsRecordResponse(null,0d,null);
//        myInvitationResponse.setPhone("1232163654787");
//        myInvitationResponse.setMoney(null);
//        myInvitationResponse.setContent("321");
        System.out.println(myInvitationResponse);

    }
}
