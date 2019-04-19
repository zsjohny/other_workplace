package com.util;

/**
 * @version V1.0
 * @Package com.util
 * @Description:
 * @author: Aison
 * @date: 2018/5/15 17:37
 * @Copyright: 玖远网络
 */
public enum GeTuiType {


    Type100("登陆退出","101");

    private String name;

    private String type;

    GeTuiType(String name,String type){
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
