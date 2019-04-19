package com.jiuyuan.service.common.area;

import com.baomidou.mybatisplus.mapper.Condition;
import com.jiuyuan.dao.mapper.common.AreaMapper;
import com.jiuyuan.entity.common.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Package com.jiuyuan.service.common.area
 * @Description: 行政区划
 * @author: Aison
 * @date: 2018/4/28 15:53
 * @Copyright: 玖远网络
 */
@Service
public class AreaService {

    @Autowired
    private AreaMapper areaMapper;
    /**
     * 获取省份的分组
     * @param
     * @date:   2018/4/28 15:57
     * @author: Aison
     */
    public Map<String,List<Area>> getGroupProvince() {

        Map<String,List<Area>> groupMap = new HashMap<>();
        List<Area> areas = areaMapper.selectList(Condition.create().and("city_code = ''").orderBy("group_name"));
        for (Area area : areas) {
            String groupName = area.getGroupName();
            List<Area> areaItem = groupMap.get(groupName);
            if(areaItem == null) {
                areaItem = new ArrayList<>();
            }
            areaItem.add(area);
            groupMap.put(groupName,areaItem);
        }
        return groupMap;
    }

    /**
     * 通过省份名称获取
     * @param provinceName
     * @date:   2018/5/3 17:54
     * @author: Aison
     */
    public Area getByProvinceName(String provinceName) {
        List<Area> areas = areaMapper.selectList(Condition.create().eq("province_name",provinceName));
        return areas.size() == 0 ? null :areas.get(0);
    }
}
