package com.finace.miscroservice.official_website.mapper;



import com.finace.miscroservice.official_website.entity.ImagesTypePO;
import com.finace.miscroservice.official_website.entity.Product;
import com.finace.miscroservice.official_website.entity.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OfficialWebsiteMapper {

    List<Product> getBorrowByIndex(@Param("type") String type);

    List<ImagesTypePO> getImagesTypePoList(@Param("code") Integer code);

    List<ProductsResponse> pcProductList(@Param("isxs")String isxs,@Param("time")Integer time);

    ProductDetailResponse pcProductDetail(@Param("id")Integer id);

    List<ProductRecordUsersResponse> pcProductRecordes(@Param("borrowId")Integer borrowId);

    DataCollectionResponse getDatas();

    Integer getLjcjbs();

    Integer getLjcjrzs();

    Double getBndcjje(@Param("date") String date);

    Double getZddhcjyezb();

    Double getZdshtzcjzb();

    UserProportion userProportion();

    Integer dhjebs();

    Integer ljjkrzs();

    Double dhje();

    Double getSevenGetMoney();

    Integer dqcjrsl();

    Integer dqjkrsl();
}
