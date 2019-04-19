-- 项目下有多个则命令为 V1,V2 递增，中间01,02代表当前sql的版本新增，每次新增的请把之前的sql注释,最后代表当前的sql的描述


-- 分销以前的用户关系设为历史关系
UPDATE `yjj_wxa_share` SET fans_type = -1 WHERE create_time < '2018-11-26 16:30:00';

---------------------------------版本1------------------------------------------------------
-- -- 签到
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'signDateCoin', 'sign', '连续签到奖励', '连续签到奖励', '0.01,0.02,0.03,0.04,0.05,0.06,0.07', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
-- INSERT INTO `yjj_data_dictionary` (`id`, `code`, `group_code`, `val`, `name`, `comment`, `status`, `create_user_id`, `create_user_name`, `create_time`, `last_user_id`, `last_user_name`, `last_update_time`, `parent_id`) VALUES (NULL, 'signPeriodicalPrize', 'sign', '签到阶段奖励', '签到阶段奖励', '{\"condition\":[5,10,15],\"day\":[5,15,25],\"prize\":[0.05,0.08,0.1]}', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
--
-- -- 分销以前的用户关系设为历史关系
-- UPDATE `yjj_wxa_share` SET fans_type = -1 WHERE create_time < '2018-11-26 16:30:00';
---------------------------------版本1------------------------------------------------------