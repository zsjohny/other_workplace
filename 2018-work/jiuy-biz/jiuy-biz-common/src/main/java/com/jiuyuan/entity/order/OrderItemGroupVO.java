package com.jiuyuan.entity.order;

import java.util.List;

import com.jiuyuan.entity.order.OrderItemGroup;
import com.yujj.business.assembler.composite.BrandComposite;
import com.yujj.entity.Brand;
import com.yujj.entity.order.OrderItemVO;

public class OrderItemGroupVO extends OrderItemGroup implements BrandComposite {

    private static final long serialVersionUID = 1364403641169113108L;

    private Brand brand;

    private List<OrderItemVO> orderItems;

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<OrderItemVO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemVO> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public void assemble(Brand brand) {
        this.brand = brand;
    }
}
