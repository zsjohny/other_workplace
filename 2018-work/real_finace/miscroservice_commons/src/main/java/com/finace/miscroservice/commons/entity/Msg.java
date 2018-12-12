package com.finace.miscroservice.commons.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Msg {
    private Integer msgCode;//消息类型
    private String topic;//标的名称
    private String msg;//消息
    private Long addtime; //添加时间


}
