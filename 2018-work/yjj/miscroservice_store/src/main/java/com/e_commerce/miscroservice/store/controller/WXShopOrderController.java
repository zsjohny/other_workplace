package com.e_commerce.miscroservice.store.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.order.RefundOrderRequest;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.commons.utils.IOUtils;
import com.e_commerce.miscroservice.commons.utils.OssKit;
import com.e_commerce.miscroservice.store.entity.vo.*;
import com.e_commerce.miscroservice.store.service.WXOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/wxOrder/shopOrder")
public class WXShopOrderController {

    private Log logger = Log.getInstance(WXShopOrderController.class);
    @Autowired
    private OssKit ossKit;
    @Autowired
    private WXOrderService wxOrderService;

    //@Value( "#{'${oss.upload.bucket.refundImgs}'.split(',')}")
    private List<String> refundImgs;
    /**
     * 获取订单列表
     * @param orderStatus
     * @param shopId
     * @param userId
     * @return
     */
    @Consume(ShopMemberOrderRequest.class)
    @RequestMapping("/order/list")
    public Response getRefundOrderList(@RequestParam(value = "orderStatus",required=false ) Integer orderStatus,
                                       @RequestParam(value = "shopId",required = false)Long shopId,
                                       @RequestParam(value = "userId",defaultValue = "185") Long userId,
                                       Integer pageSize,
                                       Integer pageNumber
                                      ) {
        Response response = wxOrderService.getRefundOrderList((ShopMemberOrderRequest) ConsumeHelper.getObj());
        return response;
    }


    /**
     * 删除订单按钮
     */
    @RequestMapping("/order/delete")
    public Response orderDelete(@RequestParam(value = "orderId" ) Long id,
                                 @RequestParam(value = "userId",defaultValue = "184")Long userId){
        return wxOrderService.orderDelete(id,userId);

    }
    /**
     * 订单列表申请售后按钮
     */
    @RequestMapping("/refundButton")
    public Response refundButton(@RequestParam("orderId") Long orderId,
                                 @RequestParam(value = "userId",required = false)Long userId){

        return wxOrderService.selectOrderItem1(orderId,userId);
    }


    /**
     * 申请售后
     */
    @RequestMapping("/redundApply")
    public Response applyPlease(@RequestParam("orderId")Long orderId,
                                @RequestParam("skuId") Long skuId){


        return  wxOrderService.selectItem(orderId,skuId);
    }

    /**
     * 提交申请
     */
    @Consume(SubmitRequest.class)
    @RequestMapping("/submitApply")
    public Response submit(@RequestParam(value = "type",defaultValue = "0")Integer type,//退款类型
                           @RequestParam(value = "name",defaultValue = "hkhkhkhk")String name,//退款人姓名
                           @RequestParam(value = "phone",defaultValue = "123456")String phone,//退款人手机
                           @RequestParam(value = "reson",required = false)String reson,//退款理由
                           @RequestParam(value="photo1",required = false)String photo1,//退款图片
                           @RequestParam(value = "refundRemark",required = false)String refundRemark,//退款说明
                           @RequestParam(value = "refundMoney",defaultValue = "0.01")BigDecimal refundMoney,//退款金额
                           @RequestParam(value = "orderId",defaultValue = "291")String orderId,//退单号
                           @RequestParam(value = "skuId",defaultValue = "35344")Long skuId,//商品skuid
                           @RequestParam(value = "storeId",defaultValue ="3",required = false) Long storeId,
                           @RequestParam(value = "userId",defaultValue ="188" )Long userId,//会员id
                           @RequestParam(value = "count",defaultValue = "1")Integer count,//退款数量
                           @RequestParam(value = "style",defaultValue = "0",required = false)Integer style//0默认  1店中店
    ){
        return wxOrderService.submit((SubmitRequest)ConsumeHelper.getObj());
    }
    /**
     * 上传文件(post请求)
     *
     * @param newFile       新上传的文件
     * @param path          上传的目录路径(前端可不传,默认icon)
     * @param newFileName   新上传文件名称(在OSS上的名称,保证唯一)
     * @param olderFileName 老的文件名称(需要删除的文件名,非必填)
     * @return 上传后的文件链接(url)
     * @author Charlie
     * @date 2018/11/5 17:55
     */
    @PostMapping( "/uploadImg" )
    public Response uploadImg(@RequestPart( "refundPhoto" ) @NotNull( message = "上传文件不能为空" ) MultipartFile newFile,
                              @RequestParam( value = "path", required = false ) String path,
                              @RequestParam( value = "newFileName", required = false ) String newFileName,
                              @RequestParam( value = "olderFileName", required = false ) String olderFileName,
                              HttpServletRequest request
    ) {

        logger.info ("上传图片 newFileName={},olderFileName={},path={},size={},IP={}", newFileName, olderFileName, path, newFile.getSize (), HttpUtils.getIpAddress (request));
        //可变路径
        List<String> ossCfg = new ArrayList<>();
        ossCfg.add("yjj-img-main");
        ossCfg.add("customer/");
        if (StringUtils.isNotBlank (path)) {
            ossCfg = new ArrayList<> (refundImgs);
            ossCfg.set (1, path);
        }
        String fileName = newFile.getOriginalFilename();
        String[] split = StringUtils.split(fileName, ".");
        int length = split.length;
        String suffix = StringUtils.split(fileName, ".")[length-1];
        suffix="."+suffix;
        newFileName = String.valueOf(System.currentTimeMillis ()) + String.valueOf(System.currentTimeMillis()) +suffix ;
        InputStream is = null;
        try {
            //上传
            is = newFile.getInputStream ();
            String result = ossKit.simpleUpload (is, ossCfg, newFileName);
            logger.info ("上传图片成功");
            Map<String,String> map=new HashMap<>();
            map.put("result",result);
            return Response.success (result);
        } catch (IOException e) {
            //ignore
        } finally {
            IOUtils.close (is);
        }
        return Response.error ();
    }



    /**
     * 小程序售后列表
     */
    @Consume(RefundRquest.class)
    @RequestMapping("/refundList")
    public Response refundList(@RequestParam(value = "storeId",defaultValue = "57",required = false)Long storeId,
                               @RequestParam(value = "userId",defaultValue = "184",required = false)Long userId,
                               Integer pageSize,
                               Integer pageNumber){


        return wxOrderService.selectRefundList((RefundRquest)ConsumeHelper.getObj());
    }

/**
 * 小程序售后订单详情
 */
    @RequestMapping("/refundItem")
    public Response refundItem(@RequestParam(value = "afterSaleId",defaultValue = "1542952404572198")Long afterSaleId,
                               @RequestParam(value = "userId",defaultValue = "187")Long userId){


        return wxOrderService.selectRefundItem(afterSaleId,userId);
    }


    /**
     * 售后删除订单
     */
    @RequestMapping("/deleteOrder")
    public Response deleteOrder(@RequestParam("afterSaleId")Long afterSaleId,
                                @RequestParam("userId")Long userId
    ){
        return wxOrderService.deleteOrder(afterSaleId,userId);
    }
    /**
     *退款进度
     */
    @RequestMapping("/refundStatus")
    public Response refundStatus(@RequestParam("afterSaleId")Long afterSaleId,
                                 @RequestParam("userId")Long userId
                                 ){

        return wxOrderService.selecrTime(afterSaleId,userId);
    }


    /**
     * 订单详情
     */
    @RequestMapping("/orderItem")
    public Response orderItem(@RequestParam("id")Long id,
                              @RequestParam("userId")Long userId,
                              @RequestParam("storeId")Long storeId){

        return wxOrderService.selectOrderItem(id,userId,storeId);
    }








    /**
     * 检验门店是否为空
     */
    private void checkStore(ShopDetail shopDetail) {
        if(shopDetail==null || shopDetail.getId() == 0){
            logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
           throw new RuntimeException("商家信息不能为空");
        }
    }

    /**
     * 检验会员是否为空
     */
    private void checkMember(MemberDetail memberDetail) {
        if(memberDetail==null || memberDetail.getId() == 0){
            logger.info("会员信息不能为空，该接口需要登陆，请排除问题");
            throw new RuntimeException("会员信息不能为空");
        }
    }
}
