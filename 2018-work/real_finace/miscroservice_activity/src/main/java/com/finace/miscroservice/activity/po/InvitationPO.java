package com.finace.miscroservice.activity.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class InvitationPO implements Serializable {
    private Integer invitation;

    public InvitationPO() {
    }

    public InvitationPO(Integer invitation) {
        this.invitation = invitation;
    }
}
