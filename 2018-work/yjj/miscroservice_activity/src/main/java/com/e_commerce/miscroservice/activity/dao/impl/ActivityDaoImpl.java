package com.e_commerce.miscroservice.activity.dao.impl;


import com.e_commerce.miscroservice.activity.dao.ActivityDao;
import com.e_commerce.miscroservice.activity.entity.ActivityUser;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.stereotype.Repository;

/**
 * Create by hyf on 2018/10/8
 */
@Repository
public class ActivityDaoImpl implements ActivityDao {

    /**
     * 根据手机号和活动类型查询
     *
     * @param phone
     * @param code
     * @return
     */
    @Override
    public ActivityUser findActivityUser(String phone, Integer code) {
        ActivityUser activityUser = MybatisOperaterUtil.getInstance().findOne(new ActivityUser(), new MybatisSqlWhereBuild(ActivityUser.class).eq(ActivityUser::getPhone, phone).eq(ActivityUser::getStatus, code).eq(ActivityUser::getDelStatus, StateEnum.NORMAL));

        return activityUser;
    }
    /**
     * 保存活动用户
     * @param user
     */
    @Override
    public void addActivityUser(ActivityUser user) {
        MybatisOperaterUtil.getInstance().save(user);
    }
}
