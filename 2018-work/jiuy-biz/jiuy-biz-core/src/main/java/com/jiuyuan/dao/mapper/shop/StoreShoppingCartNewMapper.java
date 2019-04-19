package com.jiuyuan.dao.mapper.shop;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.CartItemNewVO;
import com.jiuyuan.entity.newentity.StoreShoppingCartNew;

/**
 * <p>
  * 购物车表 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-10
 */
@DBMaster
public interface StoreShoppingCartNewMapper extends BaseMapper<StoreShoppingCartNew> {

	List<CartItemNewVO> getCartItemMapList(Map<String,Object> map);

}