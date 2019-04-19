package com.yujj.layimjavaclient.socket.manager;

//import dao.LayIMDao;
//import org.omg.CORBA.PRIVATE_MEMBER;
//import util.cache.LayIMCache;

import java.util.List;

import com.yujj.layimjavaclient.dao.LayIMDao;
import com.yujj.layimjavaclient.util.cache.LayIMCache;

/**
 * Created by pz on 16/11/28.
 */
public class GroupUserManager {
    private static final String cacheName = "LAYIM_GROUP";
    private static final String cacheKey = "GM_";

    //每个组存一个
    private String getCacheKey(int groupId){
        return cacheKey + groupId;
    }

    //将某个组的用户id存入缓存  key=》list
    public boolean saveGroupMemberIds(int groupId, List<String> userIds) {
        String key = getCacheKey(groupId);
        LayIMCache.getInstance().setListCache(cacheName,key,userIds);
        return true;
    }

    public List<String> getGroupMembers(int groupId){
        String key = getCacheKey(groupId);
        List<String> list = LayIMCache.getInstance().getListCache(cacheName,key);
        if (list == null || list.size()==0) {
            System.out.println("缓存中没有数据，需要从数据库读取");
            LayIMDao dao = new LayIMDao();
            List<String> memebers = dao.getMemberListOnlyIds(groupId);
            saveGroupMemberIds(groupId, memebers);
            return memebers;
        }else{
            System.out.println("直接从缓存中读取出来");
        }
        return list;
    }
}
