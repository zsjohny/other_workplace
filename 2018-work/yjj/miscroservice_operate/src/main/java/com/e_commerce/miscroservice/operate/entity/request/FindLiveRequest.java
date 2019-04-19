package com.e_commerce.miscroservice.operate.entity.request;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import lombok.Data;

/**
 * @Author hyf
 * @Date 2019/1/15 15:11
 */
@Data
public class FindLiveRequest extends BaseEntity {

    private String name;
    private String nickName;
    private Integer sex;
    private String phone;
    private Long timeStart;
    private Long timeEnd;
}
