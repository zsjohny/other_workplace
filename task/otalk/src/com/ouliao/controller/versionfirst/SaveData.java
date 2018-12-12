/**
 *
 */
package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.*;
import com.ouliao.domain.versionfirst.UserCommont;
import com.ouliao.repository.versionfirst.*;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionfirst.UserConcernService;
import com.xiaoluo.util.Des16Util;
import com.xiaoluo.util.DesIosAndAndroid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiaoluo
 * @version $Id: SaveData.java, 2016年2月19日 上午11:05:48
 */
@Controller
// @RequestMapping(value = "saveData", method = RequestMethod.POST)
@RequestMapping(value = "saveData")
public class SaveData {
    @Autowired
    private OuLiaoCrudRepository ouLiaoCrudRepository;
    @Autowired
    private UserSayContentCrudRepository userSayContentCrudRepository;
    @Autowired
    private UserSayContentRepository userSayContentRepository;
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserCommontCrudRepository userCommontCrudRepository;
    @Autowired
    private UserConcernPageRepository userConcernPageRepository;
    @Autowired
    private UserConcernService userConcernService;

    @Autowired
    private ServiceRecordTimePageRepository serviceRecordTimePageRepository;


    // 保存数据
    @ResponseBody
    @RequestMapping(value = "save", produces = "text/html;charset=utf-8")
    public String regUser(@RequestBody List<SaveDataEntity> saveDataEntitys, HttpServletRequest request,
                          HttpServletResponse response) {

        String status = "";
        try {
            if (saveDataEntitys == null || saveDataEntitys.size() == 0) {
                status = "失败...";
                return status;
            }
            List<User> list = new ArrayList<>();

            List<UserConcern> userConcerns = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (SaveDataEntity saveDataEntity : saveDataEntitys) {
                if (saveDataEntity == null) {
                    continue;
                }

                User user = new User();
                user.setUserCreateTime(simpleDateFormat.parse(saveDataEntity.getUserCreateTime()));
                user.setUserPhone(saveDataEntity.getUserPhone());
                user.setUserNickName(saveDataEntity.getUserNickName());
                user.setUserPass(saveDataEntity.getUserPass());
                user.setUserNum(UUID.randomUUID().toString());
                user.setUserAuth(saveDataEntity.getUserAuth());
                user.setUserContract("1".equals(saveDataEntity.getUserContract()) == true ? "true" : "false");
                user.setUserCallTotal(saveDataEntity.getUserCallTotal());
                user.setUserMoney(saveDataEntity.getUserMoney());
                // 主播默认登录
                user.setUserCallCost("1".equals(saveDataEntity.getUserContract()) == true ? 1.0 : null);
                user.setUserCallConsume(saveDataEntity.getUserCallConsume());
                user.setUserCallEarn(saveDataEntity.getUserCallEarn());
                user.setUserCallTimeWeek("1".equals(saveDataEntity.getUserContract()) == true
                        ? saveDataEntity.getUserCallTimeWeek() : null);
                user.setUserCallTime(
                        "1".equals(saveDataEntity.getUserContract()) == true ? saveDataEntity.getUserCallTime() : null);
                user.setIsDeleted("0");
                user.setIsRegister("1".equals(saveDataEntity.getUserContract()) == true ? "true" : "false");
                user.setUserLabel("1".equals(saveDataEntity.getUserContract()) == true ? "情感,生活,心理" : null);
                // 加载数量配置
                Properties properties = new Properties();
                properties.load(new InputStreamReader(
                        OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                        "utf-8"));

                // 默认生成签名
                user.setUserSign(properties.getProperty("signDefault"));

                // 设置加密程序
                user.setUserKey(Des16Util.encrypt(user.getUserPass(), user.getUserNum()));
                list.add(user);

            }
            if (ouLiaoCrudRepository.save(list) != null) {

                Iterable<User> lists = ouLiaoCrudRepository.findAll();
                List<ServiceRecordTime> serLits = new ArrayList<>();

                for (User us : lists) {
                    if (serviceRecordTimePageRepository.findOne(us.getUserId()) != null) {
                        continue;
                    }
                    // 默认设置有十分钟的通话时间
                    ServiceRecordTime serviceRecordTime = new ServiceRecordTime();
                    serviceRecordTime.setCreatTime(new Date());
                    //serviceRecordTime.setUserCallTime(600l);
                    serviceRecordTime.setUserCallTime(180l);
                    serviceRecordTime.setUserId(us.getUserId());
                    serviceRecordTime.setIsSysSend("true");
                    serLits.add(serviceRecordTime);
                }

                serviceRecordTimePageRepository.save(serLits);

                // 添加客服默认关注
                // 加载数量配置
                Properties properties = new Properties();
                properties.load(new InputStreamReader(
                        OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                        "utf-8"));
                User us = ouLiaoService.queryUserByPhone(properties.getProperty("ouliaoService"));
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
                // 添加默认关注的客服
                Iterable<User> findAll = ouLiaoCrudRepository.findAll();

                for (User u : findAll) {

                    if (u == null || u.getUserPhone().equals(properties.getProperty("ouliaoService"))) {

                        continue;
                    }

                    if (userConcernService.queryUserIsConcernById(us.getUserId(), us.getUserId()) != null) {
                        continue;
                    }

                    UserConcern userConcern = new UserConcern();
                    userConcern.setUserOnfocusId(us.getUserId());
                    userConcern.setUserId(u.getUserId());
                    userConcern.setIsDeleted("0");
                    userConcern.setUserCreateTime(new Date());
                    userConcern.setUserModifyTime(new Date());
                    userConcerns.add(userConcern);

                }

                userConcernPageRepository.save(userConcerns);

                status = "成功...";
            } else {
                status = "失败...";
            }
        } catch (Exception e) {
            status = "失败...";
        }

        return status;
    }

    // 保存头像
    @ResponseBody
    @RequestMapping(value = "saveHead", produces = "text/html;charset=utf-8")
    public String saveHead(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request,
                           HttpServletResponse response) {

        String status = "";
        try {

            if (files == null || files.size() == 0) {
                return "还没选择文件...";
            }

            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();

                String phone = fileName.split("\\.")[0];

                User user = ouLiaoService.queryUserByPhone(phone);

                if (user == null) {
                    continue;
                }

                //File saveFile = new File("D:\\ouliao\\user\\head\\" + user.getUserNum() + "\\");
                File saveFile = new File("/opt/ouliao/head/" + user.getUserNum() + "/");
                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }

                String name = System.currentTimeMillis() + ".jpg";
                ouLiaoService.updateHeadPicByUserNum(name, user.getUserNum());
                file.transferTo(new File(saveFile.getPath(), name));
                // 删除其他图片
                File[] fls = saveFile.listFiles();
                for (File deleFile : fls) {
                    if (deleFile.getName().equals(name)) {
                        continue;
                    }
                    System.gc();
                    deleFile.delete();

                }

            }

            status = "成功...";

        } catch (Exception e) {
            status = "失败...";
        }

        return status;
    }

    // 说说的转移
    @ResponseBody
    @RequestMapping(value = "saveSay", produces = "text/html;charset=utf-8")
    public String saveSay(@RequestBody List<SaveSayEntity> saveSayEntitys, HttpServletRequest request,
                          HttpServletResponse response) {

        String status = "";
        try {
            if (saveSayEntitys == null || saveSayEntitys.size() == 0) {
                status = "失败...";
                return status;
            }
            List<UserSayContent> list = new ArrayList<>();
            for (SaveSayEntity saveDataEntity : saveSayEntitys) {
                if (saveDataEntity == null) {
                    continue;
                }
                UserSayContent userSayContent = new UserSayContent();

                User user = ouLiaoService.queryUserByPhone(saveDataEntity.getPhone());
                if (user == null) {
                    continue;
                }
                userSayContent.setIsDeleted("0");
                userSayContent.setUserId(ouLiaoService.queryUserByPhone(saveDataEntity.getPhone()).getUserId());
                userSayContent.setUserCreateTime(new Date(Long.parseLong(saveDataEntity.getCreateTime())));
                userSayContent.setId(saveDataEntity.getId());
                userSayContent.setUserContent(saveDataEntity.getUserContent());
                list.add(userSayContent);
            }
            userSayContentCrudRepository.save(list);
            status = "成功...";
        } catch (Exception e) {
            status = "失败...";
        }

        return status;
    }

    // 评论的转移
    @ResponseBody
    @RequestMapping(value = "saveRemark", produces = "text/html;charset=utf-8")
    public String saveRemark(@RequestBody List<SaveRemarkEntity> saveSayEntitys, HttpServletRequest request,
                             HttpServletResponse response) {

        String status = "";
        try {
            if (saveSayEntitys == null || saveSayEntitys.size() == 0) {
                status = "失败...";
                return status;
            }
            List<UserCommont> list = new ArrayList<>();
            for (SaveRemarkEntity saveDataEntity : saveSayEntitys) {
                if (saveDataEntity == null) {
                    continue;
                }
                UserSayContent userSayContent = userSayContentRepository.querySayContentById(saveDataEntity.getId());

                if (userSayContent == null) {
                    continue;
                }
                User user = ouLiaoService.queryUserByPhone(saveDataEntity.getPhone());
                if (user == null) {
                    continue;
                }

                UserCommont userCommont = new UserCommont();
                userCommont.setIsDeleted("0");
                userCommont.setUserCreateTime(new Date(Long.parseLong(saveDataEntity.getCreateTime())));
                userCommont.setUserId(user.getUserId());
                userCommont.setUserCommont(saveDataEntity.getUserRemark());
                userCommont.setUserSayContentId(userSayContent.getUserSayContentId());
                list.add(userCommont);
            }
            userCommontCrudRepository.save(list);
            status = "成功...";
        } catch (Exception e) {
            status = "失败...";
        }

        return status;
    }

    // 关注的转移
    @ResponseBody
    @RequestMapping(value = "saveConcern", produces = "text/html;charset=utf-8")
    public String saveConcern(@RequestBody List<SaveConcernEntity> saveSayEntitys, HttpServletRequest request,
                              HttpServletResponse response) {

        String status = "";
        try {
            if (saveSayEntitys == null || saveSayEntitys.size() == 0) {
                status = "失败...";
                return status;
            }
            List<UserConcern> list = new ArrayList<>();
            for (SaveConcernEntity saveDataEntity : saveSayEntitys) {

                if (saveDataEntity == null) {
                    continue;
                }

                User userOn = ouLiaoService.queryUserByPhone(saveDataEntity.getUserPhone());
                if (userOn == null) {
                    continue;
                }
                User userOnfocus = ouLiaoService.queryUserByPhone(saveDataEntity.getUserOnfocusPhone());
                if (userOnfocus == null) {
                    continue;
                }

                UserConcern userConcern = new UserConcern();
                userConcern.setUserOnfocusId(userOnfocus.getUserId());
                userConcern.setUserId(userOn.getUserId());
                userConcern.setIsDeleted("0");
                userConcern.setUserCreateTime(new Date());
                userConcern.setUserModifyTime(new Date());
                list.add(userConcern);
            }
            userConcernPageRepository.save(list);
            status = "成功...";
        } catch (Exception e) {
            status = "失败...";
        }

        return status;
    }
}
