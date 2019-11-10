package com.boyceliu.proxypool;

import com.boyceliu.proxypool.fetcher.AbstractFetcher;
import com.boyceliu.proxypool.fetcher.GoubanjiaFetcher;

/**
 *
 */
public class Main {
    public static void main(String[] args) {
        AbstractFetcher crawler = new GoubanjiaFetcher(100);
        crawler.fetchAll();
    }
}
