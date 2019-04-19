package com.e_commerce.miscroservice.operate.service.activity;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * Create by hyf on 2018/12/18
 */
public interface LotteryDrawActivityService {
    Response findAllProductList(Long id, String timeStart, String timeEnd, Integer pageNumber, Integer pageSize);
    /**
     * 添加商品
     * @param banner
     * @param pic
     * @param button
     * @return
     */
    Response insertProduct(String banner, String[] pic, String button);
    /**
     * 修改产品
     * @param id
     * @param banner
     * @param pic
     * @param button
     * @return
     */
    Response updateProduct(Long id, String banner, String[] pic, String button);
    /**
     * 删除
     * @param id
     * @return
     */
    Response deleteProduct(Long id);

    /**
     * 查询所有详情内容图片
     * @param id
     * @return
     */
    Response findAllDetail(Long id);

    /**
     * 查询参加用户
     * @param phone
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Response findJoin(String phone, Integer pageNumber, Integer pageSize);
}
