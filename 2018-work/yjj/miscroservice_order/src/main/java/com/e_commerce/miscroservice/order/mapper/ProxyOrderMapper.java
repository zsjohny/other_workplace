package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrder;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyOrderQuery;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyReward;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.mapper.BaseMapper;
import com.e_commerce.miscroservice.order.vo.ProxyRefereeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述
 * @date 2018/9/18 17:25
 * @return
 */
@Mapper
public interface ProxyOrderMapper extends BaseMapper<ProxyOrder> {

    @Select("SELECT * FROM `yjj_proxy_order` ord WHERE ord.order_no=#{orderId} ORDER BY ord.create_time desc")
    List<ProxyOrder> getOrderByOrderId(@Param("orderId")String orderId);

    @Select("SELECT * FROM `yjj_proxy_order` ord WHERE ord.user_id=#{userId} and  ord.`status`=1 ORDER BY ord.create_time desc")
    List<ProxyOrderQuery> getOrderByUserId(@Param("userId")long userId);

    @Select("SELECT * FROM `yjj_proxy_order` ord WHERE ord.is_profit=1 ORDER BY ord.create_time desc")
    List<ProxyOrder> getProfitOrderList(@Param("isProfit")int isProfit);

    List<ProxyReward> getRewardOrderList(@Param("userId")long userId,@Param("startTime")String startTime ,@Param("endTime")String endTime,@Param("isGrants")List<Integer> isGrants);

    List<ProxyOrder> getOrderList(ProxyOrderQuery query);

    BigDecimal getCollectReward(@Param("userId")long userId,@Param("isGrants")List<Integer> isGrants);

    BigDecimal getTodayCollectReward(@Param("userId")long userId,@Param("isGrants")List<Integer> isGrants);

    @Select("SELECT re.referee_user_id as refereeUserId,re.refere_parent_id as refereParentId,re.recommon_user_type as recommonUserType,re.recommon_user_id as recommonUserId FROM `yjj_proxy_referee` re where re.`status` = 0 and re.recommon_user_id=#{userId}")
    ProxyRefereeVo getRefereeUser(@Param("userId")long userId);

    @Select("SELECT re.referee_user_id as refereeUserId,re.refere_parent_id as refereParentId,re.recommon_user_type as recommonUserType,re.recommon_user_id as recommonUserId FROM `yjj_proxy_referee` re where re.recommon_user_id=#{userId}")
    ProxyRefereeVo getAllRefereeUser(@Param("userId")long userId);

    @Select("SELECT * from yjj_proxy_order ord where ord.user_id in (SELECT re.recommon_user_id FROM yjj_proxy_referee re WHERE re.`status` = 0 AND" +
            " ((re.referee_user_id = #{userId} AND re.recommon_user_type =#{type}) or (re.refere_parent_id = #{userId} AND re.recommon_user_type =#{type}))" +
            ")")
    List<ProxyOrder> getOrderByUserIdAndType(@Param("userId")long userId,@Param("type")int type);


    /**
     * 描述 查询代理订单列表
     * @author hyq
     * @date 2018/9/29 18:43
     * @return
     */
//    @Select("SELECT re.referee_user_id,re.recommon_user_id,re.refere_parent_id,ord.* " +
//            "FROM yjj_proxy_referee re LEFT JOIN yjj_proxy_order ord on ord.user_id=re.recommon_user_id and ord.`status`=1 WHERE " +
//            "(( re.referee_user_id = #{userId} AND re.recommon_user_type = 2 ) OR ( re.refere_parent_id = #{userId} AND re.recommon_user_type = 2 ))")
    @Select ("SELECT b.*\n" +
            "FROM yjj_proxy_reward a\n" +
            "JOIN yjj_proxy_order b ON a.order_no = b.order_no AND b.`status` = 1\n" +
            "WHERE a.user_id = #{userId}\n" +
            "AND a.order_user_role = 2 " +
            "ORDER BY b.create_time DESC")
    List<ProxyOrderQuery> getOrderByUserIdAndTypeTwo(@Param("userId")long userId);

    /**
     * 描述 查询客户订单列表
     * @author hyq
     * @date 2018/9/29 18:43
     * @return
     */
    /*@Select("SELECT re.referee_user_id,re.recommon_user_id,re.refere_parent_id," +
            "" +
            "ord.* FROM yjj_proxy_referee re LEFT JOIN yjj_proxy_order ord on ord.user_id=re.recommon_user_id and ord.`status`=1 WHERE " +
            "(( re.referee_user_id = #{userId} AND re.recommon_user_type = 1 ) OR (re.refere_parent_id = #{userId} AND re.recommon_user_type = 1 ))")
*/
    @Select ("SELECT b.*\n " +
            "FROM yjj_proxy_reward a\n" +
            "JOIN yjj_proxy_order b ON a.order_no = b.order_no AND b.`status` = 1\n" +
            "WHERE a.user_id = #{userId}\n" +
            "AND a.order_user_role = 0 " +
            "ORDER BY b.create_time DESC")
    List<ProxyOrderQuery> getOrderByUserIdAndTypeOne(@Param("userId")long userId);

    @Select("SELECT cus.`name` FROM `yjj_proxy_customer` cus where cus.user_id=#{userId}")
    String getRefereeCustomerName(@Param("userId")long userId);

    @Select("SELECT acc.wx_name from yjj_public_account_user acc where acc.id=#{userId}")
    String getRefereePublicName(@Param("userId")long userId);

}
