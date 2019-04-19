package com.e_commerce.miscroservice.activity.PO;

import com.e_commerce.miscroservice.activity.entity.LotteryDrawActivity;
import com.e_commerce.miscroservice.activity.entity.PicturesCollection;
import lombok.Data;

import java.util.List;

/**
 * 抽奖活动内容
 * @Author hyf
 * @Date 2018/12/20 15:32
 */
@Data
public class LotteryDrawActivityPo extends LotteryDrawActivity {
    private List<PicturesCollection> list;
}
