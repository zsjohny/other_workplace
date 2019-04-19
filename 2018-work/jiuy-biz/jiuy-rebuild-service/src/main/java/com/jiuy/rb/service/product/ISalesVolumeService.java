package com.jiuy.rb.service.product;

import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.product.SalesVolumePlainRb;
import com.jiuy.rb.model.product.SalesVolumePlainRbQuery;
import com.jiuy.rb.model.product.SalesVolumeProductRb;


/**
 * 商品销量相关的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/15 11:00
 * @Copyright 玖远网络
 */
public interface ISalesVolumeService {

    /**
     * 添加商品刷量策略
     *
     * @param plain plain
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/15 11:03
     * @return  MyLog
     */
     MyLog<SalesVolumePlainRb> addSalesVolumePlain(SalesVolumePlainRb plain, UserSession userSession);

    /**
     * 修改策略
     *
     * @param plain plain
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/15 11:03
     * @return MyLog
     */
    MyLog<SalesVolumePlainRb> updateSalesVolumePlain(SalesVolumePlainRb plain, UserSession userSession);

    /**
     * 分页
     *
     * @param query query
     * @author Aison
     * @date 2018/6/19 11:05
     * @return MyLog
     */
    MyPage<SalesVolumePlainRbQuery> plainPage(SalesVolumePlainRbQuery query);

    

    /**
     * 回调修改商品销量
     *
     * @param id  id
     * @param comment comment
     * @author Aison
     * @date 2018/6/15 16:52
     */
    MyLog<Long> feedbackPlain(Long id,String comment,UserSession optUser);

    /**
     * 初始化数量
     *
     * @param source
     * @author Aison
     * @date 2018/6/19 14:15
     */

    void  initEachAddCount(SalesVolumePlainRb source);



    /**
     * 更新销量信息,如果没有就新增
     *
     * @param salesPdvInfo
     *  需要 productId,productType
     * @param addInfo
     *  如果不为null,则在原来的基础上加(而不是直接替换),是null则忽视
     * @return int
     * @author Charlie
     * @date 2018/8/5 23:40
     */
    int updOrAddSalesVolumeCount(SalesVolumeProductRb salesPdvInfo, SalesVolumeProductRb addInfo);

}
