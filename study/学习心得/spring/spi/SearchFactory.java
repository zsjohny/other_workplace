package com.newman.pay;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SearchFactory {
    private SearchFactory() {
    }

    static ServiceLoader loader = ServiceLoader.load(Search.class);


    public static void newSearch() {

        loader.reload();

        Iterator<Search> searchs = loader.iterator();
        while (searchs.hasNext()) {

            System.out.println(searchs.next().serch("111"));

        }
    }
}