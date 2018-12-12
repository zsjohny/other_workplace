package com.finace.miscroservice.activity.controller;

import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.activity.rpc.UserRpcService;
import com.finace.miscroservice.activity.service.UserJiangPinService;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Rc4Utils;
import com.finace.miscroservice.commons.utils.Response;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 2018新年活动：获取用户获奖信息, 用户获得奖励个数
 */
@RestController
@RefreshScope
public class AppYearController extends BaseController {

    private Log logger = Log.getInstance(AppYearController.class);

    @Autowired
    private UserJiangPinService userJiangPinService;

    @Autowired
    private UserRpcService userRpcService;

    @Value("${activity.shareKey}")
    protected String shareKey;


    /**
     * 获取用户获奖信息
     *
     * @param page 页数
     *             data": [</br>
     *             jplist:{</br>
     *             "jiangPinName": "20元话费",  奖品名称</br>
     *             "addTime": "2018-01-29 16:53:24", 获得时间</br>
     *             }</br>
     *             totalSize:2  总页数</br>
     *             ]
     * @return
     */
    @RequestMapping("userReward/auth")
    public Response userReward(@Param("page") int page) {
        String userId = getUserId();
        logger.info("用户{}获取获得奖励信息", userId);
        if (page < 1) {
            logger.warn("用户{}获取奖励信息, 页数错误{}", userId, page);
            return Response.error("页数错误");
        }
        Map<String, Object> map = new HashMap<>();
        BasePage bpage = new BasePage();
        bpage.setPageNum(page);
        List<UserJiangPinPO> list = userJiangPinService.getUserJiangPinByUserId(userId);
        map.put("totalSize", bpage.getTotal(list));
        map.put("jplist", list);
        return Response.successByMap(map);
    }

    /**
     * 获取所有用户获奖
     *
     * @return
     */
    @RequestMapping("allreward")
    public Response allreward() {
        return Response.success(userJiangPinService.getAllUserJiangPin());
    }

    /**
     * 用户获得奖励个数
     * <p>
     * "shareid": "813f972b4300611a66c968",
     * "accountData":
     * "bill": 20,   话费
     * "invitationNum": 1,  好友注册人数,
     * "fiftyJdk": 50  50元京东卡,
     * "ohJdk": 100  100元京东卡
     * }
     * }
     *
     * @return
     */
    @RequestMapping("rewardHome/auth")
    public Response rewardHome() {
        String userId = getUserId();
        logger.info("用户{}获得奖励情况", userId);
        Map<String, Object> map = new HashMap<>();
        User user = userRpcService.getUserByUserId(userId);
        if (null != user) {
            map.put("shareid", Rc4Utils.toHexString(user.getPhone(), shareKey));
        } else {
            map.put("shareid", "");
        }

        map.put("accountData", userJiangPinService.rewardHome(userId));
        return Response.success(map);
    }


}
