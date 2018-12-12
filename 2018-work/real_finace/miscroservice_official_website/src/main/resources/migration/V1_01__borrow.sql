
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 用户表
-- ------

CREATE TABLE `error_log` (
		`id` int(11) AUTO_INCREMENT NOT NULL,
		`type` int(3) NULL COMMENT '类型1--订单类',
    `orderid` VARCHAR(50) NULL COMMENT '错误日志',
    `msg` VARCHAR(500) NULL COMMENT '错误信息',
    `status` int(3) NULL COMMENT '状态 0--未处理1--处理中2--已处理',
    `addtime` VARCHAR(20) NULL COMMENT '新增时间',
    `addip` VARCHAR(20) NULL COMMENT '新增ip',
		`remark` VARCHAR(500) NULL COMMENT '备注',
		PRIMARY KEY ( `id` ) )
	ENGINE = InnoDB
	AUTO_INCREMENT = 10;
