package com.boyceliu.proxypool.schedule;

import com.boyceliu.common.Scheduler;
import com.boyceliu.proxypool.entity.ProxyEntity;
import com.boyceliu.proxypool.fetcher.*;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class FetchScheduler extends Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(FetchScheduler.class);

    public FetchScheduler(long defaultInterval, TimeUnit defaultUnit) {
        super(defaultInterval, defaultUnit);
    }

    @Override
    public void run() {
        logger.info("fetch scheduler running...");

        List<AbstractFetcher<List<ProxyEntity>>> fetchers = ImmutableList.of(
                new KuaiDailiFetcher(8), new Www66IPFetcher(8),
                new XichiDailiFetcher(8), new GoubanjiaFetcher(8)
        );

        for (AbstractFetcher<List<ProxyEntity>> fetcher : fetchers) {
            fetcher.fetchAll(ProxyVerifier::verifyAll);
        }

        logger.info("finish fetch scheduler");
    }
}
