package com.jiuyuan.ext.mybatis;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * <pre>
 * 根据RegisterTypeHandler注解扫描type handler。现在的实现有缺陷：对resultMap中的字段无效。
 * 在resultMap中需要手动指定type handler，比如：
 * 
 * &lt;resultMap id="ArticleJoinSourceMap" type="Article" extends="ArticleMap"&gt;
 *     &lt;result property="sourceDescription" column="Description" /&gt;
 *     &lt;result property="sourceLayoutType" column="LayoutType" typeHandler="IntEnumTypeHandler" /&gt;
 * &lt;/resultMap&gt;
 * 
 * </pre>
 * 
 */
public class CustomSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private String handledTypesPackages;

    private List<Class<?>> handledTypesBaseClasses;

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        SqlSessionFactory factory = super.buildSqlSessionFactory();
        scanHandledTypes(factory.getConfiguration());
        return factory;
    }

    protected void scanHandledTypes(Configuration configuration) {
        if (StringUtils.isBlank(handledTypesPackages) || handledTypesBaseClasses == null) {
            return;
        }

        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();

        String[] packages = StringUtils.split(handledTypesPackages, ",; ");
        for (Class<?> baseClass : handledTypesBaseClasses) {
            RegisterTypeHandler defaultHandler = baseClass.getAnnotation(RegisterTypeHandler.class);

            for (String pkg : packages) {
                ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
                resolverUtil.find(new ResolverUtil.IsA(baseClass), pkg);
                Set<Class<? extends Class<?>>> typeSet = resolverUtil.getClasses();
                for (Class<?> type : typeSet) {
                    RegisterTypeHandler handler = type.getAnnotation(RegisterTypeHandler.class);
                    if (handler != null) {
                        registry.register(type, handler.value());
                    } else if (defaultHandler != null) {
                        registry.register(type, defaultHandler.value());
                    }
                }
            }
        }
    }

    public String getHandledTypesPackages() {
        return handledTypesPackages;
    }

    public void setHandledTypesPackages(String handledTypesPackages) {
        this.handledTypesPackages = handledTypesPackages;
    }

    public List<Class<?>> getHandledTypesBaseClasses() {
        return handledTypesBaseClasses;
    }

    public void setHandledTypesBaseClasses(List<Class<?>> handledTypesBaseClasses) {
        this.handledTypesBaseClasses = handledTypesBaseClasses;
    }
}
