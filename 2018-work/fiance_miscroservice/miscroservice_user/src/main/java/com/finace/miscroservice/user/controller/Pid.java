package com.finace.miscroservice.user.controller;

import com.finace.miscroservice.commons.annotation.Column;
import com.finace.miscroservice.commons.annotation.Table;
import lombok.Data;

@Table("test_b")
@Data
public class Pid {

    private Integer userId;
    private String userNamePass;
    private String userPassWord;
    @Column("a")
    private Integer userPass;

}
