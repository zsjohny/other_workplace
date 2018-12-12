//package com.goldplusgold.td.sltp.core.auth;
//
//
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.UnsupportedEncodingException;
//
///**
// * Created by Ness on 2017/5/18.
// */
////@Component
////@Order(2)
////@RabbitListener(queues = "1111", containerFactory = "_simple")
//public class Test3 {
//
//
//    //    @RabbitHandler
////    public void process(byte[] message) {
////
////        System.out.println("111111111111111111");
////        try {
////            System.out.println(new String(message,"utf-8"));
////        } catch (UnsupportedEncodingException e) {
////
////        }
////    }
//
//
////    public void process(String message) {
////
////        System.out.println("111111111111111111");
////            System.out.println(message);
////
////    }
//  public void process(UserSltpRecord message) {
//
//        System.out.println("111111111111111111");
//            System.out.println(message);
//
//    }
//
////    @RabbitHandler
////    public void processs(String message) {
////        System.out.println("111111111111111111");
////        System.out.println(message);
////    }
//
//}
