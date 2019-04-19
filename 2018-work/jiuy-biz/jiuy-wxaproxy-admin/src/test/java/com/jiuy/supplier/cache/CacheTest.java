package com.jiuy.supplier.cache;

import java.util.List;

import org.junit.Test;

import com.admin.common.constant.cache.CacheConst;
import com.admin.core.cache.CacheKit;
import com.alibaba.fastjson.JSON;
import com.jiuy.supplier.base.BaseTest;
import com.jiuy.wxaproxy.common.constant.factory.WxaproxyConstantFactory;

/**
 * 缓存测试
 *
 * @author fengshuonan
 * @date 2017-04-24 21:00
 */
public class CacheTest extends BaseTest {

    /**
     * 测试没有缓存的情况
     *
     * @author fengshuonan
     * @Date 2017/4/24 21:00
     */
    @Test
    public void testNoCache() {
        long beginTIme = System.currentTimeMillis();

        //用缓存200万次查询,速度6秒
        for (int i = 1; i < 2000000; i++) {
            WxaproxyConstantFactory.me().getSingleRoleName(1);
        }

        System.out.println(System.currentTimeMillis() - beginTIme);

        System.out.println();

        CacheKit.removeAll(CacheConst.CONSTANT);

        List constant1 = CacheKit.getKeys(CacheConst.CONSTANT);
        System.out.println(JSON.toJSONString(constant1));
    }
}
