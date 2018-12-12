/**
 *
 */
package com.ouliao.controller.versionfirst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ouliao.domain.versionfirst.*;
import com.ouliao.domain.versionsecond.UserMissCallRecord;
import com.ouliao.domain.versionsecond.UserVisitRecord;
import com.ouliao.service.versionfirst.*;
import com.ouliao.service.versionsecond.UserMissCallRecordService;
import com.ouliao.service.versionsecond.UserSayContentSecondService;
import com.ouliao.service.versionsecond.UserSureMsgCountService;
import com.ouliao.service.versionsecond.UserVistRecordService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ouliao.repository.versionfirst.GeTuiMapperRepository;
import com.ouliao.repository.versionfirst.QueryCallTimeRecordCrudRepository;
import com.ouliao.repository.versionfirst.UserCallMarkPageRepository;
import com.ouliao.repository.versionfirst.UserCallRoomCrudRepository;
import com.ouliao.util.SecConverDate;
import com.xiaoluo.util.DesUtil;
import com.xiaoluo.util.Getui;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author xiaoluo
 * @version $Id: UserCallMarkController.java, 2016年2月27日 上午9:08:21
 */
@Controller
//@RequestMapping(value = "user/call", method = RequestMethod.POST)
@RequestMapping(value = "user/call")
public class UserCallMarkController {
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserCallMarkService userCallMarkService;
    @Autowired
    private UserCallRoomService userCallRoomService;

    @Autowired
    private UserCallRoomCrudRepository userCallRoomCrudRepository;
    @Autowired
    private GeTuiMapperRepository geTuiMapperRepository;
    @Autowired
    private UserReflectService userReflectService;
    @Autowired
    private UserAliPayService userAliPayService;
    @Autowired
    private UserBlackListService userBlackListService;
    @Autowired
    private HuanXinService huanXinService;
    @Autowired
    private ServiceRecordTimeService serviceRecordTimeService;
    @Autowired
    private SysMsgShowService sysMsgShowService;
    @Autowired
    private UserCallMarkPageRepository userCallMarkPageRepository;
    @Autowired
    private QueryCallTimeRecordCrudRepository queryCallTimeRecordCrudRepository;

    //第二版
    @Autowired
    private UserMissCallRecordService userMissCallRecordService;

    @Autowired
    private UserVistRecordService userVistRecordService;

    @Autowired
    private UserSureMsgCountService userSureMsgCountService;

    @Autowired
    private UserSayContentSecondService userSayContentSecondService;

    @Autowired
    private CompanyPayService companyPayService;
    private Double moery;
    // 加入缓存进行操作
    private JedisPool pool = new JedisPool("localhost", 10088);
    private Jedis jedis = pool.getResource();

    // 用户发起打电话
    @ResponseBody
    @RequestMapping(value = "callMark/{uid}/{id}/createCallMrak")
    public synchronized JSONObject createCallMrak(@PathVariable("uid") String uid, @PathVariable("id") String ids,
                                                  @RequestParam("key") String key, @RequestParam(value = "reback", required = false) String reback, HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        if (StringUtils.isEmpty(key)) {
            jsonObject.put("code", 202);
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        }
        // 更改也可以传环信的id--1457670018294
        Integer id = 0;
        if (ids.length() == 13) {
            HuanXin huanXin = huanXinService.queryHuanXinByName(ids);
            if (huanXin == null) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            id = huanXin.getOwnerId();
        } else {

            if (StringUtils.isEmpty(ids)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }

            id = Integer.parseInt(ids);
        }

        // 登录检测
        User user = null;
        User userCall = null;
        if (StringUtils.isNotEmpty(reback)) {

            userCall = ouLiaoService.queryUserByUserNum(uid);
            user = ouLiaoService.queryUserByUserId(id);
        } else {
            user = ouLiaoService.queryUserByUserNum(uid);
            userCall = ouLiaoService.queryUserByUserId(id);

        }

        if (userCall == null || user == null || user.getUserId().equals(id)) {
            jsonObject.put("code", 203);
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        }
        // 移出来是为了方便进行一场处理
        try {

            // 是否在黑名单---互相查询
            UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(user.getUserId(),
                    userCall.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 217);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            userBlackList = userBlackListService.queryUserIsBlackListById(userCall.getUserId(), user.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 218);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }


            //删除之前的所有的异常
            try {

                userCallRoomService.deleteAllById(user.getUserId());
            } catch (Exception e) {

            }


            // 得出所拨打的主播是否登录了客户端
            Calendar calendarPast = Calendar.getInstance();
            Calendar calendarNow = Calendar.getInstance();
            if (userCall.getUserModifyTime() == null) {
                jsonObject.put("code", 216);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            calendarPast.setTime(userCall.getUserModifyTime());
            // int code = calendarNow.get(Calendar.DAY_OF_YEAR) -
            // calendarPast.get(Calendar.DAY_OF_YEAR);
            //
            // if (calendarPast.get(Calendar.DAY_OF_YEAR) !=
            // calendarNow.get(Calendar.DAY_OF_YEAR) || code == 1) {
            // jsonObject.put("code", 216);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }

            // 加载数量配置
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            // 如果是客服号跳过
            Boolean flag = false;
            // 免费的电话的费用

            Boolean flagFree = false;
            ServiceRecordTime serviceRecordTime = null;
            if (!properties.getProperty("ouliaoService").equals(userCall.getUserPhone())) {
                // 在这里确定是主播发起的还是普通用户主动发起的
                if (!properties.getProperty("ouliaoService").equals(user.getUserPhone())) {

                    // 确定是否有免费的赠送电话
                    serviceRecordTime = serviceRecordTimeService.queryUserRecordIsExistByUserId(user.getUserId());

                    if (serviceRecordTime == null || serviceRecordTime.getUserCallTime() == 0) {

                        // 打电话前的确认用户是否还剩可用余额
                        if (user.getUserMoney() == null || user.getUserMoney() < 1
                                || user.getUserMoney() < (userCall.getUserCallCost() == null ? 1.0
                                : userCall.getUserCallCost())) {
                            jsonObject.put("code", 210);
                            DecimalFormat decimalFormat = new DecimalFormat("0.0");
                            jsonObject.put("money", user.getUserMoney() == null ? 0.00 : decimalFormat.format(user.getUserMoney()));
                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonObject;
                        }
                    } else {

                        // 使用免费的电话时间
                        flagFree = true;
                    }

                } else {
                    // 主播打普通用户
                    flag = true;
                }

            } else {
                // 普通用户打主播
                flag = true;
            }

            if (user.getUserId().equals(userCall.getUserId())) {
                jsonObject.put("code", 213);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // if (request.getSession().getAttribute(key) == null || user ==
            // null) {
            // jsonObject.put("code", 208);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }
            String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            if (!user.getUserNum().equals(userNum)) {
                jsonObject.put("code", 209);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            if (!"true".equals(userCall.getUserContract()) && !flag) {
                jsonObject.put("code", 221);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            List<UserCallMark> userCallMarks = userCallMarkService.queryUserCallMarkIsDeletedAllById(id, user.getUserId());
            if (userCallMarks != null && userCallMarks.size() != 0) {

                // 删除最新记录的房间记录ID

                //--废弃userCallMarkService.deleteUserCallMarkIsDeletedByCallMarkId();
                userCallMarkPageRepository.delete(userCallMarks);
                /*
                 * jsonObject.put("code", 204);
				 * response.setStatus(HttpServletResponse.SC_OK);
				 *
				 * return jsonObject;
				 */
            }

            // 提示主播用户进行打电话---或者正在进行通话中
            // UserCallRoom userCallRoom =
            // userCallRoomService.queryByUserCallRoomIsExistById(id);
            // if (userCallRoom != null) {
            //
            // jsonObject.put("code", 215);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }
            //
            // // 看看自己是否也打电话
            // userCallRoom =
            // userCallRoomService.queryByUserCallRoomIsExistById(user.getUserId());
            // if (userCallRoom != null) {
            // jsonObject.put("code", 215);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }

            // 判断是否在通话中
            try {
                if ("true".equals(jedis.get("user:run:" + userCall.getUserId()))) {

                    jsonObject.put("code", 215);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
            } catch (Exception e) {
                // 提示主播用户进行打电话---或者正在进行通话中
                List<UserCallRoom> userCallRooms = userCallRoomService.queryByUserCallRoomAllIsExistById(id);
                if (userCallRooms != null && userCallRooms.size() != 0) {

                    jsonObject.put("code", 215);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }

                // 看看自己是否也打电话
                userCallRooms = userCallRoomService.queryByUserCallRoomAllIsExistById(user.getUserId());
                if (userCallRooms != null && userCallRooms.size() != 0) {

                    jsonObject.put("code", 215);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
            }
            GeTuiMapper geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(id);

            if (geTuiMapper == null) {
                jsonObject.put("code", 220);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            UserCallRoom userCallRoom = new UserCallRoom();
            userCallRoom.setUserCalledId(id);
            userCallRoom.setUserId(user.getUserId());
            userCallRoom.setUserCreateTime(new Date());

            userCallRoom = userCallRoomService.createUserCallRoomBy(userCallRoom);

            if (userCallRoom == null) {
                jsonObject.put("code", 210);

                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 存储用户的房间编号
            UserCallMark userCallMark = new UserCallMark();
            userCallMark.setIsDeleted("0");
            userCallMark.setUserCalledId(id);
            userCallMark.setUserId(user.getUserId());
            userCallMark.setUserCreateTime(new Date());
            userCallMarkService.createUserCallMark(userCallMark);

            // 给他进行推送---分为安卓还是苹果
            String result = "";

            // 查询一次将打出的结果返回给前台保存，查通话结束的时间
            userCallMark = userCallMarkService.queryUserCallMarkIsDeletedById(id, user.getUserId());

            // 展示通话界面的信息------------------------------------
            JSONObject json = new JSONObject();
            json.put("callSatus", "start");
            json.put("acceptId", user.getUserId());

            if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                json.put("acceptName",
                        user.getUserNickName().substring(0, 3) + "******"
                                + new StringBuilder(new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                .reverse());
            } else {
                json.put("acceptName", user.getUserNickName());
            }

            Boolean headflag = false;
            String headUrl = user.getUserHeadPic();
            if (StringUtils.isEmpty(headUrl)) {
                headUrl = "985595";
            } else if (headUrl.contains("//")) {
                headflag = true;

            } else {
                headUrl = headUrl.split("\\.")[0];
            }
            if (headflag) {
                json.put("acceptUrl", user.getUserHeadPic());
            } else {
                json.put("acceptUrl",
                        properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
            }
            json.put("acceptAuthor", StringUtils.isEmpty(user.getUserAuth()) ? "" : user.getUserAuth());
            json.put("acceptIsContract", "true".equals(user.getUserContract()) ? "true" : "false");
            json.put("acceptRoom", user.getUserId());
            json.put("acceptPhoneId", userCallMark.getUserCallMarkId());
            // -----------------------------------------------

            // 判断打电话的人是否在接电话
            // 判断是否已经推送过去，现在只保证推送一个，其余不给推送
            // if ("true".equals(jedis.get("user:send:" + userCall.getUserId()))
            // || jedis.exists("user:send:" + user.getUserId())) {
            //
            // // 自动删除房间和记录
            //
            // userCallMark =
            // userCallMarkService.queryUserCallMarkIsDeletedById(id,
            // user.getUserId());
            //
            // if (userCallMark != null) {
            // userCallMarkPageRepository.delete(userCallMark);
            // }
            //
            // userCallRoom =
            // userCallRoomService.queryByUserCallRoomById(user.getUserId(),
            // id);
            // if (userCallRoom != null) {
            //
            // userCallRoomService.deleteUserCallRoomByUserCallRoomId(userCallRoom.getUserCallRoomId());
            // }
            //
            // result = "ok";
            // } else {

            userCallMarkService.queryUserCallMarkIsDeletedById(id, user.getUserId());


            //增加普通用户回拨的功能
            if (StringUtils.isNotEmpty(reback)) {
                json.put("reback", "true");

            }

            if (0 == geTuiMapper.getClientCata()) {


                result = Getui.SendAndroid(json.toString(), geTuiMapper.getClientId());

            } else {
                result = Getui.SendIos(json.toString(), geTuiMapper.getClientId(),
                        "亲爱的" + userCall.getUserNickName() + "," + json.get("acceptName") + "给你打电话啦!", json.toString());

                // 进行apns推送
                // if (result.contains("ok")) {
                // result = Getui.IosApns(
                // "亲爱的" + userCall.getUserNickName() + "," +
                // json.get("acceptName") + "给你打电话啦!", "来接听",
                // geTuiMapper.getGetuiDeviceToken());
                // }
            }

            try { // 加入缓存进行操作---推送成功
                if (request != null && result.contains("ok")) {
                    jedis.setnx("user:send:" + userCall.getUserId(), "true");
                }
                // }
            } catch (Exception e) {

            }
            if (request != null && result.contains("ok")) {

                jsonObject.put("code", 200);
                jsonObject.put("id", userCallMark.getUserCallMarkId());

                // 还剩多少秒
                if (!flag) {

                    // 分为是否是免费的赠送电话
                    if (flagFree) {
                        jsonObject.put("remain",
                                serviceRecordTime.getUserCallTime() + Math.floor(user.getUserMoney() == null ? 0
                                        : user.getUserMoney() / (userCall.getUserCallCost() == null ? 1.0
                                        : userCall.getUserCallCost()))
                                        * 60);
                    } else {
                        jsonObject.put("remain",
                                Math.floor(user.getUserMoney()
                                        / (userCall.getUserCallCost() == null ? 1.0 : userCall.getUserCallCost()))
                                        * 60);
                    }

                } else {
                    jsonObject.put("remain", -1);
                }

                // 展示通话界面的信息------------------------------------
                jsonObject.put("sendId", userCall.getUserId());

                if (userCall.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                    jsonObject.put("sendName",
                            userCall.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(userCall.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    jsonObject.put("sendName", userCall.getUserNickName());
                }

                headflag = false;
                headUrl = userCall.getUserHeadPic();
                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;

                } else {
                    headUrl = headUrl.split("\\.")[0];
                }
                if (headflag) {
                    json.put("sendUrl", userCall.getUserHeadPic());
                } else {
                    jsonObject.put("sendUrl", properties.getProperty("headUrl") + userCall.getUserId() + "/" + headUrl
                            + "/head/download");
                }
                jsonObject.put("sendAuthor", StringUtils.isEmpty(userCall.getUserAuth()) ? "" : userCall.getUserAuth());
                jsonObject.put("sendRoom", user.getUserId());

                // -----------------------------------------------

            } else {
                jsonObject.put("code", 222);

                // 自动删除房间和记录--空的记录

                userCallMark = userCallMarkService.queryUserCallMarkIsDeletedById(id, user.getUserId());

                if (userCallMark != null) {
                    userCallMarkPageRepository.delete(userCallMark);
                }

                userCallRoom = userCallRoomService.queryByUserCallRoomById(user.getUserId(), id);
                if (userCallRoom != null) {

                    userCallRoomService.deleteUserCallRoomByUserCallRoomId(userCallRoom.getUserCallRoomId());
                }

            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            try {
                pool = new JedisPool("localhost", 10088);
                jedis = pool.getResource();

                // 自动删除房间和记录

                List<UserCallMark> userCallMarks = userCallMarkService.queryUserCallMarkIsDeletedAllById(id,
                        user.getUserId());

                if (userCallMarks != null) {

                    userCallMarkPageRepository.delete(userCallMarks);
                }

                List<UserCallRoom> userCallRooms = userCallRoomService.queryByUserCallRoomAllById(user.getUserId(), id);

                if (userCallRooms != null && userCallRooms.size() != 0) {
                    jedis.del("user:send:" + userCallRooms.get(0).getUserCalledId());
                    userCallRoomCrudRepository.delete(userCallRooms);
                }
                jedis.disconnect();
                jsonObject.put("code", 201);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            } catch (Exception e1) {

            }
        }

        try {

            jedis.disconnect();
        } catch (Exception e) {

        }

        return jsonObject;
    }

    // 用户接通电话
    @ResponseBody
    @RequestMapping(value = "callMark/{uid}/{id}/runningCallMrak")
    public void runningCallMrak(@PathVariable("uid") String uid, @PathVariable("id") Integer id,
                                @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {

        key = key.trim().replaceAll(" ", "+");
        try {
            if (StringUtils.isEmpty(key)) {

                return;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);

            if (user == null) {
                return;
            }

            // 加入缓存进行操作---已经在接电话了
            jedis.setnx("user:run:" + id, "true");
            jedis.disconnect();
        } catch (Exception e) {

        }

    }


    //用来标识用户是否根据电话id进行挂掉电话
    @ResponseBody
    @RequestMapping(value = "callMark/{uid}/{id}/{callId}/queryCallMrak")
    public JSONObject queryCallMrak(@PathVariable("uid") String uid, @PathVariable("id") Integer id, @PathVariable("callId") Integer callId,
                                    @RequestParam("key") String key, @RequestParam(value = "reback", required = false) String reback, HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");
        try {
            if (StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);

            if (user == null) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }


            if (StringUtils.isNotEmpty(reback)) {
                Integer third = user.getUserId();
                user.setUserId(callId);
                callId = third;

            }


            try {
                if (jedis.exists("user:userCall" + callId + ":userCalled:" + user.getUserId() + ":" + id)) {
                    jsonObject.put("code", 200);


                } else {
                    jsonObject.put("code", 204);

                }

            } catch (Exception e) {
                jsonObject.put("code", 200);
            }

        } catch (Exception e) {
            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject;
        }


        response.setStatus(HttpServletResponse.SC_OK);
        return jsonObject;

    }


    // 用户关闭房间
    @ResponseBody
    @RequestMapping(value = "callMark/{uid}/{id}/{accpetId}/deleteCallMrak")
    public JSONObject deleteCallMrak(@PathVariable("uid") String uid, @PathVariable("id") Integer id,
                                     @PathVariable("accpetId") Integer accpetId, @RequestParam("key") String key,
                                     @RequestParam(value = "reback", required = false) String reback, @RequestParam(value = "activeId", required = false) String activeId, HttpServletRequest request,
                                     HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        if (StringUtils.isEmpty(key)) {
            jsonObject.put("code", 202);
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        }

        // 登录检测
        User user = ouLiaoService.queryUserByUserNum(uid);

        if (user == null) {
            jsonObject.put("code", 203);
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        }
        try {
            // if (request.getSession().getAttribute(key) == null || user ==
            // null) {
            // jsonObject.put("code", 208);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }
            // String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            // if (!user.getUserNum().equals(userNum)) {
            // jsonObject.put("code", 209);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }

            // 通话房间让出来
            UserCallRoom userCallRoom = userCallRoomService.queryByUserCallRoomIsExistById(user.getUserId());

            if (userCallRoom != null) {
                userCallRoomService.deleteUserCallRoomByUserCallRoomId(userCallRoom.getUserCallRoomId());

                // 继续关闭记录
                userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(id, 0.00, "0秒", 0.00);

                jsonObject.put("code", 200);


                //回拨来电
                if (StringUtils.isNotEmpty(reback)) {

                    Integer firId = userCallRoom.getUserId();
                    Integer secId = userCallRoom.getUserCalledId();
                    userCallRoom.setUserId(secId);
                    userCallRoom.setUserCalledId(firId);

                }


                // 告诉接打电话退出打电话界面
                JSONObject json = new JSONObject();
                json.put("callSatus", "end");
                json.put("sendId", userCallRoom.getUserCalledId());
                GeTuiMapper geTuiMapper = null;
                if (user.getUserId().equals(userCallRoom.getUserId())) {

                    geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(userCallRoom.getUserCalledId());

                } else {
                    geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(userCallRoom.getUserId());
                }

                if (geTuiMapper == null) {
                    jsonObject.put("code", 220);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }


                //增加普通用户回拨的功能
                if (StringUtils.isNotEmpty(reback)) {
                    json.put("reback", "true");

                }


                if (0 == geTuiMapper.getClientCata()) {
                    String result = Getui.SendAndroid(json.toString(), geTuiMapper.getClientId());


                } else {
                    Getui.SendIos(json.toString(), geTuiMapper.getClientId(), "", "");

                }
                try {
                    // 加入缓存进行操作---推送取消

                    jedis.del("user:send:" + userCallRoom.getUserCalledId());
                    jedis.del("user:run:" + userCallRoom.getUserCalledId());
                } catch (Exception e) {

                }


                //第二版未接来电提醒
                try {
                    if (userCallRoom != null) {

                        UserMissCallRecord userMissCallRecord = new UserMissCallRecord();
                        userMissCallRecord.setIsDeleted("0");
                        userMissCallRecord.setCreatTime(new Date());
                        userMissCallRecord.setUserCallId(userCallRoom.getUserId());
                        userMissCallRecord.setUserCalledId(userCallRoom.getUserCalledId());
                        userMissCallRecord.setIsRead("0");
                        String cid = "";
                        try {
                            List<GeTuiMapper> geTuiMappers =
                                    geTuiMapperRepository.queryGeiTuiAllByUserId(userCallRoom.getUserCalledId());

                            if (geTuiMappers != null && geTuiMappers.size() != 0) {
                                cid = geTuiMappers.get(0).getClientId();
                            }
                        } catch (Exception e) {

                        }

                        userMissCallRecord.setCid(cid);
                        Long second = new Date().getTime() - userCallRoom.getUserCreateTime().getTime();
                        Long callTime = (second / 1000);
                        userMissCallRecord.setUserCallRingTime(Integer.valueOf(callTime + ""));
                        userMissCallRecordService.createUserMissCallRecord(userMissCallRecord);

                        if (activeId == null) {
                            jedis.set("user:userCall" + userCallRoom.getUserId() + ":userCalled:" + userCallRoom.getUserCalledId() + ":" + id,
                                    String.valueOf(userCallRoom.getUserCallRoomId()));
                        } else {

                            jedis.set("user:userCall" + userCallRoom.getUserId() + ":userCalled:" + activeId + ":" + id,
                                    String.valueOf(userCallRoom.getUserCallRoomId()));
                        }
                        // 继续关闭记录
                        userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(id, 0.00, "0秒", 0.00);
                    }

                } catch (Exception e) {
                    // 继续关闭记录
                    userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(id, 0.00, "0秒", 0.00);
                }

            } else {
                jsonObject.put("code", 210);
                // 继续关闭记录
                userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(id, 0.00, "0秒", 0.00);

            }


            try {
                jedis.disconnect();
            } catch (Exception e) {

            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            try {
                pool = new JedisPool("localhost", 10088);
                jedis = pool.getResource();
                // 删除异常状态
                List<UserCallMark> userCallMark = userCallMarkService.queryUserCallMarkAllByUserCallMarkId(id);

                GeTuiMapper geTuiMapper = null;

                if (userCallMark != null && userCallMark.size() != 0) {


                    //回拨来电
                    if (StringUtils.isNotEmpty(reback)) {

                        Integer firId = userCallMark.get(0).getUserId();
                        Integer secId = userCallMark.get(0).getUserCalledId();
                        userCallMark.get(0).setUserId(secId);
                        userCallMark.get(0).setUserCalledId(firId);

                    }


                    if (user.getUserId().equals(userCallMark.get(0).getUserId())) {

                        geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(userCallMark.get(0).getUserCalledId());

                    } else {
                        geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(userCallMark.get(0).getUserId());
                    }

                    JSONObject json = new JSONObject();
                    json.put("callSatus", "end");
                    json.put("sendId", userCallMark.get(0).getUserCalledId());
                    if (geTuiMapper == null) {
                        jsonObject.put("code", 220);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }
                    if (0 == geTuiMapper.getClientCata()) {
                        Getui.SendAndroid(json.toString(), geTuiMapper.getClientId());
                    } else {
                        String result = Getui.SendIos(json.toString(), geTuiMapper.getClientId(), "", "");

                    }

                    User userCalled = ouLiaoService.queryUserByUserId(userCallMark.get(0).getUserCalledId());

                    // 删除所有房间的记录
                    userCallRoomService.deleteAllById(id);
                    // 加入缓存进行操作---推送取消
                    jedis.del("user:send:" + userCalled.getUserId());
                    jedis.del("user:run:" + userCalled.getUserId());
                } else {
                    // 加入缓存进行操作---推送取消
                    jedis.del("user:send:" + activeId);
                    jedis.del("user:run:" + activeId);
                }
                jedis.disconnect();
                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e2) {

            }
        }

        return jsonObject;
    }

    // 用户结束打电话
    @ResponseBody
    @RequestMapping(value = "callMark/{uid}/{id}/endCallMrak")
    public JSONObject endCallMrak(@PathVariable("uid") String uid, @PathVariable("id") Integer id,
                                  @RequestParam("key") String key, @RequestParam("endTime") Long endTime,
                                  @RequestParam("callInterval") Long callInterval,
                                  @RequestParam(value = "acceptId", required = false) String acceptId,
                                  @RequestParam(value = "reback", required = false) String reback, HttpServletRequest request,
                                  HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");
        // 删除异常状态
        List<UserCallMark> userCallMarks = userCallMarkService.queryUserCallMarkAllByUserCallMarkId(id);
        if (userCallMarks != null && userCallMarks.size() != 0) {
            ouLiaoService.queryUserByUserId(userCallMarks.get(0).getUserCalledId());
        }
        // 删除所有房间的记录
        userCallRoomService.deleteAllById(id);

        try {
            if (StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }


            //异常处理

            try {
                if (StringUtils.isNotEmpty(acceptId)) {
                    // 加入缓存进行操作---推送取消
                    jedis.del("user:send:" + acceptId);
                    jedis.del("user:run:" + acceptId);
                }
            } catch (Exception e) {

            }


            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);

            if (user == null) {
                jsonObject.put("code", 203);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // if (request.getSession().getAttribute(key) == null || user ==
            // null) {
            // jsonObject.put("code", 208);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            //
            // }
            // String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            // if (!user.getUserNum().equals(userNum)) {
            // jsonObject.put("code", 209);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }

            // ----测试内容
            // user.setUserMoney(100000.00);


            // 参数载入
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    UserCallMarkController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));

            UserCallMark userCallMark = userCallMarkService.queryUserCallMarkByUserCallMarkId(id);
            if (userCallMark == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }


            //回拨来电
            if (StringUtils.isNotEmpty(reback)) {

                Integer firId = userCallMark.getUserId();
                Integer secId = userCallMark.getUserCalledId();
                userCallMark.setUserId(secId);
                userCallMark.setUserCalledId(firId);

            }


            // 根据前台时间进行操作
            Date date = new Date(endTime);
            Date now = new Date();
            Long callTimeDis = 0l;
            if (date.before(now) && date.after(userCallMark.getUserCreateTime())) {
                callTimeDis = date.getTime() - userCallMark.getUserCreateTime().getTime();
            } else {
                callTimeDis = now.getTime() - userCallMark.getUserCreateTime().getTime();
            }

            Long callTime = (callTimeDis / 1000);
            // 这里进行处理，如果前端数据传输的是小值，以前端为主
            if (callTime > callInterval) {
                callTime = callInterval;
            }

            // 判断是否是客服号，如果是进行特殊处理
            int isServiceF = 0;

            User isService = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId());

            if (properties.getProperty("ouliaoService").equals(isService.getUserPhone())) {
                isServiceF = 1;
            }

            if (isServiceF == 0) {
                isService = ouLiaoService.queryUserByUserId(userCallMark.getUserId());
                if (properties.getProperty("ouliaoService").equals(isService.getUserPhone())) {
                    isServiceF = 2;
                }

            }
            if (isServiceF != 0) {

                // 给被打得一方增加时间
                // 调用环信接口，这是主动打电话一方展示其和对方的环信ID
                if (user.getUserId().equals(userCallMark.getUserId())) {
                    HuanXin huanXin = huanXinService.queryIsExist(userCallMark.getUserId());
                    jsonObject.put("ownerHXId", huanXin == null ? "" : huanXin.getHuaXinUUid());
                    huanXin = huanXinService.queryIsExist(userCallMark.getUserCalledId());
                    jsonObject.put("sendHXId", huanXin == null ? "" :
                            huanXin.getHuaXinUUid());

                    jsonObject.put("sendHXName",
                            ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId()).getUserNickName());

                } else {
                    HuanXin huanXin = huanXinService.queryIsExist(userCallMark.getUserCalledId());
                    jsonObject.put("ownerHXId",
                            huanXin == null ? "" : huanXin.getHuaXinUUid());
                    huanXin = huanXinService.queryIsExist(userCallMark.getUserId());
                    jsonObject.put("sendHXId", huanXin == null ? "" : huanXin.getHuaXinUUid());
                    jsonObject.put("sendHXName",
                            ouLiaoService.queryUserByUserId(userCallMark.getUserId()).getUserNickName());
                }

                User userCall = ouLiaoService.queryUserByUserId(userCallMark.getUserId());
                User userCalled = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId());

                // 通话房间让出来
                UserCallRoom userCallRoom = null;
                if (user.getUserId() == userCallMark.getUserId()) {
                    userCallRoom = userCallRoomService
                            .queryByUserCallRoomByUserCalledId(userCallMark.getUserCalledId());
                } else {
                    userCallRoom = userCallRoomService
                            .queryByUserCallRoomByUserCalledId(userCallMark.getUserCalledId());
                }


                if (userCallRoom != null) {
                    // 计算时长----只有在房间在并且的时候在进行计算
                    Double nums = Double.parseDouble(callTime.toString());

                    Double minute = 0.0;
                    if (nums <= 60) {
                        minute = 1.0;
                    } else {

                        Double num = nums / 60;

                        minute = Math.ceil(num);

                    }

                    int count = userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                            userCallMark.getUserCallMarkId(), 0.00, SecConverDate.SecConverCNDate(callTime), 0.00);
                    ouLiaoService.updateUserCallTotalByUserId(
                            userCall.getUserCallTotal() == null ? minute : minute + userCall.getUserCallTotal(),
                            userCall.getUserId());
                    ouLiaoService.updateUserCallTotalByUserId(
                            userCalled.getUserCallTotal() == null ? minute : minute + userCalled.getUserCallTotal(),
                            userCalled.getUserId());
                    userCallRoomService.deleteUserCallRoomByUserCallRoomId(userCallRoom.getUserCallRoomId());

                    // 增加免费电话时长--客服打的时候在会有
                    if (isServiceF == 2) {
                        ServiceRecordTime serviceRecordTime = serviceRecordTimeService
                                .queryUserRecordIsExistByUserId(userCalled.getUserId());
                        if (serviceRecordTime != null) {
                            serviceRecordTimeService.updateUserCallTimeByUserId(
                                    serviceRecordTime.getUserCallTime() + callTime, userCallMark.getUserCalledId());
                        }
                        // 增加获得时候是增加的时间
                        userCallMark = userCallMarkService.queryUserCallMarkByUserCallMarkId(id);
                    }
                }

                if (isServiceF == 2 && user.getUserId() == userCallMark.getUserCalledId()) {
                    jsonObject.put("isEran", "true");

                } else {
                    jsonObject.put("isEran", "false");

                }

                jsonObject.put("callTime", userCallMark.getUserCallTime() == null ? 0 : userCallMark.getUserCallTime());
                jsonObject.put("callCost", 0);
                jsonObject.put("calledId", userCalled.getUserId());
                String headUrl = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId()).getUserHeadPic();
                Boolean headflag = false;

                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;

                } else {
                    headUrl = headUrl.split("\\.")[0];
                }
                if (headflag) {
                    jsonObject.put("calledUrl", headUrl);
                } else {
                    jsonObject.put("calledUrl", properties.getProperty("headUrl") + userCallMark.getUserCalledId() + "/"
                            + headUrl + "/head/download");
                }
                if (userCalled.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    jsonObject.put("calledName",
                            userCalled.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(userCalled.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    jsonObject.put("calledName", userCalled.getUserNickName());
                }
                if (userCall.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    jsonObject.put("callName",
                            userCall.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(userCall.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    jsonObject.put("callName", userCall.getUserNickName());
                }

                jsonObject.put("callId", userCall.getUserId());

                jsonObject.put("code", 200);
                try {
                    // 加入缓存进行操作---推送取消

                    jedis.del("user:send:" + userCallRoom.getUserCalledId());
                    jedis.del("user:run:" + userCallRoom.getUserCalledId());
                    jedis.disconnect();
                    return jsonObject;
                } catch (Exception e) {

                }

            }


            //增加第二版内容
            try {

                if (userCallMarks != null) {
                    Boolean fag = false;

                    String countId = "";

                    countId = jedis.get("user:userCall" + userCallMarks.get(0).getUserId() + ":userCalled:" + userCallMarks.get(0).getUserCalledId() + ":" + id);

                    if (StringUtils.isNotEmpty(countId)) {
                        jsonObject.put("code", 200);

                        if (acceptId == null) {
                            jedis.del("user:userCall" + userCallMarks.get(0).getUserId() + ":userCalled:" + userCallMarks.get(0).getUserCalledId() + ":" + id);
                        } else {

                            jedis.del("user:userCall" + userCallMarks.get(0).getUserId() + ":userCalled:" + acceptId + ":" + id);
                        }

                        userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                                userCallMark.getUserCallMarkId(), 0.00, SecConverDate.SecConverCNDate(callTime),
                                0.00);
                        fag = true;


                        //把原先记录都删除
                        List<UserMissCallRecord> userMissCallRecords = userMissCallRecordService.queryUserMissCallRecordAllById(userCallMarks.get(0).getUserId(), Integer.valueOf(userCallMarks.get(0).getUserCalledId()));
                        List<Long> ids = new ArrayList<>();
                        for (UserMissCallRecord userMissCallRecord : userMissCallRecords) {
                            ids.add(userMissCallRecord.getUserMissCallRecordId());
                        }

                        if (ids.size() > 0) {

                            userMissCallRecordService.deleteUserMissCallRecordByUserMissCallRecordId(ids, userCallMarks.get(0).getUserCalledId());
                        }


                    }


                    //将未接来电拦截掉--返回过去
                    if (fag) {

                        return jsonObject;
                    }


                }

            } catch (Exception e) {

                if (userCallMarks != null) {
                    if (StringUtils.isNotEmpty(acceptId)) {
                        List<UserMissCallRecord> userMissCallRecords = userMissCallRecordService.queryUserMissCallRecordAllById(userCallMarks.get(0).getUserId(), Integer.valueOf(userCallMarks.get(0).getUserCalledId()));

                        if (userMissCallRecords != null && userMissCallRecords.size() != 0) {

                            jsonObject.put("code", 200);


                            userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                                    userCallMark.getUserCallMarkId(), 0.00, SecConverDate.SecConverCNDate(callTime),
                                    0.00);


                            //把原先记录都删除
                            List<Long> ids = new ArrayList<>();
                            for (UserMissCallRecord userMissCallRecord : userMissCallRecords) {
                                ids.add(userMissCallRecord.getUserMissCallRecordId());
                            }
                            userMissCallRecordService.deleteUserMissCallRecordByUserMissCallRecordId(ids, userCallMarks.get(0).getUserCalledId());

                            return jsonObject;
                        }
                    }
                }


            }


            // 判断打入者还是被打入者--被打入者查询账单
            if (user.getUserId().equals(userCallMark.getUserCalledId())) {
                User userCall = ouLiaoService.queryUserByUserId(userCallMark.getUserId());
                User userCalled = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId());

                // ----增加客服计算时间和计算的账号---

                QueryCallTimeRecord queryCallTimeRecord = new QueryCallTimeRecord();

                queryCallTimeRecord.setCallTime(callTime);
                queryCallTimeRecord.setCreatTime(new Date());
                queryCallTimeRecord.setUserCallId(userCall.getUserId());
                queryCallTimeRecord.setUserCallNickName(userCall.getUserNickName());
                queryCallTimeRecord.setUserCallPhone(userCall.getUserPhone());
                queryCallTimeRecord.setUserCalledId(userCalled.getUserId());
                queryCallTimeRecord.setUserCalledNickName(userCalled.getUserNickName());
                queryCallTimeRecord.setUserCalledPhone(userCalled.getUserPhone());
                queryCallTimeRecord.setUserCallCost((callTime / 60 + 1)
                        * (userCalled.getUserCallCost() == null ? 1.0 : userCalled.getUserCallCost()));

                queryCallTimeRecordCrudRepository.save(queryCallTimeRecord);

                // 增加用户提示操作
                // if (userCallMark.getUserCallCost() == null &&
                // user.getUserId().equals(userCallMark.getUserCalledId())) {
                // jsonObject.put("code", 214);
                // response.setStatus(HttpServletResponse.SC_OK);
                // return jsonObject;
                // }

                // ------------------------------------
                if (userCallMark.getUserCallCost() == null && user.getUserId().equals(userCallMark.getUserCalledId())) {

                    // 打入者的判断--防止刷的漏洞
                    if ("1".equals(userCallMark.getIsDeleted())) {
                        jsonObject.put("code", 200);
                        // 加入缓存进行操作---推送取消
                        try {
                            jedis.del("user:send:" + userCalled.getUserId());
                            jedis.del("user:run:" + userCalled.getUserId());
                            jedis.disconnect();
                        } catch (Exception e) {

                        }
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }

                    // 删除房间------------------------------------新增接通老用户提示
                    userCall = ouLiaoService.queryUserByUserId(userCallMark.getUserId());
                    userCalled = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId());
                    if (callTime > 0) {

                        // 如果有免费通话时长进行先行扣除
                        Boolean flag = true;
                        // 获赠的时间主播是要进行计费
                        Boolean sendFree = false;
                        ServiceRecordTime serviceRecordTime = serviceRecordTimeService
                                .queryUserRecordIsExistByUserId(userCallMark.getUserId());

                        if (serviceRecordTime != null) {
                            if (serviceRecordTime.getUserCallTime() > callTime) {

                                // 减去默认的10分钟--一次通话后清零,给主播先贴钱
                                if ("true".equals(serviceRecordTime.getIsSysSend())
                                        && serviceRecordTime.getUserCallTime() >= 180) {
                                    if (serviceRecordTime.getUserCallTime() == 180) {

                                        serviceRecordTimeService.updateUserCallTimeByUserId(0l,
                                                userCallMark.getUserId());
                                        serviceRecordTimeService.deleteSysSendByUserId(userCallMark.getUserId());
                                        sendFree = true;
                                    } else {

                                        long remainTime = serviceRecordTime.getUserCallTime() - 180;
                                        serviceRecordTimeService.updateUserCallTimeByUserId(remainTime,
                                                userCallMark.getUserId());
                                        serviceRecordTimeService.deleteSysSendByUserId(userCallMark.getUserId());
                                        if (callTime > 180) {
                                            callTime = 180l;
                                        }

                                        sendFree = true;
                                    }
                                } else {

                                    serviceRecordTimeService.updateUserCallTimeByUserId(
                                            serviceRecordTime.getUserCallTime() - callTime, userCallMark.getUserId());
                                    flag = false;
                                }

                            } else {
                                callTime = callTime - serviceRecordTime.getUserCallTime();
                                // 超出免费的时间直接清零
                                serviceRecordTimeService.updateUserCallTimeByUserId(0l, userCallMark.getUserId());

                                serviceRecordTimeService.deleteSysSendByUserId(userCallMark.getUserId());
                            }
                        }

                        Double fee = 0.0;
                        if (flag) {


                            if (callTime % 60 == 0) {
                                fee = (callTime / 60)
                                        * (userCalled.getUserCallCost() == null ? 1.0 : userCalled.getUserCallCost());
                            } else {
                                fee = (callTime / 60 + 1)
                                        * (userCalled.getUserCallCost() == null ? 1.0 : userCalled.getUserCallCost());
                            }


                        }

                        // 判断是否是首次使用免费电话或者是打免费电话里面的时间
                        Double personFree = userCall.getUserMoney() == null ? 0.00 : userCall.getUserMoney();

                        if (flag && !sendFree) {
                            // 扣除用户钱数
                            personFree = userCall.getUserMoney() == null ? 0.00 : userCall.getUserMoney() - fee;
                            if (personFree < 0) {
                                jsonObject.put("code", 210);
                                // 加入缓存进行操作---推送取消
                                try {
                                    jedis.del("user:send:" + userCalled.getUserId());
                                    jedis.del("user:run:" + userCalled.getUserId());
                                    jedis.disconnect();
                                } catch (Exception e) {

                                }
                                response.setStatus(HttpServletResponse.SC_OK);
                                return jsonObject;
                            }

                        }

                        ouLiaoService.updateUserMoneyConsumeByUserId(personFree,
                                userCall.getUserCallConsume() == null ? fee : userCall.getUserCallConsume() + fee,
                                userCall.getUserId());

                        // --------------------------------------------原始记录-----------------------------------------------

                        // 收取80%--接电话的人
                        // ouLiaoService.updateUserMoneyEarnByUserId(
                        // userCalled.getUserMoney() == null ? fee * 0.8 : fee *
                        // 0.8 + userCalled.getUserMoney(),
                        // userCalled.getUserCallEarn() == null ? fee * 0.8
                        // : userCalled.getUserCallEarn() + fee * 0.8,
                        // userCalled.getUserId());
                        // --------------------------------------------原始记录----------------------------------------------

                        // --------------------------------------------历史活动1，根据人气进行判断，不根据用户打主播的计算价格---------
                        if (!sendFree) {
                            // 收取80%--接电话的人
                            ouLiaoService.updateUserMoneyEarnByUserId(
                                    userCalled.getUserMoney() == null ? fee * 0.8
                                            : fee * 0.8 + userCalled.getUserMoney(),
                                    userCalled.getUserCallEarn() == null ? fee * 0.8
                                            : userCalled.getUserCallEarn() + fee * 0.8,
                                    userCalled.getUserId());
                        }
                        // --------------------------------------------历史活动一，根据人气进行判断，不根据用户打主播的计算价格---------
                        // --------------------------------------------原始记录----------------------------------------------
                        // Double consumeReocrd = fee;
                        // if (sendFree || (flag && fee == 0)) {
                        // consumeReocrd = 0.00;
                        // }
                        //
                        // int count =
                        // userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                        // userCallMark.getUserCallMarkId(), consumeReocrd,
                        // SecConverDate.SecConverCNDate(callTime), fee * 0.8);
                        // --------------------------------------------原始记录----------------------------------------------
                        // --------------------------------------------历史活动一，根据人气进行判断，不根据用户打主播的计算价格---------
                        Double consumeReocrd = fee;
                        moery = fee;
                        if (sendFree || (flag && fee == 0)) {
                            consumeReocrd = 0.00;
                        }


                        if (sendFree) {
                            int count = userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                                    userCallMark.getUserCallMarkId(), 0.00, SecConverDate.SecConverCNDate(callTime),
                                    0.00);
                        } else {
                            int count = userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                                    userCallMark.getUserCallMarkId(), consumeReocrd,
                                    SecConverDate.SecConverCNDate(callTime), fee * 0.8);


                            //增加每个月报表计费
                            CompanyPay companyPay = companyPayService.queryCompyPayByIsDelted();
                            double money = consumeReocrd;

                            if (companyPay == null) {


                                companyPay = new CompanyPay();

                                companyPay.setCreateTime(new Date());

                                companyPay.setIsDeleted("0");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy年MM月");
                                companyPay.setDate(simpleDateFormat.format(new Date()));

                                companyPay.setCompanyConsume(money);
                                companyPay.setCompanyCost(money * 0.8);
                                companyPayService.saveComanyPay(companyPay);
                            } else {

                                money = money += companyPay.getCompanyConsume();

                                companyPayService.updateComanyPayByConmanyConsume(money);
                                companyPayService.updateComanyPayByCompanyCost(money * 0.8);

                                if (new Date().getMonth() - companyPay.getCreateTime().getMonth() != 0) {

                                    companyPayService.deleteComanyPayByIsDelted();


                                    try {

                                        Properties proemail = new Properties();
                                        proemail.load(new BufferedReader(new InputStreamReader(
                                                OuLiaoSayController.class.getClassLoader().getResourceAsStream("officalSendEmailParam.properties")
                                                , "utf-8")));

                                        HtmlEmail htmlEmail = new HtmlEmail();


                                        htmlEmail.setHostName(
                                                DesUtil.decrypt(proemail.getProperty("dbHost"), proemail.getProperty("dbKey")));

                                        htmlEmail.setAuthentication(
                                                DesUtil.decrypt(proemail.getProperty("dbName"), proemail.getProperty("dbKey")),
                                                DesUtil.decrypt(proemail.getProperty("dbPassword"),
                                                        proemail.getProperty("dbKey")));

                                        htmlEmail.setFrom(
                                                DesUtil.decrypt(proemail.getProperty("dbName"), proemail.getProperty("dbKey")),
                                                proemail.getProperty("sendPerson"), "utf-8");

                                        htmlEmail.addTo(DesUtil.decrypt(proemail.getProperty("dbEmailTo"),
                                                proemail.getProperty("dbKey")), "", "utf-8");
                                        htmlEmail.setSubject(proemail.getProperty("sendSubject"));
                                        htmlEmail.setMsg(companyPay.getDate() + "本月用户累计消费：" + new DecimalFormat("0.0").format(companyPay.getCompanyConsume()) + ",主播累计收支：" + new DecimalFormat("0.0").format(companyPay.getCompanyCost()));
                                        htmlEmail.setCharset("utf-8");
                                        htmlEmail.send();


                                    } catch (Exception e) {

                                    }


                                }


                            }


                        }
                        // --------------------------------------------历史活动一，根据人气进行判断，不根据用户打主播的计算价格---------
                        // 计算时长
                        Double nums = Double.parseDouble(callTime.toString());

                        Double minute = 0.0;
                        if (nums <= 60) {
                            minute = 1.0;
                        } else {

                            Double num = nums / 60;

                            minute = Math.ceil(num);

                        }


                        ouLiaoService.updateUserCallTotalByUserId(
                                userCall.getUserCallTotal() == null ? minute : minute + userCall.getUserCallTotal(),
                                userCall.getUserId());
                        ouLiaoService.updateUserCallTotalByUserId(
                                userCalled.getUserCallTotal() == null ? minute : minute + userCalled.getUserCallTotal(),
                                userCalled.getUserId());

                        jsonObject.put("code", 200);
                    } else {
                        jsonObject.put("code", 210);

                    }

                    // 删除房间
                    // 通话房间让出来
                    UserCallRoom userCallRoom = userCallRoomService
                            .queryByUserCallRoomByUserCalledId(userCalled.getUserId());

                    if (userCallRoom != null) {
                        userCallRoomService.deleteUserCallRoomByUserCallRoomId(userCallRoom.getUserCallRoomId());
                    }
                    //return jsonObject;
                }
                jsonObject.put("callTime", userCallMark.getUserCallTime());

                // 展示给接听的80%收取--接听人
                jsonObject.put("callIncome", moery * 0.8);

                jsonObject.put("calledId", userCalled.getUserId());

                String headUrl = ouLiaoService.queryUserByUserId(userCallMark.getUserId()).getUserHeadPic();
                Boolean headflag = false;

                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;

                } else {
                    headUrl = headUrl.split("\\.")[0];
                }
                if (headflag) {
                    jsonObject.put("callUrl", headUrl);
                } else {
                    jsonObject.put("callUrl", properties.getProperty("headUrl") + userCallMark.getUserId() + "/"
                            + headUrl + "/head/download");
                }
                if (userCalled.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    jsonObject.put("calledName",
                            userCalled.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(userCalled.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    jsonObject.put("calledName", userCalled.getUserNickName());
                }
                if (userCall.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    jsonObject.put("callName",
                            userCall.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(userCall.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    jsonObject.put("callName", userCall.getUserNickName());
                }
                // 调用环信接口，这是主动打电话一方展示其和对方的环信ID
                HuanXin huanXin = huanXinService.queryIsExist(user.getUserId());

                jsonObject.put("ownerHXId", huanXin == null ? "" : huanXin.getHuaXinUUid());
                huanXin = huanXinService.queryIsExist(userCall.getUserId());
                jsonObject.put("sendHXId", huanXin == null ? "" : huanXin.getHuaXinUUid());

                jsonObject.put("callId", userCall.getUserId());
                jsonObject.put("isEran", "false");
                jsonObject.put("code", 200);

                // 加入缓存进行操作---推送取消
                try {
                    jedis.del("user:send:" + userCalled.getUserId());
                    jedis.del("user:run:" + userCalled.getUserId());
                    jedis.disconnect();
                } catch (Exception e) {

                }
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // // 打入者的判断--防止刷的漏洞
            // if ("1".equals(userCallMark.getIsDeleted())) {
            // jsonObject.put("code", 203);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }

            User userCall = ouLiaoService.queryUserByUserId(userCallMark.getUserId());
            User userCalled = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId());
            // 打入者的判断--防止刷的漏洞
            if ("1".equals(userCallMark.getIsDeleted())) {
                jsonObject.put("code", 200);
                try {
                    jedis.del("user:send:" + userCalled.getUserId());
                    jedis.del("user:run:" + userCalled.getUserId());
                    jedis.disconnect();

                } catch (Exception e) {

                }
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            if (callTime > 0) {

                // 如果有免费通话时长进行先行扣除
                Boolean flag = true;
                // 获赠的时间主播是要进行计费
                Boolean sendFree = false;
                ServiceRecordTime serviceRecordTime = serviceRecordTimeService
                        .queryUserRecordIsExistByUserId(user.getUserId());

                if (serviceRecordTime != null) {
                    if (serviceRecordTime.getUserCallTime() > callTime) {

                        // 减去默认的10分钟--一次通话后清零,给主播先贴钱
                        if ("true".equals(serviceRecordTime.getIsSysSend())
                                && serviceRecordTime.getUserCallTime() >= 180) {
                            if (serviceRecordTime.getUserCallTime() == 180) {

                                serviceRecordTimeService.updateUserCallTimeByUserId(0l, user.getUserId());
                                serviceRecordTimeService.deleteSysSendByUserId(user.getUserId());
                                sendFree = true;
                            } else {

                                long remainTime = serviceRecordTime.getUserCallTime() - 180;
                                serviceRecordTimeService.updateUserCallTimeByUserId(remainTime, user.getUserId());
                                serviceRecordTimeService.deleteSysSendByUserId(user.getUserId());
                                if (callTime > 180) {
                                    callTime = 180l;
                                }

                                sendFree = true;
                            }
                        } else {

                            serviceRecordTimeService.updateUserCallTimeByUserId(
                                    serviceRecordTime.getUserCallTime() - callTime, user.getUserId());
                            flag = false;
                        }

                    } else {
                        callTime = callTime - serviceRecordTime.getUserCallTime();
                        // 超出免费的时间直接清零
                        serviceRecordTimeService.updateUserCallTimeByUserId(0l, user.getUserId());

                        serviceRecordTimeService.deleteSysSendByUserId(user.getUserId());
                    }
                }

                Double fee = 0.0;
                if (flag) {

                    if (callTime % 60 == 0) {
                        fee = (callTime / 60)
                                * (userCalled.getUserCallCost() == null ? 1.0 : userCalled.getUserCallCost());
                    } else {
                        fee = (callTime / 60 + 1)
                                * (userCalled.getUserCallCost() == null ? 1.0 : userCalled.getUserCallCost());
                    }

                }

                // 判断是否是首次使用免费电话或者是打免费电话里面的时间
                Double personFree = userCall.getUserMoney() == null ? 0.00 : userCall.getUserMoney();

                if (flag && !sendFree) {
                    // 扣除用户钱数
                    personFree = userCall.getUserMoney() == null ? 0.00 : userCall.getUserMoney() - fee;
                    if (personFree < 0) {
                        jsonObject.put("code", 210);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }

                }

                ouLiaoService.updateUserMoneyConsumeByUserId(personFree,
                        userCall.getUserCallConsume() == null ? fee : userCall.getUserCallConsume() + fee,
                        userCall.getUserId());

                // --------------------------------------------原始记录----------------------------------------------

                // 收取80%--接电话的人
                // ouLiaoService.updateUserMoneyEarnByUserId(
                // userCalled.getUserMoney() == null ? fee * 0.8 : fee * 0.8 +
                // userCalled.getUserMoney(),
                // userCalled.getUserCallEarn() == null ? fee * 0.8 :
                // userCalled.getUserCallEarn() + fee * 0.8,
                // userCalled.getUserId());

                // --------------------------------------------原始记录----------------------------------------------

                // --------------------------------------------历史活动1，根据人气进行判断，不根据用户打主播的计算价格---------
                if (!sendFree) {
                    // 收取80%--接电话的人
                    ouLiaoService.updateUserMoneyEarnByUserId(
                            userCalled.getUserMoney() == null ? fee * 0.8 : fee * 0.8 + userCalled.getUserMoney(),
                            userCalled.getUserCallEarn() == null ? fee * 0.8 : userCalled.getUserCallEarn() + fee * 0.8,
                            userCalled.getUserId());
                }
                // --------------------------------------------历史活动1，根据人气进行判断，不根据用户打主播的计算价格---------

                jsonObject.put("callTime", SecConverDate.SecConverCNDate(callTime));

                if (sendFree || (flag && fee == 0)) {
                    jsonObject.put("callCost", 0);
                } else {
                    jsonObject.put("callCost", fee);
                }

                jsonObject.put("calledId", userCalled.getUserId());
                String headUrl = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId()).getUserHeadPic();
                Boolean headflag = false;

                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;

                } else {
                    headUrl = headUrl.split("\\.")[0];
                }
                if (headflag) {
                    jsonObject.put("calledUrl", headUrl);
                } else {
                    jsonObject.put("calledUrl", properties.getProperty("headUrl") + userCallMark.getUserCalledId() + "/"
                            + headUrl + "/head/download");
                }
                if (userCalled.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    jsonObject.put("calledName",
                            userCalled.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(userCalled.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    jsonObject.put("calledName", userCalled.getUserNickName());
                }
                if (userCall.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    jsonObject.put("callName",
                            userCall.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(userCall.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    jsonObject.put("callName", userCall.getUserNickName());
                }

                jsonObject.put("callId", userCall.getUserId());
                // 调用环信接口，这是主动打电话一方展示其和对方的环信ID
                HuanXin huanXin = huanXinService.queryIsExist(user.getUserId());

                jsonObject.put("ownerHXId", huanXin == null ? "" : huanXin.getHuaXinUUid());
                huanXin = huanXinService.queryIsExist(userCalled.getUserId());
                jsonObject.put("sendHXId", huanXin == null ? "" : huanXin.getHuaXinUUid());
                Double consumeReocrd = fee;
                if (sendFree || (flag && fee == 0)) {
                    consumeReocrd = 0.00;
                }

                // --------------------------------------------原始记录----------------------------------------------
                // int count =
                // userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                // userCallMark.getUserCallMarkId(), consumeReocrd,
                // SecConverDate.SecConverCNDate(callTime),
                // fee * 0.8);
                // --------------------------------------------原始记录----------------------------------------------

                // --------------------------------------------历史活动1，根据人气进行判断，不根据用户打主播的计算价格---------
                if (sendFree) {
                    int count = userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                            userCallMark.getUserCallMarkId(), 0.00, SecConverDate.SecConverCNDate(callTime), 0.00);
                } else {
                    int count = userCallMarkService.updateUserCallMarkIsDeletedByUserCallMarkId(
                            userCallMark.getUserCallMarkId(), consumeReocrd, SecConverDate.SecConverCNDate(callTime),
                            fee * 0.8);


                    //增加每个月报表计费
                    CompanyPay companyPay = companyPayService.queryCompyPayByIsDelted();
                    double money = consumeReocrd;
                    if (companyPay == null) {


                        companyPay = new CompanyPay();

                        companyPay.setCreateTime(new Date());

                        companyPay.setIsDeleted("0");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy年MM月");
                        companyPay.setDate(simpleDateFormat.format(new Date()));

                        companyPay.setCompanyConsume(money);
                        companyPay.setCompanyCost(money * 0.8);
                        companyPayService.saveComanyPay(companyPay);
                    } else {

                        money = money += companyPay.getCompanyConsume();

                        companyPayService.updateComanyPayByConmanyConsume(money);
                        companyPayService.updateComanyPayByCompanyCost(money * 0.8);

                        if (new Date().getMonth() - companyPay.getCreateTime().getMonth() != 0) {

                            companyPayService.deleteComanyPayByIsDelted();


                            try {


                                Properties proemail = new Properties();
                                proemail.load(new BufferedReader(new InputStreamReader(
                                        OuLiaoSayController.class.getClassLoader().getResourceAsStream("officalSendEmailParam.properties")
                                        , "utf-8")));
                                HtmlEmail htmlEmail = new HtmlEmail();


                                htmlEmail.setHostName(
                                        DesUtil.decrypt(proemail.getProperty("dbHost"), proemail.getProperty("dbKey")));

                                htmlEmail.setAuthentication(
                                        DesUtil.decrypt(proemail.getProperty("dbName"), proemail.getProperty("dbKey")),
                                        DesUtil.decrypt(proemail.getProperty("dbPassword"),
                                                proemail.getProperty("dbKey")));

                                htmlEmail.setFrom(
                                        DesUtil.decrypt(proemail.getProperty("dbName"), proemail.getProperty("dbKey")),
                                        proemail.getProperty("sendPerson"), "utf-8");

                                htmlEmail.addTo(DesUtil.decrypt(proemail.getProperty("dbEmailTo"),
                                        proemail.getProperty("dbKey")), "", "utf-8");
                                htmlEmail.setSubject(proemail.getProperty("sendSubject"));
                                htmlEmail.setMsg(companyPay.getDate() + "本月用户累计消费：" + new DecimalFormat("0.0").format(companyPay.getCompanyConsume()) + ",主播累计收支：" + new DecimalFormat("0.0").format(companyPay.getCompanyCost()));
                                htmlEmail.setCharset("utf-8");
                                htmlEmail.send();


                            } catch (Exception e) {

                            }


                        }


                    }


                }
                // --------------------------------------------历史活动1，根据人气进行判断，不根据用户打主播的计算价格---------
                // 计算时长

                Double nums = Double.parseDouble(callTime.toString());

                Double minute = 0.0;
                if (nums <= 60) {
                    minute = 1.0;
                } else {

                    Double num = nums / 60;

                    minute = Math.ceil(num);

                }

                ouLiaoService.updateUserCallTotalByUserId(
                        userCall.getUserCallTotal() == null ? minute : minute + userCall.getUserCallTotal(),
                        userCall.getUserId());
                ouLiaoService.updateUserCallTotalByUserId(
                        userCalled.getUserCallTotal() == null ? minute : minute + userCalled.getUserCallTotal(),
                        userCalled.getUserId());

                // 通话房间让出来
                UserCallRoom userCallRoom = userCallRoomService
                        .queryByUserCallRoomByUserCalledId(userCalled.getUserId());

                if (userCallRoom != null) {
                    userCallRoomService.deleteUserCallRoomByUserCallRoomId(userCallRoom.getUserCallRoomId());
                }
                jsonObject.put("isEran", "false");
                jsonObject.put("code", 200);

            } else {
                jsonObject.put("code", 210);

            }
            // 加入缓存进行操作---推送取消
            try {
                jedis.del("user:send:" + userCalled.getUserId());
                jedis.del("user:run:" + userCalled.getUserId());
                jedis.disconnect();
            } catch (Exception e) {

            }
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            try {

                // 删除异常状态
                List<UserCallMark> userCallMark = userCallMarkService.queryUserCallMarkAllByUserCallMarkId(id);
                if (userCallMark != null && userCallMark.size() != 0) {

                    // 删除所有房间的记录
                    userCallRoomService.deleteAllById(id);
                    // 加入缓存进行操作---推送取消
                    jedis.del("user:send:" + userCallMark.get(0).getUserCalledId());

                    jedis.del("user:run:" + userCallMark.get(0).getUserCalledId());

                } else {
                    // 加入缓存进行操作---推送取消
                    jedis.del("user:send:" + acceptId);
                    jedis.del("user:run:" + acceptId);
                }
                jsonObject.put("code", 200);
                jedis.disconnect();
            } catch (Exception e1) {

            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        return jsonObject;
    }

    // 消息推送和通话记录显示
    @ResponseBody
    @RequestMapping(value = "callMark/{uid}/{dic}/showCallMrakAndMsg")
    public JSONObject showCallMrakAndMsg(@PathVariable("uid") String uid, @PathVariable("dic") Integer dic,
                                         @RequestParam("key") String key, @RequestParam(value = "startCount", defaultValue = "1") Integer
                                                 startCount,
                                         @RequestParam(value = "type", required = false) Integer type, HttpServletRequest
                                                 request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
            if (user == null) {
                jsonObject.put("code", 203);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            if (request.getSession().getAttribute(key) == null || user == null) {
                jsonObject.put("code", 208);
                if (user != null) {
                    jsonObject.put("cid", user.getUserId());
                }

                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }
            String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            if (!user.getUserNum().equals(userNum)) {
                jsonObject.put("code", 209);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    UserCallMarkController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));
            Integer pageNum = Integer.valueOf(properties.getProperty("callMarkStartCount"));

            // 这里区分是系统消息还是明细页面显示的值
            // 自定义排序的标志
            int sign = 0;

            switch (dic) {
                case 0:

                    // if (startCount == 1) {
                    //
                    // Properties prop = new Properties();
                    // prop.load(new BufferedReader(new InputStreamReader(
                    // new FileInputStream(new
                    // File(properties.getProperty("msgPath"))), "utf-8")));
                    //
                    // Enumeration<?> enumeration = prop.propertyNames();
                    // JSONArray array = new JSONArray();
                    // JSONObject json = null;
                    // while (enumeration.hasMoreElements()) {
                    // json = new JSONObject();
                    // String head = (String) enumeration.nextElement();
                    // SimpleDateFormat simpleDateFormat = new
                    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // json.put("time", simpleDateFormat.format(new Date()));
                    // json.put("msg", prop.getProperty(head));
                    // json.put("sign", sign++);
                    // array.add(json);
                    // }
                    // jsonObject.put("message", array);
                    // }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    JSONObject json = null;
                    JSONArray array = new JSONArray();
                    if (startCount == 1) {

                        // 删除原来记录
                        Iterable<SysMsgShow> ids = sysMsgShowService.querySysMsgShowAll(user.getUserId());
                        if (ids != null) {
                            sysMsgShowService.deletAllSysMsgShowByUserIds(ids);
                        }

                        // 删除原来所有的没有订单的数据
                        userAliPayService.deleteEmptyUserAliPayRecordByPayId(user.getUserId());

                        Properties prop = new Properties();
                        prop.load(new BufferedReader(new InputStreamReader(
                                new FileInputStream(new File(properties.getProperty("msgPath"))), "iso8859-1")));

                        Enumeration<?> enumeration = prop.propertyNames();

                        SysMsgShow sysMsgShow = null;
                        // 添加消息提示
                        List<SysMsgShow> list = new ArrayList<>();
                        while (enumeration.hasMoreElements()) {
                            sysMsgShow = new SysMsgShow();
                            String head = (String) enumeration.nextElement();
                            sysMsgShow.setCreatTime(simpleDateFormat.parse(prop.getProperty(head).split("&")[1]));
                            sysMsgShow.setIsDeleted("0");
                            sysMsgShow.setMsg(prop.getProperty(head).split("&")[0]);
                            sysMsgShow.setUserId(user.getUserId());
                            list.add(sysMsgShow);
                        }

                        // 添加充值记录
                        List<UserAliPay> userAliPays = userAliPayService.queryUserAlipayAllByPayId(user.getUserId());

                        if (userAliPays != null && userAliPays.size() != 0) {
                            for (UserAliPay userAliPay : userAliPays) {

                                if (userAliPay == null || StringUtils.isEmpty(userAliPay.getUserAliAccount())
                                        || userAliPay.getUserCreateTime() == null || userAliPay.getPayCount() == null) {
                                    continue;
                                }

                                String payAccount = userAliPay.getUserAliAccount();

                                String info = "";

                                if (payAccount.matches(
                                        "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {

                                    info = payAccount.substring(0, 1) + "***"
                                            + payAccount.substring(payAccount.split("@")[0].length() - 3,
                                            payAccount.split("@")[0].length())
                                            + "@" + payAccount.split("@")[1];

                                } else {

                                    info = payAccount.substring(0, 3) + "***"
                                            + payAccount.substring(payAccount.length() - 3, payAccount.length());

                                }

                                info = properties.getProperty("payMsgStart") + info + properties.getProperty("payMsgMiddle")
                                        + userAliPay.getPayCount() + properties.getProperty("payEnd");

                                sysMsgShow = new SysMsgShow();
                                sysMsgShow.setCreatTime(userAliPay.getUserCreateTime());
                                sysMsgShow.setIsDeleted("0");
                                sysMsgShow.setMsg(info);
                                sysMsgShow.setUserId(user.getUserId());
                                list.add(sysMsgShow);

                            }

                        }

                        // payMsgStart:您从账户
                        // payMsgMiddle:向偶聊充值
                        // payEnd:元
                        // #添加默认显示的提现记录
                        // :您提现
                        // :元至账户

                        // 提现记录
                        List<UserReflect> userReflects = userReflectService
                                .queryUserReflectWithAllDrawByUserId(user.getUserId());

                        if (userReflects != null && userReflects.size() != 0) {
                            for (UserReflect userReflect : userReflects) {

                                if (userReflect == null || StringUtils.isEmpty(userReflect.getUserAccount())) {
                                    continue;
                                }
                                String withDarwAccount = userReflect.getUserAccount();
                                String info = "";
                                if (withDarwAccount.matches(
                                        "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
                                    info = withDarwAccount.substring(0, 1) + "***"
                                            + withDarwAccount.substring(withDarwAccount.split("@")[0].length() - 3,
                                            withDarwAccount.split("@")[0].length())
                                            + "@" + withDarwAccount.split("@")[1];

                                } else {
                                    info = withDarwAccount.substring(0, 3) + "***" + withDarwAccount
                                            .substring(withDarwAccount.length() - 3, withDarwAccount.length());

                                }
                                info = properties.getProperty("withDrawStart") + userReflect.getUserReflectMoney()
                                        + properties.getProperty("withDrawEnd") + info;
                                sysMsgShow = new SysMsgShow();
                                sysMsgShow.setCreatTime(userReflect.getCreateTime());
                                sysMsgShow.setIsDeleted("0");
                                sysMsgShow.setMsg(info);
                                sysMsgShow.setUserId(user.getUserId());
                                list.add(sysMsgShow);

                            }
                        }

                        // 进行整体的添加
                        sysMsgShowService.createSysMsgShow(list);

                    } // 进行分页查询
                    Page<SysMsgShow> sysMsgShows = sysMsgShowService.querySysMsgShowByUserId(startCount - 1, pageNum,
                            user.getUserId());

                    if (sysMsgShows == null) {
                        jsonObject.put("message", array);
                    } else {

                        for (SysMsgShow sMsgShow : sysMsgShows) {
                            if (sMsgShow == null) {
                                continue;
                            }
                            json = new JSONObject();
                            json.put("msg", sMsgShow.getMsg());
                            json.put("time", simpleDateFormat.format(sMsgShow.getCreatTime()));
                            array.add(json);
                        }

                        jsonObject.put("message", array);

                    }
                    long num = sysMsgShowService.queryCountSysMsgShowByUserId(user.getUserId());

                    long pageSize = 1l;
                    if (num > pageNum) {
                        if (num % pageNum != 0) {
                            pageSize = num / pageNum + 1;
                        } else {
                            pageSize = num / pageNum;
                        }

                    }
                    jsonObject.put("count", pageSize);
                    jsonObject.put("code", 200);

                    //加入足迹的提示
                    Integer userCount = userVistRecordService.findUserVisitRecordCountByVisitId(user.getUserId());

                    UserVisitRecord userVisitRecord = userVistRecordService.findUserVisitRecordfirstByVisitId(user.getUserId());

                    if (userVisitRecord != null) {
                        User visitId = ouLiaoService.queryUserByUserId(userVisitRecord.getUserId());
                        if (visitId != null) {


                            if (visitId.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                                jsonObject.put("visitname",
                                        visitId.getUserNickName().substring(0, 3) + "******"
                                                + new StringBuilder(new StringBuilder(visitId.getUserNickName()).reverse().substring(0, 2))
                                                .reverse());
                            } else {
                                jsonObject.put("visitname", visitId.getUserNickName());
                            }


                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("aHH:mm");


                        jsonObject.put("visittime", sdf.format(userVisitRecord.getCreatTime()));

                    } else {
                        jsonObject.put("visitname", "");
                        jsonObject.put("visittime", "");

                    }


                    //添加没有读写的交标提示
                    Integer unReadcount = userSureMsgCountService.findUserSureMsgCountByUserId(user.getUserId());

                    unReadcount = unReadcount == null ? 0 : unReadcount;

                    Integer readCount = Integer.valueOf(num + "");
                    jsonObject.put("readcount", readCount);

                    if (readCount.equals(unReadcount)) {
                        jsonObject.put("unreadCount", 0);
                    } else {
                        jsonObject.put("unreadCount", readCount - unReadcount < 0 ? 0 : readCount - unReadcount);
                    }

                    jsonObject.put("visitCount", userCount == null ? 0 : userCount);


                    //评论和点赞的新的提示
                    Map<String, Integer> map = userSayContentSecondService.queryNewSupportCommontAndSupportByUserId(user.getUserId());


                    jsonObject.put("commont", map.get("commont") == 0 ? "false" : "true");

                    jsonObject.put("support", map.get("support") == 0 ? "false" : "true");

                    jsonObject.put("code", 200);


                    return jsonObject;

                default:

                    if (startCount == 1) {
                        // 数字格式显示问题
                        DecimalFormat decimalFormat = new DecimalFormat("0.0");
                        if ("true".equals(user.getUserContract())) {
                            jsonObject.put("earn",
                                    decimalFormat.format(user.getUserCallEarn() == null ? 0 : user.getUserCallEarn()));
                        } else {
                            jsonObject.put("consume", decimalFormat
                                    .format(user.getUserCallConsume() == null ? 0 : user.getUserCallConsume()));
                        }
                        jsonObject.put("money",
                                decimalFormat.format(user.getUserMoney() == null ? 0 : user.getUserMoney()));
                        jsonObject.put("callTotal", user.getUserCallTotal() == null ? 0 : user.getUserCallTotal());

                    }

                    break;
            }


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
            JSONArray arrAccept = new JSONArray();
            JSONArray arrSend = new JSONArray();
            if (type == null || type == 0) {
                // 删除原来所有的没有订单的数据
                userAliPayService.deleteEmptyUserAliPayRecordByPayId(user.getUserId());

                Page<UserCallMark> userCallMarks = userCallMarkService.queryUserCallMarkByUserId(startCount - 1, pageNum,
                        user.getUserId());
                JSONObject sendJson = null;
                JSONObject acceptJson = null;
                for (UserCallMark userCallMark : userCallMarks) {
                    // 数字格式显示问题
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    if (userCallMark == null) {
                        continue;

                    }

                    SimpleDateFormat simpleDateNow = new SimpleDateFormat("MM月dd日 HH:mm");
                    SimpleDateFormat simpleDatePast = new SimpleDateFormat("yy年MM月dd日 HH:mm");

                    Calendar calendarPast = Calendar.getInstance();
                    Calendar calendarNow = Calendar.getInstance();
                    calendarNow.setTime(new Date());
                    calendarPast.setTime(userCallMark.getUserCreateTime());
                    int year = calendarNow.get(Calendar.YEAR) - calendarPast.get(Calendar.YEAR);

                    String date = null;
                    switch (year) {
                        case 0:
                            date = simpleDateNow.format(calendarPast.getTime());
                            break;

                        default:
                            date = simpleDatePast.format(calendarPast.getTime());
                            break;
                    }
                    if (user.getUserId().equals(userCallMark.getUserCalledId())) {
                        acceptJson = new JSONObject();
                        User userFind = ouLiaoService.queryUserByUserId(userCallMark.getUserId());

                        if (userFind == null) {
                            jsonObject.put("code", 203);
                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonObject;
                        }

                        acceptJson.put("acceptId", userCallMark.getUserId());
                        String headUrl = ouLiaoService.queryUserByUserId(userCallMark.getUserId()).getUserHeadPic();
                        Boolean headflag = false;

                        if (StringUtils.isEmpty(headUrl)) {
                            headUrl = "985595";
                        } else if (headUrl.contains("//")) {
                            headflag = true;

                        } else {
                            headUrl = headUrl.split("\\.")[0];
                        }
                        if (headflag) {
                            acceptJson.put("acceptUrl", headUrl);
                        } else {
                            acceptJson.put("acceptUrl", properties.getProperty("headUrl") + userCallMark.getUserId() + "/"
                                    + headUrl + "/head/download");
                        }
                        acceptJson.put("acceptMoney",
                                decimalFormat.format(userCallMark.getUserCallEarn() == null ? 0 : userCallMark.getUserCallEarn()));

                        if (userFind.getUserNickName()
                                .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                            acceptJson.put("acceptName",
                                    userFind.getUserNickName().substring(0, 3) + "******"
                                            + new StringBuilder(
                                            new StringBuilder(userFind.getUserNickName()).reverse().substring(0, 2))
                                            .reverse());
                        } else {
                            acceptJson.put("acceptName", userFind.getUserNickName());
                        }

                        acceptJson.put("acceptCallTime", userCallMark.getUserCallTime());
                        acceptJson.put("sign", sign++);
                        acceptJson.put("acceptCallDate", date);
                        arrAccept.add(acceptJson);

                    } else {

                        sendJson = new JSONObject();
                        User userFind = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId());

                        if (userFind == null) {
                            jsonObject.put("code", 203);
                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonObject;
                        }
                        sendJson.put("sendId", userCallMark.getUserCalledId());
                        String headUrl = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId()).getUserHeadPic();

                        Boolean headflag = false;

                        if (StringUtils.isEmpty(headUrl)) {
                            headUrl = "985595";
                        } else if (headUrl.contains("//")) {
                            headflag = true;

                        } else {
                            headUrl = headUrl.split("\\.")[0];
                        }
                        if (headflag) {
                            sendJson.put("sendUrl", headUrl);
                        } else {
                            sendJson.put("sendUrl", properties.getProperty("headUrl") + userCallMark.getUserCalledId() + "/"
                                    + headUrl + "/head/download");
                        }
                        sendJson.put("sendMoney", decimalFormat.format(userCallMark.getUserCallCost()));
                        if (userFind.getUserNickName()
                                .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                            sendJson.put("sendName",
                                    userFind.getUserNickName().substring(0, 3) + "******"
                                            + new StringBuilder(
                                            new StringBuilder(userFind.getUserNickName()).reverse().substring(0, 2))
                                            .reverse());
                        } else {
                            sendJson.put("sendName", userFind.getUserNickName());
                        }

                        sendJson.put("sendCallTime", userCallMark.getUserCallTime());
                        sendJson.put("sign", sign++);
                        sendJson.put("sendCallDate", date);
                        arrSend.add(sendJson);
                    }

                }
            }

            JSONArray arrPay = new JSONArray();
            JSONArray arrWithDraw = new JSONArray();
            JSONObject payJson = null;
            JSONObject withDrawJson = null;
            if (type == null || type == 1) {
                // 添加提现和充值记录
                Page<UserAliPay> userAliPays = userAliPayService.queryUserAlipayPayRecordByPayId(startCount - 1, pageNum,
                        user.getUserId());
                if (userAliPays != null) {

                    for (UserAliPay userAliPay : userAliPays) {

                        if (userAliPay == null || StringUtils.isEmpty(userAliPay.getUserAliAccount())) {
                            continue;
                        }
                        payJson = new JSONObject();

                        String payAccount = userAliPay.getUserAliAccount();

                        if (payAccount.matches(

                                "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {

                            payJson.put("payAccount",
                                    payAccount.substring(0, 1) + "***"
                                            + payAccount.substring(payAccount.split("@")[0].length() - 3,
                                            payAccount.split("@")[0].length())
                                            + "@" + payAccount.split("@")[1]);

                        } else {

                            payJson.put("payAccount", payAccount.substring(0, 3) + "***"
                                    + payAccount.substring(payAccount.length() - 3, payAccount.length()));

                        }
                        payJson.put("payCount", userAliPay.getPayCount() + "");
                        payJson.put("payTime", simpleDateFormat.format(userAliPay.getUserCreateTime()));
                        payJson.put("sign", sign++);
                        arrPay.add(payJson);
                    }
                }
            }
            if (type == null || type == 2) {
                // 提现记录
                Page<UserReflect> userReflects = userReflectService.queryUserReflectWithDrawByUserId(startCount - 1,
                        pageNum, user.getUserId());

                if (userReflects != null) {

                    for (UserReflect userReflect : userReflects) {
                        if (userReflect == null || StringUtils.isEmpty(userReflect.getUserAccount())) {
                            continue;
                        }
                        withDrawJson = new JSONObject();

                        String withDarwAccount = userReflect.getUserAccount();

                        if (withDarwAccount.matches(
                                "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
                            withDrawJson.put("withDarwAccount",
                                    withDarwAccount.substring(0, 1) + "***"
                                            + withDarwAccount.substring(withDarwAccount.split("@")[0].length() - 3,
                                            withDarwAccount.split("@")[0].length())
                                            + "@" + withDarwAccount.split("@")[1]);

                        } else {
                            withDrawJson.put("withDarwAccount", withDarwAccount.substring(0, 3) + "***"
                                    + withDarwAccount.substring(withDarwAccount.length() - 3, withDarwAccount.length()));

                        }

                        withDrawJson.put("withDrawCount", userReflect.getUserReflectMoney() + "");
                        withDrawJson.put("withDarwTime", simpleDateFormat.format(userReflect.getCreateTime()));

                        withDrawJson.put("sign", sign++);
                        arrWithDraw.add(withDrawJson);

                    }
                }
            }
            jsonObject.put("code", 200);

            if (type != null) {
                List<UserReflect> userReflects = userReflectService.queryUserReflectWithAllDrawByUserId(user.getUserId());

                int totalreflect = 0;
                if (userReflects != null && userReflects.size() != 0) {


                    for (UserReflect userReflect : userReflects) {

                        if (userReflect == null || userReflect.getUserReflectMoney() == null) {
                            continue;
                        }

                        totalreflect += userReflect.getUserReflectMoney();

                    }

                }

                jsonObject.put("totalreflect", totalreflect);
            }

            if (type == null || type == 0) {
                jsonObject.put("acceptRecord", arrAccept);
                jsonObject.put("sendRecord", arrSend);
            }
            if (type == null || type == 1) {

                jsonObject.put("payRecord", arrPay);
            }
            if (type == null || type == 2) {
                jsonObject.put("withDrawRecord", arrWithDraw);
            }
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;

    }

    // 用户结束后评分
    @ResponseBody
    @RequestMapping(value = "callMark/{uid}/{id}/{score}/endCallMrakScore")
    public JSONObject endCallMrakScore(@PathVariable("uid") String uid, @PathVariable("score") Long score,
                                       @PathVariable("id") Integer id, @RequestParam("key") String key, HttpServletRequest request,
                                       HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
            if (user == null) {
                jsonObject.put("code", 203);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            if (request.getSession().getAttribute(key) == null || user == null) {
                jsonObject.put("code", 208);
                if (user != null) {
                    jsonObject.put("cid", user.getUserId());
                }

                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }
            String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            if (!user.getUserNum().equals(userNum)) {
                jsonObject.put("code", 209);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            UserCallMark userCallMark = userCallMarkService.queryUserCallMarkByUserCallMarkId(id);
            if (userCallMark == null || "true".equals(userCallMark.getIsScore())) {
                jsonObject.put("code", 214);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 判断打入者还是被打入者--打入者可以评分
            if (user.getUserId().equals(userCallMark.getUserId())) {
                user = ouLiaoService.queryUserByUserId(userCallMark.getUserCalledId());
                if (user != null) {
                    int count = ouLiaoService.updateUserCallScoreByUserId(
                            user.getUserCallScore() == null ? score : user.getUserCallScore() + score,
                            user.getUserId());
                    if (count == 1) {

                        count = userCallMarkService.updateUserCallMarkIsScoreByUserCallMarkId(id);
                        if (count == 1) {
                            jsonObject.put("code", 200);
                        } else {
                            jsonObject.put("code", 210);
                        }

                    } else {
                        jsonObject.put("code", 210);
                    }

                } else {
                    jsonObject.put("code", 210);
                }

            } else {
                jsonObject.put("code", 210);
            }
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

}