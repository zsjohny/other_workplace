package com.jiuy.base.model;

import java.util.ArrayList;
import java.util.List;
import com.github.pagehelper.Page;
import com.jiuy.base.util.Biz;
import lombok.Data;

/**   
 * 封装的分页类
 * @author Aison
 * @date  2018年4月18日 下午1:18:03
 * @Copyright: 玖远网络
 */
@Data
public class MyPage<T> {

	private static final long serialVersionUID = 1L;

	private Integer pageNum;

	private Integer pages;

	private Integer cPage;

	private Long total;

	private Integer pageSize;

	private List<T> rows;


	/**
	 * 将结合中的元素替换成其子类的类型
	 * @param sourceList 源集合
	 * @param targetClass 目标类
	 * @param myJob 回调
	 * @author Aison
	 * @date 2018/6/5 17:39
	 */
	public static <T,E extends T> MyPage<E> copy2Child(List<T> sourceList, Class<E> targetClass,MyJob<T,E> myJob) {
		MyPage<E> myPage = new MyPage<>();myPage.fill(sourceList);

		List<E> list = new ArrayList<>();
		try{
			for (T row : sourceList) {
				E targetObj = targetClass.newInstance();
				Biz.copyBean(row,targetObj);
				myJob.execute(row,targetObj);
				list.add(targetObj);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		myPage.setRows(list);
		return myPage;
	}

	public MyPage(){}

	public MyPage(List<T> list) {
		if (list instanceof Page) {
			Page<T> page = (Page<T>) list;
			this.pageNum = page.getPageNum();
			this.pages = page.getPages();
			this.pageSize = page.getPageSize();
			this.rows = list;
			this.cPage = this.rows.size();
			this.total = page.getTotal();
		} else {
			this.rows = list;
		}
	}

	private void fill(List list) {
		if (list instanceof Page) {
			Page page = (Page) list;
			this.setPageNum(page.getPageNum());
			this.setPages(page.getPages());
			this.setPageSize(page.getPageSize());
			this.setCPage(list.size());
			this.setTotal(page.getTotal());
		}
	}


}
