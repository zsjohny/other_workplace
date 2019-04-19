package com.e_commerce.miscroservice.distribution.vo;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import lombok.Data;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/24 16:14
 */
@Data
public class AccountDTO {

    private Long userId;

    private ShopMemberAccount account;

    private Long cashOutId;


}
