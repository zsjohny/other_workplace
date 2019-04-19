package com.jiuyuan.service.common.vedio;

import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.dao.mapper.MainVedioMapper;
import com.store.entity.vedio.MainVedio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页的视频的增删改查
 * </p>
 *
 * @author Aison
 * @since 2018-04-23
 */
@Service
public class MainVedioService  {

    @Autowired
    private MainVedioMapper mainVedioMapper;

    /**
     * 添加首页视频记录
     * @param mainVedio
     * @date:   2018/4/23 16:22
     * @author: Aison
     */
    public void  addMainVedio(MainVedio mainVedio,Integer userId) {

        if(BizUtil.hasEmpty(mainVedio.getVedioUrl())) {
            throw BizException.defulat().msg("视频地址不能为空");
        }
        mainVedio.setCreateTime(System.currentTimeMillis());
        mainVedio.setLastUpdateTime(System.currentTimeMillis());
        mainVedio.setCreateUserId(Long.valueOf(userId));
        mainVedio.setLastUpdateUserId(Long.valueOf(userId));
        int rs = mainVedioMapper.insert(mainVedio);
        if(rs == 0) {
            throw BizException.defulat().msg("添加视频失败");
        }
    }

    /**
     * 修改视频记录
     * @param mainVedio
     * @date:   2018/4/23 16:27
     * @author: Aison
     */
    public void updateMainVedio(MainVedio mainVedio,Integer userId) {

        if(BizUtil.hasEmpty(mainVedio.getId())) {
            throw BizException.defulat().msg("视频信息不存在请检查id");
        }
        MainVedio old = mainVedioMapper.selectById(mainVedio.getId());
        if(old == null) {
            throw BizException.defulat().msg("视频记录Id错误");
        }
        mainVedio.setLastUpdateUserId(Long.valueOf(userId));
        if(mainVedioMapper.updateById(mainVedio)== 0) {
            throw BizException.defulat().msg("修改失败");
        }
    }

    /**
     * 删除视频
     * @param id
     * @date:   2018/4/23 18:00
     * @author: Aison
     */
    public void delete(Long id) {
        if(BizUtil.hasEmpty(id)) {
            throw BizException.defulat().msg("id为空");
        }

        if(mainVedioMapper.deleteById(id) == 0) {
            throw BizException.defulat().msg("删除视频失败");
        }
    }

    /**
     * 查询列表
     * @param query
     * @date:   2018/4/23 16:31
     * @author: Aison
     */
    public List<MainVedio> mainVedioList(Map<String,Object> query) {

        return mainVedioMapper.selectByMap(query);
    }

}
