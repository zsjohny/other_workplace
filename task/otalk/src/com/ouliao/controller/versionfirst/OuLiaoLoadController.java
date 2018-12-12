package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.*;
import com.ouliao.repository.versionfirst.GeTuiMapperCrudRepository;
import com.ouliao.repository.versionfirst.GeTuiMapperRepository;
import com.ouliao.service.versionfirst.*;
import com.xiaoluo.util.*;
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
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by p on 2016/2/18.
 */
@Controller
//@RequestMapping(value = "user/load", method = RequestMethod.POST)
@RequestMapping(value = "user/load")
public class OuLiaoLoadController {
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserSayService userSayService;
    @Autowired
    private UserConcernService userConcernService;
    @Autowired
    private ServiceRecordTimeService serviceRecordTimeService;
    private HttpSession sessions;
    @Autowired
    private HuanXinService huanXinService;
    @Autowired
    private UserBlackListService userBlackListService;
    private JedisPool pool = new JedisPool("localhost", 10088);
    private Jedis jedis = pool.getResource();
    @Autowired
    private GeTuiMapperRepository geTuiMapperRepository;
    @Autowired
    private GeTuiMapperCrudRepository geTuiMapperCrudRepository;

    // 注册或找回密码
    @ResponseBody
    @RequestMapping(value = "regUser/{phone}/{cate}/{imei}")
    public JSONObject regUser(@PathVariable("phone") String phone, @PathVariable("cate") Integer cate,
                              @PathVariable("imei") String imei, HttpServletRequest request, HttpServletResponse response) {
        String userRealIp = GainRealIpUtil.gainRealIp(request) + "_" + imei;
        JSONObject jsonObject = new JSONObject();
        // 限制同一用户只能一天注册
        UserGainCount isUser = ouLiaoService.queryUserByIp(userRealIp);

        //检测redis异常之后的情况
        boolean redisFlag = false;

        if (isUser == null) {
            isUser = new UserGainCount();
            isUser.setUserCreateTime(new Date());
            isUser.setIsDeleted("0");
            isUser.setUserRealIp(userRealIp);
            isUser.setUserGainCount(1);
            if (!ouLiaoService.saveGainCountByIp(isUser)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonObject.put("code", 201);
                return jsonObject;
            }
        } else {
            Integer count = isUser.getUserGainCount();
            if (count > 50) {
                response.setStatus(HttpServletResponse.SC_OK);

                // 启动定时任务清除用户的注册限制
                Timer timer = new Timer();

                final String ip = userRealIp;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            ouLiaoService.updateCountByIp(0, ip);
                        } catch (Exception e) {
                        }

                    }
                };
                timer.schedule(task, 1000 * 60 * 60 * 24);
                // timer.schedule(task, 1000 * 60 * 1);
                jsonObject.put("code", 207);
                return jsonObject;
            }
            ouLiaoService.updateCountByIp(++count, userRealIp);

        }

        try {

            if (phone.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$") || cate == null) {
                User user = ouLiaoService.queryUserByPhone(phone);
                Boolean flag = false;
                String num = "";
                for (int i = 0; i < 6; i++) {
                    num += String.valueOf(((int) (Math.random() * 10)));
                }

                // 注册
                if (cate == 1) {

                    if (user != null) {
                        // 这里如果有再继续验证是否是主播注册
                        if ("true".equals(user.getUserContract()) && !"true".equals(user.getIsRegister())) {
                            ouLiaoService.updateCodeIsContractByPhoneBy(num, user.getUserPhone(), "true");
                            flag = true;

                            try {
                                jedis.setex(phone, 60 * 5, num);
                            } catch (Exception e) {
                                redisFlag = true;
                                ouLiaoService.updateCodeByPhone(num, phone);
                            }
                        } else {
                            jsonObject.put("code", 203);
                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonObject;
                        }

                    } else {
                        if (ouLiaoService.queryUserByPhone(phone) == null) {
                            flag = true;
                            try {
                                jedis.setex(phone, 60 * 5, num);
                            } catch (Exception e) {
                                redisFlag = true;
                                User use = new User();
                                use.setUserPhoneCode(num);
                                use.setUserPhone(phone);
                                // 默认填写昵称
                                use.setUserNickName(phone);
                                use.setUserNum(UUID.randomUUID().toString());
                                use.setUserCreateTime(new Date());
                                use.setIsDeleted("0");

                                // 默认的签名和说说
                                // 加载数量配置
                                Properties properties = new Properties();
                                try {
                                    properties.load(new InputStreamReader(
                                            OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                                            "utf-8"));
                                } catch (Exception e1) {

                                }
                                use.setUserSign(properties.getProperty("signDefault"));

                                ouLiaoService.regUser(use);

                                User us = ouLiaoService.queryUserByPhone(phone);

                                // 默认设置有十分钟的通话时间
                                ServiceRecordTime serviceRecordTime = new ServiceRecordTime();
                                serviceRecordTime.setCreatTime(new Date());
                                //serviceRecordTime.setUserCallTime(600l);
                                serviceRecordTime.setUserCallTime(180l);
                                serviceRecordTime.setIsSysSend("true");
                                serviceRecordTime.setUserId(us.getUserId());
                                serviceRecordTimeService.createServiceRecordTime(serviceRecordTime);
                                // 发表说说
                                UserSayContent userSayContent = new UserSayContent();
                                userSayContent.setUserCreateTime(new Date());
                                userSayContent.setIsDeleted("0");
                                userSayContent.setUserId(us.getUserId());
                                userSayContent.setUserContent(properties.getProperty("contentDefault"));
                                userSayService.createUserSayContentByUserId(userSayContent);

                                // 添加默认客服关注
                                us = ouLiaoService.queryUserByPhone(properties.getProperty("ouliaoService"));
                                if (us == null) {
                                    // 设置默认客服
                                    us = new User();
                                    us.setUserPhone(properties.getProperty("ouliaoService"));
                                    us.setUserNickName(properties.getProperty("ouliaoNickName"));
                                    String uuid = UUID.randomUUID().toString();
                                    us.setUserNum(uuid);
                                    us.setUserCreateTime(new Date());
                                    us.setIsDeleted("0");
                                    us.setUserMoney(5000.0);
                                    us.setUserCallCost(0.00);
                                    us.setUserSign(properties.getProperty("ouliaoSign"));
                                    String pass = DesIosAndAndroid.encryptDES(properties.getProperty("ouliaoPass"),
                                            properties.getProperty("ouliaoService"));
                                    us.setUserPass(pass);
                                    us.setUserContract("true");
                                    us.setUserKey(Des16Util.encrypt(pass, uuid));
                                    us.setUserCallTime(properties.getProperty("ouliaoTime"));
                                    us.setUserCallTimeWeek(properties.getProperty("callTime"));
                                    ouLiaoService.regUser(us);

                                    us = ouLiaoService.queryUserByPhone(properties.getProperty("ouliaoService"));
                                }

                                UserConcern userConcern = new UserConcern();
                                userConcern.setUserOnfocusId(us.getUserId());
                                userConcern.setUserId(use.getUserId());
                                userConcern.setIsDeleted("0");
                                userConcern.setUserCreateTime(new Date());
                                userConcern.setUserModifyTime(new Date());
                                if ("true".equals(us.getUserContract())) {
                                    userConcern.setUserConract("true");
                                } else {
                                    userConcern.setUserConract("false");
                                }

                                userConcern = userConcernService.createUserConcern(userConcern, user, "service");

                                jsonObject.put("code", 200);
                            }
                        } else {
                            jsonObject.put("code", 203);
                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonObject;
                        }

                    }

                } else if (cate == 2) {

                    if (user != null) {
                        try {
                            jedis.setex(phone, 60 * 5, num);
                        } catch (Exception e) {
                            redisFlag = true;
                            ouLiaoService.updateCodeByPhone(num, phone);
                        }
                        flag = true;
                    } else {
                        jsonObject.put("code", 203);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }
                } else if (cate == 3) {// 更换手机号

                    if (user == null) {

                        jsonObject.put("code", 200);
                        sessions = request.getSession();
                        sessions.setAttribute(phone, num);
                        // 设置5分钟后消失
                        sessions.setMaxInactiveInterval(1000 * 60 * 5);
                        response.setStatus(HttpServletResponse.SC_OK);

                    } else {
                        jsonObject.put("code", 203);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }
                } else if (cate == 4) { //第三方登录绑定手机号
                    if (user == null) {
                        jsonObject.put("code", 200);
                        try {
                            jedis.setex(phone, 60 * 5, num);
                        } catch (Exception e) {
                            redisFlag = true;
                            ouLiaoService.updateCodeByPhone(num, phone);
                        }
                        flag = true;
                    } else {
                        jsonObject.put("code", 203);

                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                }

                int code = SendMsg.batchSend(phone, num);
                if (code == 200) {

                    if (flag) {
                        jsonObject.put("code", 200);
                        response.setStatus(HttpServletResponse.SC_OK);

                        if (redisFlag) {
                            // 启动线程进行消失验证码
                            Timer timer = new Timer();

                            final String savephone = phone;
                            TimerTask task = new TimerTask() {

                                @Override
                                public void run() {
                                    try {
                                        ouLiaoService.updateCodeByPhone("xiaolu", savephone);
                                    } catch (Exception e) {
                                    }

                                }
                            };
                            // 启动失效的代码时间
                            timer.schedule(task, 1000 * 60 * 5);
                        }
                        return jsonObject;
                    }

                    return jsonObject;
                }
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonObject.put("code", 201);
            } else {

                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (

                Exception e
                )

        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }

        try

        {
            jedis.disconnect();
        } catch (
                Exception e
                )

        {
        }

        return jsonObject;

    }

    // 比对验证码
    @ResponseBody
    @RequestMapping(value = "compUser/{phone}/{code}")
    public JSONObject compUser(@PathVariable("phone") String phone, @PathVariable("code") String
            code, @RequestParam(value = "changePhone", required = false) String
                                       changePhone, @RequestParam(value = "thridId", required = false) String thridId,
                               HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (phone.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")
                    && code.matches("\\d{6}")) {
                String codeOrg = "";
                try {
                    codeOrg = jedis.get(phone);

                } catch (Exception e) {
                    User us = ouLiaoService.queryUserByPhone(phone);
                    if (us == null || StringUtils.isEmpty(us.getUserPhoneCode())) {
                        jsonObject.put("code", 204);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }

                    codeOrg = us.getUserPhoneCode();

                }
                if (codeOrg == null) {

                    User us = ouLiaoService.queryUserByPhone(phone);
                    if (us == null || StringUtils.isEmpty(us.getUserPhoneCode())) {
                        jsonObject.put("code", 204);
                        response.setStatus(HttpServletResponse.SC_OK);
                        return jsonObject;
                    }

                    codeOrg = us.getUserPhoneCode();


                }
                if (code.equals(codeOrg)) {
                    User user = ouLiaoService.queryUserByPhone(phone);

                    if (user == null) {


                        //第三方绑定手机号
                        if (StringUtils.isNotEmpty(changePhone) && "true".equals(changePhone)) {
                            if (StringUtils.isEmpty(thridId)) {
                                jsonObject.put("code", 202);


                            } else {

                                if (ouLiaoService.queryUserByThridId(thridId) == null) {
                                    jsonObject.put("code", 205);


                                } else {

                                    ouLiaoService.updateUserPhoneByThridId(phone, thridId);
                                    jsonObject.put("code", 200);
                                    try {
                                        jedis.del(phone);
                                    } catch (Exception e) {

                                    }
                                }
                            }

                            response.setStatus(HttpServletResponse.SC_OK);
                            return jsonObject;
                        }


                        // 存储数据
                        user = new User();
                        user.setUserPhone(phone);
                        // 默认填写昵称
                        user.setUserNickName(phone);
                        user.setUserNum(UUID.randomUUID().toString());
                        user.setUserCreateTime(new Date());
                        user.setIsDeleted("0");

                        // 默认的签名和说说
                        // 加载数量配置
                        Properties properties = new Properties();
                        properties.load(new InputStreamReader(
                                OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                                "utf-8"));

                        user.setUserSign(properties.getProperty("signDefault"));

                        ouLiaoService.regUser(user);

                        User us = ouLiaoService.queryUserByPhone(phone);

                        // 默认设置有十分钟的通话时间
                        ServiceRecordTime serviceRecordTime = new ServiceRecordTime();
                        serviceRecordTime.setCreatTime(new Date());
                        //serviceRecordTime.setUserCallTime(600l);
                        serviceRecordTime.setUserCallTime(180l);
                        serviceRecordTime.setIsSysSend("true");
                        serviceRecordTime.setUserId(us.getUserId());
                        serviceRecordTimeService.createServiceRecordTime(serviceRecordTime);
                        // 发表说说
                        UserSayContent userSayContent = new UserSayContent();
                        userSayContent.setUserCreateTime(new Date());
                        userSayContent.setIsDeleted("0");
                        userSayContent.setUserId(us.getUserId());
                        userSayContent.setUserContent(properties.getProperty("contentDefault"));
                        userSayService.createUserSayContentByUserId(userSayContent);

                        // 添加默认客服关注
                        us = ouLiaoService.queryUserByPhone(properties.getProperty("ouliaoService"));
                        if (us == null) {
                            // 设置默认客服
                            us = new User();
                            us.setUserPhone(properties.getProperty("ouliaoService"));
                            us.setUserNickName(properties.getProperty("ouliaoNickName"));
                            String uuid = UUID.randomUUID().toString();
                            us.setUserNum(uuid);
                            us.setUserCreateTime(new Date());
                            us.setIsDeleted("0");
                            us.setUserMoney(5000.0);
                            us.setUserCallCost(0.00);
                            us.setUserSign(properties.getProperty("ouliaoSign"));
                            String pass = DesIosAndAndroid.encryptDES(properties.getProperty("ouliaoPass"),
                                    properties.getProperty("ouliaoService"));
                            us.setUserPass(pass);
                            us.setUserContract("true");
                            us.setUserKey(Des16Util.encrypt(pass, uuid));
                            us.setUserCallTime(properties.getProperty("ouliaoTime"));
                            us.setUserCallTimeWeek(properties.getProperty("callTime"));
                            ouLiaoService.regUser(us);

                            us = ouLiaoService.queryUserByPhone(properties.getProperty("ouliaoService"));
                        }

                        UserConcern userConcern = new UserConcern();
                        userConcern.setUserOnfocusId(us.getUserId());
                        userConcern.setUserId(user.getUserId());
                        userConcern.setIsDeleted("0");
                        userConcern.setUserCreateTime(new Date());
                        userConcern.setUserModifyTime(new Date());
                        if ("true".equals(us.getUserContract())) {
                            userConcern.setUserConract("true");
                        } else {
                            userConcern.setUserConract("false");
                        }

                        userConcern = userConcernService.createUserConcern(userConcern, user, "service");
                    }
                    // 创建session
                    sessions = request.getSession();
                    sessions.setAttribute(phone, true);
                    sessions.setMaxInactiveInterval(60 * 3);
                    jsonObject.put("code", 200);
                    try {
                        jedis.del(phone);
                    } catch (Exception e) {

                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
                jsonObject.put("code", 204);
                response.setStatus(HttpServletResponse.SC_OK);

            } else {

                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        try {
            jedis.disconnect();
        } catch (Exception e) {


        }
        return jsonObject;

    }

    // 设置密码
    @ResponseBody
    @RequestMapping(value = "retPass/{phone}")
    public JSONObject retPass(@PathVariable("phone") String phone,
                              @RequestParam(value = "pass", defaultValue = "1") String pass, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        pass = pass.trim().replaceAll(" ", "+");
        try {
            if (phone.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$") && pass.length() > 7) {
                User user = ouLiaoService.queryUserByPhone(phone);

                if (user == null) {
                    jsonObject.put("code", 202);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
                if (sessions.getAttribute(phone) == null) {
                    jsonObject.put("code", 211);

                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }

                user.setUserKey(Des16Util.encrypt(pass, user.getUserNum()));

                ouLiaoService.updatePassByPhone(pass, user.getUserKey(), phone);

                jsonObject.put("code", 200);

            } else {
                jsonObject.put("code", 202);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;

    }

    // 登陆
    @ResponseBody
    @RequestMapping(value = "loginUser/{loginname}")
    public synchronized JSONObject loginUser(@PathVariable("loginname") String loginname,
                                             @RequestParam(value = "pass", defaultValue = "1") String pass,
                                             @RequestParam(value = "deviceSign", required = false) Integer deviceSign,
                                             @RequestParam(value = "cid", required = false) String cid,
                                             @RequestParam(value = "devicen_token", required = false) String devicen_token, HttpServletRequest
                                                     request,
                                             HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        pass = pass.trim().replaceAll(" ", "+");
        try {
            User user = null;
            if (StringUtils.isEmpty(loginname)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            if (loginname.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                user = ouLiaoService.queryUserByPhone(loginname);
            } else {
                // 第三方登录
                user = ouLiaoService.queryUserByThridId(loginname);
            }


            if (user == null || StringUtils.isEmpty(user.getUserNum())) {

                jsonObject.put("code", 206);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            if (StringUtils.isEmpty(user.getUserPass()) || user.getUserPass().length() < 6) {
                jsonObject.put("code", 205);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            String key = Des16Util.decrypt(user.getUserKey(), user.getUserNum());
            if (pass.trim().equals(key.trim())) {
                // 用户在线的数据侦查 --根据modifyTime进行判断
                ouLiaoService.updateUserModifyTimeByUserId(new Date(), user.getUserId());
                jsonObject.put("code", 200);
                jsonObject.put("uid", user.getUserNum());
                jsonObject.put("id", user.getUserId());
                if (StringUtils.isEmpty(user.getUserAlipayAccount()) == true) {
                    jsonObject.put("isAccount", true);
                    jsonObject.put("aliName", user.getUserAlipayName());
                    jsonObject.put("aliAccount", user.getUserAlipayAccount());
                } else {
                    jsonObject.put("isAccount", false);
                }

                jsonObject.put("contract", "true".equals(user.getUserContract()) == true ? "true" : "false");
                String time = new StringBuilder(System.currentTimeMillis() + "").reverse().toString();
                String tempKey = DesUtil.encrypt(user.getUserNum(), time);
                // 存储起来
                ouLiaoService.updateTempKeyByUserNum(time, tempKey, user.getUserNum());

                jsonObject.put("key", tempKey);
                // 创建session
                request.getSession().setAttribute(tempKey, true);
                request.getSession().setMaxInactiveInterval(6000 * 24 * 60);

                // 创建环信账号
                HuanXin huanXin = huanXinService.queryIsExist(user.getUserId());
                if (huanXin != null) {
                    jsonObject.put("huanxinName", huanXin.getHuaXinName());
                } else {

                    HttpClient httpClient = HttpClients.createDefault();
                    HttpPost post = new HttpPost("https://a1.easemob.com/szkj2016/ouliao/users");

                    String name = System.currentTimeMillis() + "";
                    String huanXinpass = "xiaoluo";
                    JSONObject json = new JSONObject();
                    json.put("username", name);
                    json.put("password", huanXinpass);

                    StringEntity entity = new StringEntity(json.toString());
                    entity.setContentEncoding("utf-8");
                    entity.setContentType("application/json");
                    post.setEntity(entity);

                    HttpResponse responsea = httpClient.execute(post);
                    HttpEntity entitys = responsea.getEntity();
                    String data = EntityUtils.toString(entitys);
                    json = JSONObject.fromObject(data);
                    JSONArray array = JSONArray.fromObject(json.get("entities"));

                    json = JSONObject.fromObject(array.get(0));
                    String id = (String) json.get("uuid");
                    if (StringUtils.isNotEmpty(id)) {
                        HuanXin huanXinPost = new HuanXin();
                        huanXinPost.setCreatTime(new Date());
                        huanXinPost.setHuaXinUUid(id);
                        huanXinPost.setHuaXinName(name);
                        huanXinPost.setOwnerId(user.getUserId());
                        huanXinPost.setPass(huanXinpass);
                        huanXinService.saveHuanXin(huanXinPost);
                    }
                    jsonObject.put("huanxinName", name);
                }

                // 增加个推的信息
                try {
                    if (jedis.exists(String.valueOf(user.getUserId())) && StringUtils.isNotEmpty(cid)) {

                        GeTuiMapper geTuiMapper = null;
                        // 根据用户删除客户端id
                        try {
                            geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(user.getUserId());
                        } catch (Exception e) {
                            List<GeTuiMapper> list = geTuiMapperRepository.queryAllIsExist(cid);
                            geTuiMapper = null;
                            geTuiMapperCrudRepository.delete(list);

                        }
                        if (geTuiMapper != null) {

                            geTuiMapperCrudRepository.delete(geTuiMapper.getGeTuiMapperId());

                        }
                        // 保存个推
                        geTuiMapper = new GeTuiMapper();

                        geTuiMapper.setIsDeleted("0");
                        geTuiMapper.setClientId(cid);
                        geTuiMapper.setUserId(user.getUserId());
                        geTuiMapper.setClientCata(deviceSign == 0 ? 0 : 1);
                        geTuiMapper.setUserCreateTime(new Date());
                        geTuiMapper.setGetuiDeviceToken(deviceSign == 1 ? devicen_token : "");
                        geTuiMapperCrudRepository.save(geTuiMapper);
                        try {
                            // 保存用户信息

                            if (jedis.exists("user:" + user.getUserId())) {
                                String clientId = jedis.get("user:" + user.getUserId());
                                if (!clientId.equals(cid)) {

                                    // 两个都要推
                                    JSONObject json = new JSONObject();

                                    json.put("loadStatus", "remote");
                                    if (0 == geTuiMapper.getClientCata()) {
                                        Getui.SendAndroid(json.toString(), clientId);

                                    } else {
                                        Getui.SendIos(json.toString(), clientId, "", "");

                                    }
                                    jedis.set("user:" + user.getUserId(), cid);
                                } else {
                                    jedis.set("user:" + user.getUserId(), cid);
                                }

                            } else {
                                jedis.set("user:" + user.getUserId(), cid);
                            }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }
                } catch (Exception e) {

                }

                // -----iose友盟第三方登录
                JSONObject json = new JSONObject();

                if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                    json.put("name",
                            user.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    json.put("name", user.getUserNickName());
                }
                json.put("sign", user.getUserSign());
                json.put("aliName", user.getUserAlipayName() == null ? "" : user.getUserAlipayName());
                json.put("aliAccount", user.getUserAlipayAccount() == null ? "" : user.getUserAlipayAccount());
                json.put("phone",
                        StringUtils.isEmpty(user.getUserPhone()) ? "" : user.getUserPhone().substring(0, 3) + "******"
                                + new StringBuilder(new StringBuilder(user.getUserPhone()).reverse().substring(0, 2))
                                .reverse());
                json.put("id", user.getUserId());
                json.put("label", StringUtils.isEmpty(user.getUserLabel()) == true ? "" : user.getUserLabel());

                String headUrl = user.getUserHeadPic();
                boolean headflag = false;
                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;
                } else {

                    headUrl = headUrl.split("\\.")[0];
                }
                // 加载数量配置
                Properties properties = new Properties();
                properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));
                if (headflag) {
                    json.put("url",
                            user.getUserHeadPic());
                } else {
                    json.put("url",
                            properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
                }


                json.put("count", userBlackListService.queryBlackListCountByUserId(user.getUserId()));
                json.put("contract", "true".equals(user.getUserContract()) == true ? "true" : "false");

                jsonObject.put("set", json);

                //安桌版本强制性更新
                jsonObject.put("version", "1.0.3");

                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            try {
                jedis.disconnect();
            } catch (Exception e) {

            }
            jsonObject.put("code", 206);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (

                Exception e)

        {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;

    }

    // 设置手机的比对验证码
    @ResponseBody
    @RequestMapping(value = "findUserPhone/{phone}/{code}")
    public JSONObject findUserPhone(@PathVariable("phone") String phone, @PathVariable("code") String code,
                                    HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (phone.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")
                    && code.matches("\\d{6}")) {
                User user = ouLiaoService.queryUserByPhone(phone);

                String codeFind = (String) sessions.getAttribute(phone);

                if (user != null || codeFind == null) {

                    jsonObject.put("code", 203);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return jsonObject;
                }

                if (code.equals(codeFind)) {
                    // 删除比对的验证码
                    sessions.removeAttribute(phone);
                    // 创建session
                    sessions = request.getSession();
                    sessions.setAttribute(phone, true);
                    sessions.setMaxInactiveInterval(60 * 3);
                    jsonObject.put("code", 200);

                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
                jsonObject.put("code", 204);
                response.setStatus(HttpServletResponse.SC_OK);

            } else {

                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;

    }

    // 设置手机
    @ResponseBody
    @RequestMapping(value = "modifyPhone/{phonePast}")
    public JSONObject modifyPhone(@PathVariable("phonePast") String phonePast,
                                  @RequestParam("phoneNew") String phoneNew, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (phonePast.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")
                    && phoneNew.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                User user = ouLiaoService.queryUserByPhone(phoneNew);

                if (user != null) {
                    jsonObject.put("code", 203);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
                if (sessions.getAttribute(phoneNew) == null) {
                    jsonObject.put("code", 211);

                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }

                user = ouLiaoService.queryUserByPhone(phonePast);
                if (user == null) {
                    jsonObject.put("code", 203);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }

                int num = ouLiaoService.updateUserPhoneByUserId(phoneNew, user.getUserId());

                if (num == 1) {
                    jsonObject.put("code", 200);
                } else {
                    jsonObject.put("code", 210);
                }
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;

    }

    //第三方登录
    @ResponseBody
    @RequestMapping(value = "accessByOther")
    public JSONObject accessByOther(@RequestParam("url") String url,
                                    @RequestParam(value = "thridId") String thridId, @RequestParam(value = "name") String
                                            name, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        String cusPass = Md5.MD5(System.currentTimeMillis() + "");

        cusPass = cusPass.substring(0, 8);

        try {

            if (StringUtils.isEmpty(thridId) || StringUtils.isEmpty(name)) {
                jsonObject.put("code", 202);
                return jsonObject;
            }

            User user = ouLiaoService.queryUserByThridId(thridId);
            jsonObject.put("code", 200);
            if (user != null) {
                //传送给前端加密的密码
                jsonObject.put("task", DesIosAndAndroid.encryptDES(user.getUserPass(), thridId));

            } else {
                if (!name.matches("[\u4e00-\u9fa5]+||[A-Za-z0-9]+")) {
                    name = "偶聊用户:" + System.currentTimeMillis();
                }
                // 存储数据
                user = new User();
                user.setThridId(thridId);
                //自动填写昵称，查询是否是重复
                User use = ouLiaoService.queryUserByNickName(name);
                int i = 0;
                if (use != null) {
                    do {
                        i++;
                        name = name + i;
                        use = ouLiaoService.queryUserByNickName(name);
                    } while (use != null);

                }
                // 默认填写昵称
                user.setUserNickName(name);
                user.setUserNum(UUID.randomUUID().toString());
                user.setUserCreateTime(new Date());
                user.setIsDeleted("0");
                if (StringUtils.isNotEmpty(url)) {
                    user.setUserHeadPic(url);
                }
                user.setUserPass(cusPass);
                //第三方登录自动设置密码
                user.setUserKey(Des16Util.encrypt(cusPass, user.getUserNum()));


                // 默认的签名和说说
                // 加载数量配置
                Properties properties = new Properties();
                properties.load(new InputStreamReader(
                        OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                        "utf-8"));

                user.setUserSign(properties.getProperty("signDefault"));

                ouLiaoService.regUser(user);

                User us = ouLiaoService.queryUserByThridId(thridId);

                // 默认设置有十分钟的通话时间
                ServiceRecordTime serviceRecordTime = new ServiceRecordTime();
                serviceRecordTime.setCreatTime(new Date());
                //serviceRecordTime.setUserCallTime(600l);
                serviceRecordTime.setUserCallTime(180l);
                serviceRecordTime.setIsSysSend("true");
                serviceRecordTime.setUserId(us.getUserId());
                serviceRecordTimeService.createServiceRecordTime(serviceRecordTime);
                // 发表说说
                UserSayContent userSayContent = new UserSayContent();
                userSayContent.setUserCreateTime(new Date());
                userSayContent.setIsDeleted("0");
                userSayContent.setUserId(us.getUserId());
                userSayContent.setUserContent(properties.getProperty("contentDefault"));
                userSayService.createUserSayContentByUserId(userSayContent);

                // 添加默认客服关注
                us = ouLiaoService.queryUserByPhone(properties.getProperty("ouliaoService"));
                if (us == null) {
                    // 设置默认客服
                    us = new User();
                    us.setUserPhone(properties.getProperty("ouliaoService"));
                    us.setUserNickName(properties.getProperty("ouliaoNickName"));
                    String uuid = UUID.randomUUID().toString();
                    us.setUserNum(uuid);
                    us.setUserCreateTime(new Date());
                    us.setIsDeleted("0");
                    us.setUserMoney(5000.0);
                    us.setUserCallCost(0.00);
                    us.setUserSign(properties.getProperty("ouliaoSign"));
                    String pass = DesIosAndAndroid.encryptDES(properties.getProperty("ouliaoPass"),
                            properties.getProperty("ouliaoService"));
                    us.setUserPass(pass);
                    us.setUserContract("true");
                    us.setUserKey(Des16Util.encrypt(pass, uuid));
                    us.setUserCallTime(properties.getProperty("ouliaoTime"));
                    us.setUserCallTimeWeek(properties.getProperty("callTime"));
                    ouLiaoService.regUser(us);

                    us = ouLiaoService.queryUserByPhone(properties.getProperty("ouliaoService"));
                }

                UserConcern userConcern = new UserConcern();
                userConcern.setUserOnfocusId(us.getUserId());
                userConcern.setUserId(user.getUserId());
                userConcern.setIsDeleted("0");
                userConcern.setUserCreateTime(new Date());
                userConcern.setUserModifyTime(new Date());
                if ("true".equals(us.getUserContract())) {
                    userConcern.setUserConract("true");
                } else {
                    userConcern.setUserConract("false");
                }


                userConcern = userConcernService.createUserConcern(userConcern, user, "service");
                //传送给前端加密的密码
                jsonObject.put("task", DesIosAndAndroid.encryptDES(cusPass, thridId));
            }


            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.put("code", 201);
        }
        return jsonObject;

    }

}
