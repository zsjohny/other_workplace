package com.ouliao.controller.versionsecond;

import com.ouliao.controller.versionfirst.OuLiaoSayController;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionsecond.UserMissCallRecord;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionsecond.UserMissCallRecordService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by nessary on 16-6-1.
 */

@Controller
@RequestMapping(value = "user/userMiss")
public class UserMissCallRecordController {
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserMissCallRecordService userMissCallRecordService;

    // 未接打电话的显示
    @ResponseBody
    @RequestMapping(value = "{uid}/usercallshow")
    public JSONObject usercallshow(@PathVariable("uid") String uid,
                                   @RequestParam(value = "start", defaultValue = "1") Integer start, @RequestParam("cid") String cid, @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
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
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

            Integer pageCount = Integer.valueOf(properties.getProperty("deafultCallMiss"));


            List<UserMissCallRecord> list = userMissCallRecordService.queryUserMissCallRecordByUserCallId(start, pageCount, cid, user.getUserId());


            JSONObject json = null;

            JSONArray array = new JSONArray();


            if (list != null && list.size() != 0) {

                for (UserMissCallRecord userMissCallRecord : list) {
                    json = new JSONObject();


                    User us = ouLiaoService.queryUserByUserId(userMissCallRecord.getUserCallId());

                    if (us != null) {

                        json.put("id", userMissCallRecord.getUserMissCallRecordId());
                        json.put("uid", userMissCallRecord.getUserCallId());

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
                            json.put("url", properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl
                                    + "/head/download");
                        }
                        if (us.getUserNickName()
                                .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                            json.put("name",
                                    us.getUserNickName().substring(0, 3) + "******"
                                            + new StringBuilder(
                                            new StringBuilder(us.getUserNickName()).reverse().substring(0, 2))
                                            .reverse());
                        } else {
                            json.put("name", us.getUserNickName());
                        }

                        json.put("isContract", "true".equals(us.getUserContract()) == true ? "true" : "false");
                        json.put("author", us.getUserAuth() == null ? "" : us.getUserAuth());

                        json.put("interval", userMissCallRecord.getUserCallRingTime());

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                        json.put("month", simpleDateFormat.format(userMissCallRecord.getCreatTime()));

                        simpleDateFormat = new SimpleDateFormat("HH:mm");
                        json.put("time", simpleDateFormat.format(userMissCallRecord.getCreatTime()));

                        json.put("timestamp", userMissCallRecord.getCreatTime().getTime());


                        array.add(json);

                    }


                }


            }

            jsonObject.put("data", array);
            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    // 未接打电话的删除
    @ResponseBody
    @RequestMapping(value = "{uid}/deleteUserCall")
    public JSONObject deleteUserCall(@PathVariable("uid") String uid, @RequestParam("id") String ids,
                                     @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
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

            List<Long> list = new ArrayList<>();


            if (ids.contains(",")) {
                for (String str : ids.split(",")) {
                    list.add(Long.valueOf(str));
                }

            } else {
                list.add(Long.valueOf(ids));
            }


            userMissCallRecordService.deleUserMissCallIsReadRecordById(list, user.getUserId());


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
