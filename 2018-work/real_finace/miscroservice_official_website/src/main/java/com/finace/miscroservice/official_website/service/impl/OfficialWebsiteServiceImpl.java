package com.finace.miscroservice.official_website.service.impl;



import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.official_website.dao.CommonDao;
import com.finace.miscroservice.official_website.dao.OfficialWebsiteDao;
import com.finace.miscroservice.official_website.entity.ImagesTypePO;
import com.finace.miscroservice.official_website.entity.Product;
import com.finace.miscroservice.official_website.entity.response.*;
import com.finace.miscroservice.official_website.enums.ImagesTypeEnums;
import com.finace.miscroservice.official_website.enums.IsNoviciateProduct;
import com.finace.miscroservice.official_website.enums.MsgTypeEnums;
import com.finace.miscroservice.official_website.service.OfficialWebsiteService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 标的的service 实现层
 */
@Service
public class OfficialWebsiteServiceImpl implements OfficialWebsiteService {
    private Log logger = Log.getInstance(OfficialWebsiteServiceImpl.class);
    @Autowired
    private OfficialWebsiteDao officialWebsiteDao;
    @Autowired
    private CommonDao commonDao;

    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static int NOTICE_SIZE = 3;

    @Override
    public Response pcIndex() {
        IndexShowResponse indexShowResponse = new IndexShowResponse();
        //数据--累计交易--为用户赚取--近七天成交
        DataCollectionResponse dataCollectionResponse = officialWebsiteDao.getDatas();
        indexShowResponse.setAddUpMoney(dataCollectionResponse.getLjcjje());
        indexShowResponse.setAddUpInterest(dataCollectionResponse.getLjyhsy());
        Double sevenGetMoney = officialWebsiteDao.getSevenGetMoney();
        indexShowResponse.setSevenGetMoney(sevenGetMoney);

//        List<Product> productList = new ArrayList<>();
//        if (isxs == null || isxs.equals(NewUserEnums.IS_NEW_TRUE.getCode())) {
            //获取新手标
            List<Product>  productList2=officialWebsiteDao.getBorrowByIndex(IsNoviciateProduct.IS_NOVICIATE.getKey());
            Product xsProduct = new Product();
            if (productList2!=null&&productList2.size()>0){
                xsProduct= productList2.get(0);
            }
            indexShowResponse.setXsProduct(xsProduct);
//        }
        List<Product> productList = officialWebsiteDao.getBorrowByIndex(IsNoviciateProduct.NON_NOVICIATE.getKey());
        indexShowResponse.setProductResponseList(productList);


        //banner列表 暂定
        List<ImagesTypePO> imagesTypePOList = officialWebsiteDao.getImagesTypePoList(ImagesTypeEnums.PC_IMAGES.getCode());
        indexShowResponse.setImagesTypePOList(imagesTypePOList);
        //平台公告
        PageHelper.startPage(1,NOTICE_SIZE);
        List<ImageType> list1 = commonDao.imageType(ImagesTypeEnums.PMS_IMAGES.getCode());
        indexShowResponse.setList1(list1);

        PageHelper.startPage(1,NOTICE_SIZE);
        List<MsgTypeResponse> list2 = commonDao.newsCenter(MsgTypeEnums.OFFICIAL_NOTICE.getCode());
        indexShowResponse.setList2(list2);

        PageHelper.startPage(1,NOTICE_SIZE);
        List<MsgTypeResponse>list3 = commonDao.newsCenter(MsgTypeEnums.NEWS_CENTER.getCode());
        indexShowResponse.setList3(list3);
        return Response.success(indexShowResponse);
    }

    @Override
    public Response pcProductList(String isxs,Integer page,Integer time) {
        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<ProductsResponse> productListResponseList = officialWebsiteDao.pcProductList(isxs,time);
        PageInfo<ProductsResponse> pageInfo = new PageInfo<>(productListResponseList);
        return Response.success(pageInfo);

    }

    @Override
    public Response pcProductDetail(Integer id) {
        //产品详情
        ProductDetailResponse response = officialWebsiteDao.pcProductDetail(id);
        return Response.success(response);
    }

    @Override
    public Response pcProductRecordes(Integer page,Integer borrowId) {


        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<ProductRecordUsersResponse> list = officialWebsiteDao.pcProductRecordes( borrowId);
        PageInfo<ProductRecordUsersResponse> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo);
    }

    @Override
    public Response pcDataConllection() {
        //-- 累计成交金额 累计用户收益 累计注册人数
        DataCollectionResponse response = officialWebsiteDao.getDatas();
        // 累计成交笔数
        Integer ljcjbs = officialWebsiteDao.getLjcjbs();
        response.setLjcjbs(ljcjbs);
        //累计出借人总数(人)
        Integer ljcjrzs = officialWebsiteDao.getLjcjrzs();
        response.setLjcjrzs(ljcjrzs);
        //本年度成交金额(元)
        Double bndcjje = officialWebsiteDao.getBndcjje(DateUtils.thisYear());
        response.setBncjje(bndcjje);
        //待还金额(万元)
        Double dhje = officialWebsiteDao.dhje();
        response.setDhje(dhje);
        //最大单户出借余额占比
        Double zddhcjyezb = officialWebsiteDao.getZddhcjyezb();
        Double zddhc = NumberUtil.divide(4,zddhcjyezb,dhje);
        String zddhcjb = NumberUtil.strFormat2(NumberUtil.multiply(2,zddhc,100));
        response.setZddhcjyezb(zddhcjb);
        //其余单户出借余额占比
        Double qydhcjyezb =  NumberUtil.subtract(4,1,zddhc);
        String qydhcjb = NumberUtil.strFormat2(NumberUtil.multiply(2,qydhcjyezb,100));
        response.setQydhcjyezb(qydhcjb);
        //最大10户投资出借占比
        Double zdshtzcjzb = officialWebsiteDao.getZdshtzcjzb();
        Double zdzb = NumberUtil.divide(4,zdshtzcjzb,dhje);
        String zdshcjb = NumberUtil.strFormat2(NumberUtil.multiply(2,zdzb,100));
        response.setZdshtzcjzb(zdshcjb);
        //其余用户出借余额占比
        Double qyyhcjyezb = NumberUtil.subtract(4,1,zdzb);
        String qyyhzb = NumberUtil.strFormat2(NumberUtil.multiply(2,qyyhcjyezb,100));
        response.setQyyhcjyezb(qyyhzb);
        //性别男女 占比
        UserProportion xbnan = officialWebsiteDao.userProportion();
        response.setXbnan(NumberUtil.strFormat2(NumberUtil.multiply(2,xbnan.getNanzb(),100)));
        response.setXbnv(NumberUtil.strFormat2(NumberUtil.multiply(2,xbnan.getNvzb(),100)));
        //待还金额笔数(笔)
        Integer dhjebs = officialWebsiteDao.dhjebs();
        response.setDhjebs(dhjebs);
        //累计借款人总数(人)
        Integer ljjkrzs = officialWebsiteDao.ljjkrzs();
        response.setLjjkrzs(ljjkrzs);

//        当前借款人数量
        Integer dqjkrsl = officialWebsiteDao.dqjkrsl();
        response.setDqjkrsl(dqjkrsl);
//        当前出借人数量
        Integer dqcjrsl = officialWebsiteDao.dqcjrsl();
        response.setDqcjrsl(dqcjrsl);

        logger.info(response.toString());
        return Response.success(response);
    }


}



