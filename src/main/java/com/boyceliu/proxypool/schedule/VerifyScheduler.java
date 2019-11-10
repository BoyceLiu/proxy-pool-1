package com.boyceliu.proxypool.schedule;

import com.boyceliu.common.Scheduler;
import com.boyceliu.proxypool.entity.ProxyEntity;
import com.boyceliu.proxypool.store.ObjectStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class VerifyScheduler extends Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(VerifyScheduler.class);

    public VerifyScheduler(long defaultInterval, TimeUnit defaultUnit) {
        super(defaultInterval, defaultUnit);
    }

    @Override
    public void run() {
        List<ProxyEntity> proxys = ObjectStore.getInstance().getAll();
        logger.info("verify scheduler running, proxys:"+proxys.size());
        ProxyVerifier.refreshAll(proxys);
    }
}
