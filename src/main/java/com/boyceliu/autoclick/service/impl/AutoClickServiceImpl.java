package com.boyceliu.autoclick.service.impl;

import com.boyceliu.autoclick.AutoClickThread;
import com.boyceliu.autoclick.service.ArticleService;
import com.boyceliu.autoclick.service.AutoClickService;
import com.boyceliu.proxypool.service.OpenProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoClickServiceImpl implements AutoClickService {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private OpenProxy openProxy;

    @Override
    public void autoClick() {
        new AutoClickThread(articleService, openProxy).start();
    }
}
