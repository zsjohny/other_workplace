/**
 * 
 */
package com.jiuy.web.controller.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.ClassificationDefinition;
import com.jiuyuan.entity.Dictionary;
import com.jiuyuan.entity.ProductPropValue;

/**
 * @author LWS
 *
 */
public class PropertyTransformUtil {

    public static boolean transformCategory2Classification(Collection<Category> source, Collection<ClassificationDefinition> dest) {
        if(null == dest){
            return false;
        }
        if(null == source){
            return false;
        }
        for(Category s : source){
            ClassificationDefinition d = new ClassificationDefinition();
            d.setClassificationName(s.getCategoryName());
            d.setId((int)s.getId());
            // generate  subs category info
            generateSubsRelation(d,s);
            dest.add(d);
        }
        return true;
    }

    private static void generateSubsRelation(ClassificationDefinition d, Category s) {
        if(null == d || null == s){
            return;
        }
        List<Category> sourceSubs = s.getChildCategories();
        List<String> destSubs = new ArrayList<String>();
        if(null != sourceSubs){
            for(Category sub : sourceSubs){
                destSubs.add(sub.getCategoryName());
            }
        }
        d.setSubClassification(destSubs);
    }

    public static boolean transformProperty2Dictionary(Collection<ProductPropValue> source,Collection<Dictionary> dest){
        if(null == dest){
            dest = new ArrayList<Dictionary>();
        }
        if(null == source){
            return false;
        }
        for(ProductPropValue ppv : source){
            Dictionary d = new Dictionary();
            d.setDictName(ppv.getPropertyValue());
            d.setDictId(String.valueOf(ppv.getId()));
            d.setGroupId(String.valueOf(ppv.getPropertyNameId()));
            dest.add(d);
        }
        return true;
    }
}
