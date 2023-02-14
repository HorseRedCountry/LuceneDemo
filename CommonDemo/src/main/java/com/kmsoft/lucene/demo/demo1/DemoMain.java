package com.kmsoft.lucene.demo.demo1;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

/**
 * <p>
 * 启动类
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/13 14:17
 */
public class DemoMain {
    public static void main(String[] args) {
        String indexDir = "D:/Lucene/index";
        String dataDir = "D:/Lucene/data";
        try {
            SearchUtil.createIndex(indexDir, dataDir);
            SearchUtil.search(indexDir, "麻子");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
