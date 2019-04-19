/**
 * 
 */
package com.yujj.business.service.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yujj.business.facade.SearchFacade;

/**
* @author DongZhong
* @version 创建时间: 2016年10月8日 上午9:59:51
*/
@Component
public class IndexCreateJob {

	@Autowired
	private SearchFacade searchFacade;
	
	public void execute(){
		searchFacade.ReCreateIndex();
	}
}
