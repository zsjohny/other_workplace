package com.jiuy.rb.service.impl.product;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.mapper.product.CategoryRbMapper;
import com.jiuy.rb.model.product.CategoryRb;
import com.jiuy.rb.model.product.CategoryRbQuery;
import com.jiuy.rb.service.product.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品类目的实现类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/14 10:02
 * @Copyright 玖远网络
 */
@Service("categoryService")
public class CategoryServiceImpl implements ICategoryService {


    @Autowired
    private CategoryRbMapper categoryMapper;


    /**
     *  类目的tree
     *
     * @param categoryRbQuery 查询实体
     * @author Aison
     * @date 2018/6/14 11:24
     */
    @Override
    public List<CategoryRbQuery> categoryRbMyList(CategoryRbQuery categoryRbQuery) {

        categoryRbQuery.setNotDel(1);
        categoryRbQuery.setOrderBy("Weight desc");
        List<CategoryRb> categoryRbList = categoryMapper.selectList(categoryRbQuery);
        List<CategoryRbQuery> categoryRbQueryList = new ArrayList<>();
        categoryRbList.forEach(action->{
            if(action.getParentId()==0) {
                CategoryRbQuery query =  Biz.copyBean(action,new CategoryRbQuery());
                recursionCategory(categoryRbList,query);
                categoryRbQueryList.add(query);
            }
        });
        return categoryRbQueryList;
    }

    /**
     *  递归出一棵树
     *
     * @param categoryRbs 源list
     * @param parentCategory 父
     * @author Aison
     * @date 2018/6/14 11:29
     */
    private void recursionCategory(List<CategoryRb> categoryRbs,CategoryRbQuery parentCategory) {

        categoryRbs.forEach(action->{
            List<CategoryRbQuery> children = parentCategory.getChildren();
            if(action.getParentId().equals(parentCategory.getId())) {
                if(children==null) {
                    children = new ArrayList<>();
                }
                CategoryRbQuery query =  Biz.copyBean(action,new CategoryRbQuery());
                recursionCategory(categoryRbs,query);
                children.add(query);
                parentCategory.setChildren(children);
            }
        });
    }

    /**
     * 修改某个类目
     *
     * @param categoryRb 要修改的
     * @param optUser 操作员
     * @author Aison
     * @date 2018/6/14 15:18
     */
    @Override
    @MyLogs(logInfo = "修改类目",model = ModelType.PRODUCT_MODEL)
    public MyLog<CategoryRb> updateCategory(CategoryRbQuery categoryRb,UserSession optUser) {

        if(categoryRb.getId() == null){
            throw BizException.paramError();
        }
        CategoryRb old = categoryMapper.selectByPrimaryKey(categoryRb.getId());
        if(old == null) {
            throw BizException.paramError();
        }
        // 只允许修改3项
        // 名称 是否显示 描述 权重
        CategoryRb categoryRbRew = new CategoryRb();
        categoryRbRew.setCategoryName(categoryRb.getCategoryName());
        categoryRbRew.setStatus(categoryRb.getStatus());
        categoryRbRew.setDescription(categoryRb.getDescription());
        categoryRbRew.setWeight(categoryRb.getWeight());
        categoryRbRew.setId(categoryRb.getId());
        // 如果是删除
        if(categoryRbRew.getStatus()==-1) {
            // 判断是否有子类目
            CategoryRbQuery query = new CategoryRbQuery();
            query.setParentId(categoryRb.getId());
            int count = categoryMapper.selectCount(query);
            if(count>0) {
                throw BizException.instance(GlobalsEnums.CON_NOT_DEL_PARENT_CATEGORY);
            }
        }

        categoryMapper.updateByPrimaryKeySelective(categoryRbRew);

        return new MyLog<CategoryRb>(old,categoryRbRew,optUser).setData(categoryRbRew);
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
    public MyLog<CategoryRb> addCategory(CategoryRb category, UserSession optUser) {

        //验证几个关键的参数
        if(Biz.hasEmpty(category.getCategoryName())) {
            throw BizException.paramError();
        }
        if(Biz.hasEmpty(category.getCategoryType())) {
            category.setCategoryType(0);
        }
        // 如果是一级菜单 则查找数据库中的code最大的一级菜单加1
        String maxStr = categoryCode(category.getParentId());
        category.setCode(maxStr);
        Long time = System.currentTimeMillis();
        category.setUpdateTime(time);
        category.setCreateTime(time);
        categoryMapper.insertSelective(category);

        return  new MyLog<CategoryRb>(category,MyLog.Type.add,optUser).setData(category);
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
        CategoryRbQuery query = new CategoryRbQuery();
        if(parentId==null || parentId ==0 ){
            query.setParentId(0L);
            max = categoryMapper.selectMaxCode(query);
        } else {
            query.setParentId(parentId);
            max = categoryMapper.selectMaxCode(query);
            CategoryRb parent = categoryMapper.selectByPrimaryKey(parentId);
            parentCode = parent.getCode();
        }
        String maxStr = parentCode ;
        // 说明是1级分类
        try{
           if(Biz.hasEmpty(parentCode)){
               maxStr+=Long.valueOf(max)+1;
           }else {
               if(max== null) {
                   maxStr +="_"+10001L;
               } else {
                   String[] maxs = max.split("_");
                   maxStr+="_"+(Long.valueOf(maxs[maxs.length-1])+1);
               }
           }
        }catch (Exception e) {
           e.printStackTrace();
        }
        return maxStr;
    }
}
