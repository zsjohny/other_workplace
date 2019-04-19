package com.yujj.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.account.BindUserRelation;
import com.jiuyuan.entity.account.UserBindLog;
import com.jiuyuan.entity.account.YJJNumber;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.account.User;

@DBMaster
public interface UserMapper {
	
	public User getUser(long userId);
	
//	public User getUser4Login(String userName);
	
    public User getUserByAllWay(String userName);

//    public User getUserByPhone(String userName);
    
//    public List<User> getUserListByPhone(String userName);
    
//    public User getUserByBindPhoneOnly(String userName);
    
//    public User getUserByUserNameOnly(String userName);

//    public User getUserByRelatedName(@Param("relatedName") String relatedName, @Param("userType") UserType userType);
    
//    public User getUserByWeixinId(@Param("relatedName") String relatedName);
    
//    public User getUserByBindWeixinOnly(@Param("relatedName") String relatedName);
    
    //public User getUserByAllWay(@Param("userName") String userName);

    public int addUser(User user);
    
    public int addUserNumberIncrease(User user);
    
    public int addUserBindLog(UserBindLog userBindLog);
    
    
    public List<YJJNumber> getYJJNumberList(@Param("startNum") long startNum ,@Param("endNum") long endNum , @Param("limit") int limit, @Param("status") int status);

    public int updateUserPassword(@Param("userId") long userId, @Param("password") String password);
    
    
    public int updateUserCid(@Param("userId") long userId, @Param("userCid") String userCid);
    
    public int userPhoneBind(@Param("userId") long userId, @Param("phone") String phone);
    
    public int userWeixinBind(@Param("userId") long userId, @Param("weiXin") String weiXin, @Param("nickName") String nickName, @Param("userIconUrl") String userIconUrl);
    public int delWeixinUserByUserId(@Param("userId") long userId);
    
    
    
    public int userWeixinUnbind(@Param("userId") long userId);

    public int updateUserInfo(@Param("userId") long userId, @Param("userNickName") String userNickName,
                              @Param("userIcon") String userIcon);
    
    public int updateUserNickName(@Param("userId") long userId, @Param("userNickName") String userNickName);
    
    public int updateUserIcon(@Param("userId") long userId, @Param("userIcon") String userIcon);

    public int updateYjjNumberUsed(@Param("numUsed") long numUsed );
    
    public int updateUserYjjNumber(@Param("userId") long userId, @Param("num") long num);

	public User getUserByYJJNumber(@Param("yJJNumber") long yJJNumber);

    int updateUserInvite(@Param("userId") long userId, @Param("weekInviteCount") int weekInviteCount, @Param("time") long time);

	int update(@Param("id") long userId, @Param("weekInviteOrderCount") int weekInviteOrderCount, @Param("lastInviteOrderTime") long time);

	public void addBindUserRelation(BindUserRelation bindUserRelation);

	public BindUserRelation getBindUserRelationByOpenId(@Param("openId") String openId,@Param("type") int type);
	
	public BindUserRelation getBindUserRelationByUid(@Param("weixinId") String weixinId,@Param("type") int type);
	
	
	public StoreBusiness getStoreBusiness4Login(String userName);
	
	public StoreBusiness getStoreBusinessByPhone(String phone);

	
	
	
	
	public void updBindUserRelationUserId(@Param("bindUserRelationId") long bindUserRelationId,@Param("userId") long userId);
	
	
}
