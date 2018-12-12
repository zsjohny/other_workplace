package com.ouliao.controller.versionsecond;

import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionsecond.UserPersionalShow;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionsecond.UserPersionalShowService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by nessary on 16-5-17.
 */
@Controller
@RequestMapping(value = "user/persional")
public class UserPersionalShowController {

    @Autowired
    private UserPersionalShowService userPersionalShowService;
    @Autowired
    private OuLiaoService ouLiaoService;


    // 个人秀展示
    @ResponseBody
    @RequestMapping(value = "{uid}/{id}/displayPersionShow")
    public JSONObject displayPersionShow(@PathVariable("uid") String uid, @PathVariable("id") Integer id, @RequestParam(value = "tourist", required = false) String tourist,
                                         @RequestParam(value = "star", defaultValue = "1") Integer start, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            User user = null;
            if (StringUtils.isNotEmpty(tourist)) {
                user = new User();
                user.setUserId(0);
            } else {

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

            }


            Properties properties = new Properties();
            properties.load(new InputStreamReader(UserPersionalShowController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            Integer pagecount = Integer.valueOf(properties.getProperty("persionalShowCount"));


            List<UserPersionalShow> list = userPersionalShowService.queryUserPersionalAllByUserId(start, pagecount, id);

            JSONObject json = null;
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 aHH:mm");
            if (list != null && list.size() != 0) {

                for (UserPersionalShow userPersionalShow : list) {

                    if (userPersionalShow == null) {
                        continue;
                    }
                    json = new JSONObject();
                    json.put("id", userPersionalShow.getUserPersionalShowId());
                    json.put("url", StringUtils.isEmpty(userPersionalShow.getPersionalShowPic()) ? "" : userPersionalShow.getPersionalShowPic());
                    json.put("time", simpleDateFormat.format(userPersionalShow.getCreatTime()));
                    json.put("content", StringUtils.isEmpty(userPersionalShow.getDescribeByPersional()) ? "" : userPersionalShow.getDescribeByPersional());
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

    // 个人秀发布
    @ResponseBody
    @RequestMapping(value = "{uid}/createPersionShow")
    public JSONObject createPersionShow(@PathVariable("uid") String uid, @RequestParam("url") String url, @RequestParam("content") String content, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(content) || content.length() > 128) {
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


            UserPersionalShow userPersionalShow = new UserPersionalShow();
            userPersionalShow.setUserId(user.getUserId());
            userPersionalShow.setCreatTime(new Date());
            userPersionalShow.setIsDeleted("0");
            userPersionalShow.setDescribeByPersional(content);
            userPersionalShow.setPersionalShowPic(StringUtils.isEmpty(url) ? "" : url);
            userPersionalShowService.createUserPersionalShow(userPersionalShow);

            jsonObject.put("code", 200);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    // 个人秀删除
    @ResponseBody
    @RequestMapping(value = "{uid}/deletePersionShow")
    public JSONObject deletePersionShow(@PathVariable("uid") String uid, @RequestParam("id") String id, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        try {


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(id)) {
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


            List<Long> ids = new ArrayList<>();

            if (id.contains(",")) {

                for (String str : id.split(",")) {

                    if (StringUtils.isEmpty(str)) {
                        continue;
                    }
                    ids.add(Long.valueOf(str));
                }

            } else {
                ids.add(Long.valueOf(id));
            }


            userPersionalShowService.deleteUserPersionalShowById(ids, user.getUserId());


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
