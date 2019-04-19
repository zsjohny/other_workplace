package com.e_commerce.miscroservice.activity.entityvo;

import com.e_commerce.miscroservice.activity.entity.BaseEntity;
import lombok.Data;

@Data
public class ChannelRequest extends BaseEntity {
    private Long id;
    private String name;
    private String phone;
    private String startTime;
    private String overTime;
    private Integer status;
    private Long channelId;


    //是否删除
    private Integer delStatus=0;

}
