package com.onway.baib.core.cache;

import com.onway.baib.core.enums.SysConfigCacheKeyEnum;




/**
 * 系统参数缓存
 * 
 * @author guangdong.li
 * @version $Id: SysConfigCacheManager.java, v 0.1 17 Feb 2016 15:35:47 guangdong.li Exp $
 */
public interface SysConfigCacheManager {

    /**
     * 根据参数配置的key获取对应的value
     * 
     * @param configKeyEnum 配置的key
     * 
     * @return
     */
    public String getConfigValue(SysConfigCacheKeyEnum configKeyEnum);
}
