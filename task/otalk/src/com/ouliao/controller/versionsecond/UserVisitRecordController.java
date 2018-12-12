package com.ouliao.controller.versionsecond;

import com.ouliao.domain.versionfirst.HuanXin;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionsecond.UserSureMsgCount;
import com.ouliao.domain.versionsecond.UserVisitRecord;
import com.ouliao.service.versionfirst.HuanXinService;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionsecond.UserSureMsgCountService;
import com.ouliao.service.versionsecond.UserVistRecordService;
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
@RequestMapping(value = "user/uservisit")
public class UserVisitRecordController {

    @Autowired
    private UserVistRecordService userVistRecordService;
    @Autowired
    private OuLiaoService ouLiaoService;

    @Autowired
    private HuanXinService huanXinService;

    @Autowired
    private UserSureMsgCountService userSureMsgCountService;

    // 足迹展示
    @ResponseBody
    @RequestMapping(value = "{uid}/displayUserVisit")
    public JSONObject displayUserVisit(@PathVariable("uid") String uid, @RequestParam(value = "star", defaultValue = "1") Integer start, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest request, HttpServletResponse response) {
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

            Integer pagecount = Integer.valueOf(properties.getProperty("userPersionVisitRecordCount"));


            List<UserVisitRecord> list = userVistRecordService.findUserVisitRecordAllByVisitId(start, pagecount, user.getUserId());

            JSONObject json = null;
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat simpleDateFormat = null;
            if (list != null && list.size() != 0) {

                for (UserVisitRecord userVisitRecord : list) {

                    if (userVisitRecord == null) {
                        continue;
                    }

                    User us = ouLiaoService.queryUserByUserId(userVisitRecord.getUserId());

                    if (us == null) {
                        continue;

                    }


                    json = new JSONObject();

                    simpleDateFormat = new SimpleDateFormat("MM月dd日 aHH:mm");


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

                    if ("true".equals(us.getUserContract())) {
                        json.put("isContract", "true");
                        json.put("author", StringUtils.isNotEmpty(us.getUserAuth()) ? us.getUserAuth() : "");
                    } else {
                        json.put("isContract", "false");
                    }


                    if (new Date().getDay() - userVisitRecord.getCreatTime().getDay() == 1) {
                        simpleDateFormat = new SimpleDateFormat("aHH:mm");
                        json.put("time", "昨天 " + simpleDateFormat.format(userVisitRecord.getCreatTime()));
                    } else {
                        json.put("time", simpleDateFormat.format(userVisitRecord.getCreatTime()));
                    }


                    json.put("sign", StringUtils.isEmpty(us.getUserSign()) ? "" : us.getUserSign());


                    // 创建环信账号
                    HuanXin huanXin = huanXinService.queryIsExist(us.getUserId());
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
                            huanXinPost.setOwnerId(us.getUserId());
                            huanXinPost.setPass(huanXinpass);
                            huanXinService.saveHuanXin(huanXinPost);
                        }
                        json.put("huanxinName", name);
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


    // 足迹删除
    @ResponseBody
    @RequestMapping(value = "test")
    public UserVisitRecord isReadVisisById(HttpServletRequest request) {






        UserVisitRecord userVisitRecord = userVistRecordService.findUserVisitRecordfirstByVisitId(942);

        System.out.println(userVisitRecord);
        return userVisitRecord;
    }


    // 足迹已读
    @ResponseBody
    @RequestMapping(value = "{uid}/isReadVisisById")
    public JSONObject isReadVisisById(@PathVariable("uid") String
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


            userVistRecordService.updateUserVisitRecordIsReadByVisitId(user.getUserId());


            jsonObject.put("code", 200);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    // 足迹删除
    @ResponseBody
    @RequestMapping(value = "{uid}/deleteUserVisit")
    public JSONObject deleteUserVisit(@PathVariable("uid") String uid, @RequestParam("id") String
            id, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest
                                              request, HttpServletResponse response) {
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


            userVistRecordService.deleteUserVisitRecordById(ids, user.getUserId());


            jsonObject.put("code", 200);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            return jsonObject;

        }
        return jsonObject;
    }


    // 系统消息已读
    @ResponseBody
    @RequestMapping(value = "{uid}/isReadMsgById")
    public JSONObject isReadMsgById(@PathVariable("uid") String
                                            uid, @RequestParam("count") Integer count, @RequestParam(value = "key", defaultValue = "sz") String key, HttpServletRequest
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


            //添加没有读写的交标提示
            Integer readCount = userSureMsgCountService.findUserSureMsgCountByUserId(user.getUserId());

            if (readCount == null || readCount == 0) {

                UserSureMsgCount sureMsgCount = new UserSureMsgCount();
                sureMsgCount.setIsDeleted("0");
                sureMsgCount.setCreatTime(new Date());
                sureMsgCount.setReadCount(count);
                sureMsgCount.setUserId(user.getUserId());
                userSureMsgCountService.saverUserSureMsgCountDao(sureMsgCount);
            } else {
                userSureMsgCountService.updateUSerSureMsgCountByUserId(count, user.getUserId());
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


}
