package com.kmsoft.lucene.database.demo.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 进行搜索
 * </P>
 *
 * @author Major Tom
 * @since 2023/2/14 12:23
 */
public class Searcher {
    /**
     * 进行搜索
     *
     * @param queryStr  查询串
     * @param indexPath 索引所在目录
     */
    public static void search(String queryStr, String indexPath) throws ParseException, IOException {
        long startTime = System.currentTimeMillis();
        //1.创建查询对象，用于查询
        //查询解析对象
        QueryParser queryParser = new QueryParser("title", new IKAnalyzer());
        Query query = queryParser.parse(queryStr);
        //2.创建索引搜索对象，用于执行索引
        //索引路径
        FSDirectory directory = FSDirectory.open((new File(indexPath)).toPath());
        //索引读取对象
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        long midTime = System.currentTimeMillis();
        System.out.println("准备搜索耗时：" + (midTime - startTime) + "ms");
        //3.搜索，得到返回集
        TopDocs searchResult = indexSearcher.search(query, 10);
        long endTime = System.currentTimeMillis();
        System.out.println("搜索耗时:" + (endTime - midTime) + "ms");
        System.out.println(searchResult.totalHits + "个文件被找到，共耗时:" + (endTime - startTime) + "ms");
        System.out.println("查询到的数量为：" + searchResult.totalHits);
        //4.打印结果
        for (ScoreDoc scoreDoc : searchResult.scoreDocs) {
            System.out.println("==============================================================");
            System.out.println("文档ID：" + scoreDoc.doc + "，文档评分：" + scoreDoc.score);
            //获取文档数据
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("电影ID：" + doc.get("filmId"));
            System.out.println("电影名称：" + doc.get("title"));
            System.out.println("电影简介：" + doc.get("description"));
            System.out.println("电影上映年份：" + doc.get("releaseYear"));
            System.out.println("字符数组");
            byte[] byteArr = doc.getBinaryValue("byteArr").bytes;
            for (byte b : byteArr) {
                System.out.print(b + "--");
            }
            byte[] myNames = doc.getBinaryValue("myName").bytes;
            System.out.println("测试："+doc.get("myName"));
            for (byte myName : myNames) {
                System.out.println(myName);
            }
            System.out.println("==============================================================");
        }
        directoryReader.close();
    }
}
