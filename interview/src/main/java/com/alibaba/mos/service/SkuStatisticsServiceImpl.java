package com.alibaba.mos.service;

import com.alibaba.mos.api.SkuReadService;
import com.alibaba.mos.api.SkuStatisticsService;
import com.alibaba.mos.data.ChannelInventoryDO;
import com.alibaba.mos.data.SkuDO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkuStatisticsServiceImpl implements SkuStatisticsService {

    @Resource
    private SkuReadService skuReadService;

    // TODO 不考虑service 单例
    private final List<String> skuArrayByPrice = new ArrayList<>();
    private final Map<String, TreeSet<ChannelInventoryDOExt>> map = new HashMap<>();
    private BigDecimal totalPrice = BigDecimal.ZERO; // 长度溢出问题，可以用字符按位相加，进位


    @Override
    public void statisticsData() {
        skuReadService.loadSkus(skuDO -> {
            // 维护中间价格数据
            int price = skuDO.getPrice().intValue();
            skuArrayByPrice.add(price, skuDO.getId());
            // top5
            buildTop5ByChannel(skuDO);
            // sum
            totalPrice = totalPrice.add(skuDO.getPrice());
            return skuDO;
        });

        // =========打印结果=============

        printMiddlePrice();
        printTop5();
        log.info("Sum Price:{}", totalPrice);

    }

    // 获取价格在最中间的任意一个skuId , TODO 偶数？
    void printMiddlePrice() {
        skuArrayByPrice.remove(null);
        // 3 - 0,1,2
        // 4 - 0,1,2,3
        log.info("获取价格在最中间的任意一个skuId:{}", skuArrayByPrice.get(skuArrayByPrice.size() / 2));
    }

    void printTop5() {
        for (Map.Entry<String, TreeSet<ChannelInventoryDOExt>> entry : map.entrySet()) {
            String channel = entry.getKey();
            List<String> skuList = entry.getValue().stream().map(ChannelInventoryDOExt::getSkuId).collect(Collectors.toList());
            log.info("channel:{},top5 sku:{}", channel, skuList);
        }
    }

    // 每个渠道库存量为前五的skuId列表
    void buildTop5ByChannel(SkuDO skuDO) {
        // mail : [1,3,5,7,9]
        for (ChannelInventoryDO channelInventoryDO : skuDO.getInventoryList()) {
            ChannelInventoryDOExt doExt = new ChannelInventoryDOExt(channelInventoryDO);
            doExt.setSkuId(skuDO.getId());
            if (map.get(channelInventoryDO.getChannelCode()) == null) {
                TreeSet<ChannelInventoryDOExt> set = new TreeSet<>(Comparator.comparing(ChannelInventoryDO::getInventory));
                set.add(doExt);
            } else {
                TreeSet<ChannelInventoryDOExt> set = map.get(channelInventoryDO.getChannelCode());
                set.add(doExt);
                if (set.size() > 5) {
                    set.remove(set.first());
                }
            }
        }
    }


    @Data
    class ChannelInventoryDOExt extends ChannelInventoryDO {
        private String skuId;

        ChannelInventoryDOExt(ChannelInventoryDO inventoryDO) {
            this.setChannelCode(inventoryDO.getChannelCode());
            this.setInventory(inventoryDO.getInventory());
        }

    }

}
