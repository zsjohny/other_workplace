package com.finace.miscroservice.official_website.dao;


import com.finace.miscroservice.official_website.entity.ImagesTypePO;
import com.finace.miscroservice.official_website.entity.Product;
import com.finace.miscroservice.official_website.entity.response.*;

import java.util.List;

/**
 * 标的Dao层
 */
public interface OfficialWebsiteDao {

    List<Product> getBorrowByIndex(String key);

    List<ImagesTypePO> getImagesTypePoList(Integer code);

    List<ProductsResponse> pcProductList(String isxs,Integer time);

    ProductDetailResponse pcProductDetail(Integer id);

    List<ProductRecordUsersResponse> pcProductRecordes(Integer borrorId);

    /**
     * -- 累计成交金额 累计用户收益 累计注册人数
     * @return
     */
    DataCollectionResponse getDatas();

    /**
     * 累计成交笔数
     * @return
     */
    Integer getLjcjbs();

    /**
     * 累计出借人总数(人)
     * @return
     */
    Integer getLjcjrzs();

    /**
     * 本年度成交金额(元)
     * @param s
     * @return
     */
    Double getBndcjje(String s);

    /**
     * 最大单户出借余额占比
     * @return
     */
    Double getZddhcjyezb();

    /**
     * 最大10户投资出借占比
     * @return
     */
    Double getZdshtzcjzb();

    /**
     * 性别男女 占比
     * @return
     */
    UserProportion userProportion();

    /**
     * 待还金额笔数(笔)
     * @return
     */
    Integer dhjebs();

    /**
     * 累计借款人总数(人)
     * @return
     */
    Integer ljjkrzs();

    /**
     * 待还金额(万元)
     * @return
     */
    Double dhje();

    /**
     * 近7日成交金额
     * @return
     */
    Double getSevenGetMoney();

    /**
     * 当前借款人数量
     * @return
     */
    Integer dqjkrsl();

    /**
     * 当前出借人数量
     * @return
     */
    Integer dqcjrsl();
}
