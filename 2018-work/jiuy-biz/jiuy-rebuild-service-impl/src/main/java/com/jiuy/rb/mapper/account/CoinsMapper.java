package com.jiuy.rb.mapper.account; 
 
import com.jiuy.base.model.Query;
import com.jiuy.rb.model.account.Coins;
import com.jiuy.base.mapper.BaseMapper;
import com.jiuy.rb.model.account.CoinsQuery;
import com.jiuy.rb.model.account.CoinsUpVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 
 * 玖币表 的mapper 文件
 
 * @author Aison
 * @version V1.0 
 * @date 2018年07月18日 下午 05:24:57
 * @Copyright 玖远网络 
 */
public interface CoinsMapper extends BaseMapper<Coins>{  

	// @Costom 
	// 从上一行注释以上为系统生成 从这一行往下为用户自定义 请将自定义的sql对应的接口放在此行下面


    /**
     * 加减可用的玖币
     *
     * @param coinsUpVo 更新类
     * @author Aison
     * @date 2018/7/5 10:48
     * @return int
     */
    int modifyAliveCoins(CoinsUpVo coinsUpVo);



    /**
     * 加减不可用的玖币
     *
     * @param coinsUpVo 更新类
     * @author Aison
     * @date 2018/7/5 10:48
     * @return int
     */
    int modifyUnAliveCoins(CoinsUpVo coinsUpVo);

    /**
     * 将金额从可用转移到不可用 或者将不可用金额转移到可用
     * 如果是正数..则是将不可用转移到可用
     * 如果是负数..则是从可用转移到不可用
     *
     * @param coinsUpVo coinsUpVo
     * @author Aison
     * @date 2018/7/18 13:43
     * @return int
     */
    int moveUnAlive2Live(CoinsUpVo coinsUpVo);


    /**
     * 添加版本号
     *
     * @param userId 用户id
     * @param userType 用户类型
     * @author Aison
     * @date 2018/7/5 10:52
     * @return int
     */
    int updateVersion(@Param("userId") Long userId, @Param("userType") Integer userType);




    /**
     * 通过coinsId和status进行分组 返回一个map
     *
     * @param param param 有两个key  coinsIds coins的id  statuses 需要筛选的状态
     * @author Aison
     * @date 2018/7/11 15:54
     * @return java.util.Map<java.lang.String,com.jiuy.rb.model.account.CoinsQuery>
     */
    @MapKey("coinsGroup")
    Map<String,CoinsQuery> selectDetailSumGroup(Map<String,Object> param);


    /**
     * 查询列表跟小程序用户关联查询
     *
     * @param query query
     * @author Aison
     * @date 2018/7/11 17:21
     * @return java.util.List<com.jiuy.rb.model.account.Coins>
     */
    List<CoinsQuery> selectCoinsWithMember(Query query);


    /**
     * 通过id 查找并且锁定记录
     *
     * @param id id
     * @author Aison
     * @date 2018/7/19 15:56
     * @return com.jiuy.rb.model.account.Coins
     */
    Coins  selectByIdForUpdate(Long id);


}
