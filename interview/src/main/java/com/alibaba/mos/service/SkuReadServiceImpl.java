/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alibaba.mos.service;

import com.alibaba.mos.api.SkuReadService;
import com.alibaba.mos.data.SkuDO;
import com.alibaba.mos.util.XlsxParse;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 * TODO: 实现
 *
 * @author superchao
 * @version $Id: SkuReadServiceImpl.java, v 0.1 2019年10月28日 10:49 AM superchao Exp $
 */
@Service
public class SkuReadServiceImpl implements SkuReadService {

    /**
     * 这里假设excel数据量很大无法一次性加载到内存中
     *
     * @param handler
     */
    @Override
    public void loadSkus(SkuHandler handler) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("data/skus.xlsx");
        String filepath = url.getPath();
        XlsxParse xlsxParse = new XlsxParse(filepath, 200);

        new Thread(() -> {
            try {
                xlsxParse.process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        while (!xlsxParse.isFinish()) {
            BlockingQueue<SkuDO> list = xlsxParse.getDataList();
            if (list == null || list.isEmpty()) {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }

                continue;
            }
            while (true) {
                SkuDO skuDO = list.poll();
                if (skuDO == null) {
                    break;
                } else {
                    handler.handleSku(skuDO);
                }
            }
        }
    }
}