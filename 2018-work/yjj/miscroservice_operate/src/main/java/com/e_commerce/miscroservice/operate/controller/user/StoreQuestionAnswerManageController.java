package com.e_commerce.miscroservice.operate.controller.user;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionAnswerTypeRequest;
import com.e_commerce.miscroservice.operate.entity.request.StoreQuestionInsertRequest;
import com.e_commerce.miscroservice.operate.service.user.StoreQuestionAnswerManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP QA
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 16:14
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/qa/store")
public class StoreQuestionAnswerManageController {

    @Autowired
    private StoreQuestionAnswerManageService storeQuestionAnswerManageService;
    /**
     * 查询 问题类型
     * @param typeName
     * @param createBy
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    @RequestMapping("/find/type/all")
    @Consume(StoreQuestionAnswerTypeRequest.class)
    public Response findAllStoreQuestionAnswerTpye(String typeName, String createBy, String createTimeStart, String createTimeEnd, @RequestParam(name ="pageNumber",defaultValue = "1") Integer pageNumber, @RequestParam(name ="pageSize",defaultValue = "10")Integer pageSize){
        return  storeQuestionAnswerManageService.findAllStoreQuestionAnswerTpye((StoreQuestionAnswerTypeRequest)ConsumeHelper.getObj());
    }


    /**
     * 查询问题类型总数量
     * @return
     */
    @RequestMapping("/question/type/size")
    public Response findQuestionTypeSize(){
        return storeQuestionAnswerManageService.findQuestionTypeSize();
    }

    /**
     * 新建问题类型
     * @return
     */
    @RequestMapping("/type/add")
    public Response addQuestionType(String name,Integer sort){
        return storeQuestionAnswerManageService.addQuestionType(name,sort);
    }

    /**
     * 编辑问题类型
     * @return
     */
    @RequestMapping("/type/up")
    public Response upQuestionType(String name,Integer sort,Long id){
        return storeQuestionAnswerManageService.upQuestionType(name,sort,id);
    }

    /**
     * 转移问题类型
     * @param exchangeId 名称
     * @param id id
     * @return
     */
    @RequestMapping("/type/exchange")
    public Response exchangeQuestionType(Long exchangeId,Long id){
        return storeQuestionAnswerManageService.exchangeQuestionType(exchangeId,id);
    }

    /*-----------------------------------------------------------------------------------------------------------*/


    /**
     * 查询问题
     * @param questionName
     * @param typeName
     * @param createBy
     * @param createTimeStart
     * @param createTimeEnd
     * @param status
     * @param typeStatus
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping("/find/all")
    @Consume(StoreQuestionAnswerTypeRequest.class)
    public Response findAllStoreQuestionAnswer(String questionName, String typeName, String createBy, String createTimeStart, String createTimeEnd,String status,Integer typeStatus, @RequestParam(name ="pageNumber",defaultValue = "1") Integer pageNumber, @RequestParam(name ="pageSize",defaultValue = "10")Integer pageSize){
        return  storeQuestionAnswerManageService.findAllStoreQuestionAnswer((StoreQuestionAnswerTypeRequest)ConsumeHelper.getObj());
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
    @Consume(StoreQuestionInsertRequest.class)
    public Response addStoreQuestion(  Long questionTypeId, Integer sort, String question, String answer){
        return storeQuestionAnswerManageService.addStoreQuestion((StoreQuestionInsertRequest)ConsumeHelper.getObj());
    }

    /**
     * 修改问题
     * @param questionTypeId 问题 类型id
     * @param sort 排序
     * @param question 问题
     * @param answer 答案
     * @return
     */
    @RequestMapping("/question/update")
    @Consume(StoreQuestionInsertRequest.class)
    public Response updateStoreQuestion(  Long id, Long questionTypeId, Integer sort, String question, String answer){
        return storeQuestionAnswerManageService.updateStoreQuestion((StoreQuestionInsertRequest)ConsumeHelper.getObj());
    }

    /*--------------------------------------------------------------------------*/
    /**
     * 根据问题id查询单个 问题
     * @param id
     * @return
     */
    @RequestMapping("find/one")
    public Response findOneStoreQuestionAnswer(Long id){
        return storeQuestionAnswerManageService.findOneStoreQuestionAnswer(id);
    }

    /**
     * 获取问题类型下拉选
     * @return
     */
    @RequestMapping("find/type/selection")
    public Response findTypeSelection(){
        return storeQuestionAnswerManageService.findTypeSelection();
    }


    /**
     * 批量显示隐藏
     * @param ids 需要批量显示或者隐藏的 id 集合用“,”隔开
     * @param showNot 0 显示 1 隐藏
     * @return
     */
    @RequestMapping("batch/display/hiding")
    public Response batchDisplayOrHiding(String ids,Integer showNot){

        return storeQuestionAnswerManageService.batchDisplayOrHiding(ids,showNot);
    }

    /**
     * 批量 删除
     * @param ids 需要批量显示或者隐藏的 id 集合用“,”隔开
     * @param delNot 1 删除 0 正常
     * @return
     */
    @RequestMapping("batch/del")
    public Response batchDelNot(String ids,Integer delNot){

        return storeQuestionAnswerManageService.batchDelNot(ids,delNot);
    }
    /*-----------------------------------------------------------------------------------*/

    /**
     * 删除 问题类型
     * @param id
     * @return
     */
    @RequestMapping("type/del")
    public Response typeDel(Long id){
        return storeQuestionAnswerManageService.typeDel(id);
    }
}
