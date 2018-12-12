package com.onway.baib.core.model;

import com.onway.platform.common.base.ToString;

/**
 * 字段的项
 * 
 * @author wwf
 * @version $Id: Item.java, v 0.1 2016年9月7日 下午5:46:55 wwf Exp $
 */
public class Item extends ToString {
    /**  */
    private static final long serialVersionUID = 1L;

    // 中文姓名
    private String            zh_name;

    // 英文字段
    private String            field;

    // 不为空     true 不为空;false 可以为空
    private boolean           notnull;

    // 是否展示  true 展示；false 不展示
    private boolean           showOrNot;

    // 最大长度
    private int               maxlength;

    //是否展示在表单
    private boolean           formShowOrNot;

    //数据在网页中的类型 
    private String            dataWebType;

    public String getDataWebType() {
        return dataWebType;
    }

    public void setDataWebType(String dataWebType) {
        this.dataWebType = dataWebType;
    }

    public boolean isNotnull() {
        return notnull;
    }

    public void setNotnull(boolean notnull) {
        this.notnull = notnull;
    }

    public boolean isShowOrNot() {
        return showOrNot;
    }

    public void setShowOrNot(boolean showOrNot) {
        this.showOrNot = showOrNot;
    }

    public int getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
    }

    public String getZh_name() {
        return zh_name;
    }

    public void setZh_name(String zh_name) {
        this.zh_name = zh_name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isFormShowOrNot() {
        return formShowOrNot;
    }

    public void setFormShowOrNot(boolean formShowOrNot) {
        this.formShowOrNot = formShowOrNot;
    }

}
