package com.jiuy.rb.service.common;

import com.jiuy.base.model.MyPage;
import com.jiuy.rb.model.common.ShopStoreAuthReasonRb;


/**
 *  一些通用类型的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 14:18
 * @Copyright 玖远网络
 */
public interface ICommonService {

    /**
     * 拒绝原因
     *
     * @param shopStoreAuthReasonRb 拒绝原因
     * @author Aison
     * @date 2018/6/13 14:20
     */
    MyPage<ShopStoreAuthReasonRb> authReasonList(ShopStoreAuthReasonRb shopStoreAuthReasonRb);

}
