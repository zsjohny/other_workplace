package org.dream.utils.quota.client;

import org.dream.utils.quota.OrderQuotaInfoModel;
import org.dream.utils.quota.handler.QuotaDataHandleService;

/**
 * Created by yhj on 16/10/26.
 */
public interface QuotaClient {
    public static final Long DELAY = 10000L;
    public static final Long SUB_DELAY = 2000L;

    public static final Integer INTERVAL = 60000;
    public static final Integer SUB_INTERVAL = 40000;



    /**
     *
     * 容器退出 强制销毁所有资源
     *
     */
    public boolean destory()  ;

    public boolean subscribe(OrderQuotaInfoModel... params) throws Exception;

    public boolean unSubscribe(OrderQuotaInfoModel... params) throws Exception;

    /**
     * 设置回调类
     * @param service
     * @return
     */
    public void setQuotaDataHandleService(QuotaDataHandleService service);
}
