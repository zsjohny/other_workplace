package com.e_commerce.miscroservice.product.controller.live;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.product.service.LiveProductService;
import com.e_commerce.miscroservice.product.vo.LiveProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 9:24
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("product/live/room/product")
public class LiveProductController {

    @Autowired
    private LiveProductService liveProductService;


    /**
     * 编辑商品
     *
     * @param liveProductId liveProductId
     * @param operType 编辑类型:top(置顶),simple(修改排序,改价,取消直播)
     * @param sortNo 排序时间戳
     * @param livePrice 改价的价格
     * @param liveStatus 直播状态:0正常,1取消直播
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/15 14:12
     */
    @RequestMapping( "update" )
    public Response update(
            Long liveProductId,
            String operType,
            Long sortNo,
            String sortNoJson,
            Double livePrice,
            Integer liveStatus
    ) {
        LiveProductVO vo = new LiveProductVO();
        vo.setId(liveProductId);
        vo.setOperType(operType);
        vo.setSortNo(sortNo);
        vo.setSortNoJson(sortNoJson);
        vo.setLivePrice(livePrice);
        vo.setLiveStatus(liveStatus);
        return ResponseHelper.shouldLogin()
                .invokeNoReturnVal(userId->{
                    vo.setMemberId(userId);
                    liveProductService.update(vo);
                })
                .returnResponse();
    }


    /**
     * 直播商品选择列表
     *
     * @param productName 商品名称
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/14 17:36
     */
    @RequestMapping( "productSelectList" )
    public Response productSelectList(
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam( value = "pageSize", defaultValue = "1" ) Integer pageSize,
            @RequestParam( value = "pageNumber", defaultValue = "10" ) Integer pageNumber
    ) {
        LiveProductVO vo = new LiveProductVO();
        vo.setProductName(productName);
        vo.setPageSize(pageSize);
        vo.setPageNumber(pageNumber);
        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(userId-> {
                    vo.setMemberId(userId);
                    return liveProductService.productSelectList(vo);
                })
                .returnResponse();
    }


    /**
     * 新建主播商品
     *
     * @param shopProductIds 小程序主播就是小程序商品id, 平台直播就是平台商品id
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/15 11:36
     */
    @RequestMapping( "add" )
    public Response add(
            @RequestParam("shopProductIds") List<Long> shopProductIds
    ) {
        LiveProductVO vo = new LiveProductVO();
        vo.setShopProductIds(shopProductIds);
        return ResponseHelper.shouldLogin()
                .invokeNoReturnVal(userId->{
                    vo.setMemberId(userId);
                    liveProductService.batchInsertByProductIds(vo);
                })
                .returnResponse();
    }


    /**
     * 直播商品列表
     *
     * @param liveStatus 当是正在直播时,传0,直播管理列表不传值
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/15 17:47
     */
    @RequestMapping( "listLiveProduct" )
    public Response listLiveProduct(
            @RequestParam(value = "liveStatus", required = false, defaultValue = "0") Integer liveStatus,
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam( value = "pageSize", defaultValue = "10" ) Integer pageSize,
            @RequestParam( value = "pageNumber", defaultValue = "1" ) Integer pageNumber
    ) {
        LiveProductVO vo = new LiveProductVO();
        vo.setPageNumber(pageNumber);
        vo.setProductName(productName);
        vo.setPageSize(pageSize);
        vo.setLiveStatus(liveStatus);
        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(userId->{
                    vo.setMemberId(userId);
                    return liveProductService.listLiveProduct(vo);
                })
                .returnResponse();
    }





    /**
     * 正在推荐商品列表
     *
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     * @param roomNum 直播间号
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/16 14:47
     */
    @RequestMapping( "listOnRecommended" )
    public Response listOnRecommended(
            @RequestParam( value = "pageSize", defaultValue = "10" ) Integer pageSize,
            @RequestParam( value = "pageNumber", defaultValue = "1" ) Integer pageNumber,
            @RequestParam( value = "roomNum") Long roomNum) {
        LiveProductVO vo = new LiveProductVO();
        vo.setRoomNum(roomNum);
        vo.setPageNumber(pageNumber);
        vo.setPageSize(pageSize);
        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(userId->{
                    vo.setMemberId(userId);
                    return liveProductService.listOnRecommended(vo);
                }).returnResponse();
    }



    /**
     * 商品简介
     *
     * @param liveProductId liveProductId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/16 16:15
     */
    @RequestMapping( "productIntro" )
    public Response productIntro(@RequestParam( value = "liveProductId" ) Long liveProductId) {
        return ResponseHelper.canShouldNotLogin()
                .invokeHasReturnVal(userId->liveProductService.productIntro(liveProductId))
                .returnResponse();
    }


    /**
     * 商品详情
     *
     * @param liveProductId liveProductId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/16 16:15
     */
    @RequestMapping( "productDetail" )
    public Response productDetail(@RequestParam( value = "liveProductId" ) Long liveProductId) {
        return ResponseHelper.canShouldNotLogin()
                .invokeHasReturnVal(userId->liveProductService.productDetail(liveProductId))
                .returnResponse();
    }

}
