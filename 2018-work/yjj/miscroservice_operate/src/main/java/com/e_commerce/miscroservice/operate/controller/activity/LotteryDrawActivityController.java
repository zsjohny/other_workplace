package com.e_commerce.miscroservice.operate.controller.activity;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.service.activity.LotteryDrawActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抽奖活动
 * @author hyf
 * @version V1.0
 * @date 2018/12/18 16:45
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/lottery/draw")
public class LotteryDrawActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;
    /**
     * 找到所有活动
     * @param timeStart 开始时间
     * @param timeEnd 结束时间
     * @param pageNumber 页码：默认1
     * @param pageSize 每页显示数量：默认10
     * @return
     */
    @RequestMapping("/product/list")
    public Response findAllProductList(Long id,String timeStart, String timeEnd,
                                       @RequestParam(name = "pageNumber",defaultValue = "1") Integer pageNumber,
                                       @RequestParam(name = "pageSize",defaultValue = "10")  Integer pageSize){
        return lotteryDrawActivityService.findAllProductList(id,timeStart,timeEnd,pageNumber,pageSize);
    }

    /**
     * 查询详情内容
     * @param id 产品id
     * @return
     */
    @RequestMapping("/details/pic")
    public Response findAllDetail(Long id){
        return lotteryDrawActivityService.findAllDetail(id);
    }
    /**
     * 添加商品
     * @param banner banner
     * @param pic 图片集合 例如pic1,pic2
     * @param button 按钮
     * @return
     */
    @RequestMapping("insert/product")
    public Response insertProduct(String banner,String[] pic,String button){
        return lotteryDrawActivityService.insertProduct(banner,pic,button);

    }

    /**
     * 修改产品
     * @param id 产品id
     * @param banner banner
     * @param pic 图片集合 例如pic1,pic2
     * @param button 按钮
     * @return
     */
    @RequestMapping("update/product")
    public Response updateProduct(Long id,String banner,String pic[],String button){
        return lotteryDrawActivityService.updateProduct(id,banner,pic,button);

    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("delete/product")
    public Response deleteProduct(Long id){
        return lotteryDrawActivityService.deleteProduct(id);

    }

    /**
     * 查询参加的用户
     * @param phone
     * @return
     */
    @RequestMapping("find/join")
    public Response findJoin(String phone,Integer pageNumber,Integer pageSize){
        return lotteryDrawActivityService.findJoin(phone,pageNumber,pageSize);
    }
}
