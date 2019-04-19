package com.e_commerce.miscroservice.activity.controller;

import com.e_commerce.miscroservice.activity.entityvo.ChannelRequest;
import com.e_commerce.miscroservice.activity.entityvo.ChannelUserRequest;
import com.e_commerce.miscroservice.activity.service.ChannelService;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.application.conver.DownFilesUtils;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ExcelUtil;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channel/store")
public class ChannelUserController {

    Log logger = Log.getInstance(ChannelUserController.class);
    @Autowired
    private ChannelService channelService;


    @RequestMapping("/test")
    public Response test(Long memberId,Long storeId){

        return channelService.test(memberId,storeId);
    }
    /**
     * 添加渠道商
     * @Author:胡坤
     * @return
     */
    @RequestMapping("/add/myChannel")
    public Response addMyChannel(@RequestParam("name")String name,
                                 @RequestParam("phone")String phone
    ){
        return ResponseHelper.canShouldNotLogin()
                .invokeNoReturnVal(userId-> channelService.addChannel(name,phone))
                .returnResponse();
    }

    /**
     * 修改渠道商
     * @Author:胡坤
     */
    @RequestMapping("/update/channel")
    public Response uodateChannel(
                                  @RequestParam("id")Long id,
                                  @RequestParam("name")String name,
                                  @RequestParam("phone")String phone,
                                  @RequestParam("status")Integer status// 0已结束合作  1 (默认)合作中
    ){

        return channelService.updateChannel(id,name,phone,status);
    }

    /**
     * 渠道商姓名
     * 渠道商id
     * 渠道商状态
     * 渠道商手机号
     * 开始的添加时间
     * 截止的添加时间
     * 渠道商搜索
     * @Author:胡坤
     */
    @Consume(ChannelRequest.class)
    @RequestMapping("/query/allChannel")
    public Response queryAll(@RequestParam(value = "name",required = false)String name,//渠道商姓名
                             @RequestParam(value = "id",required = false)Long id,//渠道商id
                             @RequestParam(value = "status",required = false)Integer status,//渠道商状态 ,查寻全部的时候为2 , 0终止合作,1合作中(default:1)
                             @RequestParam(value = "phone",required = false)String phone,//渠道商手机号
                             @RequestParam(value = "startTime",required = false)String startTime,//开始的添加时间
                             @RequestParam(value = "overTime",required = false)String overTime,//截止的添加时间
                             @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                             @RequestParam(value = "pageNumber",defaultValue = "1")Integer pageNumber
    ){
        return channelService.search((ChannelRequest) ConsumeHelper.getObj());
    }


    /**
     * 描述
     * @param name 用户微信昵称
     * @param phone 用户手机号
     * @param sex 用户性别  '0未知,1:男,2:女',
     * @param authority 是否授权 0授权   1未授权(游客)
     * @param stareTime 开始时间
     * @param overTime 截止时间
     * @param channelId 渠道商id
     * @param pageSize
     * @param oageNumber
     * @author 胡坤
     * @date 2018/12/27 16:45
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     */
    @Consume(ChannelUserRequest.class)
    @RequestMapping("/query/MyUser")
    public Response queryMyUser(@RequestParam(value = "name",required = false)String name,//用户微信昵称
                                @RequestParam(value = "phone",required = false)String phone,//用户手机号
                                @RequestParam(value = "sex",required = false)Integer sex,//用户性别  '0未知,1:男,2:女',
                                @RequestParam(value = "authority",required = false)Integer authority,//是否授权 0授权   1未授权(游客)
                                @RequestParam(value = "startTime",required = false)String startTime,//开始时间
                                @RequestParam(value = "overTime",required = false)String overTime,//截止时间
                                @RequestParam(value = "channelId",defaultValue = "1")Long channelId,//渠道商id
                                @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                @RequestParam(value = "pageNumber",defaultValue = "1")Integer pageNumber
    ){

        return channelService.searchUser((ChannelUserRequest) ConsumeHelper.getObj());
    }

    /**
     * 下载二维码,没有则生成
     * @Author:胡坤
     */
    @RequestMapping("/down/qrCode")
    public Response downCode(@RequestParam("channelId")Long channelId){
        return channelService.searchQrCode(channelId);
    }

    /**
     * 导出表
     *  @Author:胡坤
     */
    @Consume(ChannelUserRequest.class)
    @RequestMapping("/export/table")
    public Response export(HttpServletResponse response,
                       @RequestParam(value = "name",required = false)String name,//用户微信昵称
                       @RequestParam(value = "phone",required = false)String phone,//用户手机号
                       @RequestParam(value = "sex",required = false)Integer sex,//用户性别  '0未知,1:男,2:女',
                       @RequestParam(value = "authority",required = false)Integer authority,//是否授权 0授权   1未授权(游客)
                       @RequestParam(value = "startTime",required = false)String startTime,//开始时间
                       @RequestParam(value = "overTime",required = false)String overTime,//截止时间
                       @RequestParam(value = "channelId",defaultValue = "1")Long channelId,//渠道商id
                            @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                           @RequestParam(value = "pageNumber",defaultValue = "1")Integer pageNumber
                       ) throws IOException {
        List<Map<String, Object>> list = channelService.exUser((ChannelUserRequest) ConsumeHelper.getObj());
        String[] columnNames = {"微信昵称", "手机号", "性别", "微信授权状态", "邀请时间", "下单数"};
        //map中的key
        String[] keys = {"wxName","wxPhone", "sex","authority","createTime","allOrderCount"};
        ExcelUtil.exportExcel(response, list, keys, columnNames, "渠道商用户表");
        return Response.success();

    }


}
