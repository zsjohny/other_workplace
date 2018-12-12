package com.goldplusgold.td.sltp.core.parammodel;

import java.io.Serializable;

/**
 * 前端操作的pm
 * Created by Ness on 2017/5/23.
 */
public class UserSltpOperPM  implements Serializable {

    private static final long serialVersionUID = 8601003286547222930L;
    /**
     * 止盈止损系统的id/  用户的Id
     */
    private String id;
    /**
     * 合约的名称(非必须字段)
     */
    private String name;
    /**
     * 点击的类型
     */
    private Integer clickType;

    /**
     *  空头还是多头(非必须字段)  0 空 1多
     */
    private Integer type;

    /**
     * 开始的页数  默认第一个页数
     */
    private  Integer page;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClickType() {
        return clickType;
    }

    public void setClickType(Integer clickType) {
        this.clickType = clickType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
