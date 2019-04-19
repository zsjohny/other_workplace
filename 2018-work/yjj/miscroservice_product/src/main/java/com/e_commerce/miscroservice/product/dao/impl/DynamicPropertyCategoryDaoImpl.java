package com.e_commerce.miscroservice.product.dao.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.product.dao.DynamicPropertyCategoryDao;
import com.e_commerce.miscroservice.product.entity.DynamicPropertyCategory;
import com.e_commerce.miscroservice.product.mapper.DynamicPropertyProductMapper;
import com.e_commerce.miscroservice.product.vo.ProductPropertyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/31 16:11
 */
@Component
public class DynamicPropertyCategoryDaoImpl implements DynamicPropertyCategoryDao {

    @Autowired
    private DynamicPropertyProductMapper dynamicPropertyProductMapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductPropertyDTO> findByProductId(Long productId, Long categoryId) {

        List<DynamicPropertyCategory> dpcList = MybatisOperaterUtil.getInstance().finAll(
                new DynamicPropertyCategory(),
                new MybatisSqlWhereBuild(DynamicPropertyCategory.class)
                        .eq(DynamicPropertyCategory::getCateId, categoryId)
                        .eq(DynamicPropertyCategory::getStatus, 1)
                        .orderBy(MybatisSqlWhereBuild.ORDER.ASC, DynamicPropertyCategory::getWeight)
        );
        if (dpcList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> dynaPropIds = dpcList.stream().mapToLong(value -> value.getDynaPropId()).boxed().collect(Collectors.toList());

        List<ProductPropertyDTO> productPropertyDTOS = dynamicPropertyProductMapper.listDynaPropAndValue(productId);
        if (productPropertyDTOS.isEmpty()) {
            return Collections.emptyList();
        }

        return productPropertyDTOS.stream()
                .filter(dto -> dynaPropIds.contains(dto.getDynaPropId()))
                .collect(Collectors.toList());
    }
}
