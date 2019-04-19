package com.jiuy.rb.service.user;

import com.jiuy.rb.model.user.LinkUs;
import com.jiuy.rb.model.user.ShopMemberRb;
import com.jiuy.rb.model.user.ShopMemberRbQuery;

import java.util.List;
import java.util.Map;

/**
 * 小程序用户
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/23 10:40
 * @Copyright 玖远网络
 */
public interface IShopMemberService {

    /**
     * 通过条件查询一个member
     *
     * @param query query
     * @author Aison
     * @date 2018/7/23 10:42
     * @return com.jiuy.rb.model.user.ShopMemberRb
     */
    ShopMemberRb getShopMember(ShopMemberRbQuery query);


    /**
     * 用户与我们小程序的关系
     * @param linkUs linkUs
     * @author Aison
     * @date 2018/7/24 10:17
     * @return com.jiuy.rb.model.user.LinkUs
     */
    LinkUs doLinkUs(LinkUs linkUs);


    /**
     * 查询小程序的映射
     *
     * @param memberIds memberIds
     * @author Aison
     * @date 2018/8/7 16:25
     * @return java.util.Map<java.lang.Long,com.jiuy.rb.model.user.ShopMemberRb>
     */
    Map<Long,ShopMemberRb> getMemberMap(List<Long> memberIds);

}
