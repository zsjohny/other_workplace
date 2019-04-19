package com.jiuyuan.service.common.logs;


import com.jiuyuan.entity.log.UserLoginLogNew;

/**
 * @version V1.0
 * @Package com.jiuyuan.service.common.logs
 * @Description:
 * @author: Aison
 * @date: 2018/5/15 18:06
 * @Copyright: 玖远网络
 */
public interface ILogsService {

    /**
     * 添加登陆记录
     * @param userLoginLog
     * @date:   2018/5/15 17:02
     * @author: Aison
     */
    void addUserLoginLog(UserLoginLogNew userLoginLog);


    /**
     * 单设备登陆
     * @param userLoginLog
     * @date:   2018/5/15 17:03
     * @author: Aison
     */
    void singleDeviceLogin(UserLoginLogNew userLoginLog);

    /**
     * 更新
     * @param userLoginLog
     * @date:   2018/5/15 18:56
     * @author: Aison
     */
    void update(UserLoginLogNew userLoginLog);


    /**
     * 用户登出
     * @param userLoginLogNew
     * @date:   2018/5/16 12:11
     * @author: Aison
     */
    void logout(UserLoginLogNew userLoginLogNew);



}
