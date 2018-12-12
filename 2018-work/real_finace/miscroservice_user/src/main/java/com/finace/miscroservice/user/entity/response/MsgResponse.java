package com.finace.miscroservice.user.entity.response;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MsgResponse implements Serializable {
    public static Integer DEFAULT_PAGE_SIZE=10;

    private Integer msgCode;//消息类型
    private String topic;//标的名称
    private String msg;//消息
    private Long addtime; //添加时间
}
