package com.jiuyuan.service.common.job;

import com.jiuyuan.builder.JobCallbackUrl;
import com.jiuyuan.builder.JobRequestBuilder;
import com.jiuyuan.constant.JobTaskType;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.util.JobDetailVo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.jiuyuan.constant.JobServerConfiguration.PRODUCT_PUTAWAY;
import static com.jiuyuan.constant.JobTaskType.ADD;
import static com.jiuyuan.constant.JobTaskType.DELETE;
import static com.jiuyuan.constant.JobTaskType.UPDATE;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 商品job service
 * @package jiuy-biz
 * @description
 * @date 2018/5/30 13:40
 * @copyright 玖远网络
 */
@Service
public class ProductJobService extends AbstractJobService{


    /**
     * 新建商品审核后定时上架
     *
     * @param time 定时上架的日期
     * @param supplierId       商品所属供应商id
     * @return is success ?
     * @auther Charlie(唐静)
     * @date 2018/5/30 14:12
     */
    public void timingProductPutaway(Long time, Long productId, Long supplierId) {
        /* 组装请求参数 */
        JobRequestBuilder builder = new JobRequestBuilder();

        // job任务注册
        builder.setJobGroupName(PRODUCT_PUTAWAY.getJobGroupName())
            .setJobName(PRODUCT_PUTAWAY.getJobName()+productId)
            .setCronExpression(IJobService.date2Cron(time))
            .setJobComment("供应商新建的商品定时上架 productId:"+productId);

        // 回调服务
        String callbackUrl = getDefaultCallbackURL(PRODUCT_PUTAWAY.getCallBackUrlCode());
        Map<String, String> data = new HashMap<>(2);
        data.put("supplierId", supplierId.toString());
        data.put("productId", productId.toString());
        String feedBackData = BizUtil.bean2json(data);
        JobCallbackUrl jobCallbackUrl = new JobCallbackUrl(callbackUrl, feedBackData);

        builder.setCallbackServer(jobCallbackUrl);

        try {
            //向job服务中新增一个定时任务
            long index = System.currentTimeMillis();
            JobResponse response = addJob("新增一个商品定时上架的任务 index:"+index, builder.build());
            if (response.ok()) {
                logger.info("新增一个商品定时上架的任务success index:"+index);
            }
            logger.warn("新增一个商品定时上架, 调用服务失败:"+response.msg());
            throw new RuntimeException(response.msg());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("设置新建商品审核后定时上架失败: " +e);
        }
    }


    /**
     * 新增,修改,删除 定时修改商品上架状态
     *
     * @param supplierId 供应商id      NoNull
     * @param addTask    需要添加的任务 NullAble
     * @param delTask    需要删除的任务 NullAble
     * @param updTask    需要修改的任务 NullAble
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/9 10:23
     */
    public void timingProductPutawayMulti(Collection<ProductDetail> addTask, Collection<ProductDetail> delTask
            , Collection<ProductDetail> updTask, Long supplierId) {

        if (addTask.isEmpty() && updTask.isEmpty() && delTask.isEmpty()) {
            throw new NullPointerException("批量修改sku库存任务, 请求任务列为为0");
        }

        List<JobDetailVo> addList = buildAddAndUpdJobDetail(addTask, ADD, supplierId);
        List<JobDetailVo> updList = buildAddAndUpdJobDetail(updTask, UPDATE, supplierId);
        List<JobDetailVo> delList = buildAddAndUpdJobDetail(delTask, DELETE, supplierId);

        try {
            long index = System.currentTimeMillis();
            //向job服务中新增一个定时任务
            JobResponse response = applyMultiJobTask("申请job服务, 定时修改商品上架状态 index:"+index, addList, updList, delList);
            if (! response.ok()) {
                logger.error("申请job服务, 定时修改商品上架状态error:" + response.msg());
                throw BizException.defulat().msg(response.msg());
            }else {
                logger.info("申请job服务, 定时修改商品上架状态success index:"+index);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("定时设置sku库存量, 申请job服务失败: " + e);
        }

    }

    private List<JobDetailVo> buildAddAndUpdJobDetail(Collection<ProductDetail> tasks, JobTaskType jobTaskType, Long supplierId) {

        if (tasks == null || tasks.isEmpty()) {
            return new ArrayList<>();
        }

        List<JobDetailVo> list = new ArrayList<>(tasks.size());
        String jobComment = "定时修改商品上架状态";
        for (ProductDetail detail : tasks) {
            JobDetailVo vo = new JobDetailVo();
            // job server 注册信息
            vo.setJobGroup(PRODUCT_PUTAWAY.getJobGroupName());
            vo.setJobName(PRODUCT_PUTAWAY.getJobName() + detail.getId().toString());
            vo.setJobComment(jobComment);
            vo.setJobType(jobTaskType.getType());
            String cronEps = jobTaskType.equals(DELETE) ? "" : IJobService.date2Cron(detail.getTimingPutwayTime());
            vo.setCronExpression(cronEps);

            // job server 回调URL 是删除则不设置
            if (jobTaskType.equals(DELETE)) {
                vo.setFeedbackUrl(null);
                vo.setFeedbackData(BizUtil.bean2json(null));
            }
            else if (jobTaskType.equals(ADD) || jobTaskType.equals(UPDATE)) {
                vo.setFeedbackUrl(getDefaultCallbackURL(PRODUCT_PUTAWAY.getCallBackUrlCode()));

                Map<String, String> data = new HashMap<>(2);
                data.put("supplierId", supplierId.toString());
                data.put("productId", detail.getProductId().toString());
                vo.setFeedbackData(BizUtil.bean2json(data));
            }
            else {
                throw new RuntimeException("设置定时修改商品上架状态, 不支持的jobType" + jobTaskType.getType());
            }

            list.add(vo);
        }
        return list;

    }
}
