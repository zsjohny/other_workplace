/**
 *
 */
package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.*;
import com.ouliao.domain.versionsecond.UserVisitRecord;
import com.ouliao.repository.versionfirst.UserConcernRepository;
import com.ouliao.service.versionfirst.*;
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author xiaoluo
 * @version $Id: OuLiaoCommontController.java, 2016年2月19日 下午6:56:54
 */
@Controller
@RequestMapping(value = "user/say", method = RequestMethod.POST)
public class OuLiaoSayController {
    @Autowired
    private UserSayService userSayService;
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserConcernService userConcernService;
    @Autowired
    private UserBlackListService userBlackListService;
    @Autowired
    private UserConcernRepository userConcernRepository;
    @Autowired
    private UserVistRecordService userVistRecordService;

    @Autowired
    private HuanXinService huanXinService;
    private JedisPool jedisPool = new JedisPool("localhost", 10088);

    private Jedis jedis = jedisPool.getResource();


    // 产生评论
    @ResponseBody
    @RequestMapping(value = "commont/{uid}/{sayId}/createCommont")
    public JSONObject createCommont(@PathVariable("uid") String uid, @PathVariable("sayId") Integer sayId,
                                    @RequestParam("commont") String commont, @RequestParam("key") String key,
                                    @RequestParam(value = "replyId", required = false) Integer replyId, HttpServletRequest request,
                                    HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(commont) || commont.length() > 1000) {
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

            // 检测sayId(说说)是否存在
            UserSayContent userSayContent = userSayService.querySayContentByUserSayContentId(sayId);

            if (userSayContent == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // 是否在黑名单---互相查询
            UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(user.getUserId(),
                    userSayContent.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 217);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            userBlackList = userBlackListService.queryUserIsBlackListById(userSayContent.getUserId(), user.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 218);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            UserCommont userCommont = new UserCommont();
            userCommont.setIsDeleted("0");
            Date date = new Date();
            userCommont.setUserCreateTime(date);
            userCommont.setUserId(user.getUserId());
            userCommont.setUserCommont(commont);
            userCommont.setUserSayContentId(sayId);
            //第二版内容
            userCommont.setUserContractId(userSayContent.getUserId());
            userCommont.setIsReader("false");
            if (replyId != null) {
                userCommont.setReplyUserId(replyId);
            }

            if (userSayService.createUserCommontByUserId(userCommont)) {
                jsonObject.put("code", 200);

                // 安卓端返回评论的内容和评论的信息

                JSONObject json = new JSONObject();
                json.put("replyId", user.getUserId());
                json.put("replyCommontId", replyId == null ? "0" : replyId);

                if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                    json.put("replyNmae",
                            user.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    json.put("replyNmae", user.getUserNickName());
                }

                Properties properties = new Properties();
                properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

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
                    json.put("replyUrl", user.getUserHeadPic());
                } else {
                    json.put("replyUrl",
                            properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
                }
                json.put("replyIsContract", "true".equals(user.getUserContract()) == true ? "true" : "false");

                json.put("replyCommont", commont);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 ahh:mm");
                json.put("replyTime", simpleDateFormat.format(date));
                jsonObject.put("data", json);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 查看评论--查看某条说说的所有评论
    @ResponseBody
    @RequestMapping(value = "commont/{uid}/{sayId}/{startPage}/viewCommontAll")
    public JSONObject viewCommontAll(@PathVariable("uid") String uid, @PathVariable("sayId") Integer sayId,
                                     @PathVariable("startPage") Integer startPage, @RequestParam("key") String key,
                                     @RequestParam(value = "tourist", required = false) String tourist, HttpServletRequest request,
                                     HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        try {

            if (StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            User userOwner = null;
            if (StringUtils.isEmpty(tourist)) {

                // 登录检测
                userOwner = ouLiaoService.queryUserByUserNum(uid);
                if (request.getSession().getAttribute(key) == null || userOwner == null) {
                    jsonObject.put("code", 208);
                    if (userOwner != null) {
                        jsonObject.put("cid", userOwner.getUserId());
                    }

                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
                String userNum = DesUtil.decrypt(key, userOwner.getCurrentTime());
                if (!userOwner.getUserNum().equals(userNum)) {
                    jsonObject.put("code", 209);

                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }

            } else {
                userOwner = new User();
                userOwner.setUserId(0);
            }

            // 检测sayId(说说)是否存在
            UserSayContent userSayContent = userSayService.querySayContentByUserSayContentId(sayId);
            if (userSayContent == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            Properties properties = new Properties();
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

            Integer pageCount = Integer.valueOf(properties.getProperty("sayCommontStartCount"));

            Page<UserCommont> userCommonts = userSayService.querySayCommontAllByUserSayContentId(startPage - 1,
                    pageCount, sayId);
            int supportCount = userSayService.querySupportCountById(sayId);

            JSONArray jsonArray = new JSONArray();

            // 2016-02-20 16:37:30
            SimpleDateFormat simpleDatePast = new SimpleDateFormat("yy年MM月dd日 ahh:mm");
            SimpleDateFormat simpleDateNow = new SimpleDateFormat("MM月dd日 ahh:mm");

            Calendar calendarPast = Calendar.getInstance();
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.setTime(new Date());
            // 说说时间显示
            calendarPast.setTime(userSayContent.getUserCreateTime());
            int year = calendarNow.get(Calendar.YEAR) - calendarPast.get(Calendar.YEAR);

            switch (year) {
                case 0:
                    int month = calendarNow.get(Calendar.MONTH) - calendarPast.get(Calendar.MONTH);
                    switch (month) {
                        case 0:
                            int day = calendarNow.get(Calendar.DAY_OF_MONTH) - calendarPast.get(Calendar.DAY_OF_MONTH);
                            switch (day) {
                                case 0:
                                    int hour = calendarNow.get(Calendar.HOUR_OF_DAY) - calendarPast.get(Calendar.HOUR_OF_DAY);
                                    switch (hour) {
                                        case 0:
                                            jsonObject.put("conTime",
                                                    calendarNow.get(Calendar.MINUTE) - calendarPast.get(Calendar.MINUTE) + "分钟前");
                                            break;

                                        default:
                                            jsonObject.put("conTime", hour + "小时前");
                                    }
                                    break;
                                default:
                                    jsonObject.put("conTime", day + "天前");
                            }

                            break;
                        default:
                            jsonObject.put("conTime", month + "月前");
                    }

                    break;

                case 1:
                    month = calendarNow.get(Calendar.MONTH) + 12 - calendarPast.get(Calendar.MONTH);
                    jsonObject.put("conTime", month + "月前");
                    break;
                default:
                    jsonObject.put("conTime", year + "年前");
            }
            // 根据uid查询用户基本信息
            User user = ouLiaoService.queryUserByUserId(userSayContent.getUserId());

            if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                jsonObject
                        .put("nickNmae",
                                user.getUserNickName().substring(0, 3) + "******"
                                        + new StringBuilder(
                                        new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                        .reverse());
            } else {
                jsonObject.put("nickNmae", user.getUserNickName());
            }

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
                jsonObject.put("url",
                        properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
            }
            jsonObject.put("content", userSayContent.getUserContent());
            jsonObject.put("isContract", "true".equals(user.getUserContract()) == true ? "true" : "false");
            jsonObject.put("count", supportCount);


            //第二版增加的内容
            jsonObject.put("picurl", StringUtils.isEmpty(userSayContent.getUserPicUrls()) ? "" : userSayContent.getUserPicUrls());
            jsonObject.put("title", StringUtils.isEmpty(userSayContent.getUserSayContentSubject()) ? "" : userSayContent.getUserSayContentSubject());


            UserSupportSay usc = userSayService.querySupportUniqueById(userOwner.getUserId(),
                    userSayContent.getUserSayContentId());

            if (usc == null) {
                jsonObject.put("isSupport", "false");
            } else {
                jsonObject.put("isSupport", "true");
            }

            // 展示用户点赞的id和头像和是否是主播
            List<UserSupportSay> lists = userSayService
                    .querySupporIsDeletedByUserId(userSayContent.getUserSayContentId());
            JSONArray array = new JSONArray();
            JSONObject js = null;
            int count = 0;
            if (lists != null && lists.size() != 0) {
                js = new JSONObject();
                for (UserSupportSay uss : lists) {

                    if (uss == null) {
                        continue;
                    }

                    User us = ouLiaoService.queryUserByUserId(uss.getUserId());
                    if (us == null) {
                        continue;
                    }

                    js.put("supId", us.getUserId());

                    headflag = false;
                    headUrl = us.getUserHeadPic();
                    if (StringUtils.isEmpty(headUrl)) {
                        headUrl = "985595";
                    } else if (headUrl.contains("//")) {
                        headflag = true;

                    } else {
                        headUrl = headUrl.split("\\.")[0];
                    }
                    if (headflag) {
                        js.put("supHeadUrl", us.getUserHeadPic());
                    } else {
                        js.put("supHeadUrl",
                                properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl + "/head/download");
                    }
                    js.put("supIsContract", "true".equals(us.getUserContract()) == true ? "true" : "false");
                    array.add(js);
                    if (count++ == 100) {
                        break;
                    }
                }

            }
            jsonObject.put("supportLists", array);
            // 评价用户的显示

            // JSONObject jsonReply = null;
            for (UserCommont userCommont : userCommonts) {
                if (userCommont == null) {
                    continue;
                }

                JSONObject json = new JSONObject();
                user = ouLiaoService.queryUserByUserId(userCommont.getUserId());
                if (user == null) {
                    continue;
                }
                // //这是回复者的评论的回复，暂时取消
                // if (userCommont.getReplyUserId() != null) {
                //
                // jsonReply = new JSONObject();
                // user =
                // ouLiaoService.queryUserByUserId(userCommont.getUserId());
                //
                // if (user.getUserNickName()
                // .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$"))
                // {
                //
                // jsonReply.put("rNickNmae",
                // user.getUserNickName().substring(0, 3) + "******"
                // + new
                // StringBuilder(user.getUserNickName()).reverse().substring(0,
                // 2));
                // } else {
                // jsonReply.put("rNickNmae", user.getUserNickName());
                // }
                //
                // jsonReply.put("rId", user.getUserId());
                // jsonReply.put("rIsContract",
                // "true".equals(user.getUserContract()) == true ? "true" :
                // "false");
                // jsonReply.put("rUrl", properties.getProperty("headUrl") +
                // user.getUserId() + "/"
                // + user.getUserHeadPic() + "/head/download");
                // jsonReply.put("rCommontId", userCommont.getUserCommontId());
                // user =
                // ouLiaoService.queryUserByUserId(userCommont.getReplyUserId());
                //
                // if (user.getUserNickName()
                // .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$"))
                // {
                //
                // jsonReply.put("rSendName",
                // user.getUserNickName().substring(0, 3) + "******"
                // + new
                // StringBuilder(user.getUserNickName()).reverse().substring(0,
                // 2));
                // } else {
                // jsonReply.put("rSendName", user.getUserNickName());
                // }
                // jsonReply.put("rSendId", user.getUserId());
                //
                // json.put("reply", jsonReply);
                //
                // }
                // if
                // (userSayContent.getUserId().equals(userCommont.getUserId()))
                // {
                // // json.put("uid", 0);//--状态改成我取消掉
                // json.put("uid", user.getUserId());// --状态改成我取消掉
                // json.put("ownerUrl", properties.getProperty("headUrl") +
                // user.getUserId() + "/"
                // + user.getUserHeadPic() + "/head/download");
                // json.put("oCommontId", userCommont.getUserCommontId());
                // json.put("oIsContract", "true".equals(user.getUserContract())
                // == true ? "true" : "false");
                // } else {
                // json.put("uid", userCommont.getUserId());
                // json.put("oIsContract", "true".equals(user.getUserContract())
                // == true ? "true" : "false");
                // json.put("oCommontId", userCommont.getUserCommontId());
                // json.put("ownerUrl", properties.getProperty("headUrl") +
                // user.getUserId() + "/"
                // + user.getUserHeadPic() + "/head/download");
                //
                // }
                // if
                // (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$"))
                // {
                //
                // json.put("uNickNmae", user.getUserNickName().substring(0, 3)
                // + "******"
                // + new
                // StringBuilder(user.getUserNickName()).reverse().substring(0,
                // 2));
                // } else {
                // json.put("uNickNmae", user.getUserNickName());
                // }

                // 返回参数
                /*--返回参数:json
                {"code":200,"isSupport":"true|false",isContract":"true|false",url":"data0","conTime":"data1","nickNmae":"data2","id":"data3","content":"data4","count":"data5","data":[{uid":"data6","ownerUrl":"data6-1","oCommontId":"评论的id","oIsContract":"true|false","uNickNmae":"data7","time":"data8","commont":"data9","reply":{rNickNmae":"data10","rCommontId":"评论的id","rId":"data11",rUrl:"data12","rIsContract":"true|false","rSendId":"被回复@符号的Id"，"rSendName":"被回复@符号的昵称"}}]

				"supportLists":{"supId":"data13","supHeadUrl":"data14",,"supIsContract":"data15"}
				} 代表查看评论成功,

				isSupport是否是点赞过该说说，为true是指点赞,flase没有点赞  isContract为true代表是主播 false不是主播   data0 代表发表说说人的头像预览 data1表示说说发表的时间(格式已经转换好，直接显示出来就可以), data2表示发表说说人的昵称,data3是发表说说的id,data4是用户发表的内容,data5是该条说说的支持
				      data6是用户评价的id(如果是用户自己的，则id是0),  oIsContract是回复者的信息 为true代表是主播 false不是主播  ,data6-1代表用户评价人的头像预览 data7是用户评价的昵称, data8是用户的时间(已经做好直接显示),data9是用户评价的内容,data10是回复给谁的昵称，有可能没有(没有是否需要我传这个参数，只不过是传空值，到时候跟我说下，暂时没有我就没传) data11是对回复人的id，跟data10连起来   data12是代表回复人头像预览地址  rIsContract是回复人的信息 为true代表是主播 false不是主播
				      reply 代表是回复人的信息
				      supportLists代表点赞的人的所有信息  data13代表点赞的人的id，data14代表点赞的头像的url,data15代表是否是主播
				      返回状态值为200*/

                // ------------------修改的对象------------------------------------------

                if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                    json.put("replyNmae",
                            user.getUserNickName().substring(0, 3) + "******"
                                    + new StringBuilder(
                                    new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                    .reverse());
                } else {
                    json.put("replyNmae", user.getUserNickName());
                }
                json.put("replyId", userCommont.getUserId());
                json.put("replyCommontId",
                        userCommont.getUserCommontId() == null ? "0" : userCommont.getUserCommontId());
                User us = ouLiaoService.queryUserByUserId(userCommont.getUserId());

                headflag = false;
                headUrl = us.getUserHeadPic();
                if (StringUtils.isEmpty(headUrl)) {
                    headUrl = "985595";
                } else if (headUrl.contains("//")) {
                    headflag = true;

                } else {
                    headUrl = headUrl.split("\\.")[0];
                }
                if (headflag) {
                    json.put("replyUrl", us.getUserHeadPic());
                } else {
                    json.put("replyUrl",
                            properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl + "/head/download");
                }
                // ----------------------------------------------------------------------

                calendarPast.setTime(userCommont.getUserCreateTime());

                if (calendarNow.get(Calendar.YEAR) - calendarPast.get(Calendar.YEAR) == 0) {
                    json.put("replyTime", simpleDateNow.format(userCommont.getUserCreateTime()));
                } else {
                    json.put("replyTime", simpleDatePast.format(userCommont.getUserCreateTime()));
                }
                json.put("replyIsContract", "true".equals(us.getUserContract()) == true ? "true" : "false");

                String returnCommoent = URLDecoder.decode(userCommont.getUserCommont(), "utf-8");

                // 增加@符号的筛选
                if (userCommont.getReplyUserId() != null) {
                    User userfind = ouLiaoService.queryUserByUserId(userCommont.getReplyUserId());

                    String nickName = "";
                    if (userfind.getUserNickName()
                            .matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                        nickName = userfind.getUserNickName().substring(0, 3) + "******"
                                + new StringBuilder(
                                new StringBuilder(userfind.getUserNickName()).reverse().substring(0, 2))
                                .reverse();
                    } else {
                        nickName = userfind.getUserNickName();
                    }

                    String replyStart = "";
                    if (returnCommoent.contains(":")) {
                        replyStart = returnCommoent.substring(0, returnCommoent.indexOf(":") + 1);
                    } else if (returnCommoent.contains("：")) {
                        replyStart = returnCommoent.substring(0, returnCommoent.indexOf("：") + 1);
                    }


                    if (replyStart.contains("*")) {
                        replyStart = replyStart.replace("*", "\\*");
                    }

                    returnCommoent = returnCommoent.replaceFirst(replyStart, "回复@" + nickName + "：");

                } else {
                    //不能及时检索名称的问题，解决安桌的问题
                    returnCommoent = returnCommoent.replaceFirst(":", "：");
                }

                json.put("replyCommont", URLEncoder.encode(returnCommoent, "utf-8"));
                jsonArray.add(json);
            }
            jsonObject.put("data", jsonArray);
            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (

                Exception e)

        {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;

    }

    // 删除评论
    @ResponseBody
    @RequestMapping(value = "commont/{uid}/{commontId}/deleteCommont")
    public JSONObject deleteCommont(@PathVariable("uid") String uid, @PathVariable("commontId") Integer commontId,
                                    @RequestParam(value = "sayId", required = false) Integer sayId, @RequestParam("key") String key,
                                    HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
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
            // 检测sayId(说说)是否存在 或者 commontId是否存在
            Boolean flag = false;
            if (commontId == null) {
                if (userSayService.querySayContentByUserSayContentId(sayId) == null) {
                    flag = true;
                }

            } else {
                if (userSayService.querySayCommontOneByUserCommontId(commontId) == null) {
                    flag = true;
                }

            }

            if (flag) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            int count = 0;

            if (userSayService.querySayContentUniqueById(user.getUserId(), sayId) == null) {
                count = userSayService.deleteCommontById(commontId, user.getUserId(), 0);
            } else {
                // 说说拥有者删除其下所有评论
                count = userSayService.deleteCommontById(commontId, 0, sayId);
            }
            if (count == 1) {
                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 产生说说
    @ResponseBody
    @RequestMapping(value = "content/{uid}/createSayContent")
    public JSONObject createSayContent(@PathVariable("uid") String uid, @RequestParam(value = "content", required = false) String content,
                                       @RequestParam(value = "picurl", required = false) String picurl, @RequestParam(value = "subject", required = false) String subject, @RequestParam("key") String key, @RequestParam(value = "versionTwo", required = false) String version, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(version)) {
                if (StringUtils.isEmpty(key) || StringUtils.isEmpty(content) || content.length() > 2014) {
                    jsonObject.put("code", 202);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
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

            UserSayContent userSayContent = new UserSayContent();
            userSayContent.setUserCreateTime(new Date());
            userSayContent.setIsDeleted("0");
            userSayContent.setUserId(user.getUserId());

            userSayContent.setUserContent(StringUtils.isEmpty(content) ? "" : content);

            if (StringUtils.isNotEmpty(version)) {


                userSayContent.setUserSayContentSubject(StringUtils.isEmpty(subject) ? "" : subject);
                userSayContent.setUserPicUrls(StringUtils.isEmpty(picurl) ? "" : picurl);
            }

            if (userSayService.createUserSayContentByUserId(userSayContent)) {
                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 删除说说
    @ResponseBody
    @RequestMapping(value = "content/{uid}/{sayId}/deleteSayContent")
    public JSONObject deleteSayContent(@PathVariable("uid") String uid, @PathVariable("sayId") Integer sayId,
                                       @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
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

            List<UserSayContent> list = userSayService.querySayContentByUserId(user.getUserId());

            if (list == null || list.size() == 0) {
                jsonObject.put("code", 212);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            int count = userSayService.deleteUserSayContentByUserId(user.getUserId(), sayId);
            if (count == 1) {

                // 删除该条说说的所有评论
                List<UserCommont> lists = userSayService.querySayCommontAllByUserSayContentId(sayId);

                if (lists != null && lists.size() != 0) {
                    for (UserCommont userCommont : lists) {
                        userSayService.deleteCommontAllByUserCommontId(userCommont.getUserCommontId());
                    }
                }

                jsonObject.put("code", 200);
                response.setStatus(HttpServletResponse.SC_OK);

            } else {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 查看说说--查看某用户的所有说说
    @ResponseBody
    @RequestMapping(value = "conntent/{uid}/{id}/{startPage}/viewConntentAll")
    public JSONObject viewConntentAll(@PathVariable("uid") String uid, @PathVariable("id") Integer id,
                                      @PathVariable("startPage") Integer startPage,
                                      @RequestParam(value = "tourist", required = false) String tourist, HttpServletRequest request,
                                      @RequestParam(value = "versionTwo", required = false) String version, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {

            // 检测sayId(说说)是否存在
            User user = null;
            if (uid.matches("\\d+")) {
                user = ouLiaoService.queryUserByUserId(Integer.parseInt(uid));
            } else {
                user = ouLiaoService.queryUserByUserNum(uid);
            }

            if (StringUtils.isNotEmpty(tourist)) {
                user = new User();
                user.setUserId(0);
            }

            if (user == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }


            //增加足迹
            try {
                if (!id.equals(user.getUserId())) {

                    //增加重复刷新的功能
                    try {

                        if (!jedis.exists("user:" + user.getUserId() + "uservisit:" + id)) {

                            UserVisitRecord userVisitRecord = new UserVisitRecord();
                            userVisitRecord.setUserId(id);
                            userVisitRecord.setCreatTime(new Date());
                            userVisitRecord.setIsDeleted("0");
                            userVisitRecord.setIsReader("false");
                            userVisitRecord.setVisitUserId(user.getUserId());

                            userVistRecordService.saveUserVisitRecord(userVisitRecord);


                            //增加重复刷新的功能
                            try {
                                jedis.setex("user:" + user.getUserId() + "uservisit:" + id, 60, "true");

                                jedis.disconnect();
                            } catch (Exception e) {

                            }

                        }


                    } catch (Exception e) {

                    }


                }
            } catch (Exception e) {

            }


            Properties properties = new Properties();
            properties.load(OuLiaoSayController.class.getClassLoader().getResourceAsStream("paramsSet.properties"));

            Integer pageCount = Integer.valueOf(properties.getProperty("sayContentStartCount"));

            Page<UserSayContent> userSayContents = userSayService.querySayContentAllIsDeletedByUserId(startPage - 1,
                    pageCount, user.getUserId());

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


                //第二版内容
                if (StringUtils.isNotEmpty(version)) {

                    // 展示用户点赞的id和头像和是否是主播
                    List<UserSupportSay> lists = userSayService
                            .querySupporIsDeletedByUserId(userSayContent.getUserSayContentId());
                    JSONArray array = new JSONArray();
                    JSONObject jsSupport = null;
                    int count = 0;
                    if (lists != null && lists.size() != 0) {
                        jsSupport = new JSONObject();
                        for (UserSupportSay uss : lists) {

                            if (uss == null) {
                                continue;
                            }

                            User us = ouLiaoService.queryUserByUserId(uss.getUserId());
                            if (us == null) {
                                continue;
                            }

                            jsSupport.put("supId", us.getUserId());

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
                                jsSupport.put("supHeadUrl", us.getUserHeadPic());
                            } else {
                                jsSupport.put("supHeadUrl",
                                        properties.getProperty("headUrl") + us.getUserId() + "/" + headUrl + "/head/download");
                            }
                            jsSupport.put("supIsContract", "true".equals(us.getUserContract()) == true ? "true" : "false");
                            array.add(jsSupport);
                            if (count++ == 100) {
                                break;
                            }
                        }

                    }


                    json.put("subject", StringUtils.isEmpty(userSayContent.getUserSayContentSubject()) ? "" : userSayContent.getUserSayContentSubject());

                    json.put("picurl", StringUtils.isEmpty(userSayContent.getUserPicUrls()) ? "" : userSayContent.getUserPicUrls());

                    json.put("supportLists", array);


                }

                jsonArray.add(json);


            }
            // 根据uid查询用户基本信息---json放入一个表
            JSONObject js = new JSONObject();

            if (user.getUserNickName().matches("^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$")) {

                js.put("nickNmae",
                        user.getUserNickName().substring(0, 3) + "******"
                                + new StringBuilder(new StringBuilder(user.getUserNickName()).reverse().substring(0, 2))
                                .reverse());
            } else {
                js.put("nickNmae", user.getUserNickName());
            }

            js.put("id", user.getUserId());

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
                js.put("url", user.getUserHeadPic());
            } else {
                js.put("url", properties.getProperty("headUrl") + user.getUserId() + "/" + headUrl + "/head/download");
            }
            js.put("isContract", "true".equals(user.getUserContract()) == true ? "true" : "false");

            // 是否在黑名单--单项查看--如果是自己就不用返回
            if (!user.getUserId().equals(id)) {
                UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(id, user.getUserId());
                if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                    js.put("isBlack", "true");

                } else {
                    js.put("isBlack", "false");
                }
                UserConcern isConcern = userConcernService.queryUserIsConcernById(id, user.getUserId());
                if (isConcern == null || "1".equals(isConcern.getIsDeleted())) {

                    js.put("isConCern", "false");
                } else {
                    js.put("isConCern", "true");
                }

            }
            if ("true".equals(user.getUserContract())) {

                js.put("auth", StringUtils.isEmpty(user.getUserAuth()) ? "" : user.getUserAuth());
            }
            js.put("sign", StringUtils.isEmpty(user.getUserSign()) ? "" : user.getUserSign());
            // 人气和关注暂时没写--已经补充

            js.put("concern", userConcernService.queryUserConcernByUserId(user.getUserId()));

            // 拦截偶聊客服关注的搜索
            if (properties.get("ouliaoService").equals(user.getUserPhone())) {
                js.put("concerned", "9999");

                js.put("service", true);
            } else {

//                if ("true".equals(user.getUserContract())) {
//
//                    Integer k = userConcernService.queryUserConcerneAlldByUserConcernId(user.getUserId());
//                    js.put("concerned", k);
//
//                } else {
//                    js.put("concerned", userConcernService.queryUserConcernedByUserOnfocusId(user.getUserId()));
//                }
                js.put("service", false);
                js.put("concerned", userConcernService.queryUserConcernedByUserOnfocusId(user.getUserId()));
            }

            //第二版内容
            if (StringUtils.isNotEmpty(version)) {
                //第二版内容
                if (StringUtils.isNotEmpty(version)) {


                    jsonObject.put("score", user.getUserCallScore() == null ? 0 : user.getUserCallScore());
                    jsonObject.put("backurl", StringUtils.isEmpty(user.getBackPicUrl()) ? "" : user.getBackPicUrl());


                    jsonObject.put("callpay", user.getUserCallCost() == null ? 1.00 : user.getUserCallCost());

                    // 第二版增加了环信Id--和个人签名
                    // 创建环信账号
                    HuanXin huanXin = huanXinService.queryIsExist(user.getUserId());
                    if (huanXin != null) {
                        jsonObject.put("huanxinName", huanXin.getHuaXinName());
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
                            huanXinPost.setOwnerId(user.getUserId());
                            huanXinPost.setPass(huanXinpass);
                            huanXinService.saveHuanXin(huanXinPost);
                        }
                        jsonObject.put("huanxinName", name);
                    }


                }

            }
            jsonObject.put("person", js);
            jsonObject.put("say", jsonArray);
            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 支持说说
    @ResponseBody
    @RequestMapping(value = "support/{uid}/{sayId}/supportSayContent")
    public JSONObject supportSayContent(@PathVariable("uid") String uid, @PathVariable("sayId") Integer sayId,
                                        @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
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

            // 检测sayId(说说)是否存在
            UserSayContent userSayContent = userSayService.querySayContentByUserSayContentId(sayId);
            if (userSayContent == null) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            // 是否在黑名单---互相查询
            UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(user.getUserId(),
                    userSayContent.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 217);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            userBlackList = userBlackListService.queryUserIsBlackListById(userSayContent.getUserId(), user.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 218);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            UserSupportSay userSupportSay = userSayService.querySupportUniqueExpecIsDeletedById(user.getUserId(),
                    sayId);

            if (userSupportSay == null) {
                userSupportSay = new UserSupportSay();
                userSupportSay.setIsDeleted("0");
                userSupportSay.setUserId(user.getUserId());
                userSupportSay.setUserSayContentId(sayId);
                userSupportSay.setUserSupportId(userSayContent.getUserId());
                userSupportSay.setCreateTime(new Date());
                userSupportSay.setIsReader("false");
                userSayService.saveSupportsByUserId(userSupportSay);

            } else {
                // 不可重复支持
                if ("0".equals(userSupportSay.getIsDeleted())) {
                    jsonObject.put("code", 203);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }

                userSayService.updateSupportSayContentById("0", user.getUserId(), sayId);
            }

            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 取消支持说说
    @ResponseBody
    @RequestMapping(value = "support/{uid}/{sayId}/cancelSupportSayContent")
    public JSONObject cancelSupportSayContent(@PathVariable("uid") String uid, @PathVariable("sayId") Integer
            sayId,
                                              @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
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

            UserSupportSay userSayContent = userSayService.querySupportUniqueById(user.getUserId(), sayId);

            if (userSayContent == null) {
                jsonObject.put("code", 210);// 返回无结果
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 是否在黑名单---互相查询
            UserBlackList userBlackList = userBlackListService.queryUserIsBlackListById(user.getUserId(),
                    userSayContent.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 217);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            userBlackList = userBlackListService.queryUserIsBlackListById(userSayContent.getUserId(), user.getUserId());
            if (userBlackList != null && "0".equals(userBlackList.getIsDeleted())) {
                jsonObject.put("code", 218);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            userSayService.updateSupportSayContentById("1", user.getUserId(), sayId);

            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

}
