package com.e_commerce.miscroservice.activity.PO;

import com.e_commerce.miscroservice.activity.entity.channel.ChannelOrderRecord;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/26 16:23
 * @Copyright 玖远网络
 */
@Data
public class ChannelOrderRecordVO extends ChannelOrderRecord {
    private Long storeId;
}
