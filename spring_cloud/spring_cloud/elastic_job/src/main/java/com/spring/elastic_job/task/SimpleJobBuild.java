package com.spring.elastic_job.task;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import javax.sql.DataSource;

/**
 * 简单任务的相关配置
 */
public class SimpleJobBuild {


    /**
     * 简单任务使用
     *
     * @param serverLists  注册服务列表 例如 192.168.89.128:2181,192.168.89.128:2182
     * @param namespace    当前的命名空间
     * @param jobClass     简单作业的类
     * @param jobName      作业的名称
     * @param cron         作业的表达式
     * @param jobParameter 工作任务的参数
     * @param dataSource   数据源 可无
     * @param params       参数数组
     */
    public void start(String serverLists, String namespace, Class<? extends Job> jobClass,
                      String jobName, String cron,
                      String jobParameter, DataSource dataSource, String... params) {
        ZookeeperRegistryCenter registerInfo = createRegisterInfo(serverLists, namespace);
        LiteJobConfiguration simpleJobWork = createSimpleJobWork(jobClass, jobName,
                cron, params.length, JobParseUtil.arr2HexString(params), jobParameter);
        JobScheduler jobScheduler;
        if (dataSource == null) {
            jobScheduler = new JobScheduler(registerInfo, simpleJobWork);
        } else {
            jobScheduler = new JobScheduler(registerInfo, simpleJobWork, createSaveDatasource(dataSource));
        }


        jobScheduler.init();

//


    }

    /**
     * 设置注册中心
     *
     * @param serverLists 注册服务列表 例如 192.168.89.128:2181,192.168.89.128:2182
     * @param namespace   当前的命名空间
     * @return
     */
    private ZookeeperRegistryCenter createRegisterInfo(String serverLists, String namespace) {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, namespace);
        zookeeperConfiguration.setMaxRetries(6);
        //KeeperErrorCode = OperationTimeout 如果报错则增加重试次数
        ZookeeperRegistryCenter registryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
        registryCenter.init();

        return registryCenter;
    }

    /**
     * 创建保存的数据源
     *
     * @param dataSource 数据源
     * @return
     */
    private JobEventConfiguration createSaveDatasource(DataSource dataSource) {
        return new JobEventRdbConfiguration(dataSource);
    }

    /**
     * 创建简单的作业
     *
     * @param jobClass               简单作业的类
     * @param jobName                作业的名称
     * @param cron                   作业的表达式
     * @param shardingTotalCount     总的分片数目
     * @param shardingItemParameters 分片的参数
     * @param jobParameter           工作任务的参数
     * @return
     */
    private LiteJobConfiguration createSimpleJobWork(Class<? extends Job> jobClass,
                                                     String jobName, String cron,
                                                     int shardingTotalCount, String shardingItemParameters, String jobParameter) {
        //创建简单作业配置构建器.
        JobCoreConfiguration.Builder builder =
                JobCoreConfiguration.newBuilder(jobName,
                        cron, shardingTotalCount);


        //设置分片参数
        builder.shardingItemParameters(shardingItemParameters);

        builder.jobParameter(jobParameter);
        //创建作业核心配置
        JobCoreConfiguration coreConfig = builder.build();


        //创建作业类型配置
        SimpleJobConfiguration
                simpleJobConfig = new SimpleJobConfiguration(coreConfig, jobClass.getCanonicalName());

        LiteJobConfiguration.Builder jobBuilder = LiteJobConfiguration.newBuilder(simpleJobConfig);
        LiteJobConfiguration liteJobConfiguration = jobBuilder.build();

        return liteJobConfiguration;

    }


}
