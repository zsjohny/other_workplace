/**
 *
 */
package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.*;
import com.ouliao.service.versionfirst.*;
import com.xiaoluo.util.DesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author xiaoluo
 * @version $Id: OuLiaoConcernController.java, 2016年2月23日 下午10:06:49
 */
@Controller
//@RequestMapping(value = "user/concern", method = RequestMethod.POST)
@RequestMapping(value = "user/concern")
public class OuLiaoConcernController {
    @Autowired
    private UserConcernService userConcernService;
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserSayService userSayService;
    @Autowired
    private UserCallRoomService userCallRoomService;
    @Autowired
    private UserBlackListService userBlackListService;
    @Autowired
    private HuanXinService huanXinService;

    // 加入缓存进行操作
    private JedisPool pool = new JedisPool("localhost", 10088);
    private Jedis jedis = pool.getResource();

    // 增减关注
    @ResponseBody
    @RequestMapping(value = "userConcern/{uid}/{userOnfocusId}/{isConcern}/updateUserConcern")
    public JSONObject updateUserConcern(@PathVariable("uid") String uid,
                                        @PathVariable("userOnfocusId") Integer userOnfocusId, @PathVariable("isConcern") String isConcern,
                                        @RequestParam("key") String key, @RequestParam(value = "cid", required = false) String cid,
                                        HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || !isConcern.matches("[01]")) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
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
                jsonObject.put("cid", user.getUserId());
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 不能添加自己
            if (user.getUserId().equals(userOnfocusId)) {
                jsonObject.put("code", 213);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 是否在黑名单---互相查询
            UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(user.getUserId(),
                    userOnfocusId);
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 217);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            userBlackList = userBlackListService.queryUserIsBlackListById(userOnfocusId, user.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 218);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 查询所要添加的关注人是否在用户列表中
            User isUser = ouLiaoService.queryUserByUserId(userOnfocusId);

            if (isUser == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }

            UserConcern userConcern = userConcernService.queryUserIsConcernById(user.getUserId(), userOnfocusId);
            int count = 0;

         
            if (userConcern != null) {

                count = userConcernService.updateUserConcernByUserConcernId(isConcern, userConcern.getUserConcernId(),
                        user);

            } else {
                userConcern = new UserConcern();
                userConcern.setUserOnfocusId(userOnfocusId);
                userConcern.setUserId(user.getUserId());
                userConcern.setIsDeleted("0");
                userConcern.setUserCreateTime(new Date());
                userConcern.setUserModifyTime(new Date());
                if ("true".equals(isUser.getUserContract())) {
                    userConcern.setUserConract("true");
                } else {
                    userConcern.setUserConract("false");
                }

                String userSign = "customer";
                if (StringUtils.isNotEmpty(cid)) {
                    userSign = cid;
                }
                userConcern = userConcernService.createUserConcern(userConcern, user, userSign);

                if (userConcern != null) {
                    count = 1;
                }

            }

            if (count == 1) {
                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (

                Exception e)

        {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;

    }

    // 查看关注
    @ResponseBody
    @RequestMapping(value = "userConcern/{uid}/{startPage}/viewUserConcernList")
    public JSONObject viewUserConcernList(@PathVariable("uid") String uid, @PathVariable("startPage") Integer startPage,
                                          @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
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
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            Integer pageCount = Integer.valueOf(properties.getProperty("concernStartCount"));

            Page<UserConcern> userConcernLists = userConcernService.queryUserConcernByUserConcernId(startPage - 1,
                    pageCount, user.getUserId());

            if (userConcernLists == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }

            JSONArray jsonArray = new JSONArray();
            JSONObject json = null;

            for (UserConcern uc : userConcernLists) {
                if (uc == null) {
                    continue;
                }
                json = new JSONObject();

                user = ouLiaoService.queryUserByUserId(uc.getUserOnfocusId());
                // 用户昵称没有设置的
                if (user == null || StringUtils.isEmpty(user.getUserNickName())) {
                    continue;
                }
                json.put("id", user.getUserId());

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
                    json.put("url", user.getUserHeadPic());
                } else {
                    json.put("url",
                            properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
                }
                if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    json.put("name",
                            user.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    json.put("name", user.getUserNickName());
                }
                json.put("sign", user.getUserSign() == null ? "" : user.getUserSign());
                json.put("author", user.getUserAuth() == null ? "" : user.getUserAuth());
                // 判断主播是否在线和在线可接电话
                // 表示在线
                boolean flag = false;
                String[] weekArr = null;
                if (StringUtils.isEmpty(user.getUserCallTimeWeek())) {
                    weekArr = properties.getProperty("callTime").split(",");
                } else if (user.getUserCallTimeWeek().indexOf(",") > -1) {
                    weekArr = user.getUserCallTimeWeek().split(",");
                } else {
                    weekArr = new String[]{user.getUserCallTimeWeek()};
                }
                for (String str : weekArr) {
                    if (StringUtils.isEmpty(str)) {
                        continue;
                    }

                    Date date = new Date();

                    Calendar calendarBefore = Calendar.getInstance();
                    Calendar calendarAfter = Calendar.getInstance();
                    int day = 0;
                    switch (str) {
                        case "每周日":
                            day = 1;
                            break;
                        case "每周一":
                            day = 2;
                            break;
                        case "每周二":
                            day = 3;
                            break;
                        case "每周三":
                            day = 4;
                            break;
                        case "每周四":
                            day = 5;
                            break;
                        case "每周五":
                            day = 6;
                            break;
                        default:
                            day = 7;
                            break;
                    }
                    // 先判断是否在今天
                    if (day != calendarAfter.get(Calendar.DAY_OF_WEEK)) {
                        continue;
                    }

                    // 表示他在线
                    flag = true;

                    String[] times = null;

                    if (StringUtils.isEmpty(user.getUserCallTime())) {
                        times = properties.getProperty("defaultOnlieTime").split("-");

                    } else {
                        times = user.getUserCallTime().trim().split("-");
                    }

                    if (times[0].split(":")[0].startsWith("0")) {
                        calendarBefore.set(Calendar.HOUR_OF_DAY,
                                Integer.parseInt(times[0].split(":")[0].substring(1, 2)));
                    } else {
                        calendarBefore.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0].split(":")[0]));
                    }
                    calendarBefore.set(Calendar.MINUTE, Integer.parseInt(times[0].split(":")[1]));
                    calendarAfter.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[1].split(":")[0]));
                    calendarAfter.set(Calendar.MINUTE, Integer.parseInt(times[1].split(":")[1]));
                    // 在线
                    if (date.after(calendarBefore.getTime()) && date.before(calendarAfter.getTime())) {
                        // 在线是否可以接听电话

                        // UserCallRoom userCallRoom = userCallRoomService
                        // .queryByUserCallRoomByUserCalledId(user.getUserId());
                        // 原来是任意一方接听和主动打电话
                        // UserCallRoom userCallRoom = userCallRoomService
                        // .queryByUserCallRoomIsExistById(user.getUserId());
                        //
                        // if (userCallRoom != null) {
                        //
                        // json.put("onlineStaus", 2);// 在线可以打通
                        // } else {
                        // json.put("onlineStaus", 1);// 在线不可打通
                        // }

                        try {
                            if ("true".equals(jedis.get("user:run:" + user.getUserId()))) {

                                json.put("onlineStaus", 2);// 在线可以打通

                            } else {
                                json.put("onlineStaus", 1);// 在线不可打通
                            }
                        } catch (Exception e) {
                            // 原来是任意一方接听和主动打电话
                            List<UserCallRoom> userCallRooms = userCallRoomService
                                    .queryByUserCallRoomAllIsExistById(user.getUserId());

                            if (userCallRooms != null && userCallRooms.size() != 0) {

                                json.put("onlineStaus", 2);// 在线可以打通
                            } else {
                                json.put("onlineStaus", 1);// 在线不可打通
                            }

                        }
                    } else {
                        json.put("onlineStaus", 0);// 不在线
                    }

                }
                if (!flag) {
                    json.put("onlineStaus", 0);// 不在线
                }

                jsonArray.add(json);
            }
            try {
                jedis.disconnect();
            } catch (Exception e) {

            }
            jsonObject.put("code", 200);

            jsonObject.put("data", jsonArray);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 查看他的关注
    @ResponseBody
    @RequestMapping(value = "userConcern/{uid}/{startPage}/viewUserConcernListByUserConcern")
    public JSONObject viewUserConcernListByUserConcern(@PathVariable("uid") String uid,
                                                       @PathVariable("startPage") Integer startPage, @RequestParam("id") Integer id,
                                                       @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
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
            User userFind = ouLiaoService.queryUserByUserId(id);
            if (request.getSession().getAttribute(key) == null || user == null || userFind == null) {
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
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            Integer pageCount = Integer.valueOf(properties.getProperty("concernStartCount"));

            Page<UserConcern> userConcernLists = userConcernService.queryUserConcernByUserConcernId(startPage - 1,
                    pageCount, id);

            if (userConcernLists == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }

            JSONArray jsonArray = new JSONArray();
            JSONObject json = null;

            for (UserConcern uc : userConcernLists) {
                if (uc == null) {
                    continue;
                }
                json = new JSONObject();

                user = ouLiaoService.queryUserByUserId(uc.getUserOnfocusId());
                // 用户昵称没有设置的
                if (user == null || StringUtils.isEmpty(user.getUserNickName())) {
                    continue;
                }
                json.put("id", user.getUserId());

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
                    json.put("url", user.getUserHeadPic());
                } else {
                    json.put("url",
                            properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
                }
                if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    json.put("name",
                            user.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    json.put("name", user.getUserNickName());
                }
                List<UserSayContent> list = userSayService.querySayContentByUserId(user.getUserId());

                if (list == null || list.size() == 0) {
                    json.put("sign", "");
                } else {
                    json.put("sign", list.get(0).getUserContent());
                }

                json.put("contract", "true".equals(user.getUserContract()) == true ? "true" : "false");
                jsonArray.add(json);
            }

            jsonObject.put("code", 200);
            jsonObject.put("data", jsonArray);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 查看关注他的
    @ResponseBody
    @RequestMapping(value = "userConcern/{uid}/{startPage}/viewUserConcernListByUserConcerned")
    public JSONObject viewUserConcernListByUserConcerned(@PathVariable("uid") String uid,
                                                         @PathVariable("startPage") Integer startPage, @RequestParam("id") Integer id,
                                                         @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
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
            User userFind = ouLiaoService.queryUserByUserId(id);
            if (request.getSession().getAttribute(key) == null || user == null || userFind == null) {
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
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            // 第一期的活动考虑到内存问题只能去掉查询
            // if ("true".equals(userFind.getUserContract())) {
            // jsonObject.put("data", new JSONArray());
            // jsonObject.put("open", false);
            // jsonObject.put("code", 200);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }

            // 拦截偶聊客服关注的搜索
            if (properties.get("ouliaoService").equals(userFind.getUserPhone())) {
                jsonObject.put("data", new JSONArray());
                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            Integer pageCount = Integer.valueOf(properties.getProperty("concernStartCount"));

            List<UserConcern> userConcernLists = userConcernService.queryUserConcernedByUserConcernId(startPage,
                    pageCount, id);

            if (userConcernLists == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }

            JSONArray jsonArray = new JSONArray();
            JSONObject json = null;

            for (UserConcern uc : userConcernLists) {
                if (uc == null) {
                    continue;
                }
                json = new JSONObject();

                user = ouLiaoService.queryUserByUserId(uc.getUserId());
                // 用户昵称没有设置的
                if (user == null || StringUtils.isEmpty(user.getUserNickName())) {
                    continue;
                }
                json.put("id", user.getUserId());

                String headUrl = user.getUserHeadPic();
                Boolean headflag = false;
                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;

                } else {
                    headUrl = headUrl.split("\\.")[0];
                }
                if (headflag) {
                    json.put("url", user.getUserHeadPic());
                } else {
                    json.put("url",
                            properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
                }
                if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    json.put("name",
                            user.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    json.put("name", user.getUserNickName());
                }
                List<UserSayContent> list = userSayService.querySayContentByUserId(user.getUserId());

                if (list == null || list.size() == 0) {
                    json.put("sign", "");
                } else {
                    json.put("sign", list.get(0).getUserContent());
                }

                json.put("contract", "true".equals(user.getUserContract()) == true ? "true" : "false");


                // 第二版增加了环信Id
                // 创建环信账号
                HuanXin huanXin = huanXinService.queryIsExist(user.getUserId());
                if (huanXin != null) {
                    json.put("huanxinName", huanXin.getHuaXinName());
                } else {

                    HttpClient httpClient = HttpClients.createDefault();
                    HttpPost post = new HttpPost("https://a1.easemob.com/szkj2016/ouliao/users");

                    String name = System.currentTimeMillis() + "";
                    String huanXinpass = "xiaoluo";
                    JSONObject jsons = new JSONObject();
                    jsons.put("username", name);
                    jsons.put("password", huanXinpass);

                    StringEntity entity = new StringEntity(jsons.toString());
                    entity.setContentEncoding("utf-8");
                    entity.setContentType("application/json");
                    post.setEntity(entity);

                    HttpResponse responsea = httpClient.execute(post);
                    HttpEntity entitys = responsea.getEntity();
                    String data = EntityUtils.toString(entitys);
                    jsons = JSONObject.fromObject(data);
                    JSONArray arrays = JSONArray.fromObject(jsons.get("entities"));

                    jsons = JSONObject.fromObject(arrays.get(0));
                    String ids = (String) jsons.get("uuid");
                    if (StringUtils.isNotEmpty(ids)) {
                        HuanXin huanXinPost = new HuanXin();
                        huanXinPost.setCreatTime(new Date());
                        huanXinPost.setHuaXinUUid(ids);
                        huanXinPost.setHuaXinName(name);
                        huanXinPost.setOwnerId(user.getUserId());
                        huanXinPost.setPass(huanXinpass);
                        huanXinService.saveHuanXin(huanXinPost);
                    }
                    json.put("huanxinName", name);
                }

                jsonArray.add(json);
            }

            jsonObject.put("code", 200);
            jsonObject.put("data", jsonArray);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 查看他的关注和关注他的人的搜索
    @ResponseBody
    @RequestMapping(value = "userConcern/{uid}/{startCount}/{sign}/concernFind")
    public JSONObject concernFind(@PathVariable("uid") String uid, @PathVariable("startCount") Integer startCount,
                                  @PathVariable("sign") Integer sign, @RequestParam("key") String key, @RequestParam("word") String word,
                                  @RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(word) || startCount <= 0) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
            User userFind = ouLiaoService.queryUserByUserId(id);
            if (request.getSession().getAttribute(key) == null || user == null || userFind == null) {
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
                    OuLiaoRecommondController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));

            // 第一期的活动考虑到内存问题只能去掉查询
            // if ("true".equals(userFind.getUserContract())) {
            // jsonObject.put("data", new JSONArray());
            // jsonObject.put("open", false);
            // jsonObject.put("code", 200);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }

            // 拦截偶聊客服关注的搜索
            if (properties.get("ouliaoService").equals(userFind.getUserPhone())) {
                jsonObject.put("data", new JSONArray());
                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            Integer pageSize = Integer.valueOf(properties.getProperty("concernCount"));
            List<Integer> ids = null;
            switch (sign) {
                case 0:
                    List<UserConcern> userConcerns = userConcernService.queryUserConcerndAllByUserId(id);

                    ids = new ArrayList<>();
                    for (UserConcern uc : userConcerns) {
                        if (uc == null) {
                            continue;
                        }
                        ids.add(uc.getUserOnfocusId());

                    }
                    break;

                default:
                    userConcerns = userConcernService.queryUserConcerndedAllByUserId(id);

                    ids = new ArrayList<>();
                    for (UserConcern uc : userConcerns) {
                        if (uc == null) {
                            continue;
                        }
                        ids.add(uc.getUserId());

                    }

                    break;
            }

            List<User> users = ouLiaoService.queryUserByUserNickNameOrUserAuthAndUserId((startCount - 1) * pageSize,
                    pageSize, ids, word);

            JSONObject json = null;
            JSONArray array = new JSONArray();
            for (User us : users) {
                if (us == null) {
                    continue;
                }

                json = new JSONObject();
                json.put("id", us.getUserId());

                Boolean headflag = false;
                String headUrl = us.getUserHeadPic();
                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;

                } else {
                    headUrl = headUrl.split("\\.")[0];
                }
                if (headflag) {
                    json.put("url", us.getUserHeadPic());
                } else {
                    json.put("url",
                            properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl + "/head/download");
                }
                if (us.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    json.put("name",
                            us.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(us.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    json.put("name", us.getUserNickName());
                }

                json.put("author", us.getUserAuth() == null ? "" : us.getUserAuth());

                List<UserSayContent> list = userSayService.querySayContentByUserId(us.getUserId());

                if (list == null || list.size() == 0) {
                    json.put("sign", "");
                } else {
                    json.put("sign", list.get(0).getUserContent());
                }

                json.put("contract", "true".equals(us.getUserContract()) == true ? "true" : "false");

                array.add(json);

            }

            jsonObject.put("code", 200);

            jsonObject.put("data", array);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

}
