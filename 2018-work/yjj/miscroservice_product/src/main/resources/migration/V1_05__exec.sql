-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述

-- APP用户表加店中店openId
-- ALTER TABLE yjj_StoreBusiness ADD in_shop_open_id VARCHAR (50) COMMENT '店中店openId';

--
-- ALTER TABLE shop_goods_car modify `sku_number` bigint(11) DEFAULT '1' COMMENT '商品数量';
-- ALTER TABLE shop_goods_car modify `car_suk_status` int(4) DEFAULT '1' COMMENT '购物车中商品状态  -1 删除，0禁用, 1正常，2失效';
-- ALTER TABLE shop_goods_car modify `selected` int(4) DEFAULT '0' COMMENT '是否选择';

-- 直播 订单添加直播商品id
ALTER TABLE shop_member_order_item ADD live_product_id bigint (11) DEFAULT '0' COMMENT '直播商品id';

