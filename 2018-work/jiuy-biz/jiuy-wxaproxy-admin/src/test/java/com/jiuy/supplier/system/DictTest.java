package com.jiuy.supplier.system;

import javax.annotation.Resource;

import org.junit.Test;

import com.jiuy.supplier.base.BaseTest;
import com.jiuy.wxaproxy.common.system.service.IDictService;

/**
 * 字典服务测试
 *
 * @author fengshuonan
 * @date 2017-04-27 17:05
 */
public class DictTest extends BaseTest{

    @Resource
    IDictService dictService;

    @Test
    public void addTest() {
        dictService.addDict("测试","1:冻结;2:jiedong;3:接触");
    }

    @Test
    public void editTest() {
        dictService.editDict(1,"测试","");
    }
}
