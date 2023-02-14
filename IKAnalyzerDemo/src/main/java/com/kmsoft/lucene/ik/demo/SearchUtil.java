package com.kmsoft.lucene.ik.demo;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

/**
 * <p>
 * 搜索工具类
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/14 10:21
 */
public class SearchUtil {

    public static Indexer indexer;
    public static Searcher searcher;

    public static void createIndex(String indexDir, String dataDir) throws IOException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFilter());
        long endTime = System.currentTimeMillis();
        System.out.println(numIndexed + "，共耗时: " + (endTime - startTime) + " ms");
        indexer.close();
    }

    public static void search(String indexDir, String queryStr) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs topDocs = searcher.search(queryStr);
        long endTime = System.currentTimeMillis();
        System.out.println(topDocs.totalHits + "个文件被找到，共耗时:" + (endTime - startTime));
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = searcher.getDocument(scoreDoc);
            System.out.println("File: " + document.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }

}
