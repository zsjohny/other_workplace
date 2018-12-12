package com.finace.miscroservice.official_website.dao.impl;

import com.finace.miscroservice.official_website.dao.OfficialWebsiteDao;
import com.finace.miscroservice.official_website.entity.ImagesTypePO;
import com.finace.miscroservice.official_website.entity.Product;
import com.finace.miscroservice.official_website.entity.response.*;
import com.finace.miscroservice.official_website.mapper.OfficialWebsiteMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 标的 Dao层实现类
 */
@Component
public class OfficialWebsiteDaoImpl implements OfficialWebsiteDao {

    @Resource
    private OfficialWebsiteMapper officialWebsiteMapper;
    @Override
    public List<Product> getBorrowByIndex(String key) {
        return officialWebsiteMapper.getBorrowByIndex( key);
    }

    @Override
    public List<ImagesTypePO> getImagesTypePoList(Integer code) {
        return officialWebsiteMapper.getImagesTypePoList(code);
    }

    @Override
    public List<ProductsResponse> pcProductList(String isxs,Integer time) {
        return officialWebsiteMapper.pcProductList(  isxs,  time) ;
    }

    @Override
    public ProductDetailResponse pcProductDetail(Integer id) {
        return officialWebsiteMapper.pcProductDetail(id);
    }

    @Override
    public List<ProductRecordUsersResponse> pcProductRecordes(Integer borrowId) {
        return officialWebsiteMapper.pcProductRecordes(borrowId);
    }

    @Override
    public DataCollectionResponse getDatas() {
        return officialWebsiteMapper.getDatas();
    }

    @Override
    public Integer getLjcjbs() {
        return officialWebsiteMapper.getLjcjbs();
    }

    @Override
    public Integer getLjcjrzs() {
        return officialWebsiteMapper.getLjcjrzs();
    }

    @Override
    public Double getBndcjje(String date) {
        return officialWebsiteMapper.getBndcjje(date);
    }

    @Override
    public Double getZddhcjyezb() {
        return officialWebsiteMapper.getZddhcjyezb();
    }

    @Override
    public Double getZdshtzcjzb() {
        return officialWebsiteMapper.getZdshtzcjzb();
    }

    @Override
    public UserProportion userProportion() {
        return officialWebsiteMapper.userProportion();
    }

    @Override
    public Integer dhjebs() {
        return officialWebsiteMapper.dhjebs();
    }

    @Override
    public Integer ljjkrzs() {
        return officialWebsiteMapper.ljjkrzs();
    }

    @Override
    public Double dhje() {
        return officialWebsiteMapper.dhje();
    }

    @Override
    public Double getSevenGetMoney() {
        return officialWebsiteMapper.getSevenGetMoney();
    }

    @Override
    public Integer dqjkrsl() {
        return officialWebsiteMapper.dqjkrsl();
    }

    @Override
    public Integer dqcjrsl() {
        return officialWebsiteMapper.dqcjrsl();
    }

}


