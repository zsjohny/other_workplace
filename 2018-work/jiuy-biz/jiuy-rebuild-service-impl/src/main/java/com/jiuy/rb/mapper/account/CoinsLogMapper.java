package com.jiuy.rb.mapper.account; 
 
import com.jiuy.rb.model.account.CoinsLog; 
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.account.CoinsLogQuery;

import java.util.List;

/** 
 * 玖币操作明细 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月12日 上午 10:21:57
 * @Copyright 玖远网络 
 */
public interface CoinsLogMapper extends BaseMapper<CoinsLog>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 进出张的分组
     *
     * @param logQuery logQuery
     * @author Aison
     * @date 2018/7/12 14:54
     * @return java.util.List<com.jiuy.rb.model.account.CoinsLog>
     */
    List<CoinsLog> selectInOutGroup(CoinsLogQuery logQuery);

}
