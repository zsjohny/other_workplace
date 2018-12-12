package com.goldplusgold.td.sltp.core.viewmodel;

import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;

import java.util.List;

/**
 *
 * 用户查询所有的止盈止损的vm
 * Created by Ness on 2017/5/23.
 */
public class UserSltpAllVM extends UserSltpVM {
    private static final long serialVersionUID = -1355659351767131018L;
    /**
     * 前端所有的userSltpRecord
     */
    public List<UserSltpRecord> data;

    public List<UserSltpRecord> getData() {
        return data;
    }

    public void setData(List<UserSltpRecord> data) {
        this.data = data;
    }

}
