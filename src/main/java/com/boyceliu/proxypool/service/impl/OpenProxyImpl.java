package com.boyceliu.proxypool.service.impl;

import com.boyceliu.proxypool.service.OpenProxy;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.*;

@Service
public class OpenProxyImpl implements OpenProxy {
    @Value("${app.proxy-url}")
    private String proxyUrl;

    private BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(50);

    private static final Logger logger = LoggerFactory.getLogger(OpenProxyImpl.class);

    @PostConstruct
    public void init() {
        generateProxy();
    }

    @Override
    public String getOneProxy() {
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generateProxy() {
        new Thread(() -> {
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(60);
            while (true) {
                for (String ipPort: getAnonymousProxy()) {
                    fixedThreadPool.execute(() -> {
                        if (checkUrl(ipPort.split(":")[0], ipPort.split(":")[1])) {
                            try {
                                blockingQueue.put(ipPort);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String[] getAnonymousProxy() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(3, TimeUnit.MINUTES).build();
        final Request request = new Request.Builder()
                .url(proxyUrl)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            return response.body().string().split("\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return new String[0];
    }

    private boolean checkUrl(String ip, String port) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, Integer.parseInt(port))))
                .build();
        final Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            return response.code() == 200;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
