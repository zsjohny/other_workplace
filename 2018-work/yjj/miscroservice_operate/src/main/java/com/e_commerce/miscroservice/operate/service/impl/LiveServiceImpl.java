package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.LiveDao;
import com.e_commerce.miscroservice.operate.entity.request.AddAnchorRequest;
import com.e_commerce.miscroservice.operate.entity.request.FindLiveRequest;
import com.e_commerce.miscroservice.operate.entity.response.FindAllLiveResponse;
import com.e_commerce.miscroservice.operate.service.live.LiveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 直播
 * @Author hyf
 * @Date 2019/1/15 15:39
 */
@Service
public class LiveServiceImpl implements LiveService {
    private Log logger = Log.getInstance(LiveServiceImpl.class);
    @Resource
    private LiveDao liveDao;
    @Override
    public Response findOfficialAnchor(FindLiveRequest liveRequest) {
        logger.info("查询官方主播列表={}", liveRequest);
        List<FindAllLiveResponse> liveUser = liveDao.findAllAnchorByType(liveRequest,1);
        SimplePage<FindAllLiveResponse> simplePage = new SimplePage<>(liveUser);
        return Response.success(simplePage);
    }

    @Override
    public Response findStoreAnchor(FindLiveRequest liveRequest) {
        logger.info("查询店家主播列表={}", liveRequest);
        List<FindAllLiveResponse> liveUser = liveDao.findAllAnchorByType(liveRequest,0);
        SimplePage<FindAllLiveResponse> simplePage = new SimplePage<>(liveUser);
        return Response.success(simplePage);
    }

    @Override
    public Response findCommonAnchor(FindLiveRequest liveRequest) {
        logger.info("查询普通主播列表={}", liveRequest);
        List<FindAllLiveResponse> liveUser = liveDao.findAllAnchorByType(liveRequest,3);
        liveUser.forEach(user ->{
            LiveUser live = liveDao.findAnchorPhoneNumberByStoreIdType(user.getStoreId(),3);
            user.setUpName(live.getName());
            user.setUpPhone(live.getPhone());
        });
        SimplePage<FindAllLiveResponse> simplePage = new SimplePage<>(liveUser);
        return Response.success(simplePage);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response addAnchor(AddAnchorRequest obj) {


        LiveUser li  = liveDao.findAnchorByPhone(obj.getPhone());
        if (li!=null){
            logger.warn("该手机号已存在={}", li);
            return Response.errorMsg("该手机号已存在");
        }
        LiveUser liveUser = new LiveUser();
        liveUser.setAge(obj.getAge());
        liveUser.setIcon(obj.getIcon());
        liveUser.setIdCard(obj.getIdCard());
        liveUser.setLiveType(1);
        liveUser.setName(obj.getName());
        liveUser.setNickName(obj.getNickName());
        liveUser.setPhone(obj.getPhone());
        liveUser.setSex(obj.getSex());
        liveUser.setRoomNum(System.currentTimeMillis());
        liveUser.setLiveRoomType(TaskTypeEnums.LIVE_SPECIAL_PLATFORM.getKey());
        liveDao.addAnchor(liveUser);
        return Response.success();
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response delOfficialAnchor(Long id) {
        if (id==null){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        LiveUser liveUser = new LiveUser();
        liveUser.setDelStatus(1);
        liveUser.setId(id);
        liveDao.upOfficialAnchor(liveUser);
        return Response.success();
    }
}
