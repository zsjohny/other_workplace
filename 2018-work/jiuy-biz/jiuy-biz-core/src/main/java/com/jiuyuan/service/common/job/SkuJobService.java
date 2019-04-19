package com.jiuyuan.service.common.job;

import com.jiuyuan.constant.JobTaskType;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.util.JobDetailVo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.jiuyuan.constant.JobServerConfiguration.SKU_SET_REMAIN_COUNT;
import static com.jiuyuan.constant.JobTaskType.ADD;
import static com.jiuyuan.constant.JobTaskType.DELETE;
import static com.jiuyuan.constant.JobTaskType.UPDATE;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title sku job service
 * @package jiuy-biz
 * @description
 * @date 2018/06/07 13:40
 * @copyright 玖远网络
 */
@Service
public class SkuJobService extends AbstractJobService{


    /**
     * 新增一个修改sku库存job任务
     *
     * @param supplierId 供应商id          NoNull
     * @param task       需要添加/修改的任务 NoNull
     * @param productMap 需要添加/修改任务时,Sku对应的product map(key productId, value Product) NoNull
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/9 10:23
     */
    public void timingSetRemainCountAdd(Collection<ProductSkuNew> task, Map<Long, ProductNew> productMap, Long supplierId) {
        timingSetRemainCountMulti(task, null, null, productMap, supplierId);
    }

    /**
     * 新增,修改,删除 sku库存job任务
     *
     * @param supplierId 供应商id      NoNull
     * @param addTask    需要添加的任务 NullAble
     * @param delTask    需要删除的任务 NullAble
     * @param updTask    需要修改的任务 NullAble
     * @param productMap 需要修改任务时,Sku对应的product map(key productId, value Product) NullAble
     *                   当updTask不为空时, productMap也不可为空
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/9 10:23
     */
    public void timingSetRemainCountMulti(Collection<ProductSkuNew> addTask, Collection<ProductSkuNew> delTask
            , Collection<ProductSkuNew> updTask, Map<Long, ProductNew> productMap, Long supplierId) {

        if (addTask.isEmpty() && updTask.isEmpty() && delTask.isEmpty()) {
            throw new NullPointerException("批量申请定时修改sku库存任务, 请求任务列为为0");
        }

        List<JobDetailVo> addList = buildAddAndUpdJobDetail(addTask, ADD, productMap, supplierId);
        List<JobDetailVo> updList = buildAddAndUpdJobDetail(updTask, UPDATE, productMap, supplierId);
        List<JobDetailVo> delList = buildAddAndUpdJobDetail(delTask, DELETE, productMap, supplierId);

        try {
            //向job服务中新增一个定时任务
            long index = System.currentTimeMillis();
            JobResponse response = applyMultiJobTask("批量申请job服务, 定时修改sku库存 index:" + index, addList, updList, delList);
            if (response.ok()) {
                logger.info("批量申请job服务, 定时修改sku库存success index:" + index);
            }
            else {
                logger.error("批量申请job服务, 定时修改sku库存error:" + response.msg());
                throw BizException.defulat().msg(response.msg());
            }
        } catch (IOException e) {
            throw BizException.defulat().msg("定时设置sku库存量, 申请job服务失败: " + e);
        }
    }


    /**
     * 构件增加,修改一个job任务的服务地址
     *
     * @param taskList
     * @param productMap
     * @param supplierId
     * @param jobTaskType job 任务服务类型
     * @return java.util.List<com.util.JobDetailVo>
     * @auther Charlie(唐静)
     * @date 2018/6/10 9:46
     */
    private List<JobDetailVo> buildAddAndUpdJobDetail(Collection<ProductSkuNew> taskList, JobTaskType jobTaskType, Map<Long, ProductNew> productMap, Long supplierId) {

        if (taskList == null || taskList.isEmpty()) {
            return new ArrayList<>();
        }

        List<JobDetailVo> list = new ArrayList<>(taskList.size());
        String jobComment = "添加定时修改库存";
        for (ProductSkuNew sku : taskList) {
            JobDetailVo vo = new JobDetailVo();
            // job server 注册信息
            vo.setJobGroup(SKU_SET_REMAIN_COUNT.getJobGroupName());
            vo.setJobName(SKU_SET_REMAIN_COUNT.getJobName() + sku.getId().toString());
            vo.setJobComment(jobComment);
            vo.setJobType(jobTaskType.getType());
            String cronEps = jobTaskType.equals(DELETE) ? "" : formatApplyTime(sku, productMap.get(sku.getProductId()));
            vo.setCronExpression(cronEps);

            // job server 回调URL 是删除则不设置
            if (jobTaskType.equals(DELETE)) {
                vo.setFeedbackUrl(null);
                vo.setFeedbackData(BizUtil.bean2json(null));
            }
            else if (jobTaskType.equals(ADD) || jobTaskType.equals(UPDATE)) {
                vo.setFeedbackUrl(getDefaultCallbackURL(SKU_SET_REMAIN_COUNT.getCallBackUrlCode()));

                Map<String, String> data = new HashMap<>(2);
                data.put("skuId", String.valueOf(sku.getId()));
                data.put("supplierId", String.valueOf(supplierId));
                data.put("count", String.valueOf(sku.getTimingSetCount() == null ? 0 : sku.getTimingSetCount()));
                vo.setFeedbackData(BizUtil.bean2json(data));
            }
            else {
                throw new RuntimeException("设置定时修改库存, 不支持的jobType" + jobTaskType.getType());
            }

            list.add(vo);
        }
        return list;
    }


    /**
     * 格式化申请日期, 转换成cron表达式
     * <p>
     * 根据设置定时上架的类型不同, 格式化时间
     *
     * @param sku     NoNull
     * @param product NullAble
     * @return java.lang.String
     * @auther Charlie(唐静)
     * @date 2018/6/10 8:40
     */
    private String formatApplyTime(ProductSkuNew sku, ProductNew product) {
        Integer type = sku.getTimingSetType();
        switch (type) {
            case 0:
                return "";
            case 1:
                return IJobService.date2Cron(sku.getTimingSetRemainCountTime());
            case 2:
                // 最后上架时间+设定时间
                long fmtTime = product.getLastPutonTime() + sku.getTimingSetRemainCountTime() * 24 * 3600 * 1000;
                return IJobService.date2Cron(fmtTime);
            default:
                throw new IllegalArgumentException("未知的类型 TimingSetType:" + type);
        }
    }

}
