package com.e_commerce.miscroservice.operate.entity.response;

import com.e_commerce.miscroservice.commons.entity.activity.LotteryDrawActivity;
import com.e_commerce.miscroservice.commons.entity.activity.PicturesCollection;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/18 17:34
 * @Copyright 玖远网络
 */
@Data
public class LotteryDrawPo extends LotteryDrawActivity {
    private String strCreateTime;
    private String strUpdateTime;
    private String pic;
    private List<PicturesCollection> list;

    public void setStrCreateTime(Timestamp time) {
        this.strCreateTime = TimeUtils.stamp2Str(time);
    }

    public void setStrUpdateTime(Timestamp time) {
        this.strUpdateTime = TimeUtils.stamp2Str(time);
    }
}
