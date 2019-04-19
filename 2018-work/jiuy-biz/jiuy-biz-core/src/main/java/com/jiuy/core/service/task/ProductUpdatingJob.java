/**
 * 
 */
package com.jiuy.core.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiuy.core.service.ProductService;

/**
 * @author LWS
 * 
 * 执行产品的自动上架、下架工作
 *
 */
@Component
public class ProductUpdatingJob {
	
	@Autowired
	private ProductService productService;

	public void execute(){
		productService.updateProductStatus();
	}
}
