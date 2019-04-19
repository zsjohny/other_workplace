package com.yujj.business.handler.account;

import com.jiuyuan.entity.ClientPlatform;
import com.yujj.entity.account.User;

public interface UserHandler {

    void postUserCreation(User user, String inviteCode, ClientPlatform clientPlatform);
    
    void postUserCreation(User user, String inviteCode, String relatedId, ClientPlatform clientPlatform);
    
	void postUserCreation(User user, long yJJNumber, ClientPlatform clientPlatform);
	
	void handleInvite(User user, long yJJNumber, long time, ClientPlatform clientPlatform);
	
    
}
