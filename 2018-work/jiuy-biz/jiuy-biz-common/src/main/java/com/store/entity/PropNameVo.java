package com.store.entity;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/7/10 8:58
 * @Copyright 玖远网络
 */
public class PropNameVo{

    private String codeName;
    private Long propNameId;
    private List<PropValGroupVo> groups;

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Long getPropNameId() {
        return propNameId;
    }

    public void setPropNameId(Long propNameId) {
        this.propNameId = propNameId;
    }

    public List<PropValGroupVo> getGroups() {
        return groups;
    }

    public void setGroups(List<PropValGroupVo> groups) {
        this.groups = groups;
    }
}
