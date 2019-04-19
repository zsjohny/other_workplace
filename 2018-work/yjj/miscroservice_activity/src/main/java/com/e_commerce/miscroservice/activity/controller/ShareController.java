package com.e_commerce.miscroservice.activity.controller;

import com.e_commerce.miscroservice.activity.entity.WxaShare;
import com.e_commerce.miscroservice.activity.service.ShareService;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.FileUtils;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/21 15:03
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "/activity/share" )
public class ShareController{

    private Log logger = Log.getInstance(ShareController.class);


    @Autowired
    private ShareService shareService;


    /**
     * 分享
     * <p>前端调用逻辑: 分享商品,分享APP</p>
     *
     * @param sourceUser       邀请者memberId
     * @param shareObjectId    分享事物id(商品id)
     * @param shareType        1活动分享(预留),2 商品分享,3优惠券分享(预留),4分享小程序
     * @param wxId             wxId
     * @param wxNickname       wxNickname
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/22 11:13
     */
    @RequestMapping( "shareFriend" )
    public Response shareFriend(
            @RequestParam( value = "sourceUser" ) Long sourceUser,
            @RequestParam( value = "currentUserSex" , defaultValue = "0") Integer currentUserSex,
            @RequestParam( value = "shareType" ) Integer shareType,
            @RequestParam( value = "shareObjectId" , defaultValue = "0") Long shareObjectId,
            @RequestParam( value = "wxId", required = false ) String wxId,
            @RequestParam( value = "wxNickname", defaultValue = "" ) String wxNickname
    ) {

        if (shareType == 2 && shareObjectId == 0) {
            return Response.error ("分享商品,商品为空");
        }

        /*
         * 有效粉丝和粉丝的概念
         *   粉丝===>只要是邀请的新用户,就是粉丝,体现在分销系统中
         *   有效粉丝===>邀请的新用户,并做(有效)判断,如果是,则分享者获得收益,否则只记录分享日志不增加收益
         */


        return ResponseHelper.shouldLogin ()
                .invokeNoReturnVal (userId -> {
                    //下面两个拆开,因为都调用了其他模块,为了保持事务的一致性,暂时放在这里
                    //被邀请者获得收益
                    boolean isEffectiveFans = false;
                    try {
                        //建立邀请关系
                        WxaShare wxaShare = new WxaShare();
                        wxaShare.setTargetUser(userId);
                        wxaShare.setSourceUser(sourceUser);
                        wxaShare.setShareType(shareType);
                        wxaShare.setWxNickname(wxNickname);
                        wxaShare.setWxId(wxId);
                        wxaShare.setWxNickname(wxNickname);
                        isEffectiveFans = shareService.shareFriend(wxaShare, currentUserSex);
                        logger.info("是否有效粉丝 ={}", isEffectiveFans);

                    } catch (ErrorHelper e) {
                        logger.info(e.getMsg());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        logger.error("分享关系出错,直接返回, e={}", e.getMessage());
                        return;
                    }
                    //记录分享商品
                    shareService.doShareProduct (sourceUser, shareType, shareObjectId, userId, isEffectiveFans);
                })
                .returnResponse ();
    }



    /**
     * 渠道商分享
     *
     * @param channelUserId 渠道商用户id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/24 17:13
     */
    @RequestMapping( "shareFromChannel" )
    public Response shareFromChannel(
            @RequestParam( value = "channelUserId" ) Long channelUserId
            ) {
        return ResponseHelper.shouldLogin()
                .invokeNoReturnVal(userId-> shareService.shareFromChannel(channelUserId, userId))
                .returnResponse();
    }


    /**
     * 二维码分享
     *
     * @param shareType shareType
     * @param request request
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie
     * @date 2018/7/17 17:31
     */
    @RequestMapping(value = "/share/image")
    public Response shareImage(Integer type, Integer shareType, HttpServletRequest request)
    {
        logger.info("小程序二维码分享:type={},shareType={}",type,shareType);
        try {
            //获取图片
            String image = shareService.getImage(type,shareType);
            //阿里云图片地址转为服务器本地地址
            String serverImg = FileUtils.aliImgToServerImg(image,request);

            Map<String,Object> data = new HashMap();
            data.put("wxaProductShareImage", serverImg);//商品分享长图
            logger.info("获取小程序商品分享图片返回数据data={}",data);
            return Response.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("小程序商品分享图片:"+e.getMessage());
            return Response.errorMsg("分享失败");
        }

    }



}
