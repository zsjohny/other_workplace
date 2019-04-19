package com.e_commerce.miscroservice.store.controller;

import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/5 16:44
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "shop/common" )
public class CommonController{

    private Log logger = Log.getInstance (CommonController.class);

}
