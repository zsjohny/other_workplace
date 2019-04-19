package com.e_commerce.miscroservice.crm.entity.response;

import com.e_commerce.miscroservice.crm.po.CustomerPoolPO;
import lombok.Data;

/**
 * 公海池 拓展类
 * @author hyf
 * @version V1.0
 * @date 2018/9/14 14:16
 * @Copyright 玖远网络
 */
@Data
public class CustomerPoolResponse extends CustomerPoolPO {
    /**
     * 所属员工姓名
     */
    private String employeeName;

    private String time;

}
