package com.jiuyuan.dao.mapper.supplier.express;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.express.ExpressModel;

import java.math.BigDecimal;

/**
 * <p>
  * 运费模板 Mapper 接口
 * </p>
 *
 * @author Aison
 * @since 2018-04-27
 */
@DBMaster
public interface ExpressModelMapper extends BaseMapper<ExpressModel> {


    /**
     * 获取某个供应商价格最低的运费
     * @param supplierId
     * @date:   2018/5/2 9:05
     * @author: Aison
     */
    ExpressModel selectMiniMoney(Long supplierId);
}