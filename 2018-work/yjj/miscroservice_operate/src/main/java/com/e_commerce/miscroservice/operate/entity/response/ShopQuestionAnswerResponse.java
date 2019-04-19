package com.e_commerce.miscroservice.operate.entity.response;

import com.e_commerce.miscroservice.commons.entity.application.user.ShopQuestionAnswer;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/19 11:54
 * @Copyright 玖远网络
 */
@Data
public class ShopQuestionAnswerResponse extends ShopQuestionAnswer {
    private String time;
    private String typeValue;

    public void setTime(String time) {
        this.time = TimeUtils.stamp2Str(getCreateTime());
    }

    public String getTime() {
        return time;
    }
}
