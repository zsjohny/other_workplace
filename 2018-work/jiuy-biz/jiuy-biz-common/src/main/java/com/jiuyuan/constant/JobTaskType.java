package com.jiuyuan.constant;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title job服务类型
 * @package jiuy-biz
 * @description
 * @date 2018/6/8 12:35
 * @copyright 玖远网络
 */
public enum JobTaskType{

    /**
     * 新增一个task
     */
    ADD(1),
    /**
     * 修改一个task
     */
    UPDATE(2),
    /**
     * 暂停一个task
     */
    PAUSE(3),
    /**
     * 暂停一个task
     */
    RESTART(4),
    /**
     * 删除一个task
     */
    DELETE(5);

    private Integer type;

    JobTaskType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 注册相同jobName的job任务, 判断申请job的类型
     *
     * <p>根据是否有未执行的历史job任务, 判断新加入的job任务是增?删?改?
     * @param hasUnDoTask 是否有未执行的job任务
     * @param isNewTaskValid 新的job任务是否有效
     * @return com.jiuyuan.constant.JobTaskType
     * @auther Charlie(唐静)
     * @date 2018/6/9 9:52
     */
    public static JobTaskType jobTaskUpgradingJudge(boolean hasUnDoTask, boolean isNewTaskValid) {
        if (hasUnDoTask && isNewTaskValid) {
            // 有未执行task, 新task是有效的
            return UPDATE;
        }
        else if (hasUnDoTask) {
            // 有未执行task, 新task是无效的
            return DELETE;
        }
        else if (isNewTaskValid) {
            // 没有未执行task, 新task是有效的
            return ADD;
        }
        else {
            // 没有未执行task, 新task是无效的
            return null;
        }
    }
}
