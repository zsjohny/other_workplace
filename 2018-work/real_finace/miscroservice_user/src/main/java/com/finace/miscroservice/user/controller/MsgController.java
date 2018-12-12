package com.finace.miscroservice.user.controller;

import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.enums.MsgCodeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.service.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息中心
 */
@RestController
public class MsgController extends BaseController {

    @Autowired
    private MsgService msgService;

    Log logger = Log.getInstance(MsgController.class);

    /**
     * 消息中心 首页
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "0": {
     * "msgCode": 0,
     * "topic": "0",
     * "msg": "0",
     * "addtime": 1521531989433
     * },
     * "2": {
     * "msgCode": 2,
     * "topic": "2",
     * "msg": "2",
     * "addtime": 1521531981433
     * }
     * }
     * }
     *
     * @return
     */
    @RequestMapping("show/home/msg/auth")
    public Response showHomeMsg() {
//        String userId ="20388";
        String userId = getUserId();
        if (userId.equals(null)) {
            logger.warn("用户id为空");
        }
        logger.info("ip={}用户查询消息首页userId={}",Iptools.gainRealIp(request),userId);
        return msgService.findHomeMsg(Integer.parseInt(userId));
    }

    /**
     * 消息详情列表
     *
     * @param page    页码
     * @param msgCode 消息类型
     *                <p>
     *                {
     *                "msg": "",
     *                "code": 200,
     *                "data": {
     *                "0": {
     *                "msgCode": 0,
     *                "topic": "0",
     *                "msg": "0",
     *                "addtime": 1521531989433
     *                },
     *                "2": {
     *                "msgCode": 2,
     *                "topic": "2",
     *                "msg": "2",
     *                "addtime": 1521531981433
     *                }
     *                }
     *                }
     * @return
     */
    @RequestMapping("show/msgs/auth")
    public Response showMsgDetailedList(Integer page, Integer msgCode) {
//        String userId ="20388";
        String userId = getUserId();
        if (userId.equals(null)) {
            logger.warn("用户id为空");
        }
        if (page == null || page < 1) {
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        if (msgCode == null) {
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        logger.info("ip={}用户查询消息列表userId={}", Iptools.gainRealIp(request),userId);
        return msgService.findMsgDetailedList(Integer.parseInt(userId), page, msgCode);
    }

}
