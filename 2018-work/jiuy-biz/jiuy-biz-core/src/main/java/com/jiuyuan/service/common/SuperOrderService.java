package com.jiuyuan.service.common;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jiuyuan.dao.mapper.common.SuperOrderMapper;
import com.jiuyuan.entity.newentity.SuperOrder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Charlie
 * @since 2018-08-14
 */
@Service
public class SuperOrderService extends ServiceImpl<SuperOrderMapper, SuperOrder> implements ISuperOrderService {
	
}
