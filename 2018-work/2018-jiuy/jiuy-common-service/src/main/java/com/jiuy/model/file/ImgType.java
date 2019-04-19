package com.jiuy.model.file;

 /**
 * 图片类型
 *
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/6/11 15:14
 */
public enum ImgType {


     /**
      * 图片橱窗
      */
     PRODUCT_MAIN(1, "图片橱窗"),
     /**
      * 图片详情
      */
     PRODUCT_DETAIL(2, "图片详情"),
     /**
      * 头像图片
      */
     USER_AVATAR(3, "头像图片");

     private Integer code;

     private String name;

     ImgType(Integer code, String name) {
         this.name = name;
         this.code = code;
     }

     public Integer getCode() {
         return code;
     }

     public String getName() {
         return name;
     }
 }