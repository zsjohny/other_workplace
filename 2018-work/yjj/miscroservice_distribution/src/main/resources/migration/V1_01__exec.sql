-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述


-- 提现方式配置
-- UPDATE `yjj_data_dictionary`
-- SET `comment` = '{"minMoney":"50","maxMoney":"20000","assignDateMaxMoney":"500"}'
-- WHERE group_code = 'cashOut'
--   AND `code` = 'limitMoney'
-- ;


-- 金币人民币兑换率
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'exchangeRate', 'goldCoin', '1', '金币人民币兑换率(1块钱兑换多少个金币)', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 合伙人升级条件
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'partner', 'upgradeCondition', '{\"distributor\":\"5\",\"countMoney\":\"500000\"}', '合伙人升级条件', '合伙人升级条件 分销商数量，团队销售业绩', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'distributor', 'upgradeCondition', '{\"classA\":\"200\",\"classAB\":\"500\",\"countMoney\":\"100000\"}', '分销商升级条件', '分销商升级条件 一级二级粉丝人数，销售业绩', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'store', 'upgradeCondition', '{\"buyCount\":\"1\",\"classA\":\"30\"}', '店长升级条件', '店长升级条件 一级粉丝数量 自购数量', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);



-- 提现
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'limitMoney', 'cashOut', '500', '提现方式限额判断', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'limitTime', 'cashOut', 'yyyy-MM-dd HH+3', '在限额内的提现指定日期', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 分销收益策略
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '1', '0', '0.16', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '1', '1', '0.16', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '1', '2', '0.08', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '2', '0', '0.16', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '2', '1', '0.16', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '2', '2', '0.08', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '3', '0', '0.16', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '3', '1', '0.16', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '3', '2', '0.08', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '2', '10', '0.10', '0.20', '0');
-- INSERT INTO `yjj_distribution_earnings_strategy` (`id`, `user_role_type`, `earnings_type`, `earnings_ratio`, `currency_ratio`, `del_status`) VALUES (NULL, '3', '10', '0.06', '0.20', '0');

-- 分享商品收益配置
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'productConfig', 'share', NULL, '分享商品收益设置', '{\"maxShareCount\":5,\"acceptUserGoldCoinEarnings\":0.08,\"sendUserGoldCoinEarnings\":0.05}', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 订单表加字段
-- alter table shop_member_order add column `gold_coin` decimal(7,2) DEFAULT '0.00' COMMENT '使用金币';


---------------------------------版本1------------------------------------------------------
-- -- ------
-- -- 公海池
-- -- ------
--
-- INSERT INTO `yjj_work_user` (`login_name`, `user_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `salt`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
-- ('huwei', '胡威', '11', '', '15958150974', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'yangjiangbei', '杨江北', '01', '', '13311555992', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'liuxiang', '刘翔', '01', '', '13067921513', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'tangyingjie', '唐英杰', '01', '', '13067735681', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'yuanzongxian', '袁宗献', '01', '', '15805810030', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'yewanzhen', '叶婉贞', '01', '', '18857810549', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'zhaokai', '赵楷', '01', '', '18158139233', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'daizijun', '戴子军', '01', '', '18005707060', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'xuchuang', '许闯', '01', '', '17601231856', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'pengciguo', '彭赐果', '01', '', '13069727359', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'wuqing', '吴青', '01', '', '18696243385', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, ''),
-- ( 'zhangqian', '张倩', '11', '', '18605811788', '0', '', 'E10ADC3949BA59ABBE56E057F20F883E', '', '0', '0', '', NULL, '', NULL, '', NULL, '')
--

---------------------------------版本1------------------------------------------------------