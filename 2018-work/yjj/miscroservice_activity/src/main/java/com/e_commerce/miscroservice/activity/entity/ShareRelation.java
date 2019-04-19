package com.e_commerce.miscroservice.activity.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

@Data
@Table(commit = "分享后绑定关系表格")
public class ShareRelation {

    @Id
    private Long id;

    @Column(commit = "分享者Id")
    private Long sharerId;

    @Column(commit = "被分享者Id")
    private Long sharedId;


    public boolean wasEmpty() {
        return sharedId == null && sharerId == null;
    }

}
