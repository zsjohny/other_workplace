package com.e_commerce.miscroservice.activity.entityvo;

import com.e_commerce.miscroservice.activity.entity.BaseEntity;
import lombok.Data;

@Data
public class ChannelUserRequest extends BaseEntity {

    private String name;
    private String phone;
    private Integer sex;
    private Integer authority;//授权
    private String startTime;
    private String overTime;
    private Long channelId;

}
