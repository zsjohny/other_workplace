package com.e_commerce.miscroservice.operate.controller.live;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.AddAnchorRequest;
import com.e_commerce.miscroservice.operate.entity.request.FindLiveRequest;
import com.e_commerce.miscroservice.operate.service.live.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author hyf
 * @Date 2019/1/15 15:06
 */
@RestController
@RequestMapping("live/manage")
public class LiveController {


    @Autowired
    private LiveService liveService;

    /**
     * 查询官方主播
     * @param name 姓名
     * @param nickName 昵称
     * @param sex 性别
     * @param phone  手机号
     * @param timeStart 时间起
     * @param timeEnd 时间止
     * @return
     */
    @Consume(FindLiveRequest.class)
    @RequestMapping("/find/official/anchor")
    public Response findOfficialAnchor(String name, String nickName, Integer sex, String phone, Long timeStart, Long timeEnd,Integer pageNumber,Integer pageSize){
        return liveService.findOfficialAnchor((FindLiveRequest) ConsumeHelper.getObj());
    }

    /**
     * 查询门店主播
     * @param name 姓名
     * @param nickName 昵称
     * @param sex 性别
     * @param phone 手机号
     * @param timeStart 时间起
     * @param timeEnd 时间止
     * @return
     */
    @Consume(FindLiveRequest.class)
    @RequestMapping("/find/store/anchor")
    public Response findStoreAnchor(String name, String nickName, Integer sex, String phone, Long timeStart, Long timeEnd,Integer pageNumber,Integer pageSize){
        return liveService.findStoreAnchor((FindLiveRequest) ConsumeHelper.getObj());
    }

    /**
     * 查询普通主播
     * @param name
     * @param nickName
     * @param sex
     * @param phone
     * @param timeStart
     * @param timeEnd
     * @return
     */
    @Consume(FindLiveRequest.class)
    @RequestMapping("/find/common/anchor")
    public Response findCommonAnchor(String name, String nickName, Integer sex, String phone, Long timeStart, Long timeEnd,Integer pageNumber,Integer pageSize){
        return liveService.findCommonAnchor((FindLiveRequest) ConsumeHelper.getObj());
    }


    /**
     * 添加官方主播
     * @param name 姓名
     * @param nickName 昵称
     * @param icon 头像
     * @param age 年龄
     * @param idCard 身份证
     * @param phone 手机
     * @param sex 性别
     * @return
     */
    @Consume(AddAnchorRequest.class)
    @RequestMapping("add/anchor")
    public Response addAnchor(String name, String nickName,String icon,Integer age,String idCard,String phone,Integer sex){
         return liveService.addAnchor((AddAnchorRequest)ConsumeHelper.getObj());
    }

    /**
     * 删除官方主播
     * @param id
     * @return
     */
    @RequestMapping("del/official/anchor")
    public Response delOfficialAnchor(Long id){
        return liveService.delOfficialAnchor(id);
    }
}
