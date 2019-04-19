package com.e_commerce.miscroservice.store.entity.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Data
public class SubmitRequest {
       private Integer type;
       private String name;
       private String phone;//退款人手机
       private String reson;//退款理由
       private String photo1;//退款图片
       private String photo2;//退款图片
       private String photo3;//退款图片
       private String photo4;//退款图片
       private String photo5;//退款图片
       private BigDecimal refundMoney;//退款金额
       private String orderId;//退款订单
       private Long skuId;//skuId
       private Long storeId;//商家id
       private Long userId;//会员id
       private Integer count;//退款数量
       private String refundRemark;//退款说明
       private Integer style;//店中店标识
}
