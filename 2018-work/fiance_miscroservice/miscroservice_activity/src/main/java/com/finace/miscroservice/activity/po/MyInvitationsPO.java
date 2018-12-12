package com.finace.miscroservice.activity.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MyInvitationsPO implements Serializable{
    private Integer invited; //已邀请的好友数量
    private Double jdCards;//已获京东卡 总额
    private Integer redPackets;//红包数量
}
