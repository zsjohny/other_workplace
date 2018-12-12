package com.goldplusgold.td.sltp.core.controller;

import com.goldplusgold.td.sltp.core.operate.component.RedisHashOperateComponent;
import com.goldplusgold.td.sltp.core.operate.enums.DbOperateEnum;
import com.goldplusgold.td.sltp.core.operate.enums.InstTypeEnum;
import com.goldplusgold.td.sltp.core.parammodel.UserSltpOperPM;
import com.goldplusgold.td.sltp.core.parammodel.UserSltpSavePM;
import com.goldplusgold.td.sltp.core.parammodel.UserSltpUpdatePM;
import com.goldplusgold.td.sltp.core.service.UserSltpService;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpAllVM;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpOneVM;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpOperVM;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

/**
 * 止盈止损用户服务层
 * Created by Ness on 2017/5/10.
 */
@RestController
@RequestMapping("sltp")
public class UserSltpController {

    @Autowired
    private UserSltpService userSltpService;

    @Autowired
    RedisHashOperateComponent component;

    @GetMapping("index")
    public String index() {

        for (int i = 0; i < 10; i++) {
            UserSltpRecord userSltpRecord = new UserSltpRecord();
            userSltpRecord.setUserId((int) (Math.random() * 1000) + "");
            userSltpRecord.setUuid((int) (Math.random() * 1000) + "");
            userSltpRecord.setContract(InstTypeEnum.AG.toName());
            userSltpRecord.setCommissionStartDate(System.currentTimeMillis());
            userSltpRecord.setCommissionEndDate(userSltpRecord.getCommissionStartDate() + UserSltpRecord.NOWDAY_EXPIRE_TIME);
            userSltpRecord.setBearBull(UserSltpRecord.SltpType.BULL.toType());
            userSltpRecord.setSltpType(UserSltpRecord.SltpType.SL.toType());
            userSltpRecord.setSlPrice(270d + Math.random() * 10);
            userSltpRecord.setLots(2);
            userSltpRecord.setFloatPrice(2.00);
            userSltpRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
            component.add(userSltpRecord);
        }
        return "ok";
    }

    /**
     * 根据 止盈止损的id查询 单记录
     *
     * @param userSltpPM 止盈止损的id
     * @return
     */
    @GetMapping("get")
    public UserSltpOneVM getUserSltpInfo(UserSltpOperPM userSltpPM) {

        return userSltpService.findUserSltpOneByUuid(userSltpPM == null ? null : userSltpPM.getId());
    }


    /**
     * 插入新的止盈止损记录
     *
     * @return
     */
    @GetMapping("save")
    public UserSltpOperVM insertUserSltpInfo(UserSltpSavePM userSltpPM) {

        return userSltpService.saveUserSltpRecordInfo(userSltpPM == null ? null : userSltpPM.pm2UserSltp(), DbOperateEnum.REDIS.getFlag());

    }

    /**
     * 更新的止盈止损记录
     *
     * @return
     */
    @GetMapping("update")
    public UserSltpOperVM updateUserSltpInfo(UserSltpUpdatePM userSltpPM) {

        return userSltpService.updateUserSltpRecordInfoByUuid(userSltpPM == null ? null : userSltpPM.pm2UserSltp(), DbOperateEnum.REDIS.getFlag());

    }


    /**
     * 删除的止盈止损记录
     *
     * @param userSltpPM 止盈止损的id
     * @return
     */
    @GetMapping("delete")
    public UserSltpOperVM removeUserSltpInfoByUuid(UserSltpOperPM userSltpPM) {

        return userSltpService.removeUserSltpRecordByUuid(userSltpPM == null ? null : userSltpPM.getId());

    }


    /**
     * 根据 止盈止损的id查询 所有记录
     *
     * @return
     */
    @GetMapping("getAll")
    public UserSltpAllVM findUserSltpInfoAll(UserSltpOperPM userSltpPM) {

        return userSltpService.findAllUserSltpInByUserId(userSltpPM == null ? null : userSltpPM.getId(),
                userSltpPM == null ? null : userSltpPM.getClickType(), userSltpPM == null ? null : userSltpPM.getName(),
                userSltpPM == null ? null : userSltpPM.getType(), userSltpPM == null ? null : userSltpPM.getPage());
    }


}
