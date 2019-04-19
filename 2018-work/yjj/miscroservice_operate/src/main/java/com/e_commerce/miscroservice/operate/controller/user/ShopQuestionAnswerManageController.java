package com.e_commerce.miscroservice.operate.controller.user;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.ShopQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.service.user.ShopQuestionAnswerManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序 QA
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 16:14
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/qa/shop")
public class ShopQuestionAnswerManageController {

    @Autowired
    private ShopQuestionAnswerManageService shopQuestionAnswerManageService;

    /**
     * 查询 问题类型
     * @param typeName 类型名称
     * @param createBy 创建人
     * @param createTimeStart 创建时间 始
     * @param createTimeEnd 创建时间 止
     * @param pageNumber 页码
     * @param pageSize 数量
     * @return
     */
    @RequestMapping("/find/type/all")
    @Consume(ShopQuestionAnswerTypeRequest.class)
    public Response findAllStoreQuestionAnswerTpye(String typeName, String createBy, String createTimeStart, String createTimeEnd, @RequestParam(name ="pageNumber",defaultValue = "1") Integer pageNumber, @RequestParam(name ="pageSize",defaultValue = "10")Integer pageSize){
        return  shopQuestionAnswerManageService.findAllShopQuestionAnswerType((ShopQuestionAnswerTypeRequest)ConsumeHelper.getObj());
    }


    /**
     * 查询问题类型总数量
     * @return
     */
    @RequestMapping("/question/type/size")
    public Response findQuestionTypeSize(){
        return shopQuestionAnswerManageService.findQuestionTypeSize();
    }


    /**
     * 新建问题类型
     * @param name 类型名称
     * @param sort 排序
     * @return
     */
    @RequestMapping("/type/add")
    public Response addQuestionType(String name,Integer sort){
        return shopQuestionAnswerManageService.addQuestionType(name,sort);
    }

    /**
     * 编辑问题类型
     * @param name 名称
     * @param sort 排序
     * @param id id
     * @return
     */
    @RequestMapping("/type/up")
    public Response upQuestionType(String name,Integer sort,Long id){
        return shopQuestionAnswerManageService.upQuestionType(name,sort,id);
    }


    /**
     * 转移问题类型
     * @param exchangeId 转移对象id
     * @param id 自身id
     * @return
     */
    @RequestMapping("/type/exchange")
    public Response exchangeQuestionType(Long exchangeId,Long id){
        return shopQuestionAnswerManageService.exchangeQuestionType(exchangeId,id);
    }

    /*-----------------------------------------------------------------------------------------------------------*/



    /**
     * 查询问题
     * @param questionName 问题名称
     * @param typeName 类型名称
     * @param createBy 创建人
     * @param createTimeStart 创建开始时间
     * @param createTimeEnd 创建开始时间 止
     * @param status 是否隐藏 0 显示 1隐藏
     * @param typeStatus  问题类型
     * @param pageNumber 页码
     * @param pageSize 数量
     * @return
     */
    @RequestMapping("/find/all")
    @Consume(ShopQuestionAnswerTypeRequest.class)
    public Response findAllShopQuestionAnswer(String questionName, String typeName, String createBy, String createTimeStart, String createTimeEnd,String status,Integer typeStatus, @RequestParam(name ="pageNumber",defaultValue = "1") Integer pageNumber, @RequestParam(name ="pageSize",defaultValue = "10")Integer pageSize){
        return  shopQuestionAnswerManageService.findAllShopQuestionAnswer((ShopQuestionAnswerTypeRequest)ConsumeHelper.getObj());
    }



    /**
     * 新建问题
     * @param questionTypeId 问题 类型id
     * @param sort 排序
     * @param question 问题
     * @param answer 答案
     * @return
     */
    @RequestMapping("/question/add")
    @Consume(ShopQuestionInsertRequest.class)
    public Response addStoreQuestion(  Long questionTypeId, Integer sort, String question, String answer){
        return shopQuestionAnswerManageService.addShopQuestion((ShopQuestionInsertRequest)ConsumeHelper.getObj());
    }

    /**
     * 修改问题
     * @param id 问题id
     * @param questionTypeId 问题类型id
     * @param sort 排序
     * @param question 问题
     * @param answer 答案
     * @return
     */
    @RequestMapping("/question/update")
    @Consume(ShopQuestionInsertRequest.class)
    public Response updateStoreQuestion(  Long id, Long questionTypeId, Integer sort, String question, String answer){
        return shopQuestionAnswerManageService.updateShopQuestion((ShopQuestionInsertRequest)ConsumeHelper.getObj());
    }
/*--------------------------------------------------------------------------*/
    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    @RequestMapping("find/one")
    public Response findOneShopQuestionAnswer(Long id){
        return shopQuestionAnswerManageService.findOneShopQuestionAnswer(id);
    }

    /**
     * 获取问题类型下拉选
     * @return
     */
    @RequestMapping("find/type/selection")
    public Response findTypeSelection(){
        return shopQuestionAnswerManageService.findTypeSelection();
    }


    /**
     * 批量显示隐藏
     * @param ids 需要批量显示或者隐藏的 id 集合用“,”隔开
     * @param showNot 0 显示 1 隐藏
     * @return
     */
    @RequestMapping("batch/display/hiding")
    public Response batchDisplayOrHiding(String ids,Integer showNot){

        return shopQuestionAnswerManageService.batchDisplayOrHiding(ids,showNot);
    }

    /**
     * 批量 删除
     * @param ids 需要批量显示或者隐藏的 id 集合用“,”隔开
     * @param delNot 1 删除 0 正常
     * @return
     */
    @RequestMapping("batch/del")
    public Response batchDelNot(String ids,Integer delNot){

        return shopQuestionAnswerManageService.batchDelNot(ids,delNot);
    }

    /*-----------------------------------------------------------------------------------*/

    /**
     * 删除 问题类型
     * @param id
     * @return
     */
    @RequestMapping("type/del")
    public Response typeDel(Long id){
        return shopQuestionAnswerManageService.typeDel(id);
    }
}
