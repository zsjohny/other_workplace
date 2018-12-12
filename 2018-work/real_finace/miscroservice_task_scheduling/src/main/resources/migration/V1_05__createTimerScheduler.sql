
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 定时任务调度表
-- ------
-- CREATE TABLE `t_timer_scheduling` (
--   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
--   `timer_scheduler_name` varchar(512) DEFAULT "" COMMENT '定时任务名称',
--   `uuid` varchar(512) DEFAULT "" COMMENT '定时任务uuid',
--   `timer_scheduler_cron` varchar(512) DEFAULT "" COMMENT '定时任务 时间表达式(eg: * * * * * ?)',
--   `timer_scheduler_param` varchar(512) DEFAULT "" COMMENT '定时任务参数(json格式)',
--   `timer_scheduler_type` int(11)  COMMENT '定时任务类型 1红包 ...更多待补充',
--    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
--   `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
--   PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*CREATE TABLE `t_timer_scheduling` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `timer_scheduler_name` varchar(512) DEFAULT '' COMMENT '定时任务名称',
  `uuid` varchar(512) DEFAULT '' COMMENT '定时任务uuid',
  `timer_scheduler_cron` varchar(512) DEFAULT '' COMMENT '定时任务 时间表达式(eg: * * * * * ?)',
  `timer_scheduler_param` varchar(512) DEFAULT '' COMMENT '定时任务参数(json格式)',
  `timer_scheduler_type` int(11) DEFAULT NULL COMMENT '定时任务类型 1红包 ...更多待补充',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/


INSERT INTO `t_timer_scheduling` (`timer_scheduler_name`,`uuid`,`timer_scheduler_cron`,`timer_scheduler_param`,`timer_scheduler_type`)
VALUES ('invest_statis_message','1','0 0 1 * * ?','{"type":"1"}', 2);
alter table t_timer_scheduling add unique index uuid (timer_scheduler_name,uuid)
