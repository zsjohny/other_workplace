package com.jiuyuan.dao.mapper.supplier;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.newentity.BrandNew;


import java.util.List;
import java.util.Map;

/**
 * <p>
  * 品牌表 Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2017-10-16
 */
@DBMaster
public interface BrandNewMapper extends BaseMapper<BrandNew> {


    /**
     * 品牌页面查询
     * @param param param
     * @param page 分页
     * @author Aison
     * @date 2018/6/26 10:24
     */
    List<BrandVO> selectBrandList(Pagination page , Map<String, Object> param);

}