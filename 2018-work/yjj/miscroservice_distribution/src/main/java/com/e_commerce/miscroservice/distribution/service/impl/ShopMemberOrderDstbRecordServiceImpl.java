package com.e_commerce.miscroservice.distribution.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberOrderDstbRecord;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.distribution.dao.ShopMemberOrderDstbRecordDao;
import com.e_commerce.miscroservice.distribution.service.ShopMemberOrderDstbRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/16 17:16
 * @Copyright 玖远网络
 */
@Service
public class ShopMemberOrderDstbRecordServiceImpl implements ShopMemberOrderDstbRecordService{

    @Autowired
    private ShopMemberOrderDstbRecordDao shopMemberOrderDstbRecordDao;

}
