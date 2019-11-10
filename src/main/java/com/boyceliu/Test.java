package com.boyceliu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Test {
    public static void main(String[] args) throws InterruptedException {
//        try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
//            final HtmlPage page = webClient.getPage("https://mparticle.uc.cn/article.html");
//            System.out.println(page.asXml());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        for (int i = 0; i < 8000; i++) {
            System.out.println("++++++++++++++++++++++++" + i + "++++++++++++++++++++++++++++");
            driver.get("https://mparticle.uc.cn/article.html?uc_param_str=frdnsnpfvecpntnwprdssskt&wm_aid=d71cd060eb25449899f321fc13c7cccc");
            Thread.sleep(200);
            driver.get("https://mparticle.uc.cn/article.html?uc_param_str=frdnsnpfvecpntnwprdssskt&wm_aid=65b31ebf7f1b48a6b65c1e7b6812c0f8");
            Thread.sleep(10000);
        }

        driver.close();
    }
}