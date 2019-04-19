package com.jiuy.rb.service.product;

import com.jiuy.base.model.MyLog;
import com.jiuy.rb.model.product.SalesVolumePlainDetailRb;
import com.jiuy.rb.model.product.SalesVolumePlainRb;

import java.util.List;

/**
 * 销量添加回调
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/15 19:16
 * @Copyright 玖远网络
 */
public interface ISalesVolumeFeedbackService {

    /**
     * 初始化明细
     *
     * @param plain plain
     * @author Aison
     * @date 2018/6/15 16:58
     * @return List<SalesVolumePlainDetailRb>
     */
    List<SalesVolumePlainDetailRb> initExecute(SalesVolumePlainRb plain);

    /**
     * 处理回调
     *
     * @param detailRb plainRb
     * @param count count
     * @author Aison
     * @date 2018/6/15 19:22
     * @return doFeedback
     */
    MyLog<Long> doFeedback(SalesVolumePlainDetailRb detailRb, Long count);



    /**
     * 重新初始化
     * <P>
     *     如果是一次执行,不做任何操作,如果是轮询,更新初始化日期,如果是随机刷数量,还会更新预刷数量
     * </P>
     * @param plain plain
     * @author Aison
     * @date 2018/6/25 10:27
     */
    void reInit(SalesVolumePlainRb plain);
}
