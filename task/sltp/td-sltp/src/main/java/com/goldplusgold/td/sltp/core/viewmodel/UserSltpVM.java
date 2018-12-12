package com.goldplusgold.td.sltp.core.viewmodel;

import java.io.Serializable;

/**
 * 前端返回的sltp的vm
 * Created by Ness on 2017/5/23.
 */
public class UserSltpVM  implements Serializable{

    private static final long serialVersionUID = -4400742403128628209L;

    /**
     * 前端的状态码
     */
    public int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
