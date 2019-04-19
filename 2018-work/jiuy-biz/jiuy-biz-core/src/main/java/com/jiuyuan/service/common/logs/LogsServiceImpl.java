package com.jiuyuan.service.common.logs;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jiuyuan.dao.mapper.store.UserLoginLogNewMapper;
import com.jiuyuan.entity.log.UserLoginLogNew;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.GetuiUtil;
import com.util.GeTuiType;
import com.util.GeTuiVo;
import com.util.LoginWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @version V1.0
 * @Package com.jiuyuan.service.common.logs
 * @Description:
 * @author: Aison
 * @date: 2018/5/15 17:00
 * @Copyright: 玖远网络
 */

@Service
public class LogsServiceImpl implements  ILogsService {



    @Autowired
    private UserLoginLogNewMapper userLoginLogNewMapper;

    /**
     * 添加登陆记录
     * @param userLoginLog
     * @date:   2018/5/15 17:02
     * @author: Aison
     */
    @Override
    public void addUserLoginLog(UserLoginLogNew userLoginLog) {
        userLoginLogNewMapper.insert(userLoginLog);
    }

    /**
     * 单设备登陆
     * @param userLoginLog
     * @date:   2018/5/15 17:03
     * @author: Aison
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void singleDeviceLogin(UserLoginLogNew userLoginLog) {

        // 添加记录
        // addUserLoginLog(userLoginLog);

        // 查询登陆的设备
        EntityWrapper<UserLoginLogNew> wrapper = new EntityWrapper<>();
        wrapper.eq("user_name",userLoginLog.getUserName());
        wrapper.and("id <>"+userLoginLog.getId()).eq("alive",1).and("device_id <> '"+userLoginLog.getDeviceId()+"'");
        List<UserLoginLogNew> userLoginLogLists = userLoginLogNewMapper.selectList(wrapper);
        Set<String> cids = new HashSet<>();
        // 如果只有一个则reutun
        if(userLoginLogLists.size()==0) {
            return ;
        } else {
            // 将所有的都设置成失效
            for (UserLoginLogNew userLoginLogList : userLoginLogLists) {
                userLoginLogList.setAlive(0);
                userLoginLog.setOfflineTime(System.currentTimeMillis());
                userLoginLogNewMapper.updateById(userLoginLogList);
                cids.add(userLoginLogList.getCid());
            }
        }
        String phone = userLoginLog.getUserName();
        // 推送消息给手机
        StringBuilder stringBuilder = new StringBuilder();
        LoginWay loginWay = LoginWay.getWayEnum(userLoginLog.getLoginWay());

        stringBuilder.append("你的门店宝账号于").append(BizUtil.formart(userLoginLog.getCreateTime())).append("在")
                .append(userLoginLog.getDeviceName()).append("设备上通过").append(loginWay.getName()).append("登陆。")
                .append(loginWay.getComment());

          // 不要给用户推送消息
//        GeTuiVo geTuiVo = GeTuiVo.getIntance(stringBuilder.toString(),"","","",System.currentTimeMillis()+"",GeTuiType.Type100.getType(),phone);
//        String rs = GetuiUtil.touchuan(cids,BizUtil.bean2json(geTuiVo));
//        if(!geTuiVo.isSuccess(rs)) {
//            throw BizException.defulat().msg(rs);
//        }
    }

    /**
     * 更新
     * @param userLoginLog
     * @date:   2018/5/15 18:56
     * @author: Aison
     */
    @Override
    public void update(UserLoginLogNew userLoginLog) {

        userLoginLogNewMapper.updateById(userLoginLog);
    }


    /**
     * 用户登出
     * @param userLoginLogNew
     * @date:   2018/5/16 12:11
     * @author: Aison
     */
    @Override
    public void logout(UserLoginLogNew userLoginLogNew) {
        userLoginLogNewMapper.logout(userLoginLogNew.getDeviceId(),userLoginLogNew.getUserName());
    }

}
