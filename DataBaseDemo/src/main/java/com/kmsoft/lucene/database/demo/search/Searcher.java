package com.kmsoft.lucene.database.demo.search;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

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
    public static void search(String queryStr, String indexPath) throws ParseException, IOException, InvalidTokenOffsetsException {
        long startTime = System.currentTimeMillis();
        //1.创建查询对象，用于查询
        //查询解析对象
        IKAnalyzer ikAnalyzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("description", ikAnalyzer);
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
        QueryScorer queryScorer = new QueryScorer(query);
        SimpleSpanFragmenter simpleSpanFragmenter = new SimpleSpanFragmenter(queryScorer, 50);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, queryScorer);
        highlighter.setTextFragmenter(simpleSpanFragmenter);
        //4.打印结果
        for (ScoreDoc scoreDoc : searchResult.scoreDocs) {
            System.out.println("==============================================================");
            System.out.println("文档ID：" + scoreDoc.doc + "，文档评分：" + scoreDoc.score);
            //获取文档数据
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("电影ID：" + doc.get("filmId"));

            System.out.println("电影名称：" + doc.get("title"));
//            System.out.println("电影简介：" + doc.get("description"));
            System.out.println("电影上映年份：" + doc.get("releaseYear"));
            String description = doc.get("description");
            if (null != description && description.length() > 0) {
                TokenStream tokenStream = ikAnalyzer.tokenStream("description", new StringReader(description));
                System.out.println(highlighter.getBestFragment(tokenStream, description));
            }
            System.out.println("==============================================================");
        }
        directoryReader.close();
    }
}
