/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alibaba.mos.service;

import com.alibaba.mos.api.ProviderConsumer;
import com.alibaba.mos.api.SkuReadService;
import com.alibaba.mos.data.ChannelInventoryDO;
import com.alibaba.mos.data.ItemDO;
import com.alibaba.mos.data.SkuDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author superchao
 * @version $Id: ItemAggregationProviderConsumerImpl.java, v 0.1 2019年11月20日 3:06 PM superchao Exp $
 */
@Service
@Slf4j
public class ItemAggregationProviderConsumer implements ProviderConsumer<List<ItemDO>> {
    @Autowired
    SkuReadService skuReadService;

    private static final Integer capacity = 1000;
    private static final int providerSize = 1;
    private static final int consumerSize = 2;
    private static final int timeout = 1000 * 10;
    private static final Map<String, ItemDO> spuMap = new ConcurrentHashMap<>();
    private static final Map<String, ItemDO> artMap = new ConcurrentHashMap<>();

    final BlockingQueue<SkuDO> queue = new ArrayBlockingQueue<>(capacity);
    private static volatile boolean providerFinish = false;

    @Override
    public void execute(ResultHandler<List<ItemDO>> handler) {
        for (int i = 0; i < providerSize; i++) {
            new Thread(new Provider()).start();
        }
        CountDownLatch countDownLatch = new CountDownLatch(consumerSize);
        for (int i = 0; i < consumerSize; i++) {
            new Thread(new Consumer(countDownLatch)).start();
        }
        try {
            countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("countDownLatch.await exp", e);
        }

        List<ItemDO> list = new ArrayList<>();
        list.addAll(spuMap.values());
        list.addAll(artMap.values());
        handler.handleResult(list);
    }

    class Provider implements Runnable {

        private boolean stop = false;

        @Override
        public void run() {
            while (!stop) {
                skuReadService.loadSkus(skuDO -> {
                    if (skuDO == null) {
                        stop = true;
                        providerFinish = true;
                        return null;
                    }
                    try {
                        queue.put(skuDO);
                    } catch (InterruptedException e) {
                        log.error("provider put error,skuId:{}", skuDO.getId(), e);
                    }
                    return skuDO;
                });
            }
        }
    }

    class Consumer implements Runnable {
        private CountDownLatch countDownLatch;

        Consumer(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    SkuDO skuDO = queue.poll();
                    if (skuDO == null) {
                        if (providerFinish) {
                            break;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    // 按货号(artNo)
                    if ("ORIGIN".equalsIgnoreCase(skuDO.getSkuType())) {
                        ItemDO last = artMap.get(skuDO.getArtNo());
                        if (last == null) {
                            artMap.put(skuDO.getArtNo(), buildItem(skuDO));
                        } else {
                            artMap.put(skuDO.getArtNo(), rebuildItem(last, skuDO));
                        }
                    }
                    // 按spuId
                    else if ("DIGITAL".equalsIgnoreCase(skuDO.getSkuType())) {
                        ItemDO last = spuMap.get(skuDO.getSpuId());
                        if (last == null) {
                            spuMap.put(skuDO.getSpuId(), buildItem(skuDO));
                        } else {
                            spuMap.put(skuDO.getSpuId(), rebuildItem(last, skuDO));
                        }
                    }
                }
            } finally {
                countDownLatch.countDown();
            }
        }

        //item的最大价格、最小价格、sku列表及总库存
        private ItemDO buildItem(SkuDO skuDO) {
            ItemDO itemDO = new ItemDO();
            itemDO.setName(skuDO.getName()); // TODO 聚合后以哪个为准
            itemDO.setArtNo(skuDO.getArtNo());
            itemDO.setSpuId(skuDO.getSpuId()); // TODO 按artNo聚合后，以哪个值为准
            itemDO.setMaxPrice(skuDO.getPrice());
            itemDO.setMinPrice(skuDO.getPrice());
            List<String> skuIds = new ArrayList<>();
            skuIds.add(skuDO.getId());
            itemDO.setSkuIds(skuIds);
            BigDecimal sum = skuDO.getInventoryList().stream().map(ChannelInventoryDO::getInventory).reduce(BigDecimal.ZERO, BigDecimal::add);
            itemDO.setInventory(sum);
            return itemDO;
        }

        private ItemDO rebuildItem(ItemDO itemDO, SkuDO skuDO) {

            if (skuDO.getPrice().compareTo(itemDO.getMaxPrice()) > 0) {
                itemDO.setMaxPrice(skuDO.getPrice());
            }
            if (skuDO.getPrice().compareTo(itemDO.getMinPrice()) < 0) {
                itemDO.setMinPrice(skuDO.getPrice());
            }

            List<String> skuIds = itemDO.getSkuIds();
            if (!skuIds.contains(skuDO.getId())) {
                skuIds.add(skuDO.getId());
            }
            itemDO.setInventory(itemDO.getInventory().add(skuDO.getPrice()));
            return itemDO;
        }
    }
}