package com.jiuy.core.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyCategory;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyValue;
import com.jiuyuan.entity.newentity.dynamicproperty.DynamicPropertyWithValue;
import com.jiuyuan.service.common.IDynamicPropertyCategoryService;
import com.jiuyuan.service.common.IDynamicPropertyService;
import com.jiuyuan.service.common.IDynamicPropertyValueService;
import com.jiuyuan.util.BizUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Charlie(唐静) on 2018/5/10
 */
@Service
public class ShopPropertyValueDelegator{

    @Autowired
    private IDynamicPropertyValueService iDynamicPropertyValueService;
    @Autowired
    private IDynamicPropertyService iDynamicPropertyService;

    @Autowired
    private IDynamicPropertyCategoryService iDynamicPropertyCategoryService;

    @Autowired
    private com.jiuy.core.dao.modelv2.CategoryMapper categoryMapper;

    /**
     * 遍历查询categoryId及其子孙类的所有PropertyValue
     * @param categoryId
     * @return
     * @author Charlie(唐静)
     * @date 18/05/10
     */
    public List<DynamicPropertyWithValue> propertyValueByCategory(Long categoryId) {
        //查询category层级(默认3)
        int level = 3;
        if (categoryId == null) {
            return null;
        }

        //所有category
        categoryMapper.getCategories();
        List<Category> sources = categoryMapper.getCategoriesByStatus(Arrays.asList(0, 1));
        if (! BizUtil.isNotEmpty(sources) || sources.size() == 0) {
            return null;
        }


        /* 获取categoryId对应的3级子id */
        Set<Long> categoryIds = prepareCategoryIds(categoryId, level, sources);
        if (categoryIds == null || categoryIds.isEmpty()) return null;


        //dynamicPropIdList
        Wrapper<DynamicPropertyCategory> propertyCategoryWrapper = new EntityWrapper<>();
        propertyCategoryWrapper.eq("status", 1).in("cate_id", categoryIds);
        List<DynamicPropertyCategory> propertyCategories = iDynamicPropertyCategoryService.selectList(propertyCategoryWrapper);
        if (propertyCategories == null || propertyCategories.isEmpty()) {
            return null;
        }
        List<Long> dynamicPropIdList = takePropIdFromPropCategory(propertyCategories);


        //propertyValue
        if (dynamicPropIdList == null || dynamicPropIdList.isEmpty()) {
            return null;
        }

        //通过动态属性id获取动态属性和动态属性值(一对多)
        return iDynamicPropertyService.getPropertyAndValues(dynamicPropIdList);
    }


    /**
     * 获取categoryId对应的3级子id
     * @param targetId
     * @param level
     * @param sources
     * @return
     */
    private Set<Long> prepareCategoryIds(Long targetId, int level, List<Category> sources) {
        Set<Long> categoryIds = null;
        boolean hasCat = false;
        for (Category category : sources) {
            if (category.getId() == targetId.longValue()) {
                hasCat = true;
                if (category.getCategoryLevel() == level) {
                    categoryIds = new HashSet<>(1);
                    categoryIds.add(targetId);
                } else {
                    categoryIds = findDescendantSimple(sources, targetId, level, new HashSet<>(sources.size()));
                }
                break;
            }
        }
        if (! hasCat) {
            return null;
        }
        return categoryIds;
    }


    /**
     * 获取动态属性id集合
     *
     * @param propertyCategories
     * @return 动态属性id集合
     * @author Charlie(唐静)
     * @date 18/05/11
     */
    public List<Long> takePropIdFromPropCategory(List<DynamicPropertyCategory> propertyCategories) {
        if (propertyCategories == null || propertyCategories.size() == 0) {
            return null;
        }
        ArrayList<Long> ids = new ArrayList<>(propertyCategories.size());
        for (DynamicPropertyCategory d : propertyCategories) {
            ids.add(d.getDynaPropId());
        }
        return ids;
    }


    /**
     * 查询所有子孙节点
     *
     * @param source
     * @param root
     * @return
     * @author Charlie(唐静)
     * @date 18/05/11
     */
    @Deprecated
    public Set<Long> findDescendantUnion(List<Category> source, Long root) {

        if (BizUtil.isNotEmpty(root, source) && source.size() > 0) {
            Map<Long, Long> pool = new HashMap<>(source.size());
            for (Category category : source) {
                pool.put(category.getId(), category.getParentId());
            }

            Long pId = pool.get(root);
            boolean noRoot = (pId == null);
            if (noRoot) {
                return null;
            }
            deleteUpper(pool, pId);

            boolean clear = false;
            while (! clear) {
                clear = true;
                for (Iterator<Map.Entry<Long, Long>> it = pool.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Long, Long> entry = it.next();
                    if (entry.getKey().longValue() == root.longValue()) {
                        continue;
                    }

                    pId = entry.getValue();

                    if (pId.longValue() == root.longValue()) {
                        continue;
                    }

                    Long grandpaId = pool.get(pId);
                    if (grandpaId == null) {
                        it.remove();
                    } else {
                        pool.put(entry.getKey(), grandpaId);
                        clear = false;
                    }
                }
            }
            return pool.keySet();
        }
        return null;
    }


    /**
     * 查询子孙
     * @param source
     * @param root
     * @param level
     * @param container
     * @return
     */
    public Set<Long> findDescendantSimple(List<Category> source, Long root, int level, Set<Long> container) {
        if (source == null || source.size() == 0) {
            return container;
        }
        for (Category category : source) {
            if (category.getParentId() == root.longValue()) {

                if (category.getCategoryLevel() == level) {
                    container.add(category.getId());
                } else {
                    findDescendantSimple(source, category.getId(), level, container);
                }
            }
        }
        return container;
    }


    /**
     * 删除祖先id
     * @param map
     * @param key
     * @date: 2018/5/10
     * @author: Charlie(唐静)
     */
    private void deleteUpper(Map<Long, Long> map, Long key) {
        boolean isRoot = null == map.get(key);
        if (! isRoot) {
            for (; key != null; key = map.remove(key)) {
            }
        }
    }

    /**
     * 在source中得到target及其所有的子孙
     *
     * @param source
     * @param root
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/10
     */
    @Deprecated
    public Set<Long> getDescendant(List<Category> source, Long root) {
        if (BizUtil.isNotEmpty(root, source) && source.size() > 0) {
            Map<Long, Node> tree = initTree(source);
            return callDescendant(tree, Arrays.asList(root), new HashSet<>(source.size()));
        }
        return null;
    }


    /**
     * 寻找所有子节点
     *
     * @param tree
     * @param roots
     * @param container
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/10
     */
    @Deprecated
    private Set<Long> callDescendant(Map<Long, Node> tree, List<Long> roots, Set<Long> container) {
        for (Long root : roots) {
            Node node = tree.remove(root);
            if (node != null) {
                container.add(node.getId());
                if (node.getChildren() == null || node.getChildren().isEmpty()) {
                    continue;
                }
                container.addAll(callDescendant(tree, node.getChildren(), container));
            }
        }
        return container;
    }


    /**
     * 组建关系
     *
     * @param source
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/10
     */
    private Map<Long, Node> initTree(List<Category> source) {
        Map<Long, Node> map = new HashMap<>(source.size());
        for (Category category : source) {

            long id = category.getId();
            Node node = map.get(id);
            if (node == null) {
                node = new Node();
                node.setId(id);
                map.put(node.id, node);
            }

            long pId = category.getParentId();
            Node pNode = map.get(pId);
            if (pNode == null) {
                pNode = new Node();
                pNode.setId(pId);
                pNode.setChildren(new ArrayList<>(1));
                map.put(pId, pNode);
            }
            List<Long> pChildren = pNode.getChildren();
            pChildren.add(category.getId());
            pNode.setChildren(pChildren);
        }
        return map;
    }

    @Deprecated
    class Node{
        private Long id;
        private List<Long> children;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public List<Long> getChildren() {
            return children;
        }

        public void setChildren(List<Long> children) {
            this.children = children;
        }
    }

}
