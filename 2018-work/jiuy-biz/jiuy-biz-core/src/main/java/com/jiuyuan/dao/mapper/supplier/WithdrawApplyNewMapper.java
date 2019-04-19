package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;

/**
 * <p>
  * 商家提现申请审批表 Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2017-10-16
 */
@DBMaster
public interface WithdrawApplyNewMapper extends BaseMapper<WithdrawApplyNew> {
	
	public List<WithdrawApplyNew> search(@Param("page") Page<Map<String,Object>> page,
			                             @Param("tradeId")long tradeId, 
			                             @Param("tradeNo")String tradeNo, 
			                             @Param("status")int status, 
			                             @Param("type")int type, 
			                             @Param("startApplyMoney")double startApplyMoney, 
			                             @Param("endApplyMoney")double endApplyMoney, 
			                             @Param("startCreateTimeL")long startCreateTimeL,
			                             @Param("endCreateTimeL")long endCreateTimeL,
			                             @Param("supplierId")long supplierId);

}
