/**
 *
 */
package com.ouliao.util;

import com.ouliao.controller.versionfirst.OuLiaoSayController;
import com.ouliao.controller.versionfirst.UserCallMarkController;
import com.ouliao.domain.versionfirst.ServiceRecordTime;
import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserConcern;
import com.ouliao.domain.versionfirst.UserSayContent;
import com.ouliao.domain.versionsecond.UserDisconery;
import com.ouliao.repository.versionfirst.OuLiaoRepository;
import com.ouliao.repository.versionfirst.UserConcernRepository;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionfirst.ServiceRecordTimeService;
import com.ouliao.service.versionfirst.UserConcernService;
import com.ouliao.service.versionfirst.UserSayService;
import com.ouliao.service.versionsecond.UserDisconeryService;
import com.xiaoluo.util.Des16Util;
import com.xiaoluo.util.DesIosAndAndroid;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiaoluo
 * @version $Id: OuLiaoAdminController.java, 2016年2月22日 上午10:04:57
 */
@Controller
@RequestMapping(value = "user/admin")
public class OuLiaoAdminController {
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserSayService userSayService;
    @Autowired
    private ServiceRecordTimeService serviceRecordTimeService;
    @Autowired
    private OuLiaoRepository ouLiaoRepository;
    @Autowired
    private UserConcernService userConcernService;
    private static String FORWARD = "forward:";

    @Autowired
    private UserConcernRepository userConcernRepository;
    @Autowired
    private UserDisconeryService userDisconeryService;


    @RequestMapping(value = "userAdmin/login")
    public String login() {
        return "load";
    }

    // 展示管理页面
    @RequestMapping(value = "userAdmin/recommondDis")
    public String recommondDis(@RequestParam(value = "startCount", defaultValue = "1") Integer startCount,
                               HttpServletRequest request) {
        try {


            // 加载数量配置
            Properties properties = new Properties();
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

            Integer pageCount = Integer.valueOf(properties.getProperty("contarctUserCount"));

            Object object = ouLiaoService.queryUserIsContractByAll(startCount - 1, pageCount, "userOwnerOrder");

            List<User> list = new ArrayList<>();

            if (object instanceof Page) {
                Page<User> users = (Page) object;

                for (User us : users) {

                    list.add(us);

                }

            }

            JSONObject json = null;
            List<JSONObject> lists = new ArrayList<>();
            String url = properties.getProperty("downloadHead");
            for (User us : list) {
                json = new JSONObject();
                json.put("id", us.getUserId());
                json.put("name", us.getUserNickName());
                json.put("phone", us.getUserPhone());
                json.put("author", us.getUserAuth());
                json.put("order", us.getUserOwnerOrder() == null ? "" : us.getUserOwnerOrder());
				json.put("order", us.getUserOwnerOrder() == null ? "" : us.getUserOwnerOrder());
                String headUrl = us.getUserHeadPic();
                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else {
                    headUrl = headUrl.split("\\.")[0];
                }

                json.put("url", url + us.getUserId() + "/" + headUrl + "/head/download");

                lists.add(json);
            }
            request.setAttribute("count", ouLiaoService.queryUserContractCountByIsDeleted() / pageCount + 1);
            request.setAttribute("data", lists);
            request.setAttribute("isQuery", "false");
            request.setAttribute("startPage", startCount);
        } catch (Exception e) {
            request.setAttribute("msg", "网络加载失败！！");

        }
        return "recommondDis";
    }

    // 展示搜索页面
    @RequestMapping(value = "userAdmin/queryDis")
    public String queryDis(@RequestParam(value = "startCount", defaultValue = "1") Integer startCount,
                           @RequestParam("word") String word, HttpServletRequest request) {
        try {
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

            Integer pageCount = Integer.valueOf(properties.getProperty("contarctUserCount"));

            List<User> users = ouLiaoService.queryUserContractByUserNickNameOrUserAuth((startCount - 1), pageCount,
                    word);

            JSONObject json = null;
            List<JSONObject> lists = new ArrayList<>();

            String url = properties.getProperty("downloadHead");
            for (User us : users) {
                if (us == null) {
                    continue;
                }
                json = new JSONObject();
                json.put("id", us.getUserId());
                json.put("name", us.getUserNickName());
                json.put("phone", us.getUserPhone());
                json.put("author", us.getUserAuth());
                json.put("order", us.getUserOwnerOrder() == null ? "" : us.getUserOwnerOrder());
                String headUrl = us.getUserHeadPic();
                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else {
                    headUrl = headUrl.split("\\.")[0];
                }

                json.put("url", url + us.getUserId() + "/" + headUrl + "/head/download");

                lists.add(json);
            }
            Integer count = ouLiaoRepository.queryUserContractCountByUserNickNameOrUserAuth(word) / pageCount;
            request.setAttribute("count", count == null ? 1 : count + 1);
            request.setAttribute("isQuery", "true");
            request.setAttribute("data", lists);
            request.setAttribute("startPage", startCount);
        } catch (Exception e) {
            request.setAttribute("msg", "网络加载失败！！");

        }
        return "recommondDis";
    }

    // 进行推荐
    @RequestMapping(value = "userAdmin/recommond")
    public String recommond(@RequestParam("recommond") String recommond,
                            @RequestParam(value = "nowPage", defaultValue = "1") Integer startCount, HttpServletRequest request) {

        try {
            String isRecommond = recommond.split(",")[1];
            Integer id = Integer.parseInt(recommond.split(",")[0]);

            User user = ouLiaoService.queryUserByUserId(id);

            if (user == null) {
                request.setAttribute("msg", "账号不存在！！");
                return "recommondDis";
            }
            String isOrder = "";
            if ("0".equals(isRecommond)) {

                isOrder = "true";
            } else {
                isOrder = "false";
            }

            ouLiaoService.updateIsRecommondUserId(isOrder, id);

            request.setAttribute("msg", "推荐成功！！");

            return FORWARD + "/user/admin/userAdmin/recommondDis?startCount=" + startCount;

        } catch (Exception e) {
            request.setAttribute("msg", "网络加载失败！！");
            return FORWARD + "/user/admin/userAdmin/recommondDis?startCount=" + 1;
        }
    }

    @RequestMapping(value = "userAdmin/author")
    public String regUser(@RequestParam("phone") String phone,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "classicProgram", required = false) String classicPrograms,
                          @RequestParam(value = "listenProgram", required = false) String listenPrograms,
                          @RequestParam(value = "disPicUrls", required = false) MultipartFile[] multipartFiles,
                          @RequestParam(value = "file", required = false) MultipartFile headFile,
                          @RequestParam(value = "author", required = false) String author,
                          ModelMap modelMap) {
        User user = null;

        try {
            if (!phone.matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {
                modelMap.addAttribute("successMsg", "参数填写不正确!!");
                return "load";
            }
            // 加载数量配置
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"), "utf-8"));

            user = ouLiaoService.queryUserByPhone(phone);
            String callTimeWeek = properties.getProperty("callTime");

            String callTime = properties.getProperty("ouliaoTime");

            String userLabel = properties.getProperty("defaultLabel");


            String nameGreet = "";
            if (user != null) {
                if (user.getUserNickName()
                        .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                    nameGreet = user.getUserNickName().substring(0, 3) + "******"
                            + new StringBuilder(
                            new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                            .reverse();
                } else {
                    nameGreet = user.getUserNickName();
                }
            }


            String userGreet = "我是" + nameGreet + properties.getProperty("defaultUserGreet");
            if (user != null) {

                String auth = user.getUserAuth();

                String nickName = "";

                if (StringUtils.isEmpty(name)) {

                    if (StringUtils.isEmpty(user.getUserNickName())) {

                        nickName = user.getUserPhone();
                    } else {
                        nickName = user.getUserNickName();
                    }
                } else {
                    nickName = name;
                }
                // 防止替换用户设置的价格
                Double cost = 1.00;
                if (user.getUserCallCost() != null && user.getUserCallCost() != 0) {
                    cost = user.getUserCallCost();
                }
                if (StringUtils.isNotEmpty(user.getUserCallTimeWeek())) {
                    callTimeWeek = user.getUserCallTimeWeek();
                }
                if (StringUtils.isNotEmpty(user.getUserCallTime())) {
                    callTime = user.getUserCallTime();
                }


                if (StringUtils.isNotEmpty(user.getUserLabel())) {
                    userLabel = user.getUserLabel();
                }

                if (StringUtils.isNotEmpty(user.getUserGreet())) {
                    userGreet = user.getUserGreet();
                }

                if (StringUtils.isNotEmpty(author)) {
                    auth = author;
                }

                user.setUserGreet(properties.getProperty("defaultUserGreet"));

                ouLiaoService.updateUserAuthByUserPhone(userGreet, userLabel, callTimeWeek, callTime, nickName, auth, cost, phone);
                userConcernService.updateUserContractByUserOnfocuseId("true", user.getUserId());
            } else {

                user = new User();
                user.setUserPhone(phone);
                user.setUserNum(UUID.randomUUID().toString());
                user.setUserCreateTime(new Date());
                user.setUserAuth(author);
                // 统一费用
                user.setUserCallCost(1.00);
                user.setUserCallTimeWeek(callTimeWeek);
                user.setUserCallTime(callTime);
                user.setUserLabel(properties.getProperty("defaultLabel"));


                nameGreet = "";

                // 默认设置昵称
                String nickName = "";

                if (StringUtils.isEmpty(name)) {
                    nickName = phone;

                    nameGreet = phone.substring(0, 3) + "******"
                            + new StringBuilder(
                            new StringBuilder(new StringBuilder(phone).reverse().substring(0, 2))
                                    .reverse());

                } else {
                    nickName = name;
                    nameGreet = name;
                }
                user.setUserNickName(nickName);
                user.setUserContract("true");
                user.setIsDeleted("0");


                user.setUserGreet("我是" + nameGreet + properties.getProperty("defaultUserGreet"));


                // 默认生成签名
                user.setUserSign(properties.getProperty("signDefault"));

                if (!ouLiaoService.regUser(user)) {
                    modelMap.addAttribute("successMsg", "认证失败,数据不存在!!");
                    return "load";
                }

                User us = ouLiaoService.queryUserByPhone(phone);

                // 默认设置有十分钟的通话时间
                ServiceRecordTime serviceRecordTime = new ServiceRecordTime();
                serviceRecordTime.setCreatTime(new Date());
                //  serviceRecordTime.setUserCallTime(600l);
                serviceRecordTime.setUserCallTime(180l);
                serviceRecordTime.setUserId(us.getUserId());
                serviceRecordTime.setIsSysSend("true");
                serviceRecordTimeService.createServiceRecordTime(serviceRecordTime);

                // 默认发表说说
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
                userConcern = userConcernService.createUserConcern(userConcern, user, "service");


                //查询出刚认证的主播的信息
                user = ouLiaoService.queryUserByPhone(phone);


            }


            //第二版发现的增加


            if (StringUtils.isNotEmpty(classicPrograms) || StringUtils.isNotEmpty(listenPrograms) || multipartFiles.length > 1) {


                if (user == null) {
                    modelMap.addAttribute("successMsg", "认证失败!!");
                    return "load";
                }

                UserDisconery userDisconery = userDisconeryService.queryUserDisconveryByUserId(user.getUserId());

                String classicProgram = "";
                String listenProgram = "";

                //将传过来的值进行转换
                if (StringUtils.isNotEmpty(classicPrograms.trim())) {

                    if (classicPrograms.contains(" ")) {

                        for (String str : classicPrograms.split(" ")) {
                            if (StringUtils.isEmpty(str)) {
                                continue;
                            }
                            classicProgram += ",《" + str + "》";

                        }
                    } else {
                        classicProgram = "《" + classicPrograms.trim() + "》";

                    }

                } else {
                    classicProgram = classicPrograms.trim();
                }

                if (StringUtils.isNotEmpty(listenPrograms.trim())) {

                    if (classicPrograms.contains(" ")) {

                        for (String str : listenPrograms.split(" ")) {

                            if (StringUtils.isEmpty(str)) {
                                continue;
                            }
                            listenProgram += ",《" + str + "》";

                        }
                    } else {
                        listenProgram = "《" + listenPrograms.trim() + "》";
                    }

                } else {
                    listenProgram = listenPrograms.trim();
                }


                //再把包含，的中文注释去掉
                if (listenProgram.startsWith(",")) {
                    listenProgram = listenProgram.substring(1);
                }

                if (classicProgram.startsWith(",")) {
                    classicProgram = classicProgram.substring(1);
                }


                if (userDisconery != null) {
                    userDisconery.setCreatTime(new Date());
                    classicProgram = StringUtils.isEmpty(classicProgram) ? userDisconery.getClassicProgram() : classicProgram;
                    listenProgram = StringUtils.isEmpty(listenProgram) ? userDisconery.getListenProgram() : listenProgram;

                    String picurl = userDisconery.getDisPicUrls();


                    //存储图片
                    if (multipartFiles != null) {

                        String path = properties.getProperty("defaultDiscoveryUrl") + File.separator + user.getUserNum();
                        File file = new File(path);

                        if (!file.exists()) {
                            file.mkdirs();

                        } else {
                            for (File fe : file.listFiles()) {
                                System.gc();
                                fe.delete();
                            }

                        }
                        picurl = "";

                        String picname = "";
                        for (MultipartFile multipartFile : multipartFiles) {

                            if (multipartFile == null || multipartFile.getSize() == 0) {
                                continue;
                            }
                            String picpath = System.currentTimeMillis() + ".jpg";
                            file = new File(path + File.separator + picpath);
                            if (!file.exists()) {
                                file.mkdirs();
                            }


                            multipartFile.transferTo(file);
                            Thread.sleep(1000);
                            picname += picpath + ",";
                            picurl = picname;
                        }


                    }


                    userDisconeryService.updateUserDiscoveryByUserId(classicProgram, picurl, listenProgram, user.getUserId());

                } else {

                    userDisconery = new UserDisconery();

                    userDisconery.setCreatTime(new Date());
                    userDisconery.setUserId(user.getUserId());
                    userDisconery.setIsDeleted("0");
                    userDisconery.setClassicProgram(StringUtils.isEmpty(classicProgram) ? "" : classicProgram);
                    userDisconery.setListenProgram(StringUtils.isEmpty(listenProgram) ? "" : listenProgram);
                    userDisconery.setIsContract("true");


                    //存储图片
                    if (multipartFiles != null) {

                        String path = properties.getProperty("defaultDiscoveryUrl") + File.separator + user.getUserNum();
                        File file = new File(path);

                        if (!file.exists()) {
                            file.mkdirs();

                        } else {
                            for (File fe : file.listFiles()) {
                                System.gc();
                                fe.delete();
                            }

                        }

                        String picname = "";
                        for (MultipartFile multipartFile : multipartFiles) {
                            if (multipartFile == null || multipartFile.getSize() == 0) {
                                continue;
                            }
                            String picpath = System.currentTimeMillis() + ".jpg";
                            file = new File(path + File.separator + picpath);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            multipartFile.transferTo(file);
                            Thread.sleep(1000);
                            picname += picpath + ",";
                            userDisconery.setDisPicUrls(picname);
                        }


                    }

                    userDisconeryService.saveUserDiconvery(userDisconery);

                }
            }


            //主播默认头像的上传
            if (headFile != null && headFile.getSize() != 0) {

                user = ouLiaoService.queryUserByPhone(phone);

                // File saveFile = new File("D:\\ouliao\\user\\head\\" + user.getUserNum() + File.separator);
                File saveFile = new File("/opt/ouliao/head/" + user.getUserNum() + File.separator);

                name = System.currentTimeMillis() + ".jpg";

                if (saveFile.exists()) {


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

                } else {
                    saveFile.mkdirs();
                }
                ouLiaoService.updateHeadPicByUserNum(name, user.getUserNum());
                headFile.transferTo(new File(saveFile.getPath(), name));

            }
            modelMap.addAttribute("successMsg", "认证成功!!");
        } catch (Exception e) {
            modelMap.addAttribute("successMsg", "认证失败,网络请求失败!!");
        }
        return "load";
    }

    // 上传配置
    @RequestMapping(value = "userAdmin/uploadPro")
    public String uploadPro(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(
                    UserCallMarkController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));

            File fileSave = new File(properties.getProperty("msgPath"));

            if (fileSave.exists()) {

                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "utf-8"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Properties prop = new Properties();
                prop.load(new BufferedInputStream(new FileInputStream(fileSave)));
                byte[] b = new byte[1024];

                String str = "";
                int count = prop.size() + 1;
                while ((str = br.readLine()) != null) {
                    count++;
                    prop.setProperty(count + "",
                            new String(str.getBytes("utf-8"), "utf-8") + "&" + simpleDateFormat.format(new Date()));

                }
                OutputStream os = new FileOutputStream(fileSave);
                prop.store(os, "xiaoluo");
                os.close();
                System.gc();
            } else {

                file.transferTo(new File(properties.getProperty("msgPath")));
            }
            modelMap.addAttribute("successMsg", "上传成功!!");
        } catch (Exception e) {
            modelMap.addAttribute("successMsg", "上传失败,网络请求失败!!");
        }
        return "load";
    }


    //取消认证
    @RequestMapping(value = "userAdmin/cancelAuthor")
    public String cancelAuthor(@RequestParam("id") Integer id,
                               @RequestParam(value = "nowPage", defaultValue = "1") Integer startCount, HttpServletRequest request) {

        try {


            User user = ouLiaoService.queryUserByUserId(id);

            if (user == null) {
                request.setAttribute("msg", "账号不存在！！");
                return "recommondDis";
            }

            ouLiaoService.updateUserAuthorAndUserContractByUserId(user.getUserId());
            userConcernService.updateUserContractByUserOnfocuseId("false", user.getUserId());

            //把相应的发现干掉
            userDisconeryService.cancelUserContractByUserId(user.getUserId());


            request.setAttribute("msg", "取消认证成功！！");

            return FORWARD + "/user/admin/userAdmin/recommondDis?startCount=" + startCount;

        } catch (Exception e) {
            request.setAttribute("msg", "网络加载失败！！");
            return FORWARD + "/user/admin/userAdmin/recommondDis?startCount=" + 1;
        }
    }


}
