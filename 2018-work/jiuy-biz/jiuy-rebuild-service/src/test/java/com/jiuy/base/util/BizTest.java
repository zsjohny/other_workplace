package com.jiuy.base.util;

import org.junit.Test;
import org.springframework.util.Assert;


/**
 * TODO
 *
 * @author Charlie(唐静)
 * @version V1.0
 * @Copyright 玖远网络
 * @date ${date} ${time}
 */
public class BizTest{

    @Test
    public void atLeastOneEquals() {
        Assert.isTrue(!Biz.atLeastOneEquals("1", 1, 2, 3, 4));
        Assert.isTrue(Biz.atLeastOneEquals("1", "1", 2, 3, 4));
        Assert.isTrue(!Biz.atLeastOneEquals(null, "1", 2, 3, 4));
        Assert.isTrue(!Biz.atLeastOneEquals("1"));
        Assert.isTrue(! Biz.atLeastOneEquals(1, null));
    }
}
