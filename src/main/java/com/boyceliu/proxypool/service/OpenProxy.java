package com.boyceliu.proxypool.service;

public interface OpenProxy {

    /**
     * 获得一个代理，没有回阻塞
     * @return
     */
    String getOneProxy();
}
