package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.product.vo.SupplierProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/2/15 13:33
 */
@Mapper
public interface SupplierUserProductConfigMapper {


    /**
     * 查询品牌混批
     *
     * @param brandIdList brandIdList
     * @return com.e_commerce.miscroservice.product.vo.SupplierProductDTO
     * @author Charlie
     * @date 2019/2/15 13:53
     */
    List<SupplierProductDTO> listTholesaleTipByBrandIds(@Param( "brandIdList" ) List<Long> brandIdList);

}
