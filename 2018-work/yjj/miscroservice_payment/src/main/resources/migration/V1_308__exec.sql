-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述

-- 店中店多加的字段,默认值是空 18/12/17
-- alter table jiuy_store_wxa MODIFY `in_shop_open_id` varchar(50) DEFAULT NULL  COMMENT '店中店openId(作废)';

-- ALTER TABLE shop_goods_car modify `sku_number` bigint(11) DEFAULT '1' COMMENT '商品数量';
-- ALTER TABLE shop_goods_car modify `car_suk_status` int(4) DEFAULT '1' COMMENT '购物车中商品状态  -1 删除，0禁用, 1正常，2失效';
-- ALTER TABLE shop_goods_car modify `selected` int(4) DEFAULT '0' COMMENT '是否选择';

-- 店中店二维码清空
-- UPDATE `yjj_store_wxa_shop_audit_data` SET share_qr_code_url = NULL
-- WHERE store_id IN (12458, 11671, 11758);
--
-- UPDATE `yjj_store_wxa_shop_audit_data` AS a ,
-- `yjj_store_wxa_shop_audit_data` AS b
-- SET a.boss_name = b.shop_name, a.shop_name = b.boss_name
-- WHERE a.id = b.id
-- ;

-- UPDATE `yjj_whitephone` SET Phone = '13429177360', `Name` = '院玲测试' where Id = 0;

-- UPDATE `store_biz_order` SET real_price = '0.01',total_price='0.01' AND order_no = '2019011111095202400118459242';
-- ALTER TABLE `shop_member_delivery_address` ADD COLUMN `default_status` int(20) DEFAULT '0' COMMENT '默认地址:0不是  1:默认';