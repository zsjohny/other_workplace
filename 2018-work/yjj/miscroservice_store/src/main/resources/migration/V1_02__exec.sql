-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述
-- 2018年9月29日
-- alter TABLE `store_refund_order` add column `store_order_item_id` tinyint DEFAULT NULL COMMENT '订单详情id';
-- 2018年12月15日
-- ALTER TABLE `store_refund_order` ADD COLUMN `store_back_money` DOUBLE (11, 2) DEFAULT '0' COMMENT '商家给定退款金额';

-- ALTER TABLE `store_refund_order` ADD COLUMN `real_back_money` DOUBLE (11, 2) DEFAULT '0' COMMENT '实际退款金额';