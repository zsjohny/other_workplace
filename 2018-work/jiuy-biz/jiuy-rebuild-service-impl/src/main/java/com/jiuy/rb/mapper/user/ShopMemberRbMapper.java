package com.jiuy.rb.mapper.user; 
 
import com.jiuy.base.model.Query;
import com.jiuy.rb.model.user.ShopMemberRb;
import com.jiuy.base.mapper.BaseMapper;
import com.store.entity.member.ShopMember;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/** 
 * 小程序会员表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月23日 上午 10:39:37
 * @Copyright 玖远网络 
 */
public interface ShopMemberRbMapper extends BaseMapper<ShopMemberRb>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 查询转成map
     *
     * @param query query
     * @author Aison
     * @date 2018/8/7 16:29
     * @return java.util.Map<java.lang.Long,com.jiuy.rb.model.user.ShopMemberRb>
     */
    @MapKey("id")
    Map<Long,ShopMemberRb>  selectMap(Query query);

    String findBindWeiXin(@Param("memberId") Long memberId);
}