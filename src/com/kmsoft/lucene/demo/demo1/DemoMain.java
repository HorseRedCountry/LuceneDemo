package com.kmsoft.lucene.demo.demo1;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

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

    String indexDir = "D:/Lucene/index";
    String dataDir = "D:/Lucene/data";
    Indexer indexer;
    Searcher searcher;

    public static void main(String[] args) {
        DemoMain test;
        try {
            test = new DemoMain();
//            test.createIndex();
            test.search("zhangsan");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFilter());
        long endTime = System.currentTimeMillis();
        System.out.println(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");
        indexer.close();
    }

    private void search(String queryStr) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs topDocs = this.searcher.search(queryStr);
        long endTime = System.currentTimeMillis();
        System.out.println(topDocs.totalHits + " documents found. Time :" + (endTime - startTime));
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = searcher.getDocument(scoreDoc);
            System.out.println("File: " + document.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }

}
