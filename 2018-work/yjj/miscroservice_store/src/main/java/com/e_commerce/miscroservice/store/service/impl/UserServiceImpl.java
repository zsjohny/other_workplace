package com.e_commerce.miscroservice.store.service.impl;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.store.dao.ProductDao;
import com.e_commerce.miscroservice.store.dao.StoreOrderDao;
import com.e_commerce.miscroservice.store.dao.ShopOrderItemDao;
import com.e_commerce.miscroservice.store.entity.response.OrderListResponse;
import com.e_commerce.miscroservice.store.entity.vo.OrderDetailsVo;
import com.e_commerce.miscroservice.store.service.ShopOrderService;
import com.e_commerce.miscroservice.store.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户
 * @author hyf
 * @version V1.0
 * @date 2018/9/26 17:27
 * @Copyright 玖远网络
 */
@Service
public class UserServiceImpl implements UserService {

    private Log logger = Log.getInstance(UserServiceImpl.class);

}
