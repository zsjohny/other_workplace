package com.jiuy.rb.service.impl.common;

import com.jiuy.base.model.MyPage;
import com.jiuy.rb.mapper.common.ShopStoreAuthReasonRbMapper;
import com.jiuy.rb.model.common.ShopStoreAuthReasonRb;
import com.jiuy.rb.service.common.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 通用功能的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 14:19
 * @Copyright 玖远网络
 */
@Service("commonService")
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private ShopStoreAuthReasonRbMapper shopStoreAuthReasonRbMapper;

    /**
     * 拒绝原因
     *
     * @param shopStoreAuthReasonRb 拒绝原因
     * @author Aison
     * @date 2018/6/13 14:20
     */
    @Override
    public MyPage<ShopStoreAuthReasonRb> authReasonList(ShopStoreAuthReasonRb shopStoreAuthReasonRb) {

        return new MyPage<>(shopStoreAuthReasonRbMapper.selectList(shopStoreAuthReasonRb));
    }
}
