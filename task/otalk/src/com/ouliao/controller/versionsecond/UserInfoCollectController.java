package com.ouliao.controller.versionsecond;

import com.ouliao.controller.versionfirst.OuLiaoSayController;
import com.ouliao.controller.versionfirst.UserCallMarkController;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionfirst.UserSupportSay;
import com.ouliao.domain.versionsecond.UserAdvice;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionfirst.UserSayService;
import com.ouliao.service.versionsecond.UserInfoService;
import com.ouliao.service.versionsecond.UserSayContentSecondService;
import com.ouliao.service.versionsecond.UserSecondService;
import com.xiaoluo.util.DesUtil;
import com.xiaoluo.util.GainRealIpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by nessary on 16-5-9.
 */
@Controller
@RequestMapping(value = "user/info")
public class UserInfoCollectController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserSecondService userSecondService;
    @Autowired
    private UserSayService userSayService;
    @Autowired
    private UserSayContentSecondService userSayContentSecondService;
    private static Jedis jedis = new JedisPool("localhost", 10088).getResource();

    // 意见征集
    @ResponseBody
    @RequestMapping(value = "{uid}/sendMsg")
    public JSONObject createUserAdvice(@PathVariable("uid") String uid, @RequestParam(value = "isHide", defaultValue = "true") String isHide,
                                       @RequestParam("advice") String advice, @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            String realIp = GainRealIpUtil.gainRealIp(request);

            HttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://whois.pconline.com.cn/jsFunction.jsp?callback=jsShow&ip=" + realIp);

            HttpResponse httpResponse = httpClient.execute(post);


            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                try {
                    String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");

                    realIp = result.substring(35, result.indexOf("'", 27) + 8);
                } catch (Exception e) {

                }
            }


            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(advice)) {
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


            UserAdvice userAdvice = new UserAdvice();
            userAdvice.setUserId(user.getUserId());
            userAdvice.setAdvice(advice);
            userAdvice.setIsDeleted("0");
            userAdvice.setCreatTime(new Date());
            userAdvice.setIsHide("true".equals(isHide) ? "true" : "false");
            userAdvice.setRealIp(realIp);
            userAdvice.setUserPhone(StringUtils.isEmpty(user.getUserPhone()) ? "" : user.getUserPhone());
            userAdvice.setUserNickName(user.getUserNickName());

            userInfoService.createUserAdvice(userAdvice);

            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }

    //发送邮箱验证码
    @ResponseBody
    @RequestMapping(value = "{uid}/sendUserEmail")
    public JSONObject sendUserEmail(@PathVariable("uid") String uid, @RequestParam("email") String email, @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {


            if (!email.matches(
                    "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
                    && !email.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
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


            //检测是否存在
            User isExistUser = userSecondService.queryUserByEamil(email);

            if (isExistUser != null) {
                jsonObject.put("code", 203);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }


            String num = "";
            for (int i = 0; i < 6; i++) {
                num += String.valueOf(((int) (Math.random() * 10)));
            }


            // 参数载入
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    UserCallMarkController.class.getClassLoader().getResourceAsStream("officalSendEmailParam.properties"),
                    "utf-8"));

            HtmlEmail htmlEmail = new HtmlEmail();

            htmlEmail.setHostName(
                    DesUtil.decrypt(properties.getProperty("dbHost"), properties.getProperty("dbKey")));


            htmlEmail.setAuthentication(
                    DesUtil.decrypt(properties.getProperty("dbName"), properties.getProperty("dbKey")),
                    DesUtil.decrypt(properties.getProperty("dbPassword"),
                            properties.getProperty("dbKey")));

            htmlEmail.setFrom(
                    DesUtil.decrypt(properties.getProperty("dbName"), properties.getProperty("dbKey")),
                    properties.getProperty("sendPerson"), "utf-8");

            htmlEmail.addTo(email, "", "utf-8");
            htmlEmail.setSubject(properties.getProperty("sendSubject"));
            htmlEmail.setMsg(properties.getProperty("sendMsg") + num);
            htmlEmail.setCharset("utf-8");
            htmlEmail.send();


            try {
                jedis.set("us:" + user.getUserId() + ":sendEmail", num);
                //绑定邮箱的时候用到
                jedis.set("us:" + user.getUserId() + ":bindEmail", email);
                jedis.disconnect();
            } catch (Exception e) {
                userSecondService.updateUserEmailCodeAndUserEmailByUserNum(email, num, uid);

            }


            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    //比对验证码和设置密码---邮箱0和手机1
    @ResponseBody
    @RequestMapping(value = "{uid}/{type}/compCodeAndSetPasByVaildInfo")
    public JSONObject compCodeAndSetPasByVaildInfo(@PathVariable("uid") String uid, @PathVariable("type") Integer type, @RequestParam("key") String key, @RequestParam(value = "pass", required = false) String pass,
                                                   @RequestParam("code") String code, @RequestParam(value = "bind", required = false) String bind, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");


        try {

            //绑定邮箱
            if (StringUtils.isEmpty(bind)) {
                if (StringUtils.isEmpty(pass) || pass.length() < 7) {

                    jsonObject.put("code", 202);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
            }

            if (!code.matches("\\d{6}")) {
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


            String codeSave = "";


            try {


                switch (type) {
                    case 0:

                        if (!jedis.exists("us:" + user.getUserId() + ":sendEmail")) {
                            jsonObject.put("code", 203);
                            return jsonObject;
                        }
                        codeSave = jedis.get("us:" + user.getUserId() + ":sendEmail");


                        //检测是否存在
                        User isExistUser = userSecondService.queryUserByEamil(codeSave);

                        if (isExistUser != null) {
                            jsonObject.put("code", 205);
                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonObject;
                        }


                        break;
                    case 1:
                   /*     if (StringUtils.isEmpty(user.getUserPhone())) {
                            jsonObject.put("code", 205);
                            return jsonObject;
                        }*/


                        if (!jedis.exists(user.getUserPhone())) {
                            jsonObject.put("code", 203);
                            return jsonObject;
                        }

                        codeSave = jedis.get(user.getUserPhone());
                        break;

                }


            } catch (Exception e) {


                switch (type) {
                    case 0:


                        if (StringUtils.isEmpty(user.getEmailCode())) {
                            jsonObject.put("code", 203);
                            return jsonObject;
                        }

                        codeSave = user.getEmailCode();
                        break;
                    case 1:
                        if (StringUtils.isEmpty(user.getUserPhone()) || StringUtils.isEmpty(user.getUserPhoneCode())) {
                            jsonObject.put("code", 203);
                            return jsonObject;
                        }

                        codeSave = user.getUserPhoneCode();
                        break;
                }

            }


            if (code.equals(codeSave)) {

                if (StringUtils.isNotEmpty(bind)) {

                    if (StringUtils.isEmpty(user.getEamil())) {

                        try {
                            String email = jedis.get("us:" + user.getUserId() + ":bindEmail");
                            userSecondService.updateUserEmailCodeAndUserEmailByUserNum(email, "", user.getUserNum());
                        } catch (Exception e) {
                            jsonObject.put("code", 300);

                        }

                    }

                    jsonObject.put("code", 200);
                } else {

                    userSecondService.updateUserPassByUserNum(pass, uid);
                }
                jsonObject.put("code", 200);
            } else {

                jsonObject.put("code", 204);
            }


            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                jedis.disconnect();
            } catch (Exception e1) {

            }

            return jsonObject;

        }

        try {
            jedis.disconnect();
        } catch (Exception e) {

        }

        return jsonObject;
    }


    //个人背景的上传
    @ResponseBody
    @RequestMapping(value = "{uid}/updateUserBackPic")
    public JSONObject updateUserBackPic(@PathVariable("uid") String uid, @RequestParam("url") String url, HttpServletRequest request, @RequestParam("key") String key, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");


        try {


            if (StringUtils.isEmpty(url)) {
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


            userSecondService.updateUserBackPicUrlByUserNum(url, uid);
            jsonObject.put("code", 200);


            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    //个人头像的上传
    @ResponseBody
    @RequestMapping(value = "{uid}/updateUserHeadPic")
    public JSONObject updateUserHeadPic(@PathVariable("uid") String uid, @RequestParam("url") String url, HttpServletRequest request, @RequestParam("key") String key, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");


        try {


            if (StringUtils.isEmpty(url)) {
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


            userSecondService.updateUserheadPicUrlByUserNum(url, uid);
            jsonObject.put("code", 200);


            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    //说说搜索
    @ResponseBody
    @RequestMapping(value = "{uid}/{start}/{id}/queryUserSayContent")
    public JSONObject queryUserSayContent(@PathVariable("uid") String uid, @PathVariable("start") Integer start, @PathVariable("id") Integer id, @RequestParam("word") String word,
                                          @RequestParam(value = "tourist", required = false) String tourist, HttpServletRequest request, @RequestParam(value = "key", required = false) String key, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        if (StringUtils.isEmpty(tourist)) {
            key = key.trim().replaceAll(" ", "+");

        }
        try {


            if (StringUtils.isEmpty(word) || start <= 0) {
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
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

            Integer pageCount = Integer.valueOf(properties.getProperty("sayContentStartCount"));

            List<UserSayContent> userSayContents = userSayContentSecondService.queryUserSayContentBySubjectOrContent(start, pageCount, word, id);

            JSONArray jsonArray = new JSONArray();

            // 08月20日 20:20
            SimpleDateFormat simpleDate = null;

            for (UserSayContent userSayContent : userSayContents) {
                if (userSayContent == null) {
                    continue;
                }
                JSONObject json = new JSONObject();
                json.put("content", userSayContent.getUserContent());
                json.put("sid", userSayContent.getUserSayContentId());
                simpleDate = new SimpleDateFormat("HH:mm");
                json.put("time", simpleDate.format(userSayContent.getUserCreateTime()));
                simpleDate = new SimpleDateFormat("MM月");
                json.put("month", simpleDate.format(userSayContent.getUserCreateTime()));
                simpleDate = new SimpleDateFormat("dd");
                json.put("day", simpleDate.format(userSayContent.getUserCreateTime()));

                UserSupportSay usc = userSayService.querySupportUniqueById(id, userSayContent.getUserSayContentId());

                if (usc == null) {
                    json.put("isSupport", "false");
                } else {
                    json.put("isSupport", "true");
                }
                // 显示评论个数和点赞个数
                int supportCount = userSayService.querySupportCountById(userSayContent.getUserSayContentId());
                if (supportCount > 99) {
                    json.put("supportCount", "99+");

                } else {
                    json.put("supportCount", supportCount);
                }

                int commontCount = userSayService
                        .querySayCommontCountCountByUserSayContentId(userSayContent.getUserSayContentId());
                if (commontCount > 99) {
                    json.put("commontCount", "99+");

                } else {
                    json.put("commontCount", commontCount);
                }

                jsonArray.add(json);


            }

            jsonObject.put("content", jsonArray);

            jsonObject.put("code", 200);


            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    //打招呼的更新
    @ResponseBody
    @RequestMapping(value = "{uid}/updateUserGreet")
    public JSONObject updateUserGreet(@PathVariable("uid") String uid, @RequestParam("greet") String greet, HttpServletRequest request, @RequestParam("key") String key, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");


        try {


            if (StringUtils.isEmpty(greet) || greet.length() < 10 || greet.length() > 30) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }


            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
//            if (request.getSession().getAttribute(key) == null || user == null) {
//                jsonObject.put("code", 208);
//                jsonObject.put("cid", user.getUserId());
//                response.setStatus(HttpServletResponse.SC_OK);
//                return jsonObject;
//            }
//            String userNum = DesUtil.decrypt(key, user.getCurrentTime());
//            if (!user.getUserNum().equals(userNum)) {
//                jsonObject.put("code", 209);
//
//                response.setStatus(HttpServletResponse.SC_OK);
//                return jsonObject;
//            }


            userSecondService.updateUserGreetlByUserNum(greet, uid);
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
