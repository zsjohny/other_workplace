package com.goldplusgold.td.sltp.core.viewmodel;

import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;

/**
 * 用户查询单个的止盈止损
 * Created by Ness on 2017/5/23.
 */
public class UserSltpOneVM extends UserSltpVM {
    private static final long serialVersionUID = 1057204433425134611L;
    /**
     * 前端单个的userSltpRecord
     */
    public UserSltpRecord data;

    public UserSltpRecord getDeata() {
        return data;
    }

    public void setData(UserSltpRecord data) {
        this.data = data;
    }

}
