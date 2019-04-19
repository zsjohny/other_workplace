package com.jiuyuan.dao.mapper.store;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.store.SmartModule;


/**
 * <p>
  * app门店智能模块 Mapper 接口
 * </p>
 *
 * @author Charlie(唐静)
 * @since 2018-05-09
 */
@DBMaster
public interface SmartModuleMapper extends BaseMapper<SmartModule>{

    /**
     * 批量更新门店模块
     * @param smartModule
     */
    void updSmartModule(SmartModule smartModule);

}