package com.jiuy.rb.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 提示语: 配在字典表中
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/8/9 16:28
 * @Copyright 玖远网络
 */
public enum TipKeyEnum{

    TEAM_ACTIVITY_UNDERWAY("teamActivityUnderWay", "tip", "teamActivityUnderWay"),
    TEAM_ACTIVITY_OK ("teamActivityOK", "tip", "teamActivityOK"),
    ;


    /**
     * 服务对应的key,路由路径
     */
    private String rout;
    /**
     * db中字典表对应的groupCode字段
     */
    private String groupCode;
    /**
     * db字典表对应的code字段
     */
    private String code;

    TipKeyEnum(String rout, String groupCode, String code) {
        this.rout = rout;
        this.groupCode = groupCode;
        this.code = code;
    }


    /**
     * 获取字典表对应的 groupCode code
     *
     * @param rout rout
     * @return nullable
     * @author Charlie
     * @date 2018/8/9 16:41
     */
    public static TipKeyEnum findByRout(String rout) {
        if (StringUtils.isBlank (rout)) {
            return null;
        }

        for (TipKeyEnum tip : TipKeyEnum.values ()) {
            if (tip.getRout ().equals (rout)) {
                return tip;
            }

        }

        return null;
    }

    public String getRout() {
        return rout;
    }

    public void setRout(String rout) {
        this.rout = rout;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
