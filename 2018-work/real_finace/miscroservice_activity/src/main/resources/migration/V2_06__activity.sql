
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 活动
-- ------

-- alter table upload_address add addtime datetime comment '新增时间';
-- alter table upload_address add remark  varchar(150) comment '备注';
-- alter table userandjiangpin add remark  varchar(150) comment '备注';

--
-- CREATE TABLE `images_type` (
-- 		`id` Int( 11 ) AUTO_INCREMENT NOT NULL,
-- 		`name` VARCHAR ( 50 ) NULL COMMENT '标题',
-- 		`type` int( 2 ) NULL COMMENT '类型 1--app首页  2--活动中心  3--PC首页 4--运营报告 ',
-- 		`status` int( 2 ) NULL COMMENT '状态 0--隐藏 1--显示',
-- 		`jumurl` VARCHAR( 200 ) NULL COMMENT '点击跳转url',
-- 		`imgurl` VARCHAR( 200 ) NULL COMMENT '图片地址',
-- 		`scontent` VARCHAR( 150 ) NULL COMMENT '分享内容',
-- 		`surl` VARCHAR( 200 ) NULL COMMENT '分享地址',
-- 		`stitle` VARCHAR( 100 ) NULL COMMENT '分享标题',
-- 		`simgurl` VARCHAR( 200 ) NULL COMMENT '分享小图片地址',
-- 		`torder` int(10) NULL COMMENT '排序',
-- 		`stime` VARCHAR(20) NULL COMMENT '开始时间',
-- 		`etime` VARCHAR(20) NULL COMMENT '结束时间',
-- 		`addtime` VARCHAR(20) NULL COMMENT '新增时间',
-- 		`addip` VARCHAR(10) NULL COMMENT '新增ip',
-- 		`remark` VARCHAR(200) NULL COMMENT '备注',
-- 		PRIMARY KEY ( `id` ) )
-- 	ENGINE = InnoDB
-- 	AUTO_INCREMENT = 10;