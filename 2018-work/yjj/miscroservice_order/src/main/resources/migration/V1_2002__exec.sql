-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述


-- ------
-- 用户表
-- ------

-- alter TABLE `yjj_storebusiness` add column `auth_id` BIGINT(20) DEFAULT '0' COMMENT '认证id';

---------------------------------版本1------------------------------------------------------

---------------------------------版本1------------------------------------------------------
-- ALTER TABLE `jiuy_store_wxa` ADD COLUMN `status` SMALLINT (4) DEFAULT '0' COMMENT '状态0正常,1删除,2草稿(作废)';

ALTER TABLE store_order ADD COLUMN `type` SMALLINT(4) DEFAULT '0' COMMENT '0 普通, 1,进货金专属(平台代发货)';
ALTER TABLE store_order ADD COLUMN `shop_member_order_id` bigint(20) DEFAULT '0' COMMENT '关联的小程序订单(当前业务:当type=4时是平台代发货)';