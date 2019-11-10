package com.boyceliu.autoclick.service;

import com.boyceliu.autoclick.Article;

import java.io.FileNotFoundException;
import java.util.List;

public interface ArticleService {
    List<Article> getArticle();

    /**
     * 加载文章数据
     * @return
     */
    List<Article> loadArticles();

    /**
     * 保存文章数据
     */
    void saveArticles(List<Article> articles) throws FileNotFoundException;

    void saveArticles() throws FileNotFoundException;

    /**
     * 启动模拟器访问文章
     * @param url
     * @param ip
     * @param port
     */
    void clickArticle(String url, String ip, String port);

}
