package com.e_commerce.miscroservice.distribution.service;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;

import java.util.Map;

/**
 * @author hyf
 */
public interface DistributionSystemService {
    /**
     *  查询 上级 上上级 合伙人 分销商
     *  @param userId
     * @return
     */
    JSONObject find(Long userId);

    /**
     *  添加上级
     *  @param userId
     * @param upUserId
     * @return
     */
    Response add(Long userId, Long upUserId);

    /**
     * 添加自己
     * @param userId
     * @param grade
     * @return
     */
    Response addSelf(Long userId, Integer grade);

    /**
     * 更新
     * @param userId
     * @param grade
     * @return
     */
    Response update(Long userId, Integer grade);


    Response redisChangeToMysql(Long userId);

    /**
     * 查找我的团队信息
     * @param userId
     * @return
     */
    JSONObject findCountTeam(Long userId);

    /**
     * 我的粉丝信息
     * @param userId
     * @return
     */
    JSONObject findCountFollower(Long userId);

    /**
     * 我的一级粉丝明细
     * @param userId
     * @param page
     * @return
     */
    Response findFollowerDetails(Long userId, Integer page);

    /**
     * 晋升条件
     * @param userId
     * @return
     */
    JSONObject promoteCondition(Long userId);

    /**
     * 晋升 申请
     * @param userId
     * @param realName
     * @param wxNum
     * @param phone
     * @param idCard
     * @return
     */
    Response distributionProposer(Long userId, String realName, String wxNum, String phone, String idCard);

    /**
     * 个人中心
     * @param userId
     * @return
     */
    JSONObject myInformation(Long userId);

    /**
     * 绑定粉丝
     * @param userId
     * @param fans
     * @return
     */
    Response bindingFans(Long userId, Long fans);

    /**
     * 自动晋升店长
     * @param userId
     * @param successOrderNo
     */
    void toStore(Long userId, String successOrderNo);




    /**
     * 我的分销广告(分销收益+分享收益)
     *
     * @param userId userId
     * @return java.util.Map
     * @author Charlie
     * @date 2018/12/14 17:36
     */
    Map<String, Object> myInformationAd(Long userId);
}
