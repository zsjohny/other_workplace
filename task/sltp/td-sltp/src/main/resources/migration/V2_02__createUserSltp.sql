
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `t_users`;
CREATE TABLE `t_user_sltp_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(512) NOT NULL COMMENT '用户Id',
  `uuid` varchar(512) DEFAULT NULL COMMENT '伪Id',
  `sl_price` double(4,0)   DEFAULT NULL COMMENT '止损价格',
  `tp_price` double(8,3) DEFAULT NULL COMMENT '止盈价格',
  `float_price` double(8,3) DEFAULT NULL COMMENT '浮动价格',
  `lots` int(4) DEFAULT NULL COMMENT '手数',
  `contract` varchar(64) DEFAULT NULL COMMENT '合约',
  `commission_price` double(8,3)  DEFAULT NULL COMMENT '委托价格',
  `commission_start_date` bigint(15)  COMMENT '委托触发时间',
  `commission_end_date` bigint(15)   COMMENT '触发结束时间',
  `commission_expire_type`  bit(1) DEFAULT 0 COMMENT '触发委托失效类型  目前只有默认',
  `bear_bull`  bit(1) DEFAULT NULL COMMENT '空头还是多头 0 空头 1 多头',
  `sltp_type`  bit(1) DEFAULT NULL COMMENT '止盈止损的类型 0是止损  1是止盈',
  `commission_result`  tinyint(1) DEFAULT NULL COMMENT '委托结果 0是成功  1是失败',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
