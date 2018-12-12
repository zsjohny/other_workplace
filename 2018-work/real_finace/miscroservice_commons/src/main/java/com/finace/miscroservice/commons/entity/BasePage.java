package com.finace.miscroservice.commons.entity;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * page的基类
 */
public class BasePage {
    private final static int DEFAULT_PAGE_SIZE = 10;
    /**
     * 开始页数
     */
    private int pageNum;

    /**
     * 查询个数
     */
    private int pageSize;

    /**
     * 获取总数
     */

    private  int total;


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        if (pageNum <= 0) {
            return;
        }

        this.pageNum = pageNum;

        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        PageHelper.startPage(this.pageNum, pageSize);

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            return;
        }
        this.pageSize = pageSize;
        if (pageNum > 0) {
            PageHelper.startPage(pageNum, this.pageSize);
        }
    }


    /**
     * 静态设置开始页数
     * @param pageNum
     */
    public static void setPage(Integer pageNum) {

        if (pageNum == null || pageNum <= 0) {
            return;
        }

        PageHelper.startPage(pageNum, DEFAULT_PAGE_SIZE);



    }

    public  <T> int getTotal(List<T> list) {



        if (list == null || list.isEmpty()) {
            return total;
        }
        PageInfo<T> pageInfo = new PageInfo<>(list);

        if (pageInfo != null) {
            total = (int) pageInfo.getTotal();
        }

        return total;
    }


}
