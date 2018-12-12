package org.dream.utils.quota.handler;

import org.dream.model.order.FuturesContractsModel;

import java.util.List;

/**
 * Created by yhj on 16/10/27.
 */
public interface QuatoExchangeStatusHandler {

    boolean isSupport(Integer exchangeId);


    void resetTask(List<FuturesContractsModel> contractsModelList) ;

    /**
     * 新交易开始也是一个开市的事情.
     * 检查当前合约是否在假期里面
     * 重命名任务
     * 启动连接
     * @param contractsModelList
     */
    void onNewTradeStart(List<FuturesContractsModel> contractsModelList) ;

    /**
     * 修改分时图名称
     * 连接行情服务器
     * 修改此时访问行情内的状态
     * 数据处理状态标记为不处理,因为还没有开市,数据不正确
     * @param contractsModelList
     */
    void onGetQuota(List<FuturesContractsModel> contractsModelList) ;


    /**
     *
     * 开市, 表位数据正常处理
     * 记录分时图
     * @param contractsModelList
     */
    void onOpen(List<FuturesContractsModel> contractsModelList) ;


    /**
     * 断开连接
     * 记录分时图的最后一个记录
     * @param contractsModelList
     */
    void onClose(List<FuturesContractsModel> contractsModelList) ;

    /**
     * 当天交易结束也是一个闭市的事件
     * 修改此时访问行情内的状态
     * 计算日k
     * 闭市的相关功能
     * @param contractsModelList
     */
    void onTodayTradeEnd(List<FuturesContractsModel> contractsModelList) ;

}
