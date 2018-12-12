/**
 *
 */
package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserActive;
import com.ouliao.domain.versionfirst.UserBlackList;
import com.ouliao.domain.versionfirst.UserConcern;
import com.ouliao.service.versionfirst.*;
import com.xiaoluo.util.Des16Util;
import com.xiaoluo.util.DesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author xiaoluo
 * @version $Id: OuLiaoSetController.java, 2016年2月18日 下午10:40:09
 */
@Controller
@RequestMapping(value = "user/set", method = RequestMethod.POST)

public class OuLiaoSetController {
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserBlackListService userBlackListService;
    @Autowired
    private UserConcernService userConcernService;
    @Autowired
    private UserActiveServer userActiveServer;
    @Autowired
    private UserReflectService userReflectService;

    // 上传头像
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/uploadHeadPic")
    public JSONObject uploadHeadPic(@PathVariable("uid") String uid, @RequestParam("key") String key,
                                    @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        // File saveFile = new File("D:\\ouliao\\user\\head\\" + uid + "\\");
        File saveFile = new File("/opt/ouliao/head/" + uid + "/");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        try {
            if (StringUtils.isEmpty(uid)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
            // if (request.getSession().getAttribute(key) == null || user ==
            // null) {
            // jsonObject.put("code", 208);
            // response.setStatus(HttpServletResponse.SC_OK);
            // return jsonObject;
            // }
            /*
             * String userNum = DesUtil.decrypt(key, user.getCurrentTime()); if
			 * (!user.getUserNum().equals(userNum)) { jsonObject.put("code",
			 * 209); response.setStatus(HttpServletResponse.SC_OK); return
			 * jsonObject; }
			 */

            String name = System.currentTimeMillis() + ".jpg";

            // 删除其他图片
            File[] files = saveFile.listFiles();
            for (File deleFile : files) {
                /*
                 * if (deleFile.getName().equals(name)) { continue; }
				 */
                if (deleFile.exists()) {
                    System.gc();
                    // deleFile.deleteOnExit();
                    deleFile.delete();
                }

            }
            ouLiaoService.updateHeadPicByUserNum(name, uid);
            file.transferTo(new File(saveFile.getPath(), name));

            jsonObject.put("code", 200);
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));


            Boolean headflag = false;
            String headUrl = name;
            if (StringUtils.isEmpty(headUrl)) {
                headUrl = "985595";
            } else if (headUrl.contains("//")) {
                headflag = true;

            } else {
                headUrl = headUrl.split("\\.")[0];
            }
            if (headflag) {
                jsonObject.put("url",
                        user.getUserHeadPic());
            } else {

                jsonObject.put("url",
                        properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
            }
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;
    }

    // 修改昵称
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/updateNick")
    public JSONObject updateNickName(@PathVariable("uid") String uid, @RequestParam("nickName") String nickName,
                                     @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        try {
            if (StringUtils.isEmpty(key) || nickName.length() > 16 || nickName.matches(
                    "^(((13[0-9])(14[57])|(15([0-3]|[5-9]))|(17([0-1]|[6-8]))|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$")) {
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
            user = ouLiaoService.queryUserByNickName(nickName);

            if (user != null) {
                jsonObject.put("code", 203);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            ouLiaoService.updateNickNameByUserNum(nickName, userNum);

            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;
    }

    // 修改签名
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/updateSign")
    public JSONObject updateSign(@PathVariable("uid") String uid, @RequestParam("userSign") String userSign,
                                 @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || userSign.length() > 16) {
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

            ouLiaoService.updateSignByUserNum(userSign, userNum);

            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;
    }

    // 修改支付宝
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/updateAlipay")
    public JSONObject updateAlipay(@PathVariable("uid") String uid, @RequestParam("userAliName") String userAliName,
                                   @RequestParam("userAliAccount") String userAliAccount, @RequestParam("key") String key,
                                   HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            // 支付宝账户验证
            if (StringUtils.isEmpty(key) || userAliName.length() <= 1 || userAliAccount.length() < 8
                    || (!userAliAccount.matches(
                    "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
                    && !userAliAccount.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$"))) {
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

            int count = ouLiaoService.updateAlipayByUserNum(userAliAccount, userAliName, uid);

            if (count == 1) {
                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
            }

            return jsonObject;
        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 增减黑名单
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/{userBlackId}/{isBalckList}/updateUserBlackList")
    public JSONObject updateUserBlackList(@PathVariable("uid") String uid,
                                          @PathVariable("userBlackId") Integer userBlackId, @PathVariable("isBalckList") String isBalckList,
                                          @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || !isBalckList.matches("[01]")) {
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

            // 不能添加自己
            if (user.getUserId().equals(userBlackId)) {
                jsonObject.put("code", 213);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 查询所要添加的黑名单是否在用户列表中
            User isUser = ouLiaoService.queryUserByUserId(userBlackId);

            if (isUser == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }

            UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(user.getUserId(), userBlackId);
            int count = 0;
            if (userBlackList != null) {
                // 添加时候进行判断
                switch (isBalckList) {
                    case "0":
                        if (userBlackListService.queryBlackListCountByUserId(user.getUserId()) > 20) {
                            jsonObject.put("code", 214);
                            return jsonObject;
                        }
                        break;
                    default:
                        if (userBlackListService.queryBlackListCountByUserId(user.getUserId()) == 0) {
                            jsonObject.put("code", 214);
                            return jsonObject;
                        }
                }

                count = userBlackListService.updateUserBlackListByUserBlackListId(isBalckList,
                        userBlackList.getUserBlackListId());

                // 把对应的关注还原或者是取消
                if ("0".equals(isBalckList)) {
                    UserConcern userConcern = userConcernService.queryUserIsConcernById(user.getUserId(), userBlackId);
                    if (userConcern != null) {
                        count = userConcernService.updateUserConcernByUserConcernId("1",
                                userConcern.getUserConcernId(), user);
                    }
                }
                // } else if ("1".equals(isBalckList)) {
                // UserConcern userConcern =
                // userConcernService.queryUserIsConcernById(user.getUserId(),
                // userBlackId);
                // if (userConcern != null) {
                // count =
                // userConcernService.updateUserConcernByUserConcernId("0",
                // userConcern.getUserConcernId());
                // }
                // }

            } else {
                userBlackList = new UserBlackList();
                userBlackList.setUserBlackId(userBlackId);
                userBlackList.setUserId(user.getUserId());
                userBlackList.setIsDeleted("0");
                userBlackList.setUserCreateTime(new Date());
                userBlackList = userBlackListService.createUserBlackList(userBlackList);

                // 把对应的关注取消
                UserConcern userConcern = userConcernService.queryUserIsConcernById(user.getUserId(), userBlackId);
                if (userConcern != null) {
                    count = userConcernService.updateUserConcernByUserConcernId("1", userConcern.getUserConcernId(), user);
                }
                if (userBlackList != null) {
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

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 批量删除黑名单
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/updateUserBlackListIsDeletedByIds")
    public JSONObject updateUserBlackListIsDeletedByIds(@PathVariable("uid") String uid,
                                                        @RequestParam("ids") String ids, @RequestParam("key") String key, HttpServletRequest request,
                                                        HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(ids)) {
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

            JSONArray array = JSONArray.fromObject(ids);

            List<Integer> list = new ArrayList<>();

            for (int i = 0; i < array.size(); i++) {
                Integer id = (Integer) array.get(i);
                if (id.equals(user.getUserId())) {
                    continue;
                }
                // 查询所要添加的黑名单是否在用户列表中
                User isUser = ouLiaoService.queryUserByUserId(id);
                if (isUser == null) {
                    continue;

                }

                UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(user.getUserId(), id);
                if (userBlackList == null) {
                    continue;

                }
                list.add(id);

                // // 对应新增
                // UserConcern userConcern =
                // userConcernService.queryUserIsConcernById(user.getUserId(),
                // id);

                // if (userConcern != null) {
                // userConcernService.updateUserConcernByUserConcernId("0",
                // userConcern.getUserConcernId());
                // }

            }

            int count = userBlackListService.updateUserBlackListIsDeletedAllByUserBlackByIds(user.getUserId(), list);

            if (count == 0) {
                jsonObject.put("code", 210);

            } else {

                jsonObject.put("code", 200);
                jsonObject.put("count", userBlackListService.queryBlackListCountByUserId(user.getUserId()));

            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 查看黑名单
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/{startPage}/viewUserBlackList")
    public JSONObject viewUserBlackList(@PathVariable("uid") String uid, @PathVariable("startPage") Integer startPage,
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
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

            Integer pageCount = Integer.valueOf(properties.getProperty("blackListStartCount"));

            Page<UserBlackList> isBalckLists = userBlackListService.queryUserBlackListByUserBlackId(startPage - 1,
                    pageCount, user.getUserId());

            if (isBalckLists == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;

            }

            JSONArray jsonArray = new JSONArray();
            JSONObject json = null;

            for (UserBlackList ub : isBalckLists) {
                if (ub == null) {
                    continue;
                }
                json = new JSONObject();

                user = ouLiaoService.queryUserByUserId(ub.getUserBlackId());
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
                    json.put("url",
                            user.getUserHeadPic());
                } else {
                    json.put("url",
                            properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
                }
                if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    json.put("name", user.getUserNickName().substring(0, 3) + "******"
                            + new StringBuilder(new StringBuilder(user.getUserNickName()).reverse().substring(0, 2)));
                } else {
                    json.put("name", user.getUserNickName());
                }

                if ("true".equals(user.getUserContract())) {
                    json.put("contract", true);
                    json.put("author", StringUtils.isEmpty(user.getUserAuth()) ? "" : user.getUserAuth());
                } else {
                    json.put("contract", false);
                    json.put("author", "");
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

    // 上传录音
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/uploadRecord")
    public JSONObject uploadRecord(@PathVariable("uid") String uid, @RequestParam("key") String key,
                                   @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        // File saveFile = new File("D:\\ouliao\\user\\record\\" + uid + "\\");
        File saveFile = new File("/opt/ouliao/record/" + uid + "/");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        try {
            if (StringUtils.isEmpty(uid)) {
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

            String name = System.currentTimeMillis() + ".amr";
            ouLiaoService.updateUserRecordByUserNum(name, uid);
            file.transferTo(new File(saveFile.getPath(), name));
            // 删除其他录音
            File[] files = saveFile.listFiles();
            for (File deleFile : files) {
                if (deleFile.getName().equals(name)) {
                    continue;
                }
                System.gc();
                deleFile.delete();

            }

            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
            return jsonObject;
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;
    }

    // 设置查看
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/viewSet")
    public JSONObject viewSet(@PathVariable("uid") String uid, @RequestParam("key") String key,
                              @RequestParam(value = "versionTwo", required = false) String version, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        try {
            if (StringUtils.isEmpty(uid)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // // 参数载入
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    UserCallMarkController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));

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

            if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                jsonObject
                        .put("name",
                                user.getUserNickName().substring(0, 3) + "******"
                                        + new StringBuilder(
                                        new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                        .reverse());
            } else {
                jsonObject.put("name", user.getUserNickName());
            }
            jsonObject.put("sign", user.getUserSign());
            jsonObject.put("aliName", user.getUserAlipayName() == null ? "" : user.getUserAlipayName());
            jsonObject.put("aliAccount", user.getUserAlipayAccount() == null ? "" : user.getUserAlipayAccount());
            jsonObject.put("phone", StringUtils.isEmpty(user.getUserPhone()) ? "" : user.getUserPhone().substring(0, 3) + "******"
                    + new StringBuilder(new StringBuilder(user.getUserPhone()).reverse().substring(0, 2)).reverse());
            jsonObject.put("id", user.getUserId());
            jsonObject.put("label", StringUtils.isEmpty(user.getUserLabel()) == true ? "" : user.getUserLabel());

            List<UserActive> lists = userActiveServer.queryUserActiveAllByUserId(user.getUserId());
            if (lists != null && lists.size() != 0) {
                jsonObject.put("myScore", lists.get(0).getUserCount());
            } else {
                jsonObject.put("myScore", 0);
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
                jsonObject.put("url",
                        user.getUserHeadPic());
            } else {
                jsonObject.put("url",
                        properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
            }
            jsonObject.put("code", 200);
            jsonObject.put("count", userBlackListService.queryBlackListCountByUserId(user.getUserId()));
            jsonObject.put("contract", "true".equals(user.getUserContract()) == true ? "true" : "false");


            //第二板增加的内容
            if (StringUtils.isNotEmpty(version)) {


                JSONObject jsonOwner = new JSONObject();

                Integer count = userConcernService.queryUserConcernByUserId(user.getUserId());
                jsonOwner.put("concern", count == null ? 0 : count);

                // 拦截偶聊客服关注的搜索
                if (properties.get("ouliaoService").equals(user.getUserPhone())) {
                    jsonOwner.put("concerned", "9999");

                    jsonOwner.put("service", true);
                } else {

                    count = userConcernService.queryUserConcernedByUserOnfocusId(user.getUserId());
                    jsonOwner.put("service", false);
                    jsonOwner.put("concerned", count == null ? 0 : count);
                }

                jsonOwner.put("score", user.getUserCallScore() == null ? 0 : user.getUserCallScore());

                jsonOwner.put("author", StringUtils.isEmpty(user.getUserAuth()) ? "" : user.getUserAuth());

                jsonOwner.put("greet", StringUtils.isEmpty(user.getUserGreet()) ? "" : user.getUserGreet());

                jsonObject.put("version", jsonOwner);
            }

            return jsonObject;
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;
    }

    // 主播标签查看和修改
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/labelSet")
    public JSONObject labelSet(@PathVariable("uid") String uid, @RequestParam("key") String key,
                               @RequestParam(value = "label", required = false) String label, HttpServletRequest request,
                               HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        try {
            if (StringUtils.isEmpty(uid)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
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

            if (!"true".equals(user.getUserContract())) {
                jsonObject.put("code", 214);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            String[] arr = properties.getProperty("userLabel").split(",");
            JSONArray jsonArray = new JSONArray();
            JSONObject jsTotal = new JSONObject();
            JSONObject jsToSign = new JSONObject();

            for (int i = 0; i < arr.length; i++) {
                jsTotal.put(i + "", arr[i]);
                jsonArray.add(arr[i]);
            }

            jsonObject.put("code", 200);

            if (StringUtils.isNotEmpty(label)) {
                int count = 0;

                String[] selArr = new String[]{label};

                if (label.indexOf(",") > -1) {
                    selArr = label.split(",");
                }

                for (String st : selArr) {
                    for (String str : arr) {
                        if (st.trim().equals(str.trim())) {
                            count++;
                        }
                    }
                }
                // 代表所选择的有不是配置里面的标签

                if (count != selArr.length) {
                    jsonObject.put("code", 210);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
                ouLiaoService.updateUserLabelByUserNum(label, uid);
            } else {

                jsonObject.put("labelTotal", jsTotal);
                JSONObject json = new JSONObject();
                if (user.getUserLabel() != null) {

                    String[] strs = null;
                    if (user.getUserLabel().contains(",")) {
                        strs = user.getUserLabel().split(",");
                    } else {
                        strs = new String[]{user.getUserLabel()};
                    }

                    // 遍历循环标签
                    for (String str : strs) {
                        for (int i = 0; i < jsonArray.size(); i++) {

                            if (str.equals(jsonArray.get(i))) {

                                json.put(i + "", str);
                            }

                        }

                    }

                }
                jsonObject.put("labelSelect", json);

            }

            return jsonObject;
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;
    }

    // 主播接听时间的查看和修改
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/{deviceSign}/callTimeSet")
    public JSONObject callTimeSet(@PathVariable("uid") String uid, @RequestParam("key") String key,
                                  @PathVariable("deviceSign") String deviceSign,
                                  @RequestParam(value = "callTimeWeek", required = false) String callTimeWeek,
                                  @RequestParam(value = "callTime", required = false) String callTime, HttpServletRequest request,
                                  HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        try {
            if (StringUtils.isEmpty(uid) || !deviceSign.matches("[01]")) {
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

            if (!"true".equals(user.getUserContract())) {
                jsonObject.put("code", 214);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            jsonObject.put("code", 200);

            if (StringUtils.isNotEmpty(callTimeWeek)) {

                if (StringUtils.isEmpty(callTime) || !callTime.matches("[0-9]{2}:[0-9]{2}-[0-9]{2}:[0-9]{2}")) {
                    jsonObject.put("code", 202);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }

                int count = 0;
                Properties properties = new Properties();
                properties.load(new InputStreamReader(
                        OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                        "utf-8"));

                String[] arr = properties.getProperty("callTime").split(",");

                String[] selArr = new String[]{callTimeWeek};

                if (callTimeWeek.indexOf(",") > -1) {
                    selArr = callTimeWeek.split(",");
                }

                for (String st : selArr) {
                    for (String str : arr) {
                        if (st.equals(str)) {
                            count++;
                        }
                    }
                }
                // 代表所选择的有不是配置里面的每周几
                if (count != selArr.length) {
                    jsonObject.put("code", 210);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
                ouLiaoService.updateUserCallTimeByUserNum(callTimeWeek, callTime, uid);
            } else {

                JSONArray jsonArray = new JSONArray();
                // 给ios传递这个数组
                List<Integer> numList = new ArrayList<>();
                // 判定是苹果还是安卓系统
                Boolean flag = false;
                if ("1".equals(deviceSign)) {
                    // 苹果
                    flag = true;
                    for (int i = 0; i < 7; i++) {
                        numList.add(0);
                    }
                }

                if (user.getUserCallTimeWeek() != null) {

                    String[] arr = null;
                    if (user.getUserCallTimeWeek().contains(",")) {
                        arr = user.getUserCallTimeWeek().split(",");
                    } else {
                        arr = new String[]{user.getUserCallTimeWeek()};
                    }
                    List<Integer> list = new ArrayList<>();

                    for (String str : arr) {
                        if (flag) {
                            // 苹果系统
                            switch (str) {
                                case "每周日":
                                    list.add(0);
                                    break;
                                case "每周一":
                                    list.add(1);
                                    break;
                                case "每周二":
                                    list.add(2);
                                    break;
                                case "每周三":
                                    list.add(3);
                                    break;
                                case "每周四":
                                    list.add(4);
                                    break;
                                case "每周五":
                                    list.add(5);
                                    break;
                                default:
                                    list.add(6);
                                    break;
                            }
                        } else {
                            jsonArray.add(str);
                        }

                    }
                    if (flag) {
                        // 苹果系统
                        for (int i = 0; i < list.size(); i++) {
                            switch (list.get(i)) {
                                case 0:
                                    numList.set(0, 1);
                                    break;

                                case 1:

                                    numList.set(1, 1);
                                    break;
                                case 2:
                                    numList.set(2, 1);

                                    break;
                                case 3:

                                    numList.set(3, 1);
                                    break;
                                case 4:
                                    numList.set(4, 1);

                                    break;
                                case 5:

                                    numList.set(5, 1);
                                    break;

                                default:
                                    numList.set(6, 1);
                                    break;
                            }
                        }
                        jsonObject.put("week", numList);

                    } else {
                        jsonObject.put("week", jsonArray);
                    }

                }
                jsonObject.put("startTime",
                        user.getUserCallTime() == null ? "20:00" : user.getUserCallTime().split("-")[0]);
                jsonObject.put("endTime",
                        user.getUserCallTime() == null ? "21:00" : user.getUserCallTime().split("-")[1]);

            }

            return jsonObject;
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;
    }

    // 输入旧密码进行更改
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/resetPass")
    public JSONObject resetPass(@PathVariable("uid") String uid, @RequestParam("passPast") String passPast,
                                @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        passPast = passPast.trim().replaceAll(" ", "+");
        try {

            if (StringUtils.isEmpty(uid)) {
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

            if (passPast.equals(user.getUserPass())) {

                int count = ouLiaoService.updateUserIsCheckPassByUserId("true", user.getUserId());

                if (count == 1) {
                    jsonObject.put("code", 200);

                    final User us = user;
                    // 启动线程进行消失旧密码
                    Timer timer = new Timer();

                    TimerTask task = new TimerTask() {

                        @Override
                        public void run() {
                            try {
                                ouLiaoService.updateUserIsCheckPassByUserId("false", us.getUserId());
                            } catch (Exception e) {
                            }

                        }
                    };
                    // 启动失效的代码时间
                    timer.schedule(task, 1000 * 60 * 5);

                } else {
                    jsonObject.put("code", 210);
                }
            } else {
                jsonObject.put("code", 206);
            }

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;

    }

    // 输入旧密码开始进行更改
    @ResponseBody
    @RequestMapping(value = "setUser/{uid}/savePass")
    public JSONObject savePass(@PathVariable("uid") String uid, @RequestParam("passNew") String passNew,
                               @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        passNew = passNew.trim().replaceAll(" ", "+");
        try {

            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(passNew) || passNew.length() < 7) {
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
            if ("true".equals(user.getIsCheckPass())) {
                int count = ouLiaoService.updateUserIsCheckPassByUserId("false", user.getUserId());

                if (count == 1) {
                    ouLiaoService.updatePassByPhone(passNew, Des16Util.encrypt(passNew, user.getUserNum()),
                            user.getUserPhone());

                    jsonObject.put("code", 200);
                } else {
                    jsonObject.put("code", 210);
                }

            } else {
                jsonObject.put("code", 210);
            }

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;

    }

}
