package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.official_website.entity.BannerPic;
import com.finace.miscroservice.official_website.entity.BaseEntity;
import com.finace.miscroservice.official_website.entity.ImagesTypePO;
import com.finace.miscroservice.official_website.entity.Product;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by hyf on 2018/3/5.
 */
@Data
@ToString
public class IndexShowResponse extends BaseEntity{
    private List<Product> productResponseList;
    private Product xsProduct; //新手标
    private List<BannerPic> bannerPicList;
    private List<ImagesTypePO> imagesTypePOList;
    private String addUpMoney;  //累计交易
    private Double addUpInterest;//累计收益
    private Double sevenGetMoney; //7天交易量

    private  List<ImageType> list1;//
    private  List<MsgTypeResponse> list2;//
    private List<MsgTypeResponse> list3;//

    public String getAddUpMoney() {
        return addUpMoney;
    }

    public void setAddUpMoney(String addUpMoney) {
        this.addUpMoney = NumberUtil.strFormat2(addUpMoney);;
    }
}
