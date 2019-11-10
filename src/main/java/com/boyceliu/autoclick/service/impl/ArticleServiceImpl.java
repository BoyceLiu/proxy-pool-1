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
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Value("${app.articles-path}")
    private String fileName;

    @Value("${app.block-path}")
    private String blockPath;

    private ImmutableList<Article> articles;

    private List<String> userAgents = ImmutableList.of(
            "Mozilla/5.0 (Linux; U; Android 8.0.0; zh-CN; MHA-AL00 Build/HUAWEIMHA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/12.1.4.994 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 8.0.0; zh-CN; MHA-AL00 Build/HUAWEIMHA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.6.4.950 UWS/2.11.1.50 Mobile Safari/537.36 AliApp(DingTalk/4.5.8) com.alibaba.android.rimet/10380049 Channel/227200 language/zh-CN",
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_3) AppleWebKit/534.31 (KHTML, like Gecko) Chrome/17.0.558.0 Safari/534.31 UCBrowser/2.3.1.257",
            "Mozilla/5.0 (Linux; U; Android 4.1.1; zh-CN; M031 Build/JRO03H) AppleWebKit/534.31 (KHTML, like Gecko) UCBrowser/8.8.3.278 U3/0.8.0 Mobile Safari/534.31",
            "Mozilla/5.0 (Linux; U; Android 4.1.1; zh-CN; M040 Build/JRO03H) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/9.4.1.362 U3/0.8.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 4.2.1; zh-CN; M040 Build/JOP40D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/9.6.0.378 U3/0.8.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; M351 Build/KTU84P) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/9.9.5.489 U3/0.8.0 Mobile Safari/533.1",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0_2 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/12A405 UCBrowser/10.0.2.497 Mobile",
            "Mozilla/5.0 (iPad; U; CPU OS 8 like Mac OS X; zh-CN; iPad2,1) AppleWebKit/534.46 (KHTML, like Gecko) UCBrowser/2.7.0.448 U3/ Mobile/10A403 Safari/7543.48.3",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 8_1 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/12B411 UCBrowser/10.0.5.508 Mobile",
            "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; HM NOTE 1LTETD Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/9.9.7.500 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; HM NOTE 1LTE Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.1.0.527 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; HM NOTE 1LTE Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.2.0.535 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-CN; HUAWEI GRA-UL10 Build/HUAWEIGRA-UL10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 5.1; zh-CN; 8681-A01 Build/LMY47D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.9.2.712 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 4.3; zh-CN; SCH-N719 Build/JSS15J) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/9.9.5.489 U3/0.8.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-CN; HUAWEI MT7-CL00 Build/HuaweiMT7-CL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 5.1.1; zh-CN; HUAWEI M2-A01L Build/HUAWEIM2-A01L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-CN; MI 5 Build/MRA58K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/11.0.0.818 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; SM-G9350 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; N5209 Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.10.0.800 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; MI NOTE LTE Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 5.1; zh-CN; P01 Build/LMY47D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/11.0.4.846 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 5.1.1; zh-CN; R7Plusm Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 5.0.2; zh-CN; ZTE A2015 Build/LRX22G) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.5.2.598 U3/0.8.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; OPPO R9s Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-CN; HUAWEI NXT-AL10 Build/HUAWEINXT-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; Coolpad 8297W Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.2.5.884 Mobile Safari/537.36"
    );

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
    public void clickArticle(List<Article> articles, String ip, String port) throws FileNotFoundException {
        String ipPort = ip + ":" + port;
        Proxy p = new Proxy();
        p.setHttpProxy(ipPort);
        p.setSslProxy(ipPort);
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.PROXY, p);

        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        cap.setCapability("chrome.prefs", prefs);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("user-agent=" +
                userAgents.get(new Random(System.currentTimeMillis()).nextInt(userAgents.size())));
        cap.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = new ChromeDriver(cap);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        for (Article article : articles) {
            driver.get(article.getUrl());
        }
        driver.close();

        logger.info("clicked article " + articles);
    }
}
