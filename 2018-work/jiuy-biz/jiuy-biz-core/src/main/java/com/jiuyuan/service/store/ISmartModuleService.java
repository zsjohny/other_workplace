package com.jiuyuan.service.store;

import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.SmartModule;

import java.util.List;

/**
 * <p>
 * app门店智能模块 服务类
 * </p>
 *
 * @author Charlie(唐静)
 * @since 2018-05-09
 */
public interface ISmartModuleService {
    /**
     * 查询门店模块
     *
     * @param storeId 门店id
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    List<SmartModule> getByStoreId(Long storeId);

    /**
     * 批量更新模块
     *
     * @param list
     * @param currentStoreId 当前用户storeId
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    boolean batchUpd(List<SmartModule> list, Long currentStoreId);

    /**
     * 更新模块
     *
     * @param smartModule
     * @param currentStoreId 当前用户storeId
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    boolean upd(SmartModule smartModule, Long currentStoreId);


    /**
     * 新增当前用户的模板
     *
     * @param currentStoreId
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    void add(Long currentStoreId, StoreBusiness store);

    void add(Long currentStoreId, String openFlag);

    /**
     * 更新只能模块文章信息, 向372版本之前做历史兼容
     *
     * @param newStoreBusiness
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/1 16:40
     */
    void closeOrOpenArticleV372(StoreBusiness newStoreBusiness);
}
