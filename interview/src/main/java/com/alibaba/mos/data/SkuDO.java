/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alibaba.mos.data;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author superchao
 * @version $Id: SkuEntity.java, v 0.1 2019年10月28日 10:36 AM superchao Exp $
 */
@Data
public class SkuDO implements Serializable {
    /**
     * sku id
     */
    private String id;

    /**
     * sku 名称
     */
    private String name;

    /**
     * 货号
     */
    private String artNo;

    /**
     * 商品id
     */
    private String spuId;

    /**
     *  sku 类型
     */
    private String skuType;

    /**
     * 价格 分为单位
     */
    private BigDecimal price;

    /**
     * 渠道库存
     */
    List<ChannelInventoryDO> inventoryList;

    // 用作测试占用大内存
//    byte huge[] = new byte[1024*1024*10];
}