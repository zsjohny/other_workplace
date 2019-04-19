package com.jiuyuan.constant;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 属性值id
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/6/12 16:08
 * @Copyright 玖远网络
 */
public enum PropertyNameEnum{

    /**
     * 颜色
     */
    COLOR("color", 7L),
    /**
     * 尺码
     */
    SIZE("size", 8L),
    /**
     * 季节
     */
    SEASON("season", 9L),
    /**
     * 年份
     */
    YEAR("year", 10L);



    private String code;
    private Long id;


    PropertyNameEnum(String code, Long id) {
        this.code = code;
        this.id= id;
    }


    /**
     * 根据id查询code
     *
     * @param id id
     * @return java.lang.String
     * @author Charlie(唐静)
     * @date 2018/7/10 9:08
     */
    public static String findCodeById(Long id) {
        for (PropertyNameEnum obj : PropertyNameEnum.values()) {
            if (obj.id.equals(id)) {
                return obj.code;
            }
        }
        return null;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 该枚举是否包含所有的codes
     *
     * @param codes 属性值对应的关键字数组
     * @return 是否全部包含
     * @author Charlie(唐静)
     * @date 2018/7/8 22:37
     */
    public static boolean containCodes(List<String> codes) {

        if (codes == null || codes.isEmpty()) {
            return true;
        }
        PropertyNameEnum[] values = PropertyNameEnum.values();
        ArrayList<String> codeList = new ArrayList<>(values.length);
        for (PropertyNameEnum propertyNameEnum : values) {
            codeList.add(propertyNameEnum.code);
        }
        return codeList.containsAll(codes);
    }

    /**
     * 根据code获得对象
     *
     * @param code code
     * @return com.jiuy.rb.enums.ProductPropNameIdEnum nullable
     * @author Charlie(唐静)
     * @date 2018/7/6 17:47
     */
    public static PropertyNameEnum findIdByCode(String code) {
        for (PropertyNameEnum obj :PropertyNameEnum.values()){
            if (obj.code.equals(code)) {
                return obj;
            }
        }
        return null;
    }

    /**
     * 根据codes查询id
     *
     * @param codes code
     * @return java.lang.Long NonNull
     * @author Charlie(唐静)
     * @date 2018/7/6 17:46
     */
    public static List<Long> findIdByCode(List<String> codes) {
        ArrayList<Long> ids = new ArrayList<>(codes.size());
        codes.forEach(code->{
            PropertyNameEnum obj = findIdByCode(code);
            if (obj != null) {
                ids.add(obj.id);
            }
        });
        return ids;
    }



    /**
     * 该枚举是否包含所有的codes
     *
     * @param codes 属性值对应的关键字数组
     * @return 是否全部包含
     * @author Charlie(唐静)
     * @date 2018/7/8 22:37
     */
    public static boolean containCodes(String[] codes) {
        if (codes == null || codes.length ==0) {
            return true;
        }
        return containCodes(Arrays.asList(codes));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
