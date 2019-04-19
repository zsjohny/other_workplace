/**
 * 
 */
package com.jiuy.web.controller.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyuan.util.anno.Login;

@RequestMapping("/product")
@Controller
@Login
public class ProductNewController{

	private static final Logger logger = LoggerFactory.getLogger(ProductNewController.class);

	
}
