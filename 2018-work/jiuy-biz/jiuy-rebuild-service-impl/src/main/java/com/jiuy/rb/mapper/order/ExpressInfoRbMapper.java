package com.jiuy.rb.mapper.order; 
 
import com.jiuy.rb.model.order.ExpressInfoRb; 
import com.jiuy.base.mapper.BaseMapper;

/** 
 * 邮寄信息表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年06月28日 下午 05:24:48
 * @Copyright 玖远网络 
 */
public interface ExpressInfoRbMapper extends BaseMapper<ExpressInfoRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 通过订单号来更新
     *
     * @param expressInfoRb expressInfoRb
     * @author Aison
     * @date 2018/6/29 12:08
     * @return int
     */
    int updateByOrderNo(ExpressInfoRb expressInfoRb);

}