-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述


-- ------
-- store_refund_order
-- ------
-- 日期 2018年12月7日
-- ALTER TABLE `shop_product` ADD COLUMN `product_new_img` VARCHAR (255) DEFAULT NULL COMMENT '店中店商品分享二维码';
-- alter table `store_refund_order` modify column refund_status int(11);
