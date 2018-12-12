package com.finace.miscroservice.user.controller;

import com.finace.miscroservice.commons.annotation.Table;
import lombok.Data;

@Table("user")
@Data
public class User {

    private static final long serialVersionUID = 5059667926063885817L;

    private String username;

    private Integer order;

    private String password;
    private String paypassword;
    private Integer islock;
}
