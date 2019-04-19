
-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--   统计模块
-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- 设备每次打开的时候的记录
-- ----------------------------
-- Table structure for yjj_monitoring_device
-- ----------------------------
DROP TABLE IF EXISTS `yjj_monitoring_device`;
CREATE TABLE `yjj_monitoring_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` varchar(64) NOT NULL COMMENT '设备id',
  `open_time` timestamp NULL DEFAULT NULL COMMENT '打开设备时间',
  `close_time` timestamp NULL DEFAULT NULL COMMENT '关闭设备时间',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `version` varchar(12) NOT NULL COMMENT 'app版本号',
  `os_name` varchar(12) DEFAULT NULL COMMENT '系统名称',
  `app_stay_time` bigint(11) DEFAULT '0' COMMENT 'app停留时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8200 DEFAULT CHARSET=utf8 COMMENT='设备打开记录';

-- 每次进入某个页面的时候的记录
-- ----------------------------
-- Table structure for yjj_monitoring_page
-- ----------------------------
DROP TABLE IF EXISTS `yjj_monitoring_page`;
CREATE TABLE `yjj_monitoring_page` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` varchar(64) NOT NULL COMMENT '设备id',
  `phone` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `enter_time` timestamp NULL DEFAULT NULL COMMENT '进入页面的时间',
  `leave_time` timestamp NULL DEFAULT NULL COMMENT '离开时间',
  `event_count` int(4) DEFAULT '0' COMMENT '点击/事件次数',
  `page_name` varchar(255) NOT NULL COMMENT '页面名称',
  `page_code` varchar(32) NOT NULL COMMENT '页面编码',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `version` varchar(12) NOT NULL COMMENT 'app版本号',
  `os_name` varchar(12) DEFAULT NULL COMMENT '系统名称',
  `page_stay_time` bigint(11) DEFAULT '0' COMMENT '页面停留时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54824 DEFAULT CHARSET=utf8 COMMENT='每个页面的访问记录';


-- 与app交互的元数据
-- ----------------------------
-- Table structure for yjj_monitoring_metadata
-- ----------------------------
DROP TABLE IF EXISTS `yjj_monitoring_metadata`;
CREATE TABLE `yjj_monitoring_metadata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data` longtext NOT NULL COMMENT '文件数据',
  `status` int(1) NOT NULL COMMENT '状态:1处理成功，0未处理，2处理失败',
  `parse_count` double(4,0) NOT NULL DEFAULT '0' COMMENT '处理次数',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `parse_result` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='与app交互的元数据';

-- app打开设备报表
-- ----------------------------
-- Table structure for yjj_monitoring_total_report
-- ----------------------------
DROP TABLE IF EXISTS `yjj_monitoring_total_report`;
CREATE TABLE `yjj_monitoring_total_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `open_app_count` bigint(11) DEFAULT '0' COMMENT '打开app的总数',
  `open_app_device_count` bigint(11) DEFAULT '0' COMMENT '打开app的设备的总数',
  `page_query_count` bigint(11) DEFAULT '0' COMMENT '页面访问总数',
  `app_stay_avg` bigint(11) DEFAULT '0' COMMENT 'app平均停留时间',
  `page_query_avg` bigint(11) DEFAULT '0' COMMENT 'app平均访问页面数',
  `report_time` timestamp NULL DEFAULT NULL COMMENT '统计时间',
  `app_stay_time_count` bigint(20) DEFAULT '0' COMMENT 'app总停留时间',
  `last_report_time` timestamp NULL DEFAULT NULL COMMENT '最后统计时间',
  `report_day` date DEFAULT NULL COMMENT '是那天的数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COMMENT='app概况';

-- page 统计
-- ----------------------------
-- Table structure for yjj_monitoring_page_report
-- ----------------------------
DROP TABLE IF EXISTS `yjj_monitoring_page_report`;
CREATE TABLE `yjj_monitoring_page_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `page_name` varchar(255) NOT NULL COMMENT '页面名称',
  `page_code` varchar(32) NOT NULL COMMENT '页面编码',
  `page_uv` bigint(11) DEFAULT '0' COMMENT '页面uv',
  `page_pv` bigint(11) DEFAULT '0' COMMENT '页面pv',
  `page_stay_evg` bigint(11) DEFAULT '0' COMMENT '页面平均停留时间单位s',
  `page_lost_percent` double(2,2) DEFAULT '0.00' COMMENT '页面跳失率',
  `page_stay_total` bigint(20) DEFAULT '0' COMMENT '页面总停留时间s',
  `event_count` bigint(20) DEFAULT '0' COMMENT '页面总点击次数',
  `event_uv_percent` double(2,2) DEFAULT '0.00' COMMENT '页面uv点击率',
  `event_pv_percent` double(2,2) DEFAULT '0.00' COMMENT '页面pv点击率',
  `report_time` timestamp NULL DEFAULT NULL COMMENT '统计时间',
  `last_report_time` timestamp NULL DEFAULT NULL COMMENT '最后统计时间',
  `report_day` date DEFAULT NULL COMMENT '是那天的数据',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=280 DEFAULT CHARSET=utf8 COMMENT='app概况';


-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--   小程序购物车
-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- 小商城的购物车
drop table if exists shop_goods_car;
create table shop_goods_car(
	id BIGINT(20) NOT NULL AUTO_INCREMENT comment '主键',
	product_sku_id bigint(20) not null comment '商品ID',
	member_id bigint(20) not null comment '会员id',
	sku_number bigint(11) default 1 comment '商品数量',
	create_time bigint(20) not null  comment '创建时间',
	car_suk_status int(1) default 1 comment '购物车中商品状态  1正常，0禁用，-1 删除',
	store_id bigint(20) comment '门店id',
	product_id bigint(20) comment '商品id',
	shop_product_id bigint(20) comment 'shop_product 表主键',
	last_update_time bigint(20) not null  comment '最后修改时间',
  PRIMARY key(id),
  key(member_id,car_suk_status,product_sku_id)
)engine=innodb default charset=utf8 comment='小程序商城购物车';

