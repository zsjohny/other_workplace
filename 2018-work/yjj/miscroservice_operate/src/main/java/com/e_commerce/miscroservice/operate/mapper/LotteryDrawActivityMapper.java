package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.activity.ActivityUser;
import com.e_commerce.miscroservice.commons.entity.activity.LotteryDrawActivity;
import com.e_commerce.miscroservice.operate.entity.response.LotteryDrawPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/12/18
 */
@Mapper
public interface LotteryDrawActivityMapper {

    /**
     * 找到所有活动产品
     *
     * @param id
     * @param timeStart
     * @param timeEnd
     * @return
     */
    List<LotteryDrawPo> findAllProductList(@Param("id") Long id, @Param("timeStart") String timeStart, @Param("timeEnd") String timeEnd);

    /**
     * 删除图片
     * @param id
     */
    void delPicture(@Param("id") Long id);

    /**
     * 查询产品详情
     * @param id
     * @return
     */
    LotteryDrawPo findProductById(@Param("id") Long id);

    /**
     * 查询参加人员
     * @param phone
     * @param code
     * @return
     */
    List<ActivityUser> findJoin(@Param("phone") String phone, @Param("code")Integer code);

    /**
     * 根据类型查询
     * @param code
     * @return
     */
    LotteryDrawActivity findProductByType(@Param("code") Integer code);
}
