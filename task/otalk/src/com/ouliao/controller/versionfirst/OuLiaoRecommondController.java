/**
 *
 */
package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.HuanXin;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserCallRoom;
import com.ouliao.domain.versionfirst.UserConcern;
import com.ouliao.service.versionfirst.HuanXinService;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionfirst.UserCallRoomService;
import com.ouliao.service.versionfirst.UserConcernService;
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
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author xiaoluo
 * @version $Id: OuLiaoAdminController.java, 2016年2月22日 上午10:04:57
 */
@Controller
//@RequestMapping(value = "user/recommond", method = RequestMethod.POST)
@RequestMapping(value = "user/recommond")
public class OuLiaoRecommondController {
    @Autowired
    private OuLiaoService ouLiaoService;

    @Autowired
    private UserConcernService userConcernService;

    @Autowired
    private UserCallRoomService userCallRoomService;

    @Autowired
    private HuanXinService huanXinService;


    // 加入缓存进行操作
    private JedisPool pool = new JedisPool("localhost", 10088);
    private Jedis jedis = pool.getResource();

    // 偶推
    @ResponseBody
    @RequestMapping(value = "userRecommond/{uid}/{startCount}/{orderWay}/recommond")
    public JSONObject regUser(@PathVariable("uid") String uid, @PathVariable("startCount") Integer startCount,
                              @PathVariable("orderWay") Integer orderWay, @RequestParam("key") String key,
                              @RequestParam(value = "tourist", required = false) String tourist, HttpServletRequest request,
                              HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");
        try {

            if (StringUtils.isEmpty(tourist)) {
                if (StringUtils.isEmpty(key)) {
                    jsonObject.put("code", 202);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
            }
            User user = null;
            if (StringUtils.isEmpty(tourist)) {

                // 登录检测
                user = ouLiaoService.queryUserByUserNum(uid);
            } else {
                user = new User();
                user.setUserId(0);
            }
//             if (request.getSession().getAttribute(key) == null || user ==
//             null) {
//             jsonObject.put("code", 208);
//                 if (user != null) {
//                     jsonObject.put("cid", user.getUserId());
//                 }
//             response.setStatus(HttpServletResponse.SC_OK);
//             return jsonObject;
//             }
//             String userNum = DesUtil.decrypt(key, user.getCurrentTime());
//             if (!user.getUserNum().equals(userNum)) {
//             jsonObject.put("code", 209);
//             response.setStatus(HttpServletResponse.SC_OK);
//             return jsonObject;
//             }
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));
            Integer pageCount = Integer.valueOf(properties.getProperty("recommondCount"));
            List<JSONObject> lists = new ArrayList<>();


            switch (orderWay) {
                case 0:// 默认

                    String order = "";

                    File file = new File(properties.getProperty("randomFilePath"));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Properties pro = new Properties();
                    pro.load(new BufferedInputStream(new FileInputStream(file)));
                    String date = pro.getProperty("dateOrder");
                    if (date == null) {
                        pro.setProperty("dateOrder", new Date() + "");
                        String[] orders = properties.getProperty("randomOrder").split(",");
                        int size = (int) (Math.random() * (orders.length + 1));
                        order = orders[size];
                        pro.setProperty("dateOrderWay", order);
                        OutputStream os = new FileOutputStream(file);
                        pro.store(os, "xiaoluo");

                    } else {
                        Date datePast = new Date(date);
                        Date dateNow = new Date();
                        int day = dateNow.getDay() - datePast.getDay();
                        if (day == 0 || day == -1) {
                            order = pro.getProperty("dateOrderWay");
                        } else {
                            String[] orders = properties.getProperty("randomOrder").split(",");
                            int size = (int) (Math.random() * (orders.length + 1));
                            order = orders[size];
                            pro.setProperty("dateOrder", new Date() + "");
                            pro.setProperty("dateOrderWay", order);
                            OutputStream os = new FileOutputStream(file);
                            pro.store(os, "xiaoluo");

                        }
                    }

                    List<User> users = ouLiaoService.queryUserIsRecommondByAll();

                    JSONObject json = null;

                    // 初始页数展示我们推荐的用户
                    if (users != null && users.size() != 0 && startCount == 1) {
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
                            json.put("author", us.getUserAuth() == null ? "" : us.getUserAuth());


                            UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(),
                                    us.getUserId());

                            if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                                json.put("isConcern", false);

                            } else if ("0".equals(isConcern.getIsDeleted())) {
                                json.put("isConcern", true);
                            }


                            // 第二版增加了环信Id--和个人签名
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
                            json.put("sign", StringUtils.isEmpty(us.getUserSign()) ? "" : us.getUserSign());

                            lists.add(json);
                        }
                    }

                    Page<User> list = ouLiaoService.queryUserIsContractByIsNotRecommond(startCount - 1, pageCount, order);


                    for (User us : list) {
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

                        UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(), us.getUserId());

                        if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", false);

                        } else if ("0".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", true);
                        }


                        // 第二版增加了环信Id--和个人签名
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
                        json.put("sign", StringUtils.isEmpty(us.getUserSign()) ? "" : us.getUserSign());


                        lists.add(json);
                    }


                    break;

                case 1:// 粉丝数量

                    List<Integer> ids = userConcernService.queryUserConcerndByOrder((startCount - 1) * pageCount,
                            pageCount);

                    lists = new ArrayList<>();

                    for (Integer id : ids) {

                        if (ids.size() == 0) {
                            break;
                        }

                        json = new JSONObject();
                        User userFan = ouLiaoService.queryUserByUserId(id);
                        if (userFan == null || !"true".equals(userFan.getUserContract())) {
                            continue;
                        }

                        json.put("id", userFan.getUserId());

                        Boolean headflag = false;
                        String headUrl = userFan.getUserHeadPic();

                        if (StringUtils.isEmpty(headUrl)) {
                            headUrl = "985595";
                        } else if (headUrl.contains("//")) {
                            headflag = true;

                        } else {
                            headUrl = headUrl.split("\\.")[0];
                        }
                        if (headflag) {
                            json.put("url", userFan.getUserHeadPic());
                        } else {
                            json.put("url", properties.getProperty("headUrl") + userFan.getUserId() + "/" + headUrl
                                    + "/head/download");
                        }
                        if (userFan.getUserNickName()
                                .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                            json.put("name",
                                    userFan.getUserNickName().substring(0, 3) + "******"
                                            + new StringBuilder(
                                            new StringBuilder(userFan.getUserNickName()).reverse().substring(0, 2))
                                            .reverse());
                        } else {
                            json.put("name", userFan.getUserNickName());
                        }
                        json.put("author", userFan.getUserAuth() == null ? "" : userFan.getUserAuth());


                        UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(),
                                userFan.getUserId());


                        if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", false);

                        } else if ("0".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", true);
                        }


                        // 第二版增加了环信Id--和个人签名
                        // 创建环信账号


                        HuanXin huanXin = huanXinService.queryIsExist(userFan.getUserId());

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
                            String idss = (String) jsons.get("uuid");
                            if (StringUtils.isNotEmpty(idss)) {
                                HuanXin huanXinPost = new HuanXin();
                                huanXinPost.setCreatTime(new Date());
                                huanXinPost.setHuaXinUUid(idss);
                                huanXinPost.setHuaXinName(name);
                                huanXinPost.setOwnerId(userFan.getUserId());
                                huanXinPost.setPass(huanXinpass);
                                huanXinService.saveHuanXin(huanXinPost);
                            }
                            json.put("huanxinName", name);
                        }
                        json.put("sign", StringUtils.isEmpty(userFan.getUserSign()) ? "" : userFan.getUserSign());


                        lists.add(json);

                    }


                    break;
                case 2:// 评分

                    Object object = ouLiaoService.queryUserIsContractByAll(startCount - 1, pageCount, "userCallScore");
                    List<User> userList = new ArrayList<>();
                    if (object instanceof Page) {
                        list = (Page) object;

                        for (User us : list) {
                            if (us == null) {
                                continue;
                            }
                            userList.add(us);
                        }

                    } else {

                        userList = (List) object;

                    }

                    lists = new ArrayList<>();
                    for (User us : userList) {

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
                        UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(), us.getUserId());

                        if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", false);

                        } else if ("0".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", true);
                        }

                        // 第二版增加了环信Id--和个人签名
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
                            String idss = (String) jsons.get("uuid");
                            if (StringUtils.isNotEmpty(idss)) {
                                HuanXin huanXinPost = new HuanXin();
                                huanXinPost.setCreatTime(new Date());
                                huanXinPost.setHuaXinUUid(idss);
                                huanXinPost.setHuaXinName(name);
                                huanXinPost.setOwnerId(us.getUserId());
                                huanXinPost.setPass(huanXinpass);
                                huanXinService.saveHuanXin(huanXinPost);
                            }
                            json.put("huanxinName", name);
                        }
                        json.put("sign", StringUtils.isEmpty(us.getUserSign()) ? "" : us.getUserSign());


                        lists.add(json);
                    }

                    break;
                default:// 通话量
                    object = ouLiaoService.queryUserIsContractByAll(startCount - 1, pageCount, "userCallTotal");
                    userList = new ArrayList<>();
                    if (object instanceof Page) {
                        list = (Page) object;

                        for (User us : list) {
                            if (us == null) {
                                continue;
                            }
                            userList.add(us);
                        }

                    } else {

                        userList = (List) object;

                    }

                    lists = new ArrayList<>();

                    for (User us : userList) {
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

                        UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(), us.getUserId());

                        if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", false);

                        } else if ("0".equals(isConcern.getIsDeleted())) {
                            json.put("isConcern", true);
                        }

                        //判断是否是客服
                        if (properties.get("ouliaoService").equals(us.getUserPhone())) {

                            json.put("isService", true);
                            json.put("callTime", 0);
                        } else {
                            json.put("isService", false);
                            json.put("callTime", us.getUserCallTotal() == null ? 0 : us.getUserCallTotal());

                        }


                        // 第二版增加了环信Id--和个人签名
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
                            String idss = (String) jsons.get("uuid");
                            if (StringUtils.isNotEmpty(idss)) {
                                HuanXin huanXinPost = new HuanXin();
                                huanXinPost.setCreatTime(new Date());
                                huanXinPost.setHuaXinUUid(idss);
                                huanXinPost.setHuaXinName(name);
                                huanXinPost.setOwnerId(us.getUserId());
                                huanXinPost.setPass(huanXinpass);
                                huanXinService.saveHuanXin(huanXinPost);
                            }
                            json.put("huanxinName", name);
                        }
                        json.put("sign", StringUtils.isEmpty(us.getUserSign()) ? "" : us.getUserSign());


                        lists.add(json);
                    }

                    break;
            }

            if (lists.size() == 0) {
                // 除默认排序，其余没有数据进行时间倒序
                Page<User> list = ouLiaoService.queryUserIsContractByIsNotRecommond(startCount - 1, pageCount,
                        "userCreateTime");

                for (User us : list) {
                    if (us == null) {
                        continue;
                    }
                    JSONObject json = new JSONObject();
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

                    UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(), us.getUserId());

                    if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                        json.put("isConcern", false);

                    } else if ("0".equals(isConcern.getIsDeleted())) {
                        json.put("isConcern", true);
                    }

                    // 第二版增加了环信Id--和个人签名
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
                        String idss = (String) jsons.get("uuid");
                        if (StringUtils.isNotEmpty(idss)) {
                            HuanXin huanXinPost = new HuanXin();
                            huanXinPost.setCreatTime(new Date());
                            huanXinPost.setHuaXinUUid(idss);
                            huanXinPost.setHuaXinName(name);
                            huanXinPost.setOwnerId(us.getUserId());
                            huanXinPost.setPass(huanXinpass);
                            huanXinService.saveHuanXin(huanXinPost);
                        }
                        json.put("huanxinName", name);
                    }
                    json.put("sign", StringUtils.isEmpty(us.getUserSign()) ? "" : us.getUserSign());


                    lists.add(json);
                }
            }

            jsonObject.put("data", lists);

            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            jsonObject.put("msg", e.getMessage());
            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        }
        return jsonObject;
    }

    // 电话推荐和费用显示和修改
    @ResponseBody
    @RequestMapping(value = "userRecommond/{uid}/{sign}/recommondCall")
    public JSONObject recommondCall(@PathVariable("uid") String uid, @PathVariable("sign") Integer sign,
                                    @RequestParam("key") String key, @RequestParam(value = "cost", required = false) Double cost,
                                    @RequestParam(value = "id", required = false) Integer id,
                                    @RequestParam(value = "tourist", required = false) String tourist, HttpServletRequest request,
                                    HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            User user = null;
            if (StringUtils.isEmpty(tourist)) {

                // 登录检测
                user = ouLiaoService.queryUserByUserNum(uid);
            } else {
                user = new User();
                user.setUserId(0);
            }

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

            switch (sign) {
                case 0:// 费用修改
                    // 先判断是否是主播
                    if (!"true".equals(user.getUserContract())) {
                        jsonObject.put("code", 213);
                        response.setStatus(HttpServletResponse.SC_OK);

                        return jsonObject;
                    }
                    if (cost == null) {
                        jsonObject.put("code", 202);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }

                    int count = ouLiaoService.updateUserCallCostByUserId(cost, user.getUserId());
                    if (count == 1) {
                        jsonObject.put("code", 200);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        jsonObject.put("code", 210);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                    break;
                case 1:// 费用查看
                    // 先判断是否是主播
                    if (!"true".equals(user.getUserContract())) {
                        jsonObject.put("code", 213);
                        response.setStatus(HttpServletResponse.SC_OK);

                        return jsonObject;
                    }

                    jsonObject.put("code", 200);
                    jsonObject.put("cost", user.getUserCallCost());
                    response.setStatus(HttpServletResponse.SC_OK);

                    break;

                default:

                    if (id == null) {
                        jsonObject.put("code", 202);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }
                    user = ouLiaoService.queryUserByUserId(id);
                    // 验证查看的账号id---第二版权限放开，普通用户可以拨打
                  /*  if (user == null || !"true".equals(user.getUserContract())) {
                        jsonObject.put("code", 213);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }*/

                    if (user == null) {
                        jsonObject.put("code", 213);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }


                    //第二版增加普通用户拨打
                    if (!"true".equals(user.getUserContract())) {
                        jsonObject.put("code", 200);
                        response.setStatus(HttpServletResponse.SC_OK);

                        JSONObject json = new JSONObject();

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
                            json.put("url", properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl
                                    + "/head/download");
                        }
                        if (user.getUserNickName()
                                .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                            json.put("name", user.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                        } else {
                            json.put("name", user.getUserNickName());
                        }
                        jsonObject.put("isContract", "false");
                        jsonObject.put("data", json.toString());
                        return jsonObject;
                    }
                    jsonObject.put("isContract", "true");

                    // 查看主播是否在线
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

                        String[] times = null;
                        if (StringUtils.isEmpty(user.getUserCallTime())) {
                            times = properties.getProperty("defaultOnlieTime").trim().split("-");

                        } else {
                            times = user.getUserCallTime().trim().split("-");
                        }

                        if (times[0].split(":")[0].startsWith("0")) {
                            calendarBefore.set(Calendar.HOUR_OF_DAY,
                                    Integer.parseInt(times[0].split(":")[0].trim().substring(1, 2)));
                        } else {
                            calendarBefore.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0].trim().split(":")[0]));
                        }
                        calendarBefore.set(Calendar.MINUTE, Integer.parseInt(times[0].split(":")[1]));
                        calendarAfter.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[1].split(":")[0]));
                        calendarAfter.set(Calendar.MINUTE, Integer.parseInt(times[1].split(":")[1]));
                        // 在线
                        if (date.after(calendarBefore.getTime()) && date.before(calendarAfter.getTime())) {
                            flag = true;
                        }

                    }

                    JSONArray array = new JSONArray();
                    JSONObject json = null;

                    // 标志用户是否是在线不可拨打的状态
                    Boolean onlineStopCall = false;

                    if (flag) {

                        // 在线是否可以接听电话
                        // UserCallRoom userCallRoom =
                        // userCallRoomService.queryByUserCallRoomByUserCalledId(user.getUserId());
                        // 原来是任意一方接听和主动打电话
                        // UserCallRoom userCallRoom =
                        // userCallRoomService.queryByUserCallRoomIsExistById(user.getUserId());
                        // if (userCallRoom != null) {
                        // onlineStopCall = true;// 在线不可打通
                        //
                        // }
                        try { // 增加缓存
                            if ("true".equals(jedis.get("user:run:" + user.getUserId()))) {
                                onlineStopCall = true;// 在线不可打通

                            }
                        } catch (Exception e) {
                            // 原来是任意一方接听和主动打电话
                            List<UserCallRoom> userCallRooms = userCallRoomService
                                    .queryByUserCallRoomAllIsExistById(user.getUserId());
                            if (userCallRooms != null && userCallRooms.size() != 0) {
                                onlineStopCall = true;// 在线不可打通

                            }
                        }

                    }

                    // 主播不在线和在线不可拨打进行推荐
                    if (!flag || onlineStopCall) {
                        // json.put("id", value);
                        List<User> lists = ouLiaoService.queryUserContractByIsDeleted();

                        // 判断是否是在线
                        boolean con = false;
                        // 先判断是否在线// 08:10-20:40--排除主播自己
                        // 利用Map记录标签重复个数
                        Map<Integer, Integer> map = new HashMap<>();
                        for (User us : lists) {
                            con = false;
                            if (us == null || us.getUserCallTimeWeek() == null || us.getUserCallTime() == null
                                    || us.getUserId().equals(user.getUserId()) || id.equals(us.getUserId())) {
                                continue;
                            }

                            try { // 增加缓存
                                if ("true".equals(jedis.get("user:run:" + us.getUserId()))) {
                                    continue;

                                }
                            } catch (Exception e) {
                                // 是否在打电话
                                // UserCallRoom userCallRoom = userCallRoomService
                                // .queryByUserCallRoomByUserCalledId(us.getUserId());
                                // 之前是以主动方接听为主，现在是任何方都是

                                List<UserCallRoom> usCallRooms = userCallRoomService
                                        .queryByUserCallRoomAllIsExistById(us.getUserId());
                                if (usCallRooms != null && usCallRooms.size() != 0) {
                                    continue;

                                }
                            }


                            String[] weeks = null;
                            if (StringUtils.isEmpty(us.getUserCallTimeWeek())) {
                                weekArr = properties.getProperty("callTime").split(",");
                            } else if (us.getUserCallTimeWeek().indexOf(",") > -1) {
                                weeks = us.getUserCallTimeWeek().split(",");
                            } else {
                                weeks = new String[]{us.getUserCallTimeWeek()};
                            }
                            for (String str : weeks) {
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

                                String[] times = null;
                                if (StringUtils.isEmpty(us.getUserCallTime())) {
                                    times = properties.getProperty("defaultOnlieTime").trim().split("-");

                                } else {
                                    times = us.getUserCallTime().trim().split("-");
                                }

                                if (times[0].split(":")[0].startsWith("0")) {
                                    calendarBefore.set(Calendar.HOUR_OF_DAY,
                                            Integer.parseInt(times[0].split(":")[0].trim().substring(1, 2)));
                                } else {
                                    calendarBefore.set(Calendar.HOUR_OF_DAY,
                                            Integer.parseInt(times[0].trim().split(":")[0]));
                                }

                                calendarBefore.set(Calendar.MINUTE, Integer.parseInt(times[0].split(":")[1]));
                                calendarAfter.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[1].split(":")[0]));
                                calendarAfter.set(Calendar.MINUTE, Integer.parseInt(times[1].split(":")[1]));
                                // 在线
                                if (date.after(calendarBefore.getTime()) && date.before(calendarAfter.getTime())) {
                                    con = true;
                                }

                            }

                            if (con == false) {
                                continue;
                            }

                            // 其次在标签
                            if (StringUtils.isNotEmpty(us.getUserLabel())) {

                                // 标签的判断
                                String[] labelArr = null;
                                if (us.getUserLabel().indexOf(",") > -1) {
                                    labelArr = us.getUserLabel().split(",");
                                } else {
                                    labelArr = new String[]{us.getUserLabel()};
                                }

                                for (int i = 0; i < labelArr.length; i++) {

                                    if (us == null || us.getUserId().equals(user.getUserId())
                                            || us.getUserLabel() == null) {
                                        continue;
                                    }
                                    String[] labelComp = null;
                                    if (us.getUserLabel().indexOf(",") > -1) {
                                        labelComp = us.getUserLabel().split(",");
                                    } else {
                                        labelComp = new String[]{us.getUserLabel()};
                                    }

                                    for (String st : labelComp) {
                                        if (StringUtils.isEmpty(st)) {
                                            continue;
                                        }
                                        if (st.equals(labelArr[i])) {

                                            map.put(us.getUserId(),
                                                    map.get(us.getUserId()) == null ? 1 : map.get(us.getUserId()) + 1);
                                        }

                                    }

                                }

                            }

                        }

                        // 根据标签个数顺序排序推荐--标签时候进行操作
                        List<Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());

                        Collections.sort(list, new Comparator<Entry<Integer, Integer>>() {

                            @Override
                            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
                                return o2.getValue() - o1.getValue();
                            }
                        });
                        for (Entry<Integer, Integer> maps : list) {

                            json = new JSONObject();

                            User us = ouLiaoService.queryUserByUserId(maps.getKey());

                            // 再次是分数
                            // 评分的推荐
                            if (us == null) {
                                continue;
                            }

                            if (user.getUserCallScore() == null || us.getUserCallScore() >= user.getUserCallScore()) {
                                json = new JSONObject();
                                json.put("rId", us.getUserId());

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
                                    json.put("rUrl", us.getUserHeadPic());
                                } else {
                                    json.put("rUrl", properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl
                                            + "/head/download");
                                }
                                if (us.getUserNickName()
                                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                                    json.put("rName", us.getUserNickName().substring(0, 3) + "******"
                                            + new StringBuilder(
                                            new StringBuilder(us.getUserNickName()).reverse().substring(0, 2))
                                            .reverse());
                                } else {
                                    json.put("rName", us.getUserNickName());
                                }

                                json.put("rAuthor", us.getUserAuth());

                                array.add(json);

                            }

                        }
                    }
                    jsonObject.put("code", 200);
                    if (flag) {

                        if (onlineStopCall) {
                            jsonObject.put("onlineStaus", 2);// 在线不可打通
                            JSONArray arr = new JSONArray();
                            // 前台只需要两个参数
                            if (array.size() > 2) {
                                int numFirst = (int) (Math.random() * array.size());
                                arr.add(array.get(numFirst));
                                int numSec = (int) (Math.random() * array.size());
                                while (numFirst == numSec) {
                                    numSec = (int) (Math.random() * array.size());
                                }

                                arr.add(array.get(numSec));
                            }

                            if (array.size() > 2) {
                                jsonObject.put("data", arr);
                            } else {
                                jsonObject.put("data", array);
                            }
                        } else {
                            jsonObject.put("onlineStaus", 1);// 在线可以打通

                            jsonObject.put("id", user.getUserId());

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
                                jsonObject.put("url", user.getUserHeadPic());
                            } else {
                                jsonObject.put("url", properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl
                                        + "/head/download");
                            }
                            if (user.getUserNickName()
                                    .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                                jsonObject.put("name",
                                        user.getUserNickName().substring(0, 3) + "******"
                                                + new StringBuilder(
                                                new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                                .reverse());
                            } else {
                                jsonObject.put("name", user.getUserNickName());
                            }

                            jsonObject.put("author", user.getUserAuth());
                            String label = user.getUserLabel();
                            JSONArray arr = new JSONArray();
                            if (StringUtils.isNotEmpty(label)) {

                                if (label.contains(",")) {

                                    for (String str : label.split(",")) {
                                        if (StringUtils.isEmpty(str)) {
                                            continue;
                                        }
                                        arr.add(str);
                                    }
                                } else {
                                    arr.add(label);
                                }

                                jsonObject.put("label", arr);
                            } else {
                                jsonObject.put("label", arr);
                            }

                            jsonObject.put("cost", user.getUserCallCost() == null ? 1.00 : user.getUserCallCost());
                            String week = user.getUserCallTimeWeek();
                            StringBuilder sb = new StringBuilder();
                            int i = 0;
                            if (week.contains(",")) {
                                for (String str : week.split(",")) {
                                    if (StringUtils.isEmpty(str)) {
                                        continue;
                                    }

                                    switch (str) {
                                        case "每周日":
                                            i++;
                                            sb.append("日");
                                            sb.append(",");
                                            break;
                                        case "每周一":
                                            i++;
                                            sb.append("一");
                                            sb.append(",");
                                            break;
                                        case "每周二":
                                            i++;
                                            sb.append("二");
                                            sb.append(",");
                                            break;
                                        case "每周三":
                                            i++;
                                            sb.append("三");
                                            sb.append(",");
                                            break;
                                        case "每周四":
                                            i++;
                                            sb.append("四");
                                            sb.append(",");
                                            break;
                                        case "每周五":
                                            i++;
                                            sb.append("五");
                                            sb.append(",");
                                            break;

                                        default:
                                            i++;
                                            sb.append("六");
                                            sb.append(",");
                                            break;
                                    }
                                }
                                if (i != 7) {
                                    sb.insert(0, "每周");
                                    sb.delete(sb.length() - 1, sb.length());
                                } else {
                                    sb = new StringBuilder();
                                    sb.append("每天");
                                }
                            } else {
                                sb.append(week);
                            }

                            jsonObject.put("callWeek", sb.toString());
                            jsonObject.put("callTime", user.getUserCallTime());
                        }

                    }


                    //第二版增加了打招呼的东西
                    jsonObject.put("greet", StringUtils.isEmpty(user.getUserGreet()) ? "" : user.getUserGreet());


                    if (!flag) {
                        jsonObject.put("onlineStaus", 0);// 不在线
                        jsonObject.put("id", user.getUserId());

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
                            jsonObject.put("url", user.getUserHeadPic());
                        } else {
                            jsonObject.put("url", properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl
                                    + "/head/download");
                        }
                        jsonObject.put("cost", user.getUserCallCost() == null ? 1.00 : user.getUserCallCost());
                        String week = user.getUserCallTimeWeek();
                        StringBuilder sb = new StringBuilder();
                        int i = 0;
                        if (week.contains(",")) {
                            for (String str : week.split(",")) {
                                if (StringUtils.isEmpty(str)) {
                                    continue;
                                }

                                switch (str) {
                                    case "每周日":
                                        i++;
                                        sb.append("日");
                                        sb.append(",");
                                        break;
                                    case "每周一":
                                        i++;
                                        sb.append("一");
                                        sb.append(",");
                                        break;
                                    case "每周二":
                                        i++;
                                        sb.append("二");
                                        sb.append(",");
                                        break;
                                    case "每周三":
                                        i++;
                                        sb.append("三");
                                        sb.append(",");
                                        break;
                                    case "每周四":
                                        i++;
                                        sb.append("四");
                                        sb.append(",");
                                        break;
                                    case "每周五":
                                        i++;
                                        sb.append("五");
                                        sb.append(",");
                                        break;

                                    default:
                                        i++;
                                        sb.append("六");
                                        sb.append(",");
                                        break;
                                }
                            }
                            if (i != 7) {
                                sb.insert(0, "每周");
                                sb.delete(sb.length() - 1, sb.length());
                            } else {
                                sb = new StringBuilder();
                                sb.append("每天");
                            }
                        } else {
                            sb.append(week);
                        }


                        jsonObject.put("callWeek", sb.toString());
                        jsonObject.put("callTime", user.getUserCallTime());

                        JSONArray arr = new JSONArray();
                        // 前台只需要两个参数
                        if (array.size() > 2) {
                            int numFirst = (int) (Math.random() * array.size());
                            arr.add(array.get(numFirst));
                            int numSec = (int) (Math.random() * array.size());
                            while (numFirst == numSec) {
                                numSec = (int) (Math.random() * array.size());
                            }

                            arr.add(array.get(numSec));
                        }
                        if (array.size() > 2) {
                            jsonObject.put("data", arr);
                        } else {
                            jsonObject.put("data", array);
                        }

                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                    break;
            }

            try {
                jedis.disconnect();
            } catch (Exception e) {

            }
        } catch (

                Exception e)

        {
            jsonObject.put("error", e.getMessage());
            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;

    }

    // 偶推搜索
    @ResponseBody
    @RequestMapping(value = "userRecommond/{uid}/{startCount}/recommondFind")
    public JSONObject recommondCall(@PathVariable("uid") String uid, @PathVariable("startCount") Integer startCount,
                                    @RequestParam("key") String key, @RequestParam("word") String word, HttpServletRequest request,
                                    @RequestParam(value = "tourist", required = false) String tourist, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(word) || startCount <= 0) {
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
            properties.load(new InputStreamReader(
                    OuLiaoRecommondController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));
            Integer pageSize = Integer.valueOf(properties.getProperty("recommondFindCount"));

            List<User> users = ouLiaoService.queryUserContractByUserNickNameOrUserAuth((startCount - 1) * pageSize,
                    pageSize, word);

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

                UserConcern isConcern = userConcernService.queryUserIsConcernById(user.getUserId(), us.getUserId());

                if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {
                    json.put("isConcern", false);

                } else if ("0".equals(isConcern.getIsDeleted())) {
                    json.put("isConcern", true);
                }
                json.put("sign", StringUtils.isEmpty(us.getUserSign()) == true ? "" : us.getUserSign());
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

    // 偶推关注的搜索
    @ResponseBody
    @RequestMapping(value = "userRecommond/{uid}/{startCount}/concernFind")
    public JSONObject concernFind(@PathVariable("uid") String uid, @PathVariable("startCount") Integer startCount,
                                  @RequestParam("key") String key, @RequestParam("word") String word, HttpServletRequest request,
                                  HttpServletResponse response) {
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
                    OuLiaoRecommondController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));
            Integer pageSize = Integer.valueOf(properties.getProperty("concernCount"));

            // 用户的关注所有人的
            List<UserConcern> userConcerns = userConcernService.queryUserConcerndAllByUserId(user.getUserId());

            List<Integer> ids = new ArrayList<>();
            for (UserConcern uc : userConcerns) {
                if (uc == null) {
                    continue;
                }
                ids.add(uc.getUserOnfocusId());

            }

            List<User> users = ouLiaoService.queryUserContractByUserNickNameOrUserAuthAndUserId(
                    (startCount - 1) * pageSize, pageSize, ids, word);

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
                json.put("sign", StringUtils.isEmpty(us.getUserSign()) ? "" : us.getUserSign());

                // 判断主播是否在线和在线可接电话
                // 表示在线
                boolean flag = false;
                String[] weekArr = null;
                if (StringUtils.isEmpty(us.getUserCallTimeWeek())) {
                    weekArr = properties.getProperty("callTime").split(",");
                } else if (us.getUserCallTimeWeek().indexOf(",") > -1) {
                    weekArr = us.getUserCallTimeWeek().split(",");
                } else {
                    weekArr = new String[]{us.getUserCallTimeWeek()};
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

                    if (StringUtils.isEmpty(us.getUserCallTime())) {
                        times = properties.getProperty("defaultOnlieTime").split("-");

                    } else {
                        times = us.getUserCallTime().trim().split("-");
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
                        // UserCallRoom usCallRoom =
                        // userCallRoomService.queryByUserCallRoomByUserCalledId(us.getUserId());
                        // 之前是以主动方接听为主，现在是任何方都是
                        // UserCallRoom usCallRoom =
                        // userCallRoomService.queryByUserCallRoomIsExistById(us.getUserId());
                        // if (usCallRoom != null) {
                        //
                        // json.put("onlineStaus", 2);// 在线可以打通
                        // } else {
                        // json.put("onlineStaus", 1);// 在线不可打通
                        // }

                        try {
                            if ("true".equals(jedis.get("user:run:" + us.getUserId()))) {
                                json.put("onlineStaus", 2);// 在线可以打通

                            } else {
                                json.put("onlineStaus", 1);// 在线不可打通
                            }

                        } catch (Exception e) {
                            // 之前是以主动方接听为主，现在是任何方都是
                            List<UserCallRoom> usCallRooms = userCallRoomService
                                    .queryByUserCallRoomAllIsExistById(us.getUserId());
                            if (usCallRooms != null && usCallRooms.size() != 0) {

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

                array.add(json);
            }

            jsonObject.put("code", 200);

            jsonObject.put("data", array);

            try {

                jedis.disconnect();
            } catch (Exception e) {

            }

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }
}
