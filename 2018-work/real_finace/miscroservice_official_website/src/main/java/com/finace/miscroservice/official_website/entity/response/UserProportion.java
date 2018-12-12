package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.official_website.entity.BaseEntity;
import lombok.Data;

/**
 * 用户男女占比
 */
@Data
public class UserProportion extends BaseEntity {
    private Double nanzb;    //性别男 占比
    private Double nvzb;  //性别女 占比
}
