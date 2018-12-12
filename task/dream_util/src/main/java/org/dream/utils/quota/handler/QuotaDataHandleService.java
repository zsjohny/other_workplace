package org.dream.utils.quota.handler;

import org.dream.model.quota.Quota;

/**
 * 最新行情处理Service
 * Created by yhj on 16/11/3.
 */
public interface QuotaDataHandleService {

    /*
     * 1.处理行情对象中的相关数据，主要是行情价格的小数位数的问题
     * 2.将该条行情纪录保存到Mongodb中
     * 3.验证该条行情数据是否正确 卖一价、买一价和最新价是否是正的无穷大，如果其中以一个价格是正的无穷大的时候则用内存中相应的纪录的价格覆盖当前价格
     * 4.通过验证则将该条行情纪录按照合约代码保存到内存中。
     * 5.将该条行情写入到本地文件中，为分时图提供数据基础（注意合约编码，不要写错了文件）
     *
     */


    /**
     * 行情数据处理类
     * @param quota
     */
    public void quotaHandle(Quota quota);

    /**
     * 获取当前在线的合约代码
     * @param contractCode
     * @return
     */
    public Integer getOnlineContractByOpen(String contractCode);
}
