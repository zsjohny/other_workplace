package com.jiuy.rb.mapper.account; 
 
import com.jiuy.rb.model.account.CoinsCashOut; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.account.CoinsCashOutQuery;

import java.util.List;

/** 
 * 提现记录表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月23日 上午 10:15:35
 * @Copyright 玖远网络 
 */
public interface CoinsCashOutMapper extends BaseMapper<CoinsCashOut>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 提现记录列表 与用户表关联
     *
     * @param query
     * @author Aison
     * @date 2018/7/19 9:47
     * @return List<CoinsCashOutQuery>
     */
    List<CoinsCashOutQuery> selectCashOutList(CoinsCashOutQuery query);

}
