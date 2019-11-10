package com.boyceliu.autoclick.service.impl;

import com.boyceliu.autoclick.Article;
import com.boyceliu.autoclick.service.ArticleService;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Value("${app.articles-path}")
    private String fileName;

    private ImmutableList<Article> articles;

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Override
    public List<Article> getArticle() {
        if (CollectionUtils.isEmpty(articles)) {
            return loadArticles();
        }
        return articles;
    }

    @Override
    public List<Article> loadArticles() {
        List<Article> result = new ArrayList<>();
        try (Stream<String> lines = Files.lines(ResourceUtils.getFile(fileName).toPath())) {
            result = lines.map(line -> {
                String[] mate = line.split(",");
                return Article.builder().url(mate[0]).count(Integer.parseInt(mate[1])).build();
            }).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("读取文件失败: %s", e.getCause());
        }

        articles = ImmutableList.copyOf(result);

        return result;
    }


    @Override
    public void saveArticles(List<Article> articles) throws FileNotFoundException {
        Path fpath = ResourceUtils.getFile(fileName).toPath();
        //创建文件
        if (!Files.exists(fpath)) {
            try {
                Files.createFile(fpath);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        //创建BufferedWriter
        try {
            BufferedWriter bfw = Files.newBufferedWriter(fpath);
            bfw.write(String.join("\n",
                    articles.stream().map(Article::toString).collect(Collectors.toList())));
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveArticles() throws FileNotFoundException {
        saveArticles(articles);
    }

    @Override
    public void clickArticle(String url, String ip, String port) {
        String ipPort = ip + ":" + port;
        Proxy p = new Proxy();
        p.setHttpProxy(ipPort);
        p.setSslProxy(ipPort);
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.PROXY, p);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        cap.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = new ChromeDriver(cap);
        driver.get(url);
        driver.close();

        logger.info("clicked article " + url);
    }
}
