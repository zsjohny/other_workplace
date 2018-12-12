package com.finace.miscroservice.user.entity.po;

import com.finace.miscroservice.commons.annotation.Column;
import com.finace.miscroservice.commons.annotation.Id;
import com.finace.miscroservice.commons.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Table("user_tmp")
@Data
@ToString
public class Register implements Serializable {
    @Id
    private Integer id;

    @Column(commit = "手机号",isNUll = false)
    private String phone;

    @Column(commit = "密码" ,isNUll = false)
    private String pass;

    @Column(commit = "是否删除 默认 0：未删除 1：删除",defaultVal ="0")
    private Integer deleted;

    @Column(commit = "状态 默认 0：未验证成功 1：已验证成功" ,defaultVal ="0")
    private Integer status;


}
