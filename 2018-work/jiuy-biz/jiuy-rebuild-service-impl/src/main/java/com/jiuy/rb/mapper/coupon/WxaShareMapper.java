package com.jiuy.rb.mapper.coupon; 
 
import com.jiuy.rb.model.coupon.WxaShare; 
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Set;

/** 
 * 分享关系表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月05日 下午 05:13:44
 * @Copyright 玖远网络 
 */
public interface WxaShareMapper extends BaseMapper<WxaShare>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面

    /**
     * 获取分组 每个用户邀请了多少人
     *
     * @param userIds userIds
     * @author Aison
     * @date 2018/7/11 18:00
     * @return java.util.Map<java.lang.Long,java.util.Map<java.lang.String,java.lang.Object>>
     */
    @MapKey("sourceUser")
    Map<Long,Map<String,Object>> selectWxaCountGroup(@Param("userIds") Set<Long> userIds);

}
