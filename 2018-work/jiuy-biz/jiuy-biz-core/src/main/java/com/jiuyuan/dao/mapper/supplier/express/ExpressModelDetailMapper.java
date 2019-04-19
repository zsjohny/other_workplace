package com.jiuyuan.dao.mapper.supplier.express;

import com.jiuyuan.dao.annotation.DBMaster;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.entity.express.ExpressModelDetail;

import java.math.BigDecimal;

/**
 * <p>
  * 运费模板详情 Mapper 接口
 * </p>
 *
 * @author Aison
 * @since 2018-04-27
 */
@DBMaster
public interface ExpressModelDetailMapper extends BaseMapper<ExpressModelDetail> {

    /**
     * 获取某个模板下面最小的运费值
     * @param modelId
     * @date:   2018/5/9 14:20
     * @author: Aison
     */
    BigDecimal selectMiniMoney(Long modelId);


}