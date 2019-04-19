package com.jiuy.service.brand;

import com.jiuy.model.brand.Brand;

import java.util.List;

/**
 * @version V1.0
 * @Package com.jiuy.service.brand
 * @Description: 品牌service
 * @author: Aison
 * @date: 2018/5/3 10:05
 * @Copyright: 玖远网络
 */
public interface IBrandService {

    /**
     * 通过id获取
     * @param id
     * @date:   2018/5/3 10:06
     * @author: Aison
     */
    Brand getById(Long id);
    /**
     * 通過品牌id獲取
     * @param brandId
     * @date:   2018/5/3 16:26
     * @author: Aison
     */
    Brand getByBrandId(Long brandId) ;

}
