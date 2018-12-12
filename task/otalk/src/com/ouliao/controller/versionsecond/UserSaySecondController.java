package com.ouliao.controller.versionsecond;

import com.ouliao.domain.versionfirst.*;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionfirst.UserConcernService;
import com.ouliao.service.versionsecond.UserSayContentSecondService;
import com.xiaoluo.util.DesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nessary on 16-5-17.
 */
@Controller
@RequestMapping(value = "user/saysecond")
public class UserSaySecondController {

    @Autowired
    private UserSayContentSecondService userSayContentSecondService;
    @Autowired
    private UserConcernService userConcernService;


    @Autowired
    private OuLiaoService ouLiaoService;

    // 点赞展示
    @ResponseBody
    @RequestMapping(value = "{uid}/displayUserSupport")
    public JSONObject displayUserSupport(@PathVariable("uid") String uid, @RequestParam(value = "start", defaultValue = "1") Integer start, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key)) {
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


            Properties properties = new Properties();
            properties.load(new InputStreamReader(UserPersionalShowController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            Integer pagecount = Integer.valueOf(properties.getProperty("supportShowCount"));


            List<UserSupportSay> list = userSayContentSecondService.querySupporAllByUserId(start, pagecount, user.getUserId());

            JSONObject json = null;
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat simpleDateFormat = null;
            if (list != null && list.size() != 0) {

                for (UserSupportSay userSupportSay : list) {

                    if (userSupportSay == null) {
                        continue;
                    }

                    User us = ouLiaoService.queryUserByUserId(userSupportSay.getUserId());

                    if (us == null) {
                        continue;

                    }


                    json = new JSONObject();

                    simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");


                    if (us.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                        json.put("nickName",
                                us.getUserNickName().substring(0, 3) + "******"
                                        + new StringBuilder(new StringBuilder(us.getUserNickName()).reverse().substring(0, 2))
                                        .reverse());
                    } else {
                        json.put("nickName", us.getUserNickName());
                    }

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
                        json.put("url", properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl + "/head/download");
                    }
                    json.put("isContract", "true".equals(us.getUserContract()) == true ? "true" : "false");


                    json.put("time", simpleDateFormat.format(userSupportSay.getCreateTime()));


                    JSONObject js = new JSONObject();

                    UserSayContent userSayContent = userSayContentSecondService.queryUserSayContentByUserSayContentId(userSupportSay.getUserSayContentId());


                    if (userSayContent != null) {

                        if (StringUtils.isEmpty(userSayContent.getUserPicUrls())) {
                            js.put("saycontent", userSayContent.getUserContent());
                            js.put("saypic", "");
                        } else {
                            js.put("saycontent", "");
                            js.put("saypic", userSayContent.getUserPicUrls());
                        }

                        json.put("say", js);

                    }


                    jsonArray.add(json);
                }

            }


            jsonObject.put("code", 200);
            jsonObject.put("data", jsonArray);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }

        return jsonObject;
    }


    // 评论展示
    @ResponseBody
    @RequestMapping(value = "{uid}/displayUserCommont")
    public JSONObject displayUserCommont(@PathVariable("uid") String uid, @RequestParam(value = "start", defaultValue = "1") Integer start, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key)) {
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


            Properties properties = new Properties();
            properties.load(new InputStreamReader(UserPersionalShowController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            Integer pagecount = Integer.valueOf(properties.getProperty("commontShowCount"));


            List<UserCommont> list = userSayContentSecondService.queryUserCommontAllByUserId(start, pagecount, user.getUserId());

            JSONObject json = null;
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat simpleDateFormat = null;
            if (list != null && list.size() != 0) {

                for (UserCommont userCommont : list) {

                    if (userCommont == null) {
                        continue;
                    }

                    User us = ouLiaoService.queryUserByUserId(userCommont.getUserContractId());

                    if (us == null) {
                        continue;

                    }


                    json = new JSONObject();

                    simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");


                    if (us.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                        json.put("nickName",
                                us.getUserNickName().substring(0, 3) + "******"
                                        + new StringBuilder(new StringBuilder(us.getUserNickName()).reverse().substring(0, 2))
                                        .reverse());
                    } else {
                        json.put("nickName", us.getUserNickName());
                    }

                    json.put("id", us.getUserId());

                    json.put("comment", StringUtils.isEmpty(userCommont.getUserCommont()) ? "" : userCommont.getUserCommont());

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
                        json.put("url", properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl + "/head/download");
                    }
                    json.put("isContract", "true".equals(us.getUserContract()) == true ? "true" : "false");


                    json.put("time", simpleDateFormat.format(userCommont.getUserCreateTime()));


                    JSONObject js = new JSONObject();

                    UserSayContent userSayContent = userSayContentSecondService.queryUserSayContentByUserSayContentId(userCommont.getUserSayContentId());


                    if (userSayContent != null) {

                        if (StringUtils.isEmpty(userSayContent.getUserPicUrls())) {
                            js.put("saycontent", userSayContent.getUserContent());
                            js.put("saypic", "");
                        } else {
                            js.put("saycontent", "");
                            js.put("saypic", userSayContent.getUserPicUrls());
                        }

                        json.put("say", js);

                    }


                    jsonArray.add(json);
                }

            }


            jsonObject.put("code", 200);
            jsonObject.put("data", jsonArray);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }

        return jsonObject;
    }


    // 某条说说详细的点赞展示
    @ResponseBody
    @RequestMapping(value = "{uid}/displayUseSupportBySid")
    public JSONObject displayUseSupportBySid(@PathVariable("uid") String uid, @RequestParam("sid") Integer sid, @RequestParam(value = "tourist", required = false) String tourist, @RequestParam(value = "star", defaultValue = "1") Integer start, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key) || sid == null) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            User user = null;
            if (StringUtils.isEmpty(tourist)) {

                // 登录检测
                user = ouLiaoService.queryUserByUserNum(uid);
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
            } else {

                user = new User();
                user.setUserId(0);
            }


            Properties properties = new Properties();
            properties.load(new InputStreamReader(UserPersionalShowController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));
            Integer pagecount = Integer.valueOf(properties.getProperty("supportShowCount"));


            // 展示用户点赞的id和头像和是否是主播
            List<UserSupportSay> lists = userSayContentSecondService
                    .querySupporAllByUserSayContentId(start, pagecount, sid);
            JSONArray array = new JSONArray();
            JSONObject js = null;
            if (lists != null && lists.size() != 0) {

                for (UserSupportSay uss : lists) {
                    js = new JSONObject();
                    if (uss == null) {
                        continue;
                    }

                    User us = ouLiaoService.queryUserByUserId(uss.getUserId());
                    if (us == null) {
                        continue;
                    }

                    js.put("id", us.getUserId());

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
                        js.put("url", us.getUserHeadPic());
                    } else {
                        js.put("url",
                                properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl + "/head/download");
                    }
                    js.put("contract", "true".equals(us.getUserContract()) == true ? "true" : "false");


                    UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(), us.getUserId());

                    if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                        js.put("concern", false);

                    } else if ("0".equals(isConcern.getIsDeleted())) {
                        js.put("concern", true);
                    }

                    js.put("author", StringUtils.isEmpty(us.getUserAuth()) ? "" : us.getUserAuth());


                    array.add(js);


                }

            }

            jsonObject.put("code", 200);
            jsonObject.put("data", array);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    // 点赞已读
    @ResponseBody
    @RequestMapping(value = "{uid}/isReadSupportById")
    public JSONObject isReadSupportById(@PathVariable("uid") String
                                                uid, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest
                                                request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key)) {
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

            userSayContentSecondService.updateUserSupportIsReadByUserSupportId(user.getUserId());

            jsonObject.put("code", 200);


            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }

    // 评论已读
    @ResponseBody
    @RequestMapping(value = "{uid}/isReadCommontById")
    public JSONObject isReadCommontById(@PathVariable("uid") String
                                                uid, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest
                                                request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key)) {
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

            userSayContentSecondService.updateUserCommonttIsReadByUserContractId(user.getUserId());

            jsonObject.put("code", 200);


            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    // 获得是否有新的点赞和评论
    @ResponseBody
    @RequestMapping(value = "{uid}/showNewSupportAndCommont")
    public JSONObject showNewSupportAndCommont(@PathVariable("uid") String uid, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key)) {
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

            Map<String, Integer> map = userSayContentSecondService.queryNewSupportCommontAndSupportByUserId(user.getUserId());


            jsonObject.put("commont", map.get("commont") == 0 ? "false" : "true");

            jsonObject.put("support", map.get("support") == 0 ? "false" : "true");

            jsonObject.put("code", 200);


            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


}
