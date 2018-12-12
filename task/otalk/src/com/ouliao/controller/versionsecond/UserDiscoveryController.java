package com.ouliao.controller.versionsecond;

import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionsecond.UserDisconery;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionsecond.UserDisconeryService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by nessary on 16-5-17.
 */
@Controller
@RequestMapping(value = "user/discovery")
public class UserDiscoveryController {
    @Autowired
    private UserDisconeryService userDisconeryService;
    @Autowired
    private OuLiaoService ouLiaoService;


    // 发现展示
    @ResponseBody
    @RequestMapping(value = "displyDiscovery")
    public JSONObject displyDiscovery(@RequestParam(value = "start", defaultValue = "1") Integer start,
                                      HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        try {

            Properties properties = new Properties();
            properties.load(new InputStreamReader(UserPersionalShowController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));
            Integer pagecount = Integer.valueOf(properties.getProperty("defaultDiscoveryCount"));


            List<UserDisconery> lists = userDisconeryService.queryUserDisconveryAll(start, pagecount);


            JSONArray array = new JSONArray();
            JSONObject json = null;

            if (lists != null && lists.size() != 0) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 aHH:mm");

                for (UserDisconery userDisconery : lists) {

                    if (userDisconery == null) {
                        continue;

                    }
                    json = new JSONObject();

                    User us = ouLiaoService.queryUserByUserId(userDisconery.getUserId());
                    if (!"true".equals(us.getUserContract())) {
                        continue;

                    }

                    json.put("id", us.getUserId());


                    if (new Date().getDay() - userDisconery.getCreatTime().getDay() == 1) {
                        simpleDateFormat = new SimpleDateFormat("aHH:mm");
                        json.put("time", "昨天 " + simpleDateFormat.format(userDisconery.getCreatTime()));
                    } else {
                        json.put("time", simpleDateFormat.format(userDisconery.getCreatTime()));
                    }
                    String title = StringUtils.isNotEmpty(us.getUserAuth()) ? us.getUserAuth() : "";

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
                    String nickName = "";
                    if (us.getUserNickName()
                            .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                        nickName = us.getUserNickName().substring(0, 3) + "******"
                                + new StringBuilder(
                                new StringBuilder(us.getUserNickName()).reverse().substring(0, 2))
                                .reverse();
                    } else {
                        nickName = us.getUserNickName();
                    }
                    json.put("name", nickName);
                    json.put("author", title);

                    JSONArray arr = new JSONArray();
                    if (StringUtils.isNotEmpty(userDisconery.getClassicProgram())) {
                        if (userDisconery.getClassicProgram().contains(",")) {

                            for (String str : userDisconery.getClassicProgram().split(",")) {
                                if (StringUtils.isEmpty(str)) {
                                    continue;
                                }
                                arr.add(str);
                            }
                        } else {
                            arr.add(userDisconery.getClassicProgram());
                        }
                    }
                    json.put("classic", arr);

                    arr = new JSONArray();
                    if (StringUtils.isNotEmpty(userDisconery.getListenProgram())) {
                        if (userDisconery.getListenProgram().contains(",")) {
                            for (String str : userDisconery.getListenProgram().split(",")) {
                                if (StringUtils.isEmpty(str)) {
                                    continue;
                                }
                                arr.add(str);
                            }
                        } else {
                            arr.add(userDisconery.getListenProgram());
                        }
                    }
                    json.put("listen", arr);


                    JSONArray picarray = new JSONArray();

                    if (StringUtils.isNotEmpty(userDisconery.getDisPicUrls())) {

                        if (userDisconery.getDisPicUrls().contains(",")) {

                            for (String path : userDisconery.getDisPicUrls().split(",")) {
                                if (StringUtils.isEmpty(path)) {
                                    continue;
                                }
                                String pathurl = properties.getProperty("headUrl") + us.getUserId() + "/" + (int) Math.random() * 1000 + "/discovery/download";
                                picarray.add(pathurl + "?name=" + path);

                            }

                        } else {
                            String pathurl = properties.getProperty("headUrl") + us.getUserId() + "/" + (int) Math.random() * 1000 + "/discovery/download";
                            picarray.add(pathurl + "?name=" + userDisconery.getDisPicUrls());

                        }
                    }

                    json.put("pic", picarray);


                    array.add(json);

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

    @RequestMapping(value = "test")
    public String createUserAdvice() {


        return "load";

    }


}
