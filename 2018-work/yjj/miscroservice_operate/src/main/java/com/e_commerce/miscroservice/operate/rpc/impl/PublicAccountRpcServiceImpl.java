package com.e_commerce.miscroservice.operate.rpc.impl;//package com.e_commerce.miscroservice.operate.rpc.impl;
//
//import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser;
//import com.e_commerce.miscroservice.commons.log.Log;
//import com.e_commerce.miscroservice.commons.utils.Response;
//import com.e_commerce.miscroservice.operate.rpc.user.PublicAccountRpcService;
//import com.github.pagehelper.PageInfo;
//import org.springframework.stereotype.Component;
//
//import java.sql.Timestamp;
//
///**
// * @author Charlie
// * @version V1.0
// * @date 2018/9/25 12:38
// * @Copyright 玖远网络
// */
//@Component
//public class PublicAccountRpcServiceImpl implements PublicAccountRpcService{
//
//    private Log logger = Log.getInstance(PublicAccountRpcServiceImpl.class);
//
//
//    /**
//     * 公众号用户列表
//     *
//     * @param pageNumber 分页
//     * @param pageSize 分页
//     * @param wxName 微信名
//     * @param phone 手机号
//     * @param createTimeBefore 注册起始时间
//     * @param createTimeAfter 注册最大时间
//     * @return com.github.pagehelper.PageInfo<com.e_commerce.miscroservice.commons.entity.user.PublicAccountUser>
//     * @author Charlie
//     * @date 2018/9/25 12:45
//     */
//    @Override
//    public PageInfo<PublicAccountUser> listUser(Integer pageNumber, Integer pageSize, String wxName, String phone, Timestamp createTimeBefore, Timestamp createTimeAfter) {
//        logger.warn ("查询公众号用户列表,服务没有调用 pageNumber = [" + pageNumber + "], pageSize = [" + pageSize + "], wxName = [" + wxName + "], phone = [" + phone + "], createTimeBefore = [" + createTimeBefore + "], createTimeAfter = [" + createTimeAfter + "]");
//        return null;
//    }
//
//    @Override
//    public Response updateDelStatus(Long id, Integer delStatus) {
//        logger.warn ("更新公众号账户状态,服务没有调用 id = [" + id + "], delStatus = [" + delStatus + "]");
//        return null;
//    }
//}
