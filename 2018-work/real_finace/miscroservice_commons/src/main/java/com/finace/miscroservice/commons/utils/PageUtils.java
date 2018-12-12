package com.finace.miscroservice.commons.utils;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class PageUtils<T> implements Serializable {
        private static final long serialVersionUID = 1L;

        //总记录数
        private long total;
        //总页数
        private int pages;
        //结果集
        private List<T> list;


        public PageUtils() {
        }

        /**
         * 包装Page对象
         *
         * @param list
         */
        public PageUtils(List<T> list) {
            this(list, 8);
        }

        /**
         * 包装Page对象
         *
         * @param list          page结果
         * @param navigatePages 页码数量
         */
        public PageUtils(List<T> list, int navigatePages) {
            if (list instanceof Page) {
                Page page = (Page) list;

                this.pages = page.getPages();
                this.list = page;
                this.total = page.getTotal();
            } else if (list instanceof Collection) {

                this.list = list;
                this.total = list.size();
            }
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }




        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("PageInfo{");

            sb.append(", total=").append(total);
            sb.append(", pages=").append(pages);
            sb.append(", list=").append(list);
            sb.append('}');
            return sb.toString();
        }


}
