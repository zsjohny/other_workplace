package com.jiuyuan.util.paging;

import java.util.ArrayList;
import java.util.List;

public class PagingUtil {

    public static final List<PageRange> mapPageToNewPageSize(int page, int pageSize, int newPageSize) {
        int first = pageSize * (page - 1);
        int last = first + pageSize - 1; // inclusive

        int newFirstPage = first / newPageSize + 1;
        int newLastPage = last / newPageSize + 1; // inclusive

        List<PageRange> pageRanges = new ArrayList<PageRange>();
        for (int p = newFirstPage; p <= newLastPage; p++) {
            int newPageFirst = 0;
            int newPageLast = newPageSize - 1;
            if (p == newFirstPage) {
                newPageFirst = first % newPageSize;
            }
            if (p == newLastPage) {
                newPageLast = last % newPageSize;
            }
            pageRanges.add(new PageRange(p, newPageFirst, newPageLast + 1));
        }

        return pageRanges;
    }

    public static final List<PageRange> mapPageToNewPageSizes(int page, int pageSize, int... newPageSizes) {
        List<PageRange> pageRanges = new ArrayList<PageRange>();
        int first = pageSize * (page - 1);

        int newFirstPage = -1;
        int consumed = 0;
        int count = 0;
        for (int p = 1; p <= newPageSizes.length; p++) {
            int newPageSize = newPageSizes[p - 1];
            if (newPageSize <= 0) {
                continue;
            }
            int newPageFirst = -1;
            if (newFirstPage == -1) {
                if (count + newPageSize > first) {
                    newFirstPage = p;
                    newPageFirst = first - count;
                }
            } else {
                newPageFirst = 0;
            }

            if (newPageFirst != -1) {
                int newPageLast = Math.min(newPageSize, newPageFirst + pageSize - consumed) - 1;
                pageRanges.add(new PageRange(p, newPageFirst, newPageLast + 1));
                consumed += newPageLast - newPageFirst + 1;
                if (consumed >= pageSize) {
                    break;
                }
            }

            count += newPageSize;
        }

        return pageRanges;
    }

    public static void main(String[] args) {
        List<PageRange> pageRanges = mapPageToNewPageSizes(1, 20, 5, 10);
        for (PageRange pageRange : pageRanges) {
        }
    }
}
