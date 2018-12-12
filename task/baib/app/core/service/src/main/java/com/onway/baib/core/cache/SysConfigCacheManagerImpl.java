package com.onway.baib.core.cache;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onway.common.lang.CollectionUtils;
import com.onway.baib.common.dal.daointerface.SysConfigDAO;
import com.onway.baib.common.dal.dataobject.SysConfigDO;
import com.onway.baib.common.util.CacheMessageUtil;
import com.onway.baib.core.enums.LocalCacheNameEnum;
import com.onway.baib.core.enums.SysConfigCacheKeyEnum;
import com.onway.platform.common.enums.BaseResultCodeEnum;
import com.onway.platform.common.localcache.AbstractLocalCache;
import com.onway.platform.common.localcache.exception.LocalCacheException;

/**
 * 系统参数缓存
 * 
 * @author guangdong.li
 * @version $Id: SysConfigCacheManagerImpl.java, v 0.1 17 Feb 2016 15:36:37 guangdong.li Exp $
 */
public class SysConfigCacheManagerImpl extends AbstractLocalCache implements SysConfigCacheManager {

    /**系统参数map*/
    private Map<String, String> configMap = new HashMap<String, String>();

    /**系统参数DAO*/
    private SysConfigDAO        sysConfigDAO;

    /** 
     * @see com.onway.platform.common.localcache.AbstractLocalCache#getName()
     */

    @Override
    public String getName() {
        return LocalCacheNameEnum.SYS_CONFIG.getCode();
    }

    /** 
     * @see com.onway.platform.common.localcache.AbstractLocalCache#doRefresh()
     */
    @Override
    protected void doRefresh() {

        List<SysConfigDO> sysConfigList = sysConfigDAO.loadAll();

        if (CollectionUtils.isEmpty(sysConfigList)) {
            throw new LocalCacheException(BaseResultCodeEnum.DATA_ERROR.getCode());
        }

        Map<String, String> tmpConfigMap = new HashMap<String, String>();

        for (SysConfigDO sysConfig : sysConfigList) {
            tmpConfigMap.put(sysConfig.getSysKey(), sysConfig.getSysValue());
        }

        configMap = tmpConfigMap;
    }

    /** 
     * @see com.onway.platform.common.localcache.AbstractLocalCache#dump()
     */
    @Override
    public void dump() {

        if (CACHE_LOGGER.isInfoEnabled()) {

            StringBuilder builder = new StringBuilder();

            builder.append("\n====开始输出本地缓存[").append(getName()).append("]====");

            if (configMap == null) {
                builder.append("\n" + getName() + "缓存为空");
            } else {
                builder.append(CacheMessageUtil.map2String(configMap));
            }

            builder.append("\n====本地缓存输出完成[").append(getName()).append("]====\n");

            CACHE_LOGGER.info(builder.toString());
        }

    }

    /** 
     * @see com.onway.baib.core.cache.treasure.core.localcache.manager.SysConfigCacheManager#getConfigValue(java.lang.String)
     */
    @Override
    public String getConfigValue(SysConfigCacheKeyEnum configKeyEnum) {
        return configMap.get(configKeyEnum.getCode());

    }

    /**
     * Setter method for property <tt>sysConfigDAO</tt>.
     * 
     * @param sysConfigDAO value to be assigned to property sysConfigDAO
     */
    public void setSysConfigDAO(SysConfigDAO sysConfigDAO) {
        this.sysConfigDAO = sysConfigDAO;
    }

}
