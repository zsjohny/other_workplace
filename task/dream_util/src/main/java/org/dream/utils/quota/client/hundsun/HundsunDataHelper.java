package org.dream.utils.quota.client.hundsun;

import org.dream.utils.quota.handler.QuotaDataHandleService;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * 用户编写一些帮助类,或者保存一下数据
 * Created by yhj on 16/11/9.
 */
class HundsunDataHelper {

    private static Set<String> contractCodeSet = new HashSet<>();

    private static QuotaDataHandleService quotaDataHandleService;

    private static HundsunClient hundsunClient;


    static void putContractCode(String contractCode) {

        contractCodeSet.add(contractCode);
    }


    static boolean containContractCode(String contractCode) {

        return contractCodeSet.contains(contractCode);
    }

    static boolean isEmpty() {
        return contractCodeSet.isEmpty();
    }

    static boolean removeContractCode(String contractCode) {
        return contractCodeSet.remove(contractCode);
    }


    static boolean containContractCode(String product_id, String futureCode) {

        return contractCodeSet.contains(product_id + futureCode);
    }


    static QuotaDataHandleService getQuotaDataHandleService() {
        return quotaDataHandleService;
    }

    static void setQuotaDataHandleService(QuotaDataHandleService quotaDataHandleService) {
        HundsunDataHelper.quotaDataHandleService = quotaDataHandleService;
    }

    static void setHundsunClient(HundsunClient hundsunClient) {
        HundsunDataHelper.hundsunClient = hundsunClient;
    }

    static void flushLastUpdateTime() {
        hundsunClient.flushLastUpdateTime();
    }

    static ExecutorService getWorkThread(String contractCode) {

        return hundsunClient.getWorkThread(contractCode);

    }
}
