package com.jiuy.product.service.impl;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.product.mapper.*;
import com.jiuy.product.model.*;
import com.jiuy.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品service实现
 * @author Aison
 * @version V1.0
 * @date 2018/6/5 16:12
 * @Copyright 玖远网络
 */
@Service("productService")
public class ProductServiceImpl implements IProductService {


    private final CategoryMapper categoryMapper;

    @Autowired
    public ProductServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 添加一个商品
     * @param product 商品封装类
     * @param user 操作用户
     * @author Aison
     * @date 2018/6/5 16:03
     * @return MyLog 返回日志
     */
    @Override
    @MyLogs(logInfo = "添加商品",model = ModelType.PRODUCT_MODEL)
    public MyLog addProduct(Product product, UserSession user) {

        return new MyLog<Product>(product,MyLog.Type.add,user);
    }

    /**
     * 添加sku
     * @param productSku sku
     * @param product 商品
     * @param user 操作用户
     * @author Aison
     * @date 2018/6/5 16:04
     * @return MyLog 返回日志
     */
    @Override
    @MyLogs(logInfo = "添加sku",model = ModelType.PRODUCT_MODEL)
    public MyLog addProductSku(ProductSku productSku, Product product, UserSession user) {


        return new MyLog<Product>(productSku,MyLog.Type.add,user);
    }

    /**
     * 审核商品
     * @param product 商品对象
     * @param optUser  操作用户
     * @param supplierId 供应商id
     * @author Aison
     * @date 2018/6/5 16:10
     * @return MyLog 返回日志
     */
    @Override
    @MyLogs(logInfo = "审核商品",model = ModelType.PRODUCT_MODEL)
    public MyLog auditProduct(Product product, UserSession optUser, Long supplierId) {

        return new MyLog<Product>(product,MyLog.Type.up,optUser);
    }


    /**
     * 添加商品类目
     * @param category 类目实体
     * @param optUser 操作人
     * @author Aison
     * @date 2018/6/5 16:48
     */
    @Override
    @MyLogs(logInfo = "添加类目",model = ModelType.PRODUCT_MODEL)
    public MyLog<Long> addCategory(Category category, UserSession optUser) {

        //验证几个关键的参数
        if(Biz.hasEmpty(category.getCategoryName(),category.getCategoryType())) {
            throw BizException.paramError();
        }
        // 如果是一级菜单 则查找数据库中的code最大的一级菜单加1
        String maxStr = categoryCode(category.getParentId());
        category.setCode(maxStr);
        category.setUpdateTime(System.currentTimeMillis());
        category.setCreateTime(System.currentTimeMillis());
        categoryMapper.insertSelective(category);

        return  new MyLog<Long>(category,MyLog.Type.add,optUser).setData(category.getId());
    }

    /**
     * 取category的code 如果是1级则递增 如果是子级则找最后的那个加1
     * @param parentId 父节点
     * @author Aison
     * @date 2018/6/6 13:31
     */
    private String categoryCode(Long parentId) {

        String max ;
        String parentCode = "";
        CategoryQuery query = new CategoryQuery();
        if(parentId==null || parentId ==0 ){
            query.setParentId(0L);
            max = categoryMapper.selectMaxCode(query);
        } else {
            query.setParentId(parentId);
            max = categoryMapper.selectMaxCode(query);
            Category parent = categoryMapper.selectByPrimaryKey(parentId);
            parentCode = parent.getCode();
        }
        String maxStr = parentCode ;
        // 说明是1级分类
        if(Biz.hasEmpty(parentCode)){
            maxStr+=Long.valueOf(max);
        }else {
            if(max== null) {
                maxStr +="_"+10001L;
            } else {
                String[] maxs = max.split("_");
                maxStr+="_"+Long.valueOf(maxs[maxs.length-1]);
            }
        }
        return maxStr;
    }

    /**
     * 修改类目
     * @param categoryQuery 类目查询实体
     * @param optUser   操作用户
     * @author Aison
     * @date 2018/6/5 16:52
     */
    @Override
    public MyLog modifyCategory(CategoryQuery categoryQuery, UserSession optUser) {
        return null;
    }


    /**
     * 查询类目的树
     * @param optUser 操作用户
     * @param query 查询实体
     * @author Aison
     * @date 2018/6/5 16:50
     */
    @Override
    public List<CategoryQuery> categoryTree(CategoryQuery query, UserSession optUser) {
       //查询所有的然后进行分组
       List<Category> categoryList =  categoryMapper.selectList(query);
       List<CategoryQuery> tree = new ArrayList<>();
       categoryList.forEach(action->{
           //父id
           if(action.getParentId()==0) {
               CategoryQuery cQuery = new CategoryQuery();
               Biz.copyBean(action,cQuery);
               tree.add(cQuery);
               loopCategory(categoryList,cQuery);
           }
       });
      return tree;
    }



    /**
     * 递归出类别的树
     * @param categoryList 列表
     * @param query 某一个实体
     * @author Aison
     * @date 2018/6/5 18:03
     */
    private void loopCategory(List<Category> categoryList,CategoryQuery query) {

        categoryList.forEach(action->{
            if(action.getParentId().equals(query.getParentId())) {
                List<CategoryQuery> queries = query.getChildren();
                if(queries == null) {
                    queries = new ArrayList<>();
                }
                CategoryQuery queryChild = new CategoryQuery();
                Biz.copyBean(action,query);
                queries.add(queryChild);
                loopCategory(categoryList,queryChild);
                query.setChildren(queries);
            }
        });
    }











    @Autowired
    private CouponNewMapper couponNewMapper;

    @Autowired
    private CouponTemplateNewMapper couponTemplateNewMapper;

    @Autowired
    private ShopMemberCouponMapper shopMemberCouponMapper;

    @Autowired
    private ShopCouponTemplateMapper shopCouponTemplateMapper;

    @Autowired
    private StoreCouponMapper storeCouponMapper;

    @Autowired
    private StoreCouponTemplateMapper storeCouponTemplateMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveTemplate() {

        StoreCouponTemplateQuery query = new StoreCouponTemplateQuery();
        List<StoreCouponTemplate> storeTemp =  storeCouponTemplateMapper.selectList(query);

        List<CouponTemplateNew> templates = new ArrayList<>();
        for (StoreCouponTemplate oldTemp : storeTemp) {

            CouponTemplateNew newTemp = new CouponTemplateNew();
            newTemp.setComment("app优惠券模板");
            newTemp.setCreateTime(oldTemp.getCreateTime() > 0 ? new Date(oldTemp.getCreateTime()) :null);
            newTemp.setDeadlineBegin(new Date(oldTemp.getValidityStartTime()));
            Long endTime = oldTemp.getValidityEndTime();
            if(endTime==0) {
                newTemp.setDeadlineEnd(Biz.addDate(newTemp.getDeadlineBegin(),100*365*24));
            } else {
                newTemp.setDeadlineEnd(new Date(endTime));
            }
            newTemp.setDeadlineCount(0);
            newTemp.setDeadlineType(0);
            newTemp.setDiscount(BigDecimal.ZERO);

            Long drawStart = oldTemp.getDrawStartTime();
            Long drawEnd = oldTemp.getDrawEndTime();

            newTemp.setDrawEndTime(drawEnd >0 ? new Date(drawEnd) : null);
            newTemp.setDrawStartTime(drawStart>0 ? new Date(drawStart) : null);
            newTemp.setEachReceiveCount(oldTemp.getLimitDraw());
            newTemp.setIssueCount(Long.valueOf(oldTemp.getGrantCount()));
            newTemp.setOldId(oldTemp.getId());
            newTemp.setLimitMoney(oldTemp.getLimitMoney());
            newTemp.setName(oldTemp.getName());
            newTemp.setPrice(oldTemp.getMoney());
            newTemp.setPublishUser(oldTemp.getPublisher());
            newTemp.setPublishUserId(oldTemp.getSupplierId());
            newTemp.setSendType(1);
            newTemp.setSysType(1);
            newTemp.setStatus(oldTemp.getStatus());
            newTemp.setReceiveCount(0L);
            Integer type = oldTemp.getRangeType();
            if(type==0) {
                newTemp.setUseRange(1);
            } else if (type==5){
                newTemp.setUseRange(4);
                newTemp.setRangeIds(oldTemp.getRangeTypeIds());
            } else {
                newTemp.setUseRange(1);
            }

            couponTemplateNewMapper.insertSelective(newTemp);
        }



        templates = new ArrayList<>();
        ShopCouponTemplateQuery squery = new ShopCouponTemplateQuery();
        List<ShopCouponTemplate> shopTemps = shopCouponTemplateMapper.selectList(squery);

        for (ShopCouponTemplate oldTemp : shopTemps) {

            CouponTemplateNew newTemp = new CouponTemplateNew();
            newTemp.setComment("app优惠券模板");
            newTemp.setCreateTime(oldTemp.getCreateTime() > 0 ? new Date(oldTemp.getCreateTime()) :null);
            newTemp.setDeadlineBegin(new Date(oldTemp.getValidityStartTime()));
            Long endTime = oldTemp.getValidityEndTime();
            if(endTime==0) {
                newTemp.setDeadlineEnd(Biz.addDate(newTemp.getDeadlineBegin(),100*365*24));
            } else {
                newTemp.setDeadlineEnd(new Date(endTime));
            }
            newTemp.setDeadlineCount(0);
            newTemp.setDeadlineType(0);
            newTemp.setDiscount(BigDecimal.ZERO);

            newTemp.setDrawEndTime(null);
            newTemp.setDrawStartTime( null);
            newTemp.setEachReceiveCount(1);
            newTemp.setIssueCount(Long.valueOf(oldTemp.getGrantCount()));
            newTemp.setOldId(oldTemp.getId());
            newTemp.setLimitMoney(oldTemp.getLimitMoney());
            newTemp.setName(oldTemp.getName());
            newTemp.setPrice(oldTemp.getMoney());
            newTemp.setPublishUser(oldTemp.getStoreId()+"");
            newTemp.setPublishUserId(oldTemp.getStoreId());
            newTemp.setSendType(1);
            newTemp.setSysType(1);
            newTemp.setStatus(oldTemp.getStatus());
            newTemp.setReceiveCount(0L);
            newTemp.setUseRange(1);
            newTemp.setRangeIds("");
            couponTemplateNewMapper.insertSelective(newTemp);
        }



    }
}
