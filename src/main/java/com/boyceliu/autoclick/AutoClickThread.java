package com.boyceliu.autoclick;

import com.boyceliu.autoclick.service.ArticleService;
import com.boyceliu.proxypool.service.OpenProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;

@Component
public class AutoClickThread extends Thread {
    private ArticleService articleService;
    private OpenProxy openProxy;

    private static final Logger logger = LoggerFactory.getLogger(AutoClickThread.class);

    public AutoClickThread(ArticleService articleService, OpenProxy openProxy) {
        this.articleService = articleService;
        this.openProxy = openProxy;
    }

    @PostConstruct
    public void init() {
        run();
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("save article");
            try {
                articleService.saveArticles();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }));

        while (true) {
            String ipPort = openProxy.getOneProxy();
            if (ipPort == null) {
                continue;
            }
            String ip = ipPort.split(":")[0];
            String port = ipPort.split(":")[1];
            for (Article article : articleService.getArticle()) {
                articleService.clickArticle(article.getUrl(), ip, port);
            }
        }
    }
}
