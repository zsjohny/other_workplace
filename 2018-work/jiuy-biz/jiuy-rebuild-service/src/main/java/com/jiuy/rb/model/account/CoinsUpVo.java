package com.jiuy.rb.model.account;

import lombok.Data;

/**
 * 专门做玖币更新的类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 14:54
 * @Copyright 玖远网络
 */
@Data
public class CoinsUpVo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 操作数量 加为整数 减为负数
     */
    private Long count;
}
