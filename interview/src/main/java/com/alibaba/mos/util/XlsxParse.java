package com.alibaba.mos.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.mos.data.ChannelInventoryDO;
import com.alibaba.mos.data.SkuDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class XlsxParse {
    private String file;
    private BlockingQueue<SkuDO> dataList;
    private boolean isFinish;

    public XlsxParse(String file, int buffer) {
        this.file = file;
        dataList = new ArrayBlockingQueue<>(buffer);
    }

    public BlockingQueue<SkuDO> getDataList() {
        return dataList;
    }

    public boolean isFinish() {
        return isFinish;
    }


    public void process() {
        OPCPackage opcPackage = null;
        try {
            opcPackage = OPCPackage.open(file, PackageAccess.READ);
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            StylesTable styles = xssfReader.getStylesTable();
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            while (iter.hasNext()) {
                try (InputStream stream = iter.next()) {
                    processSheet(styles, strings, new SheetToLineHandler(), stream);
                }
            }
        } catch (Exception e) {
            log.error("exp", e);
        } finally {
            close(opcPackage);
        }

        isFinish = true;
    }

    private void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            XSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                    styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (Exception e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    private void close(OPCPackage opcPackage) {
        if (opcPackage != null) {
            try {
                opcPackage.close();
            } catch (Exception e) {
            }
        }
    }


    private class SheetToLineHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
        private int cellColumn;
        private boolean isTitle;
        private SkuDO skuDO;

        @Override
        public void startRow(int rowNum) {
            skuDO = new SkuDO();
            isTitle = rowNum == 0;
        }

        @Override
        public void endRow(int rowNum) {
            cellColumn = 0;
            if (!isTitle) {
                try {
                    dataList.put(skuDO);
                } catch (Exception e) {
                }
            }

        }

        @Override
        public void cell(String s, String value, XSSFComment xssfComment) {
            if (isTitle) {
                return;
            }
            buildSkuDO(skuDO, cellColumn, value);
            cellColumn++;
        }


        private void buildSkuDO(SkuDO skuDO, int cellColumn, String value) {
            switch (cellColumn) {
                case 0:
                    skuDO.setId(value);
                    break;
                case 1:
                    skuDO.setName(value);
                    break;
                case 2:
                    skuDO.setArtNo(value);
                    break;
                case 3:
                    skuDO.setSpuId(value);
                    break;
                case 4:
                    skuDO.setSkuType(value);
                    break;
                case 5:
                    skuDO.setPrice(new BigDecimal(value));
                    break;
                case 6:
                    skuDO.setInventoryList(JSON.parseArray(value, ChannelInventoryDO.class));
                    break;
                default:
                    log.info("无效的列,{},{}", cellColumn, value);
            }
        }

        @Override
        public void headerFooter(String s, boolean b, String s1) {

        }
    }
}
