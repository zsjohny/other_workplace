package com.e_commerce.miscroservice.crm.entity;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/18 14:23
 * @Copyright 玖远网络
 */
@Data
public class User {
    private Integer userId;
    private String loginName;
    private String userName;
    private String phonenumber;
    private String password;
    private String userType;
}
