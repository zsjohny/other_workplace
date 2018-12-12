package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.UserDao;
import com.finace.miscroservice.user.entity.response.MsgSizeResponse;
import com.finace.miscroservice.user.mapper.UserMapper;
import com.finace.miscroservice.user.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * user Dao层实现类
 */
@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserPO getUserByUserPhone(String userPhone) {
        return userMapper.findUserOneByUserPhone(userPhone);
    }

    @Override
    public UserPO getUserByUsername(String username){

        return  userMapper.findUserOneByUserPhone(username);
    }

    @Override
    public int insterUser(UserPO user){
        userMapper.insterUser(user);
        return user.getUser_id();
    }

    @Override
    public UserPO findUserOneById(String userid){

        return  userMapper.findUserOneById(userid);
    }

    @Override
    public UserPO getUserLoginInfo(String username, String password){

        return  userMapper.getUserLoginInfo(username, password);
    }

    @Override
    public int updateUserPass(Map<String, String> map){
        return  userMapper.updateUserPass(map);
    }

    @Override
    public List<UserPO> getUserListByInviter(int inviter) {
        return userMapper.getUserListByInviter(inviter);
    }

    @Override
    public int getUserCountByInviter(int inviter) {
        return userMapper.getUserCountByInviter(inviter);
    }

	@Override
	public int getCountNewUserNum() {
		return userMapper.getCountNewUserNum();
	}

	@Override
    public int updateUserRating(String userId, Integer number){
          Map<String, Object> map = new HashMap<>();
          map.put("memberlevel", number);
          map.put("userId", userId);
          return userMapper.updateUserRating(map);
    }

    @Override
    public String getUserPhoneByUserId(String userId) {
        return userMapper.getUserPhoneByUserId(userId);
    }

    @Override
    public void addFeedBack(Integer userId, String content, String ipadress, Integer code, String value, String phone, String username, String addTime) {
        userMapper.addFeedBack(userId,content,ipadress,code,value,phone,username,addTime);
    }

    @Override
    public void addMsg(Integer userId, Integer msgCode,String topic, String msg, Long  addtime) {
        userMapper.addMsg(userId, msgCode, topic, msg,addtime);
    }

    @Override
    public  List<MsgSizeResponse> findMsgSize(Integer userId) {
        return userMapper.findMsgSize(userId);
    }

    @Override
    public List<UserPO> getUsersByInviterInTime(int inviter, String starttime, String endtime) {
        return userMapper.getUsersByInviterInTime(inviter,starttime,endtime);
    }

    @Override
    public int getUserCountByInviterInTime(int inviter, String starttime, String endtime) {
        return userMapper.getUserCountByInviterInTime(inviter,starttime,endtime);
    }


}
