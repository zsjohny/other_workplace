package com.jiuyuan.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.entity.message.Message;

/**
 * 
 * 简化的Page对象
 * 用于返回前端分页Json数据
 * @author Administrator
 *
 */
public class SmallPage  implements Serializable {
	
	/* 总记录数 */
    private int total = 0;
	/* 当前页 */
    private int current = 1;
    /* 总页数 */
    private int pages = 1;
    /* 每页显示条数 */
    private int size = 0;
    /* 是否有更多：1有更多、0没有更改 */
    private int isMore = 0;
    /* 查询数据列表  */
    private List records = Collections.emptyList();
    
    public  SmallPage(int current,int size) {
		this.current = current;//当前页数
		this.size = size;//每页显示条数
    }
   
    public void setDate(int total ,List records) {
    	this.total = total;//总记录数
		this.records =  records;//查询数据记录
		this.pages = buildPages1(total,this.size);//总页数
		if(pages > current){
			this.isMore = 1;
		}else{
			this.isMore = 0;
		}
    }
    
    /**
     * 偏移量（每页条数）
     * @return
     */
    public int getLimit() {
        return this.size;
    }

    /**
     * 偏移位置
     * @return
     */
    public int getOffset() {
        return this.size * (this.current - 1);
    }
    
    /**
     * 第一种算法：

    totalPage = totalCount% loadCount== 0 ? totalCount/ loadCount : totalCount/ loadCount+ 1 ;
     * @param total2
     * @param size2
     * @return
     */
    private int buildPages1(int totalCount, int loadCount) {
		return  totalCount % loadCount== 0 ? totalCount/ loadCount : totalCount/ loadCount+ 1 ;
	}
    /**
     * 
    第二种算法：(简便，推荐)
    totalPage = (totalCount+ loadCount-1) / loadCount;
    其中 loadCount- 1 就是 totalCount/ loadCount的最大的余数
     * @param total2
     * @param size2
     * @return
     */
    private int buildPages2(int totalCount, int loadCount) {
    	return (totalCount+ loadCount-1) / loadCount;
	}

	

	public  SmallPage(Page page) {
		this.total = page.getTotal();//总记录数
		this.pages = page.getPages();//总页数
		this.current = page.getCurrent();//当前页数
		this.size = page.getSize();//每页显示条数
		this.records =  page.getRecords();//查询数据记录
		int pages = page.getPages();//总页数
		if(pages > current){
			this.isMore = 1;
		}else{
			this.isMore = 0;
		}
	}

	@Override
	public String toString() {
		return "SmallPage [current=" + current + ", size=" + size + ", records=" + records + "]";
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	public int getIsMore() {
		return isMore;
	}

	public void setIsMore(int isMore) {
		this.isMore = isMore;
	}
	
	public List getRecords() {
		return records;
	}

	public void setRecords(List records) {
		this.records = records;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}


    public SmallPage() {

    }


}
