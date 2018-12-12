package com.finace.miscroservice.user.entity.response;

import com.finace.miscroservice.user.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MyInvitationResponse extends BaseEntity {
    private Integer invitationSize;
    private Double countMoney;
    private String shareId;
}
