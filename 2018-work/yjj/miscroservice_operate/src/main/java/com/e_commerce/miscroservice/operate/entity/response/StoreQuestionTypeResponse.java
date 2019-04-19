package com.e_commerce.miscroservice.operate.entity.response;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjQuestionType;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 18:09
 * @Copyright 玖远网络
 */
public class StoreQuestionTypeResponse extends YjjQuestionType {
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        time= TimeUtils.stamp2Str(getCreateTime());
        this.time = time;
    }
}
