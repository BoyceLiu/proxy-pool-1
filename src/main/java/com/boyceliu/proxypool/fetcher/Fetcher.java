package com.boyceliu.proxypool.fetcher;

/**
 *
 */
public interface Fetcher {

    boolean hasNextPage();

    String nextPage();
}
