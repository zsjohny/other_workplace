package com.jiuy.rb.service.impl.order;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.mapper.order.ExpressInfoRbMapper;
import com.jiuy.rb.mapper.order.ExpressSupplierRbMapper;
import com.jiuy.rb.model.order.*;
import com.jiuy.rb.service.order.IExpressService;
import com.yujj.entity.account.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 物流相关的业务
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 17:21
 * @Copyright 玖远网络
 */
@Service("expressServiceRb")
public class ExpressServiceImpl implements IExpressService {


    @Resource(name = "expressSupplierRbMapper")
    private ExpressSupplierRbMapper expressSupplierRbMapper;

    @Resource(name = "expressInfoRbMapper")
    private ExpressInfoRbMapper expressInfoRbMapper;

    /**
     * 物流公司列表
     *
     * @param query query
     * @author Aison
     * @date 2018/6/28 17:26
     * @return java.util.List<com.jiuy.rb.model.order.ExpressSupplierRb>
     */
    @Override
    public List<ExpressSupplierRb> expressSupplierRbList(ExpressSupplierRbQuery query) {

        return expressSupplierRbMapper.selectList(query);
    }

    /**
     * 获取订单的物流信息
     *
     * @param orderNo orderNo
     * @author Aison
     * @date 2018/6/28 17:27
     * @return com.jiuy.rb.model.order.ExpressInfoRb
     */
    @Override
    public ExpressInfoRbQuery orderExpressInfo(Long orderNo) {

        ExpressInfoRbQuery query = new ExpressInfoRbQuery();
        query.setOrderNo(orderNo);
        ExpressInfoRb expressInfoRb =  expressInfoRbMapper.selectOne(query);
        if(expressInfoRb == null) {
            return null;
        }
        query  =  Biz.copyBean(expressInfoRb,ExpressInfoRbQuery.class);
        ExpressSupplierRbQuery esQuery = new ExpressSupplierRbQuery();
        esQuery.setEngName(query.getExpressSupplier());
        ExpressSupplierRb expressSupplierRb =  expressSupplierRbMapper.selectOne(esQuery);
        query.setExpressCnName(expressSupplierRb.getCnName());
        return query;
    }

    /**
     * 添加订单的物流信息
     *
     * @param expressInfoRb expressInfoRb
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/29 11:15
     * @return com.jiuy.base.model.MyLog<com.jiuy.rb.model.order.ExpressInfoRb>
     */
    @Override
    @MyLogs(logInfo = "添加订单物流信息",model = ModelType.ORDER_MODEL)
    public MyLog<ExpressInfoRb> addExpressInfo(ExpressInfoRb expressInfoRb, UserSession userSession) {

        int rs = expressInfoRbMapper.insertSelective(expressInfoRb);
        Declare.notNull(rs == 0,GlobalsEnums.ADD_FAILED);
        return new MyLog<ExpressInfoRb>(expressInfoRb,MyLog.Type.add,userSession).setData(expressInfoRb);
    }


    /**
     * 添加订单的物流信息
     *
     * @param expressInfoRb expressInfoRb
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/29 11:15
     * @return com.jiuy.base.model.MyLog<com.jiuy.rb.model.order.ExpressInfoRb>
     */
    @Override
    @MyLogs(logInfo = "修改订单物流信息",model = ModelType.ORDER_MODEL)
    public MyLog<ExpressInfoRb> updateExpressInfo(ExpressInfoRb expressInfoRb, UserSession userSession) {

        Declare.notNull(expressInfoRb.getOrderNo(),GlobalsEnums.PARAM_ERROR);

        ExpressInfoRbQuery query = new ExpressInfoRbQuery();
        query.setOrderNo(expressInfoRb.getOrderNo());

        ExpressInfoRb old = expressInfoRbMapper.selectOne(query);
        Declare.notNull(old,GlobalsEnums.ORDER_NOT_FOUND);

        expressInfoRb.setId(old.getId());
        int rs = expressInfoRbMapper.updateByPrimaryKeySelective(expressInfoRb);
        Declare.isFailed(rs == 0,GlobalsEnums.UP_FAILED);

        return new MyLog<ExpressInfoRb>(old,expressInfoRb,userSession).setData(expressInfoRb);
    }


    /**
     * 逻辑删除物流单号
     *
     * @param orders orders
     * @author Aison
     * @date 2018/6/29 16:49
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     */
    @MyLogs(logInfo = "逻辑删除物流单号",model = ModelType.ORDER_MODEL)
    @Override
    public MyLog<Long> removeExpress(Set<Long> orders, UserSession userSession) {

        ExpressInfoRbQuery query = new ExpressInfoRbQuery();
        query.setOrderNos(orders);
        List<ExpressInfoRb> expressInfos = expressInfoRbMapper.selectList(query);
        MyLog<Long> myLog = new MyLog<>(userSession);
        expressInfos.forEach(action->{
            ExpressInfoRb newUp = new ExpressInfoRb();
            newUp.setId(action.getId());
            newUp.setStatus(-1);
            myLog.moreLog(action,newUp,MyLog.Type.up);
            expressInfoRbMapper.updateByPrimaryKeySelective(newUp);
        });

        return myLog;
    }

}
