package com.jiuyuan.service.common.express;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuyuan.constant.Express;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.express.ExpressModelDetailMapper;
import com.jiuyuan.dao.mapper.supplier.express.ExpressModelMapper;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.common.Area;
import com.jiuyuan.entity.express.ExpressModel;
import com.jiuyuan.entity.express.ExpressModelDetail;
import com.jiuyuan.entity.express.ExpressUtilVo;
import com.jiuyuan.entity.log.Log;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.service.common.AddressNewService;
import com.jiuyuan.service.common.area.AreaService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * @version V1.0
 * @Package com.jiuyuan.service.common.express
 * @Description: 运费service
 * @author: Aison
 * @date: 2018/4/28 11:18
 * @Copyright: 玖远网络
 */
@Service("expressModelService")
public class ExpressModelService implements IExpressModelService {

    @Autowired
    private ExpressModelMapper expressModelMapper;

    @Autowired
    private ExpressModelDetailMapper expressModelDetailMapper;

    @Autowired
    private ProductSkuNewMapper productSkuNewMapper;

    @Autowired
    private AreaService areaService;

    @Autowired
    private ProductNewMapper  productNewMapper;

    @Autowired
    private AddressNewService addressNewService;


    /**
     * 添加运费模板
     *
     * @param expressModel
     * @date: 2018/4/28 11:19
     * @author: Aison
     */
    @Override
    public void addExpressModel(ExpressModel expressModel) {
        // 参数验证
        if (BizUtil.hasEmpty(expressModel.getProvince(), expressModel.getCity(), expressModel.getMinMoney(), expressModel.getMinWeight(),
                expressModel.getEachGroupMoney(), expressModel.getEachGroupWeight())) {
            throw BizException.defulat().paramError();
        }
        int rs = expressModelMapper.selectCount(Condition.create().eq("supplier_id", expressModel.getSupplierId()));
        if (rs > 0) {
            throw BizException.defulat().msg("请勿重复添加模板");
        }
        if (expressModel.getCalculationWay() == null) {
            expressModel.setCalculationWay(1);
        }
        // 是否已经存在了模板
        expressModelMapper.insert(expressModel);
    }

    /**
     * 添加模板详情
     *
     * @param expressModelDetail
     * @param supplierId         供应上id
     * @date: 2018/4/28 11:25
     * @author: Aison
     */
    @Override
    public Long addExpressModelDetail(Long supplierId, ExpressModelDetail expressModelDetail) {

        ExpressModel query = new ExpressModel();
        query.setSupplierId(supplierId);
        query = expressModelMapper.selectOne(query);
        if (query == null) {
            throw BizException.defulat().msg("供应商运费模板获取失败");
        }
        //参数验证
        if (BizUtil.hasEmpty(expressModelDetail.getModelId(), expressModelDetail.getTargetProvince(),
                expressModelDetail.getEachGroupMoney(), expressModelDetail.getEachGroupWeight(),
                expressModelDetail.getMinMoney(), expressModelDetail.getMinWeight(),
                expressModelDetail.getGroup())) {
            throw BizException.defulat().paramError();
        }


        // 判断在此模板下是否存在一个目标地址的明细
        int count = expressModelDetailMapper.selectCount(Condition.create().eq("model_id", query.getId()).eq("target_province", expressModelDetail.getTargetProvince()));
        if (count > 0) {
            throw BizException.defulat().msg("此模板有重复，请勿重复添加");
        }
        expressModelDetail.setModelId(query.getId());
        expressModelDetailMapper.insert(expressModelDetail);
        return expressModelDetail.getId();
    }

    /**
     * 批量添加明细
     *
     * @param supplierId
     * @param expressModelDetails
     * @date: 2018/5/2 10:24
     * @author: Aison
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> addExpressModelDetails(Long supplierId, List<ExpressModelDetail> expressModelDetails) {
        List<Long> ids = new ArrayList<>();
        for (ExpressModelDetail expressModelDetail : expressModelDetails) {
            addExpressModelDetail(supplierId, expressModelDetail);
            ids.add(expressModelDetail.getId());
        }
        return ids;
    }

    /**
     * 删除运费详情
     *
     * @param supplierId
     * @param detailId
     * @date: 2018/4/28 11:52
     * @author: Aison
     */
    @Override
    public void deleteExpressModelDetail(Long supplierId, Long detailId) {

        ExpressModelDetail detail = expressModelDetailMapper.selectById(detailId);
        if (detail == null) {
            throw BizException.defulat().msg("模板详情为空");
        }
        // 首页确认此模板是否是当前用户下面的
        int rs = expressModelMapper.selectCount(Condition.create().eq("id", detail.getModelId()).eq("supplier_id", supplierId));
        // 如果不是则抛出异常
        if (rs == 0) {
            throw BizException.defulat().msg("供应商运费模板获取失败");
        }
        // 如果是则删除详情
        int delRs = expressModelDetailMapper.deleteById(detailId);
        if (delRs == 0) {
            throw BizException.defulat().msg("删除失败");
        }
    }

    /**
     * 批量删除运费详情
     *
     * @param supplierId
     * @param detailIds ids
     * @date: 2018/4/28 11:52
     * @author: Aison
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteExpressModelDetails(Long supplierId, String detailIds) {
    	if(BizUtil.hasEmpty(detailIds)){
    		return ;
    	}
        String[] detailIdsLong = detailIds.split(",");
        for (String detailId : detailIdsLong) {
             deleteExpressModelDetail(supplierId,Long.valueOf(detailId));
        }
    }


    /**
     *
     * @param deleteIds
     * @param updates
     * @param adds 需要添的模板
     * @date:   2018/5/8 15:31
     * @author: Aison
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDetailBatch(Long supplierId,String deleteIds,String updates,String adds){
        // 删除详情
        deleteExpressModelDetails(supplierId,deleteIds);
        //修改详情
        List<ExpressModelDetail> expressModelDetails = null;
        if(BizUtil.isNotEmpty(updates)) {
            expressModelDetails =  BizUtil.jsonStrToListObject(updates,List.class,ExpressModelDetail.class);
        }
        List<ExpressModelDetail> expressModelDetailsAdd = null;
        if(BizUtil.isNotEmpty(adds)) {
            expressModelDetailsAdd =  BizUtil.jsonStrToListObject(adds,List.class,ExpressModelDetail.class);
        }
        if(expressModelDetails!=null) {
            for (ExpressModelDetail expressModelDetail : expressModelDetails) {
                int rs = expressModelDetailMapper.updateById(expressModelDetail);
                if(rs==0){
                    throw BizException.defulat().msg("修改失败");
                }
            }
        }
        if(expressModelDetailsAdd!=null) {
            for (ExpressModelDetail expressModelDetail : expressModelDetailsAdd) {
                int rs = expressModelDetailMapper.insert(expressModelDetail);
                if(rs==0){
                    throw BizException.defulat().msg("添加失败");
                }
            }
        }
    }


    /**
     * 更新模板
     *
     * @param expressModel
     * @date: 2018/4/28 13:59
     * @author: Aison
     */
    @Override
    public void updateModel(ExpressModel expressModel, Long supplierId) {
        if (expressModel.getId() == null) {
            throw BizException.defulat().msg("参数错误");
        }
        // 不允许修改 供应商id
        expressModel.setSupplierId(null);
        int rs = expressModelMapper.selectCount(Condition.create().eq("supplier_id", supplierId).eq("id", expressModel.getId()));
        if (rs == 0) {
            throw BizException.defulat().msg("获取不到模板信息");
        }
        rs = expressModelMapper.updateById(expressModel);
        if (rs == 0) {
            throw BizException.defulat().msg("更新失败");
        }
    }

    /**
     * 更新模板
     *
     * @param expressModelDetail
     * @param supplierId
     * @date: 2018/4/28 13:59
     * @author: Aison
     */
    @Override
    public void updateModelDetail(ExpressModelDetail expressModelDetail, Long supplierId) {
        if (expressModelDetail.getId() == null) {
            throw BizException.defulat().msg("参数错误");
        }
        ExpressModelDetail detail = expressModelDetailMapper.selectById(expressModelDetail.getId());
        if (detail == null) {
            throw BizException.defulat().msg("模板详情为空");
        }
        // 首页确认此模板是否是当前用户下面的
        int rs = expressModelMapper.selectCount(Condition.create().eq("id", detail.getModelId()).eq("supplier_id", supplierId));
        // 如果不是则抛出异常
        if (rs == 0) {
            throw BizException.defulat().msg("供应商运费模板获取失败");
        }
        // 不允许修改modelId
        expressModelDetail.setModelId(null);
        rs = expressModelDetailMapper.updateById(expressModelDetail);
        if (rs == 0) {
            throw BizException.defulat().msg("更新失败");
        }
    }

    /**
     * 获取最小的运费
     *
     * @param supplierId
     * @date: 2018/4/28 13:38
     * @author: Aison
     */
    @Override
    public BigDecimal mininExpress(Long supplierId) {

        ExpressModel expressModel = expressModelMapper.selectMiniMoney(supplierId);
        return expressModel == null ? BigDecimal.ZERO : expressModel.getMinMoney();
    }


    /**
     * 计算快递费用  供应商的商品都是有sku的  不考虑没有sku的情况
     *
     * @param provinceName 目标省份行政区划
     * @param skuinfos    sku的ids  3_1,2_1,3_1
     * @param supplierId  供应商id
     * @date: 2018/5/2 14:00
     * @author: Aison
     */
    @Override
    public BigDecimal countOrderExpressMoneyPovinceName(String provinceName, String skuinfos, Long supplierId){

        Area area =  areaService.getByProvinceName(provinceName);
        return countOrderExpressMoney(area.getProvinceCode(),skuinfos,supplierId);

    }

    /**
     * 通过省份名称获取运费
     * @param expressUtilVo 参数的封装
     * @date:   2018/5/3 17:51
     * @author: Aison
     */
    @Override
    public Map<String,BigDecimal> countOrderExpressMoneyPovinceNames(ExpressUtilVo expressUtilVo){

        if(BizUtil.hasEmpty(expressUtilVo.getData())){
          throw BizException.defulat().msg("参数错误");
        }
        Map<String,ExpressUtilVo.ExpressItem> itemMap = expressUtilVo.getExpressItemMap();
        String province = expressUtilVo.getProvinceName();
        if (StringUtils.isBlank(province)) {
            Long addressId = expressUtilVo.getAddressId();
            if(addressId!=null) {
                AddressNew addressNew = addressNewService.getAddressById(addressId);
                if (addressNew != null) {
                    province = addressNew.getProvinceName();
                }
            }
        }

        Map<String,BigDecimal> moneys = new HashMap<>();
        for(Map.Entry<String,ExpressUtilVo.ExpressItem> entry:itemMap.entrySet()) {
            ExpressUtilVo.ExpressItem expressItem = entry.getValue();
            BigDecimal money = BigDecimal.ZERO;
            if(province != null) {
                money = countOrderExpressMoneyPovinceName(province,expressItem.getSkuinfos(),null);
            }
            moneys.put(entry.getKey(),money);
        }
        return moneys;
    }


    /**
     * 计算某个订单的运费
     * @param storeOrderNewVo
     * @param storeOrderNew
     * @param addressId
     * @date:   2018/5/8 9:54
     * @author: Aison
     */
    @Override
    public void countOrderExpress(StoreOrderNewVo storeOrderNewVo,StoreOrderNew storeOrderNew,Long addressId) {
        BigDecimal expressMoney;
        try{
            List<ProductNewVo> productNewVos = storeOrderNewVo.getProductNewVoList();
            StringBuilder stringBuilder = new StringBuilder();
            for (ProductNewVo productNewVo : productNewVos) {
                List<ProductSkuNewVo> productSkuNewVos = productNewVo.getProductSkuNewVoList();
                for (ProductSkuNewVo productSkuNewVo : productSkuNewVos) {
                    stringBuilder.append(productSkuNewVo.getProductSkuNew().getId()).append("_").append(productSkuNewVo.getBuyCount()).append(",");
                }
            }
            stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
            AddressNew addressNew = addressNewService.getAddressById(addressId);
            expressMoney = countOrderExpressMoneyPovinceName(addressNew.getProvinceName(),stringBuilder.toString(),null);
            storeOrderNew.setTotalExpressMoney(expressMoney.doubleValue());
            storeOrderNewVo.setPostage(storeOrderNew.getTotalExpressMoney());
        }catch (Exception e) {
            if(e instanceof BizException) {
                BizException ex = (BizException) e;
                if(ex.getCode() == 900){
                    storeOrderNew.setTotalExpressMoney(0d);
                }else {
                    e.printStackTrace();
                    throw new RuntimeException("获取运费失败");
                }
            }
        }
    }



    /**
     * 计算快递费用  供应商的商品都是有sku的  不考虑没有sku的情况
     *
     * @param provinceName 目标省份行政区划
     * @param skuinfos    sku的ids  3_1,2_1,3_1
     * @param supplierId  供应商id
     * @date: 2018/5/2 14:00
     * @author: Aison
     */
    @Override
    public BigDecimal countOrderExpressMoney(String provinceName, String skuinfos, Long supplierId) {

        // 获取总重量
        String[] info = skuinfos.split(",");
        String[] skuIds = new String[info.length];
        Map<String, Integer> countMap = new HashMap<>();
        for (int i = 0; i < info.length; i++) {
            String[] infoItem = info[i].split("_");
            skuIds[i] = infoItem[0];
            countMap.put(skuIds[i], Integer.valueOf(infoItem[1]));
        }
        List<ProductSkuNew> productSkuNews = productSkuNewMapper.selectList(Condition.create().in("id", skuIds));
        if (productSkuNews.size() == 0) {
            throw BizException.defulat().paramError();
        }
        Set<Long> productIds = new HashSet<>();
        for (ProductSkuNew productSkuNew : productSkuNews) {
            productIds.add(productSkuNew.getProductId());
        }
        List<ProductNew> products = productNewMapper.selectList(Condition.create().in("id",productIds));
        Map<Long,ProductNew> productNewMap = new HashMap<>();
        for (ProductNew product : products) {
            productNewMap.put(product.getId(),product);
        }
        // 如果没有传递 supplierId 则查询商品id
        if(supplierId == null) {
            supplierId = products.get(0).getSupplierId();
        }
        BigDecimal weightCount = BigDecimal.ZERO;
        for (ProductSkuNew productSkuNew : productSkuNews) {
            Double pw = productSkuNew.getWeight();
            ProductNew productNew =  productNewMap.get(productSkuNew.getProductId());
            // 如果免邮则是0
            if(productNew.getExpressFree()==0){
                weightCount = weightCount.add(BigDecimal.ZERO);
            }else {
                Integer count = countMap.get(productSkuNew.getId().toString());
                pw = pw == null ? 0d : pw/1000;
                weightCount = weightCount.add(new BigDecimal(pw*count));
            }
        }
        // 如果重量都是0 则邮费为0
        if(weightCount.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }

        // 获取运费模板 起重量，起运费 每重量 每运费
        List<ExpressModel> expressModels = expressModelMapper.selectList(Condition.create().eq("supplier_id", supplierId));
        if (expressModels == null || expressModels.size() == 0) {
            return BigDecimal.ZERO;
        }
        ExpressModel expressModel = expressModels.get(0);

        List<ExpressModelDetail> expressModelDetails = expressModelDetailMapper.selectList(Condition.create()
                .eq("model_id",expressModel.getId()).eq("target_province",provinceName)
        );
        BigDecimal min , minm,each,eachm;
        if(expressModelDetails==null || expressModelDetails.size() == 0) {
            min = expressModel.getMinWeight();
            minm = expressModel.getMinMoney();
            each = expressModel.getEachGroupWeight();
            eachm = expressModel.getEachGroupMoney();
        }else {
            ExpressModelDetail ed = expressModelDetails.get(0);
            min = ed.getMinWeight();
            minm = ed.getMinMoney();
            each = ed.getEachGroupWeight();
            eachm = ed.getEachGroupMoney();
        }
        BigDecimal account = BigDecimal.ZERO;
        if(weightCount.compareTo(min)<=0) {
            account =  minm;
        }  else {
            // 多出来的
            // 保留2位小数
            BigDecimal left = weightCount.subtract(min).setScale(2,BigDecimal.ROUND_UP);
            // 再计算价格
            BigDecimal leftCount =  left.divide(each, 2, BigDecimal.ROUND_HALF_UP);
            leftCount = leftCount.setScale(0,BigDecimal.ROUND_UP);
            BigDecimal leftMoney = leftCount.multiply(eachm);
            account = account.add(minm).add(leftMoney);
        }
        return account;
    }

	@Override
	public ExpressModel supplierExpress(Long supplireId) {
		ExpressModel em = new ExpressModel();
		em.setSupplierId(supplireId);
		return expressModelMapper.selectOne(em);
	}

    /**
     * 获取某个商品的运费
     * @param productId
     * @date:   2018/5/9 14:08
     * @author: Aison
     */
	@Override
    public BigDecimal countProductExpress(Long productId) {
        ProductNew productNew =  productNewMapper.selectById(productId);
        if(BizUtil.hasEmpty(productNew)) {
            throw BizException.defulat().paramError();
        }

        if(productNew.getExpressFree()==0){
            return BigDecimal.ZERO;
        }

        Long supplierId = productNew.getSupplierId();
        EntityWrapper<ExpressModel> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("supplier_id",supplierId);
        List<ExpressModel> expressModels = expressModelMapper.selectList(entityWrapper);
        // 如果没有模板则返回0
        if(expressModels.size()==0) {
            return BigDecimal.ZERO;
        }
        ExpressModel expressModel = expressModels.get(0);
        Long modelId = expressModel.getId();
        BigDecimal mini = expressModelDetailMapper.selectMiniMoney(modelId);
        BigDecimal mainMini = expressModel.getMinMoney();
        if(mini == null) {
            return mainMini;
        } else {
            // 如果详情中的最低运费大于默认模板的则取最低的
            return mainMini.compareTo(mini) <0 ? mainMini : mini;
        }
    }

    @Override
    public void countOrderExpress4InstantOfSendGoods(StoreOrderNewVo storeOrderNewVo, StoreOrderNew storeOrderNew, String province) {
        BigDecimal expressMoney;
        try{
            List<ProductNewVo> productNewVos = storeOrderNewVo.getProductNewVoList();
            StringBuilder stringBuilder = new StringBuilder();
            for (ProductNewVo productNewVo : productNewVos) {
                List<ProductSkuNewVo> productSkuNewVos = productNewVo.getProductSkuNewVoList();
                for (ProductSkuNewVo productSkuNewVo : productSkuNewVos) {
                    stringBuilder.append(productSkuNewVo.getProductSkuNew().getId()).append("_").append(productSkuNewVo.getBuyCount()).append(",");
                }
            }
            stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
            expressMoney = countOrderExpressMoneyPovinceName(province,stringBuilder.toString(),null);
            storeOrderNew.setTotalExpressMoney(expressMoney.doubleValue());
            storeOrderNewVo.setPostage(storeOrderNew.getTotalExpressMoney());
        }catch (Exception e) {
            if(e instanceof BizException) {
                BizException ex = (BizException) e;
                if(ex.getCode() == 900){
                    storeOrderNew.setTotalExpressMoney(0d);
                }else {
                    e.printStackTrace();
                    throw new RuntimeException("获取运费失败");
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
	@Override
	public List<ExpressModelDetail> supplierExpressDetail(Long supplireId) {
		
		ExpressModel em = supplierExpress(supplireId);
		if(em==null) {
			return null;
		} else {
			return expressModelDetailMapper.selectList(Condition.create().eq("model_id", em.getId()));
		}
	}


	public static void main(String[] args) {

	 BigDecimal b = new BigDecimal(1.00001);


	 System.out.println(b.setScale(0,BigDecimal.ROUND_UP));
	}
}
