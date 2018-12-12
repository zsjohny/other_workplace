package com.finace.miscroservice.commons.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 用户认证的数据表
 */
public class UserAuth implements Serializable {
    private static final long serialVersionUID = -7700613681953847129L;

    /**
     * 用户Id
     */
    private Integer uid;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户的密码
     */
    private String pass;


    /**
     * 用户的昵称
     */
    private String nickName;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean wasEmpty() {
        if (StringUtils.isAnyEmpty(nickName, pass, name) || uid == null) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }


    @Override
    public String toString() {
        return "UserAuth{" +
                "name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }


}
