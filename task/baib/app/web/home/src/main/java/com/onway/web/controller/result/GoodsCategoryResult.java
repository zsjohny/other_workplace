package com.onway.web.controller.result;

import com.onway.platform.common.base.ToString;

public class GoodsCategoryResult extends ToString {

    private static final long serialVersionUID = 741231858441822688L;

    private int               id;

    private int               parentId;

    private String            name;

    private String            type;

    private String            leafFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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

    public String getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(String leafFlag) {
        this.leafFlag = leafFlag;
    }

}
